package com.leonovalexprog.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    @JoinColumn(name = "events_id")
    private List<Event> events;
}
