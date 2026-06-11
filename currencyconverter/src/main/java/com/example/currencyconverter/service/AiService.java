package com.example.currencyconverter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

@Service
public class AiService {

    // Properties file se secure API key utha raha hai
    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractCurrencyInfo(String query) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        // MAGIC: AI ko strict instruction diya hai ki sirf JSON format me data de
        String prompt = "Extract amount, source currency code (3 letters), and target currency code (3 letters) from this text: '" + query + "'. Return EXACTLY a raw JSON object with keys: 'amount', 'from', 'to'. No markdown, no backticks, no extra words. Example: {\"amount\": 100, \"from\": \"USD\", \"to\": \"INR\"}";

        String requestBody = "{ \"contents\": [{ \"parts\": [{\"text\": \"" + prompt + "\"}] }] }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // API Call maro
            String response = restTemplate.postForObject(url, entity, String.class);

            // Google ke lamba chauda JSON me se sirf apna kaam ka text nikalna
            JsonNode root = objectMapper.readTree(response);
            String aiText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            return aiText; // Ye seedha clean JSON dega frontend ko
            
        } catch (Exception e) {
            System.out.println("AI Error: " + e.getMessage());
            return "{\"error\": \"AI process failed\"}";
        }
    }
}
