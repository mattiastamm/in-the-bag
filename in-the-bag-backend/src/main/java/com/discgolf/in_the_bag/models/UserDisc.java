package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "user_discs")  // Maps to "user_discs" table in PostgreSQL
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDisc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "disc_id", referencedColumnName = "id")  // Foreign key to "discs.id"
    private Disc disc;

    @ManyToOne
    @JoinColumn(name = "plastic", referencedColumnName = "id")  // Foreign key to "plastics.id"
    private Plastic plastic;

    private Double weight;
    private Float customSpeed;
    private Float customGlide;
    private Float customTurn;
    private Float customFade;
    private Boolean inUse;

    @Column(name = "updated_at")
    private String updatedAt;

    private String comment;
    private String color;

    @ManyToMany
    @JoinTable(
        name = "disc_in_bag",
        joinColumns = @JoinColumn(name = "user_disc_id"),
        inverseJoinColumns = @JoinColumn(name = "bag_id")
    )
    private Set<Bag> bags = new HashSet<>();
}
