package ru.practicum.shareit.util.validator.impl;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.util.validator.StartAndEndValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartAndEndValidator implements ConstraintValidator<StartAndEndValid, BookItemRequestDto> {
    @Override
    public boolean isValid(BookItemRequestDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDto.getStart() != null && bookingDto.getEnd() != null) {
            return bookingDto.getStart().isBefore(bookingDto.getEnd());
        }
        return false;
    }
}
