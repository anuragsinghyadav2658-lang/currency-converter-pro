package com.example.currencyconverter.repository;

import com.example.currencyconverter.entity.ConversionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends JpaRepository<ConversionRecord, Long> {
    // JpaRepository ke andar save(), findAll(), delete() jaise methods pehle se bane hote hain!
}
