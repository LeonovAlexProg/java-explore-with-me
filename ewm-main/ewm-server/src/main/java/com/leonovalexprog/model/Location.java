package com.leonovalexprog.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "locations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Float lat;

    @Column(nullable = false)
    private Float lon;

    @Column(nullable = false)
    private Float rad;

    @ManyToMany
    private List<Event> events;
}
