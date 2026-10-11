package com.tripnest.service;

import com.tripnest.dto.BookingRequest;
import com.tripnest.entity.*;
import com.tripnest.repository.BookingRepository;
import com.tripnest.repository.FlightRepository;
import com.tripnest.repository.HotelRepository;
import com.tripnest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final FlightRepository flights;
    private final HotelRepository hotels;
    private final UserRepository users;

    public BookingService(BookingRepository b, FlightRepository f, HotelRepository h, UserRepository u) {
        bookings = b;
        flights = f;
        hotels = h;
        users = u;
    }

    @Transactional
    public Booking create(String email, BookingRequest r) {
        User u = users.findByEmailIgnoreCase(email).orElseThrow();
        Booking b = new Booking();
        b.user = u;
        b.type = r.type();
        b.quantity = r.quantity();
        if (r.type() == BookingType.FLIGHT) {
            Flight f = flights.findById(r.itemId()).orElseThrow(() -> new NoSuchElementException("Flight not found"));
            if (!f.departure.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Flight has departed");
            if (f.seatsAvailable < r.quantity()) throw new IllegalArgumentException("Not enough seats");
            f.seatsAvailable -= r.quantity();
            b.flight = f;
            b.totalPrice = f.price.multiply(BigDecimal.valueOf(r.quantity()));
        } else {
            Hotel h = hotels.findById(r.itemId()).orElseThrow(() -> new NoSuchElementException("Hotel not found"));
            if (r.checkIn() == null || r.checkOut() == null || r.checkIn().isBefore(LocalDate.now()) || !r.checkOut().isAfter(r.checkIn()))
                throw new IllegalArgumentException("Invalid check-in/check-out dates");
            long nights = ChronoUnit.DAYS.between(r.checkIn(), r.checkOut());
            if (h.roomsAvailable < r.quantity()) throw new IllegalArgumentException("Not enough rooms");
            h.roomsAvailable -= r.quantity();
            b.hotel = h;
            b.checkIn = r.checkIn();
            b.checkOut = r.checkOut();
            b.totalPrice = h.pricePerNight.multiply(BigDecimal.valueOf(r.quantity() * nights));
        }
        return bookings.save(b);
    }

    public List<Booking> mine(String email) {
        return bookings.findAll().stream().filter(b -> b.user.email.equalsIgnoreCase(email)).sorted(Comparator.comparing((Booking b) -> b.createdAt).reversed()).toList();
    }

    @Transactional
    public Booking cancel(String email, Long id) {
        Booking b = bookings.findById(id).orElseThrow(() -> new NoSuchElementException("Booking not found"));
        if (!b.user.email.equalsIgnoreCase(email)) throw new NoSuchElementException("Booking not found");
        if (b.status == BookingStatus.CANCELLED) throw new IllegalArgumentException("Already cancelled");
        b.status = BookingStatus.CANCELLED;
        if (b.flight != null) b.flight.seatsAvailable += b.quantity;
        if (b.hotel != null) b.hotel.roomsAvailable += b.quantity;
        return b;
    }
}
