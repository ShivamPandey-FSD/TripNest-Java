package com.tripnest.controller;

import com.tripnest.entity.Booking;
import com.tripnest.entity.Flight;
import com.tripnest.entity.Hotel;
import com.tripnest.repository.BookingRepository;
import com.tripnest.repository.FlightRepository;
import com.tripnest.repository.HotelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final FlightRepository flights;
    private final HotelRepository hotels;
    private final BookingRepository bookings;

    public AdminController(FlightRepository f, HotelRepository h, BookingRepository b) {
        flights = f;
        hotels = h;
        bookings = b;
    }

    @GetMapping("/bookings")
    public List<Booking> bookings() {
        return bookings.findAll();
    }

    @PostMapping("/flights")
    public Flight addFlight(@RequestBody Flight f) {
        f.id = null;
        validateFlight(f);
        return flights.save(f);
    }

    @PutMapping("/flights/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight f) {
        flights.findById(id).orElseThrow();
        f.id = id;
        validateFlight(f);
        return flights.save(f);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flights.deleteById(id);
    }

    @PostMapping("/hotels")
    public Hotel addHotel(@RequestBody Hotel h) {
        h.id = null;
        validateHotel(h);
        return hotels.save(h);
    }

    @PutMapping("/hotels/{id}")
    public Hotel updateHotel(@PathVariable Long id, @RequestBody Hotel h) {
        hotels.findById(id).orElseThrow();
        h.id = id;
        validateHotel(h);
        return hotels.save(h);
    }

    @DeleteMapping("/hotels/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotels.deleteById(id);
    }

    private void validateFlight(Flight f) {
        if (f.airline == null || f.flightNumber == null || f.origin == null || f.destination == null || f.departure == null || f.arrival == null || f.price == null || f.price.signum() < 0 || f.seatsAvailable == null || f.seatsAvailable < 0 || !f.arrival.isAfter(f.departure))
            throw new IllegalArgumentException("Invalid flight details");
    }

    private void validateHotel(Hotel h) {
        if (h.name == null || h.city == null || h.address == null || h.pricePerNight == null || h.pricePerNight.signum() < 0 || h.roomsAvailable == null || h.roomsAvailable < 0 || h.rating == null || h.rating < 0 || h.rating > 5)
            throw new IllegalArgumentException("Invalid hotel details");
    }
}
