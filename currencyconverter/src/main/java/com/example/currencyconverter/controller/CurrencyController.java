package com.example.currencyconverter.controller;

import com.example.currencyconverter.entity.ConversionRecord;
import com.example.currencyconverter.repository.ConversionRepository;
import com.example.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ConversionRepository conversionRepository; // Naya Repository inject kiya

    // Purana wala method (API se data laane ke liye)
    @GetMapping("/rates/{base}")
    public String getExchangeRates(@PathVariable String base) {
        return currencyService.fetchRatesFromExternalApi(base);
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

