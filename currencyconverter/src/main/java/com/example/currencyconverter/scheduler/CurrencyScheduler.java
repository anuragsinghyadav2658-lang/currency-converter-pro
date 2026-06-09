package com.example.currencyconverter.scheduler;

import com.example.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // Spring ko batane ke liye ki ye ek background worker hai
public class CurrencyScheduler {

    @Autowired
    private CurrencyService currencyService;

    // @Scheduled(cron = "0 0 0 * * ?") // Asli website ke liye: Ye har raat 12 baje chalega
    
    @Scheduled(fixedRate = 60000) // TESTING KE LIYE: Har 1 minute (60,000 milliseconds) me chalega
    @CacheEvict(value = "rates", allEntries = true) // MAGIC: Ye purane Cache (memory) ko kachre me daal dega
    public void updateRatesAutomatically() {
        System.out.println("⏰ [BACKGROUND JOB] Uth gaya! Purana cache delete kar diya...");
        
        // Purana memory delete hone ke baad, hum API ko call karke naya data layenge
        currencyService.fetchRatesFromExternalApi("USD");
        
        System.out.println("✅ [BACKGROUND JOB] Naye rates automatically fetch karke memory me save kar diye!");
    }
}
