package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(columnDefinition = "text")
    private String comment;
}
