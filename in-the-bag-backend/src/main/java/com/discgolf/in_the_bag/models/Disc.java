package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "discs")  // Maps to "discs" table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Disc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer", nullable = false)
    private Manufacturer manufacturer;

    private Float speed;
    private Float glide;
    private Float turn;
    private Float fade;
    private String type;
}

