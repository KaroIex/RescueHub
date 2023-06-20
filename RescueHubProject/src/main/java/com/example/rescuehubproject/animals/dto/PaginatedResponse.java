package com.example.rescuehubproject.animals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(name = "PaginatedResponse", description = "DTO representing a paginated response")
public class PaginatedResponse<T> {
    @Schema(description = "Content of the page")
    private List<T> content;
    @Schema(description = "Total number of pages")
    private int currentPage;
    @Schema(description = "Total number of elements")
    private long totalElements;
    @Schema(description = "Total number of pages")
    private int size;

}