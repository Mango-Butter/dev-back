package com.mangoboss.storage.task.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return (attribute == null || attribute.isEmpty())
                ? null
                : attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank())
                ? List.of()
                : Arrays.stream(dbData.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}