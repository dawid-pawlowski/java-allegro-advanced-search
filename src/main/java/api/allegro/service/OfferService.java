package api.allegro.service;

import api.allegro.entity.OfferEntity;
import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.repository.OfferRepository;
import api.allegro.resource.OfferResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OfferService {

    private final OfferResource resource;
    private final OfferRepository repository;

    private final CategoryService catSrv;

    private final List<OfferEntity> offers = new ArrayList<>();
    private final List<OfferEntity> matchingOffers = new ArrayList<>();

    public OfferService(String pu, String accessToken) {
        resource = new OfferResource(accessToken);
        repository = new OfferRepository(pu);
        catSrv = new CategoryService(pu, accessToken);
    }

    // get db offer count
    public long getOfferCount() {
        return repository.count();
    }

    // TODO: implement filtering via query params
    // fetch all user's offers from allegro
    public void synchronize() throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException {
        int limit = 1000;
        int count = 0;
        int totalCount = 0;
        int offset = 0;

        // get initial offer fields
        do {
            JSONObject response = resource.getOffers(offset, limit);
            JSONArray jsonArray = response.getJSONArray("offers");

            count += response.getInt("count");
            totalCount = response.getInt("totalCount");
            offset += limit;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                OfferEntity offer = new OfferEntity();
                offer.setId(o.getString("id"));
                offers.add(offer);
            }

        } while (count < totalCount);

        // multithreading call to get each offer here
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < offers.size(); i += 50) {
            Thread thread = new Thread(new ThreadRunner(offers.subList(i, Math.min(offers.size(), i + 50))));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // save all offers into db
        // TODO: do not save null values (?)
        repository.clear();
        repository.addAll(offers);
    }

    // load all offers from db
    public void loadAllOffers() {
        offers.addAll(repository.getAll());
    }

    // match offers using selected filters
    public void matchOffers(Map<String, List<String>> filters, String categoryId) {
        matchingOffers.clear();
        for (OfferEntity offer : offers) {
            if (offer.getParamMap().entrySet().containsAll(filters.entrySet()) && offer.getCategories().contains(categoryId)) {
                matchingOffers.add(offer);
            }
        }
    }

    // get all matching offers
    public List<OfferEntity> getMatchingOffers() {
        return matchingOffers;
    }

    // get single offer from db
    public OfferEntity loadOffer(String offerId) {
        return repository.getById(offerId);
    }

    // fetch offer from allegro and parse the response
    private OfferEntity fetchOffer(String offerId) throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException, AllegroBadRequestException {
        OfferEntity offer = new OfferEntity();
        JSONObject response = resource.getOffer(offerId);
        offer.setAdditionalServices(response.get("additionalServices").toString());
        offer.setAfterSalesServices(response.get("afterSalesServices").toString());
        offer.setAttachments(response.get("attachments").toString());

        offer.setCategory(response.get("category").toString());
        JSONObject offerCategory = new JSONObject(offer.getCategory());
        offer.setCategories(parseCategories(offerCategory.getString("id")));

        offer.setCompatibilityList(response.get("compatibilityList").toString());
        offer.setContact(response.get("contact").toString());
        offer.setCreatedAt(response.getString("createdAt"));
        offer.setCustomParameters(response.get("customParameters").toString());
        offer.setDelivery(response.get("delivery").toString());
        offer.setDescription(response.get("description").toString());
        offer.setDiscounts(response.get("discounts").toString());
        offer.setExternal(response.get("external").toString());
        offer.setFundraisingCampaign(response.get("fundraisingCampaign").toString());
        offer.setId(response.getString("id"));
        offer.setImages(response.get("images").toString());
        offer.setLocation(response.get("location").toString());
        offer.setName(response.getString("name"));

        offer.setParameters(response.get("parameters").toString());
        offer.setParamMap(parseParams((offer.getParameters())));

        offer.setPayments(response.get("payments").toString());
        offer.setProduct(response.get("product").toString());
        offer.setPromotion(response.get("promotion").toString());
        offer.setPublication(response.get("publication").toString());
        offer.setSellingMode(response.get("sellingMode").toString());
        offer.setTax(response.get("tax").toString());
        offer.setSizeTable(response.get("sizeTable").toString());
        offer.setStock(response.get("stock").toString());
        offer.setTecdocSpecification(response.get("tecdocSpecification").toString());
        offer.setB2b(response.get("b2b").toString());
        offer.setUpdatedAt(response.getString("updatedAt"));
        offer.setValidation(response.get("validation").toString());

        return offer;
    }

    // helper method to fetch category tree of current offer
    private ArrayList<String> parseCategories(String categoryId) throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        return catSrv.getCategoryPath(categoryId);
    }

    // helper method to parse offer params for quick filtering access
    private HashMap<String, List<String>> parseParams(String jsonParams) {
        JSONArray paramArray = new JSONArray(jsonParams);
        HashMap<String, List<String>> paramMap = new HashMap<>();
        for (int i = 0; i < paramArray.length(); i++) {
            JSONObject o = (JSONObject) paramArray.get(i);
            String paramId = o.getString("id");
            List<String> paramValues = new ArrayList<>();
            JSONArray values = o.getJSONArray("values");
            for (int j = 0; j < values.length(); j++) {
                paramValues.add(values.getString(j));
            }
            JSONArray valuesIds = o.getJSONArray("valuesIds");
            for (int j = 0; j < valuesIds.length(); j++) {
                paramValues.add(valuesIds.getString(j));
            }

            if (!o.isNull("rangeValue")) {
                JSONObject rangeValue = o.getJSONObject("rangeValue");
                paramValues.add(rangeValue.getString("from"));
                paramValues.add(rangeValue.getString("to"));
            }

            paramMap.put(paramId, paramValues);
        }

        return paramMap;
    }

    public boolean batchOfferDescriptionChange(List<OfferEntity> offers, String oldValue, String newValue) {
        /*
            - iterate over matching offers list
            - change description in each offer
            - call allegro api to change
            - update all offer entities at once (only if change was successful (?))
         */

        // iterate over matching offers
        return true;
    }

    // bulk change quantity in matching offers
    public boolean batchOfferQuantityChange(List<OfferEntity> offers, String type, String value) {
        try {
            List<String> ids = offers.stream()
                    .map(OfferEntity::getId)
                    .collect(Collectors.toList());
            JSONObject result = resource.batchOfferQuantityChange(ids, type, value);
            // TODO: return commandId from result object (can be used to track progress of change)
            return true;
        } catch (IOException | InterruptedException e) {
            // TODO: throw general exception
            return false;
        }
    }

    // TODO: use enum for "type" property
    // bulk change price in matching offers
    public boolean batchOfferPriceChange(List<OfferEntity> offers, String type, String value) {
        try {
            List<String> ids = offers.stream()
                    .map(OfferEntity::getId)
                    .collect(Collectors.toList());
            JSONObject result = resource.batchOfferPriceChange(ids, type, value);
            // TODO: return commandId from result object (can be used to track progress of change)
            return true;
        } catch (IOException | InterruptedException e) {
            // TODO: throw general exception
            return false;
        }
    }

    class ThreadRunner implements Runnable {
        private final List<OfferEntity> chunk;

        public ThreadRunner(List<OfferEntity> chunk) {
            this.chunk = chunk;
        }

        @Override
        public void run() {
            for (int i = 0; i < chunk.size(); i++) {
                try {
                    chunk.set(i, fetchOffer(chunk.get(i).getId()));
                } catch (IOException | InterruptedException | AllegroBadRequestException | AllegroUnauthorizedException | AllegroNotFoundException e) {
                    chunk.set(i, null);
                    e.printStackTrace();
                }
            }
        }
    }

}
