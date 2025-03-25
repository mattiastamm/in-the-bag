package com.discgolf.in_the_bag.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "disc_in_bag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscInBag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_disc_id", nullable = false)
    private UserDisc userDisc;

    @ManyToOne
    @JoinColumn(name = "bag_id", nullable = false)
    private Bag bag;
}

