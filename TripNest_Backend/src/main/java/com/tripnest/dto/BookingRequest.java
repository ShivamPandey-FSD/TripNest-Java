package com.tripnest.dto;

import com.tripnest.entity.BookingType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(@NotNull BookingType type, @NotNull Long itemId,
                             @NotNull @Min(1) @Max(10) Integer quantity, LocalDate checkIn, LocalDate checkOut) {
}
