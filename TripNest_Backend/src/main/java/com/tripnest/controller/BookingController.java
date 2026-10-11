package com.tripnest.controller;

import com.tripnest.dto.BookingRequest;
import com.tripnest.entity.Booking;
import com.tripnest.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Booking> book(Principal user, @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(201).body(service.create(user.getName(), request));
    }

    @GetMapping("/my")
    public List<Booking> mine(Principal user) {
        return service.mine(user.getName());
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(Principal user, @PathVariable Long id) {
        return service.cancel(user.getName(), id);
    }
}
