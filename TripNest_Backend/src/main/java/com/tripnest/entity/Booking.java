package com.tripnest.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    public User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public BookingType type;
    @ManyToOne
    public Flight flight;
    @ManyToOne
    public Hotel hotel;
    @Column(nullable = false)
    public Integer quantity;
    public LocalDate checkIn;
    public LocalDate checkOut;
    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public BookingStatus status = BookingStatus.CONFIRMED;
    @Column(nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
}
