package com.tripnest.controller;

import com.tripnest.entity.Flight;
import com.tripnest.entity.Hotel;
import com.tripnest.repository.FlightRepository;
import com.tripnest.repository.HotelRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class CatalogController {
    private final FlightRepository flights;
    private final HotelRepository hotels;

    public CatalogController(FlightRepository f, HotelRepository h) {
        flights = f;
        hotels = h;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/flights")
    public List<Flight> flights(@RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flights.findAll().stream().filter(f -> from == null || f.origin.equalsIgnoreCase(from)).filter(f -> to == null || f.destination.equalsIgnoreCase(to)).filter(f -> date == null || f.departure.toLocalDate().equals(date)).toList();
    }

    @GetMapping("/flights/{id}")
    public Flight flight(@PathVariable Long id) {
        return flights.findById(id).orElseThrow(() -> new NoSuchElementException("Flight not found"));
    }

    @GetMapping("/hotels")
    public List<Hotel> hotels(@RequestParam(required = false) String city) {
        return hotels.findAll().stream().filter(h -> city == null || h.city.equalsIgnoreCase(city)).toList();
    }

    @GetMapping("/hotels/{id}")
    public Hotel hotel(@PathVariable Long id) {
        return hotels.findById(id).orElseThrow(() -> new NoSuchElementException("Hotel not found"));
    }
}
