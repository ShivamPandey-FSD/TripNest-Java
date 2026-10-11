package com.tripnest.config;

import com.tripnest.entity.Flight;
import com.tripnest.entity.Hotel;
import com.tripnest.entity.Role;
import com.tripnest.entity.User;
import com.tripnest.repository.FlightRepository;
import com.tripnest.repository.HotelRepository;
import com.tripnest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DemoData {
    @Bean
    CommandLineRunner seed(UserRepository users, FlightRepository flights, HotelRepository hotels, PasswordEncoder encoder, @Value("${app.seed.enabled}") boolean enabled, @Value("${app.seed.admin-password}") String adminPassword, @Value("${app.seed.customer-password}") String customerPassword) {
        return args -> {
            if (!enabled) return;
            if (!users.existsByEmailIgnoreCase("admin@tripnest.com"))
                users.save(new User("Demo Admin", "admin@tripnest.com", encoder.encode(adminPassword), Role.ADMIN));
            if (!users.existsByEmailIgnoreCase("customer@tripnest.com"))
                users.save(new User("Demo Customer", "customer@tripnest.com", encoder.encode(customerPassword), Role.CUSTOMER));
            if (flights.count() == 0) {
                String[][] routes = {{"IndiGo", "Delhi", "Mumbai"}, {"Air India", "Delhi", "Bengaluru"}, {"Akasa Air", "Mumbai", "Goa"}, {"IndiGo", "Delhi", "Jaipur"}, {"Air India", "Bengaluru", "Delhi"}, {"IndiGo", "Mumbai", "Delhi"}, {"Akasa Air", "Delhi", "Goa"}, {"Air India", "Hyderabad", "Mumbai"}};
                for (int i = 0; i < routes.length; i++) {
                    Flight f = new Flight();
                    f.airline = routes[i][0];
                    f.flightNumber = "TN" + (200 + i);
                    f.origin = routes[i][1];
                    f.destination = routes[i][2];
                    f.departure = LocalDate.now().plusDays(3 + i).atTime(8 + i, 30);
                    f.arrival = f.departure.plusHours(2);
                    f.price = BigDecimal.valueOf(2999 + i * 650);
                    f.seatsAvailable = 30 + i * 5;
                    flights.save(f);
                }
            }
            if (hotels.count() == 0) {
                String[][] entries = {{"The Imperial Stay", "Delhi"}, {"Marine View Residency", "Mumbai"}, {"Palm Breeze Resort", "Goa"}, {"Garden City Suites", "Bengaluru"}, {"Pink City Palace", "Jaipur"}, {"Charminar Comfort", "Hyderabad"}};
                for (int i = 0; i < entries.length; i++) {
                    Hotel h = new Hotel();
                    h.name = entries[i][0];
                    h.city = entries[i][1];
                    h.address = "Central " + entries[i][1] + ", India";
                    h.description = "Comfortable stay with Wi-Fi and breakfast options.";
                    h.pricePerNight = BigDecimal.valueOf(2200 + i * 650);
                    h.roomsAvailable = 15 + i * 2;
                    h.rating = 4.1 + i * 0.1;
                    hotels.save(h);
                }
            }
        };
    }
}
