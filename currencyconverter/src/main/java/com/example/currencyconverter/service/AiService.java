package com.example.currencyconverter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractCurrencyInfo(String query) {
        // String cleanKey = apiKey.replaceAll("[\\[\\]\"\'\\s]", "").trim(); // Sab
        String cleanKey = apiKey.replaceAll("[\\[\\]\"\'\\s]", "").trim();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                + cleanKey;

        // Prompt se double quotes hata diye hain taaki koi format break na ho
        String prompt = "Extract amount, source currency code (3 letters), and target currency code (3 letters) from this text: '"
                + query + "'. " +
                "Return EXACTLY a raw JSON object with keys: 'amount', 'from', 'to'. " +
                "Do not include markdown, do not include backticks, do not include any extra words. " +
                "Example response format: {\"amount\": 100, \"from\": \"USD\", \"to\": \"INR\"}";

        try {
            // ObjectMapper se safe JSON structure banana (Escaping ki galti ab nahi hogi)
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode contentsArray = rootNode.putArray("contents");
            ObjectNode contentNode = contentsArray.addObject();
            ArrayNode partsArray = contentNode.putArray("parts");
            ObjectNode partNode = partsArray.addObject();
            partNode.put("text", prompt);

            String requestBody = objectMapper.writeValueAsString(rootNode);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Google API ko call karo
            String response = restTemplate.postForObject(url, entity, String.class);

            // Response se text nikalna
            JsonNode responseJson = objectMapper.readTree(response);
            String aiText = responseJson.path("candidates").get(0).path("content").path("parts").get(0).path("text")
                    .asText().trim();

            // Agar Gemini ne galti se ```json ... ``` ke andar response wrap kiya ho, toh
            // use saaf karo
            if (aiText.startsWith("```")) {
                aiText = aiText.replaceAll("^```json\\s*", "").replaceAll("```$", "").trim();
            }

            return aiText;

        } catch (Exception e) {
            System.out.println("AI Service Error: " + e.getMessage());
            e.printStackTrace();
            return "{\"error\": \"AI process failed\"}";
        }
    }
}
