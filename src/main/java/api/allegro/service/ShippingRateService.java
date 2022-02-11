package api.allegro.service;

import api.allegro.bean.CategoryBean;
import api.allegro.bean.ShippingRateBean;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.resource.ShippingRateResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShippingRateService {
    private final ShippingRateResource resource;

    public ShippingRateService(String accessToken) {
        resource = new ShippingRateResource(accessToken);
    }

    public List<ShippingRateBean> getShippingRates() throws AllegroUnauthorizedException, IOException, InterruptedException {
        List<ShippingRateBean> shippingRates = new ArrayList<>();

        JSONObject response = resource.getShippingRates();
        JSONArray jsonArray = response.getJSONArray("shippingRates");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = (JSONObject) jsonArray.get(i);
            ShippingRateBean rate = new ShippingRateBean();
            rate.setId(o.getString("id"));
            rate.setName(o.getString("name"));
            shippingRates.add(rate);
        }

        return shippingRates;
    }
}
