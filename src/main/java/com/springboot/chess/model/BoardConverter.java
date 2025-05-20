package com.springboot.chess.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardConverter implements AttributeConverter<String[][], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String[][] board) {
        try {
            return objectMapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting board to JSON", e);
        }
    }

    @Override
    public String[][] convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, String[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to board", e);
        }
    }
}
