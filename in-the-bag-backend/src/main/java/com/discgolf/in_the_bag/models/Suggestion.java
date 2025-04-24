package com.discgolf.in_the_bag.models;

import com.discgolf.in_the_bag.suggestions.DiscCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // fetched only when needed
    @JoinColumn(name = "disc_id", referencedColumnName = "id")  // Foreign key to "discs.id"
    private Disc disc;

    @Enumerated(EnumType.STRING)
    @Column(name = "disc_category", nullable = false)
    private DiscCategory discCategory;

    @Column(name = "category")
    private String category;

    @Column(name = "stability")
    private String stability;
}
