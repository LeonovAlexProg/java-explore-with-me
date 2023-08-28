package com.leonovalexprog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message = "Category's name size is out of bounds")
    private String name;
}
