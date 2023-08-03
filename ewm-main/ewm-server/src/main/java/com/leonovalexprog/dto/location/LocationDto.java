package com.leonovalexprog.dto.location;

import com.leonovalexprog.dto.event.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;

    private String name;

    private Float lat;

    private Float lon;

    private Float rad;

    private List<EventShortDto> events;
}
