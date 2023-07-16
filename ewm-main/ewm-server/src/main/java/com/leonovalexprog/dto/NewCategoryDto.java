package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotEmpty(message = "Name is mandatory")
    @Min(value = 1, message = "Name length must be greater then 0")
    @Max(value = 50, message = "Name length must be shorter then 50")
    private String name;
}
