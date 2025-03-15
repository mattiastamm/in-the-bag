package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manufacturers")  // Maps to "manufacturers" table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}

