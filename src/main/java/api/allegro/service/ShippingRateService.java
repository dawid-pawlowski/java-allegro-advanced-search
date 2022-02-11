package api.allegro.service;

import api.allegro.bean.ShippingRateBean;
import api.allegro.resource.ShippingRateResource;

import java.util.List;

public class ShippingRateService {
    private final ShippingRateResource resource;

    public ShippingRateService(String accessToken) {
        resource = new ShippingRateResource(accessToken);
    }

    public List<ShippingRateBean> getShippingRates() {
        
    }
}
