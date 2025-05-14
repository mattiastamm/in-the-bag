package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "user_discs")  // Maps to "user_discs" table in PostgreSQL
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDisc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "disc_id", nullable = false)
    private Disc disc;

    @ManyToOne
    @JoinColumn(name = "plastic")
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
}
