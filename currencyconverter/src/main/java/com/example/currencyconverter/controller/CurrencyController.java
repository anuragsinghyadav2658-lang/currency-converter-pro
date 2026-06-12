package com.example.currencyconverter.controller;

import com.example.currencyconverter.entity.ConversionRecord;
import com.example.currencyconverter.repository.ConversionRepository;
import com.example.currencyconverter.service.CurrencyService;
import com.example.currencyconverter.service.AiService; // <-- NAYA IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // <-- NAYA IMPORT

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private AiService aiService; // <-- NAYA: AiService ko yahan inject kiya

    // Purana wala method (API se data laane ke liye)
    @GetMapping("/rates/{base}")
    public String getExchangeRates(@PathVariable String base) {
        return currencyService.fetchRatesFromExternalApi(base);
    }

    // --- NAYA METHOD AI SMART CONVERT KE LIYE ---
    @PostMapping("/smart-convert")
    public String smartConvert(@RequestBody Map<String, String> payload) {
        String query = payload.get("query"); // Frontend se bheja gaya text uthaya
        return aiService.extractCurrencyInfo(query);
    }

    // --- NAYE METHODS DATABASE KE LIYE ---

    // 1. Data Save karne ke liye (Jab user convert button dabayega)
    @PostMapping("/save")
    public String saveConversion(@RequestBody ConversionRecord record) {
        conversionRepository.save(record);
        return "Conversion history saved successfully!";
    }

    // 2. Data Fetch karne ke liye (History dikhane ke liye)
    @GetMapping("/history")
    public List<ConversionRecord> getHistory() {
        return conversionRepository.findAll();
    }
}
