package com.leonovalexprog.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationDto {
    @Nullable
    @Size(min = 5, max = 30, message = "Location's name size is out of bounds")
    private String name;

    @Nullable
    @Min(value = -90, message = "Lat can't be lower then -90")
    @Max(value = 90, message = "Lat can't be higher then 90")
    private Float lat;

    @Nullable
    @Min(value = -180, message = "Lon can't be lower then -180")
    @Max(value = 180, message = "Lon can't be higher then 180")
    private Float lon;

    @Nullable
    private Float rad;
}
