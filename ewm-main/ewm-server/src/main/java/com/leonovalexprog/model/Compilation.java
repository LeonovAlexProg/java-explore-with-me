package com.leonovalexprog.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Compilations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "events_id")
    private List<Event> events;

    @Column(nullable = false)
    private Boolean pinned;

    @Column(nullable = false)
    private String title;
}
