package com.brokerage.insure.rest.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "insurance_products")
public class InsuranceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category; // MOTOR, LIFE, HEALTH
    private String description;
    private double basePremium; // Starting price
    private String provider; // e.g., Jubilee, Britam, APA

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setBasePremium(double basePremium) {
        this.basePremium = basePremium;
    }

    public double getBasePremium() {
        return basePremium;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}
