package com.tripnest.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String city;
    @Column(nullable = false)
    public String address;
    @Column(length = 1500)
    public String description;
    public String imageUrl;
    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal pricePerNight;
    @Column(nullable = false)
    public Integer roomsAvailable;
    @Column(nullable = false)
    public Double rating;
}
