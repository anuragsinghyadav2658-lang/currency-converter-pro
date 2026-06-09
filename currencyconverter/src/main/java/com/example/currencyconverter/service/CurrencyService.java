package com.example.currencyconverter.service;

import org.springframework.cache.annotation.Cacheable; // NAYI LINE
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();

    // NAYI LINE: Ye result ko 'rates' naam ki memory me save kar lega (key hoga jaise 'USD' ya 'INR')
    @Cacheable(value = "rates", key = "#baseCurrency")
    public String fetchRatesFromExternalApi(String baseCurrency) {
        
        // NAYI LINE: Ye console me print hoga. Agar cache chal gaya, toh ye line dubara print nahi hogi!
        System.out.println("API CALL HUI HAI! Fetching live rates for: " + baseCurrency);
        
        String url = "https://open.er-api.com/v6/latest/" + baseCurrency;
        return restTemplate.getForObject(url, String.class);
    }
}
