package com.mangoboss.storage.task.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        return (attribute == null || attribute.isEmpty())
                ? null
                : attribute.stream()
                .map(DayOfWeek::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank())
                ? List.of()
                : Arrays.stream(dbData.split(","))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toList());
    }
}