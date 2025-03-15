package com.discgolf.in_the_bag.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "bags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(columnDefinition = "text")
    private String comment;

    @ManyToMany(mappedBy = "bags")
    @JsonIgnore  // ✅ Prevents infinite recursion
    private Set<UserDisc> userDiscs = new HashSet<>();
}
