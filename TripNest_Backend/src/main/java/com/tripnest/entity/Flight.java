package com.tripnest.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String airline;
    @Column(nullable = false, unique = true)
    public String flightNumber;
    @Column(nullable = false)
    public String origin;
    @Column(nullable = false)
    public String destination;
    @Column(nullable = false)
    public LocalDateTime departure;
    @Column(nullable = false)
    public LocalDateTime arrival;
    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal price;
    @Column(nullable = false)
    public Integer seatsAvailable;
}
