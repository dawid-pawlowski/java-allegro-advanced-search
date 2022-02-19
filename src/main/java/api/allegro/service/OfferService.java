package api.allegro.service;

import api.allegro.bean.CommandBean;
import api.allegro.bean.ShippingRateBean;
import api.allegro.dto.*;
import api.allegro.entity.OfferEntity;
import api.allegro.enums.CommandTypeEnum;
import api.allegro.enums.PriceChangeModeEnum;
import api.allegro.enums.PublishChangeModeEnum;
import api.allegro.enums.QuantityChangeModeEnum;
import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.repository.OfferRepository;
import api.allegro.resource.OfferResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OfferService {

    private final OfferResource resource;
    private final OfferRepository repository;

    private final CategoryService catSrv;
    private final CommandService commandService;

    private final List<OfferEntity> offers = new ArrayList<>();
    private static final List<OfferEntity> matchingOffers = new ArrayList<>();

    public OfferService(String pu, String accessToken) {
        resource = new OfferResource(accessToken);
        repository = new OfferRepository(pu);
        catSrv = new CategoryService(pu, accessToken);
        commandService = new CommandService(accessToken);
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
            List<OfferEntity> subList = offers.subList(i, Math.min(offers.size(), i + 50));
            Thread thread = new Thread(() -> {
                subList.forEach(offerEntity -> {
                    try {
                        offerEntity = fetchOffer(offerEntity.getId());
                    } catch (AllegroUnauthorizedException | IOException | AllegroNotFoundException | InterruptedException | AllegroBadRequestException e) {
                        e.printStackTrace();
                    }
                });
            });
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
            if (offer.getCategories().contains(categoryId) && offer.getParameters().entrySet().containsAll(filters.entrySet())) {
                matchingOffers.add(offer);
            }
        }
    }

    // get all matching offers
    public static List<OfferEntity> getMatchingOffers() {
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

        offer.setId(response.getString("id"));
        offer.setName(response.getString("name"));
        offer.setCategories(parseCategories(response.get("category").toString()));
        offer.setParameters(parseParamsStreams(response.get("parameters").toString()));

        return offer;
    }

    // helper method to fetch category tree of current offer
    private List<? extends String> parseCategories(String category) throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        return catSrv.getCategoryPath(new JSONObject(category).get("id").toString());
    }

    private Map<String, List<String>> parseParamsStreams(String json) {
        return ((List<HashMap<String, Object>>) (List<?>) new JSONArray(json).toList()).stream()
                .collect(Collectors
                        .toMap(
                                o -> (String) o.get("id"),
                                o -> Stream.of(
                                        (List<String>) o.get("values"),
                                        (List<String>) o.get("valuesIds"),
                                        (List<String>) ((o.get("rangeValue") == null) ? Collections.emptyMap().values().stream().toList() : ((Map)o.get("rangeValue)")).values()))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())));
    }

    public void offerQuantityChange(List<OfferEntity> offers, QuantityChangeModeEnum mode, String value) throws IOException, InterruptedException, AllegroUnauthorizedException, AllegroBadRequestException {
        OfferCriteriaDto offerCriteriaDto = new OfferCriteriaDto();
        offerCriteriaDto.setOffers(offers.stream().map(OfferEntity::getId).collect(Collectors.toList()));

        QuantityModificationDto quantityModificationDto = new QuantityModificationDto();
        quantityModificationDto.setChangeValue(mode.name());
        quantityModificationDto.setValue(value);

        OfferChangeDto offerChangeDto = new OfferChangeDto();
        offerChangeDto.setModification(quantityModificationDto);
        offerChangeDto.setOfferCriteria(offerCriteriaDto);

        CommandBean commandBean = CommandService.createCommand(CommandTypeEnum.QUANTITY_CHANGE);
        resource.offerQuantityChange(commandBean.getId(), new JSONObject(offerChangeDto));
    }

    public void offerPriceChange(List<OfferEntity> offers, PriceChangeModeEnum mode, String value) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroBadRequestException {
        OfferCriteriaDto offerCriteriaDto = new OfferCriteriaDto();
        offerCriteriaDto.setOffers(offers.stream().map(OfferEntity::getId).collect(Collectors.toList()));

        OfferChangeDto offerChangeDto = new OfferChangeDto();
        offerChangeDto.setOfferCriteria(offerCriteriaDto);

        switch(mode) {
            case FIXED_PRICE -> {
                FixedPriceModificationDto fixedPriceModificationDto = new FixedPriceModificationDto();
                fixedPriceModificationDto.setType(mode.name());
                fixedPriceModificationDto.setPrice(value);
                offerChangeDto.setModification(fixedPriceModificationDto);
            }
            case INCREASE_PRICE, DECREASE_PRICE -> {
                AdjustPriceModificationDto adjustPriceModificationDto = new AdjustPriceModificationDto();
                adjustPriceModificationDto.setType(mode.name());
                adjustPriceModificationDto.setValue(value);
                offerChangeDto.setModification(adjustPriceModificationDto);
            }
            case INCREASE_PERCENTAGE, DECREASE_PERCENTAGE -> {
                AdjustPercentagePriceModificationDto adjustPercentagePriceModificationDto = new AdjustPercentagePriceModificationDto();
                adjustPercentagePriceModificationDto.setType(mode.name());
                adjustPercentagePriceModificationDto.setPercentage(value);
                offerChangeDto.setModification(adjustPercentagePriceModificationDto);
            }
        }

        CommandBean commandBean = CommandService.createCommand(CommandTypeEnum.PRICE_CHANGE);
        resource.offerPriceChange(commandBean.getId(), new JSONObject(offerChangeDto));
    }

    public void offerPublication(List<OfferEntity> offers, PublishChangeModeEnum mode) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroBadRequestException {
        OfferCriteriaDto offerCriteriaDto = new OfferCriteriaDto();
        offerCriteriaDto.setOffers(offers.stream().map(OfferEntity::getId).collect(Collectors.toList()));

        PublicationModificationDto publicationModificationDto = new PublicationModificationDto();
        publicationModificationDto.setAction(mode.name());

        OfferChangeDto offerChangeDto = new OfferChangeDto();
        offerChangeDto.setOfferCriteria(offerCriteriaDto);
        offerChangeDto.setPublication(publicationModificationDto);

        CommandBean commandBean = CommandService.createCommand(CommandTypeEnum.PUBLICATION_CHANGE);
        resource.offerPublication(commandBean.getId(), new JSONObject(offerChangeDto));
    }

    public void offerShippingRateChange(List<OfferEntity> offers, ShippingRateBean shippingRate) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroBadRequestException {
        OfferCriteriaDto offerCriteriaDto = new OfferCriteriaDto();
        offerCriteriaDto.setOffers(offers.stream().map(OfferEntity::getId).collect(Collectors.toList()));

        OfferDeliveryModificationDto offerDeliveryModificationDto = new OfferDeliveryModificationDto();
        offerDeliveryModificationDto.setShippingRates(shippingRate.getId());

        OfferModificationDto offerModificationDto = new OfferModificationDto();
        offerModificationDto.setDelivery(offerDeliveryModificationDto);

        OfferChangeDto offerChangeDto = new OfferChangeDto();
        offerChangeDto.setOfferCriteria(offerCriteriaDto);
        offerChangeDto.setModification(offerModificationDto);

        CommandBean commandBean = CommandService.createCommand(CommandTypeEnum.OFFER_MODIFICATION);
        resource.offerModification(commandBean.getId(), new JSONObject(offerChangeDto));
    }

}
