package com.leonovalexprog.controller.location;

import com.leonovalexprog.dto.location.UpdateLocationDto;
import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.dto.location.NewLocationDto;
import com.leonovalexprog.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/locations")
@Slf4j
@RequiredArgsConstructor
public class LocationAdminController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto postNewLocation(@Valid @RequestBody NewLocationDto newLocationDto) {
        log.info("Post new location (lat = {}, lon = {}, rad = {})", newLocationDto.getLat(), newLocationDto.getLon(), newLocationDto.getRad());
        return locationService.createNewLocation(newLocationDto);
    }

    @GetMapping("/{locationId}")
    public LocationDto getExactLocation(@PathVariable Long locationId) {
        log.info("Get location (id = {})", locationId);
        return locationService.findExactLocation(locationId);
    }

    @GetMapping
    public List<LocationDto> getLocations(@RequestParam(required = false) Float lat,
                                          @RequestParam(required = false) Float lon,
                                          @RequestParam(defaultValue = "0") Float rad,
                                          @RequestParam(defaultValue = "false") Boolean closest) {
        log.info("Find locations (lat = {}, lon = {}, rad = {}, closest = {})",
                lat, lon, rad, closest);

        return locationService.findLocations(lat, lon, rad, closest);
    }

    @PatchMapping("/{locationId}")
    public LocationDto patchLocation(@PathVariable Long locationId,
                                     @Validated @Valid @RequestBody UpdateLocationDto updateLocationDto) {
        log.info("Patch location (location id = {}, name = {}, lat = {}, lon = {}, rad = {}",
                locationId, updateLocationDto.getName(), updateLocationDto.getLat(), updateLocationDto.getLon(), updateLocationDto.getRad());

        return locationService.updateLocation(locationId, updateLocationDto);
    }
}
