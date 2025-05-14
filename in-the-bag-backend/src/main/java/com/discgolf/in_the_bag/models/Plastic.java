package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plastics")  // Maps to "plastics" table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Plastic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer", nullable = false)
    private Manufacturer manufacturer;
}

