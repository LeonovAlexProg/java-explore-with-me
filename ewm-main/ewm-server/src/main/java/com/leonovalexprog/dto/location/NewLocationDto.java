package com.leonovalexprog.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewLocationDto {
    @NotBlank(message = "Location name is mandatory")
    @Size(min = 5, max = 30, message = "Location's name size is out of bounds")
    private String name;

    @NotNull(message = "Lat is mandatory")
    @Min(value = -90, message = "Lat can't be lower then -90")
    @Max(value = 90, message = "Lat can't be higher then 90")
    private Float lat;

    @NotNull(message = "Lon is mandatory")
    @Min(value = -180, message = "Lon can't be lower then -180")
    @Max(value = 180, message = "Lon can't be higher then 180")
    private Float lon;

    @NotNull(message = "Rad is mandatory")
    @Min(value = 0, message = "Rad can't be lower then 0")
    private Float rad;
}
