package com.example.currencyconverter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Ye annotation Spring ko batata hai ki ise MySQL Table banana hai
public class ConversionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double convertedAmount;
    private LocalDateTime timestamp = LocalDateTime.now(); // Jis time convert hua

    // Getters and Setters (Inke bina Spring Boot error dega)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(double convertedAmount) { this.convertedAmount = convertedAmount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
