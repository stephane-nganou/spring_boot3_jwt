package com.masteranything.security.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PageResponse<T> (
    List<T> content,
    int number,
    int size,
    @JsonProperty("total_elements")
    long totalElements,
    @JsonProperty("total_pages")
    int totalPages,
    boolean first,
    boolean last
){}