package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "user_discs")  // Maps to "user_discs" table in PostgreSQL
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDisc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // ✅ Explicitly map this field to the "id" column in the DB
    private Long userDiscId;

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

    @Size(max = 50, message = "Comment cannot exceed 50 characters.")
    private String comment;

    private String color;

    @Column(name = "custom_plastic")
    @Size(max = 20, message = "Custom plastic name cannot exceed 20 characters.")
    private String customPlastic;

    @ManyToMany
    @JoinTable(
        name = "disc_in_bag",
        joinColumns = @JoinColumn(name = "user_disc_id"),
        inverseJoinColumns = @JoinColumn(name = "bag_id")
    )
    private Set<Bag> bags = new HashSet<>();
}
