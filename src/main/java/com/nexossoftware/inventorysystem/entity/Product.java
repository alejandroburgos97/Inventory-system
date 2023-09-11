package com.nexossoftware.inventorysystem.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer quantity;
    private LocalDate entryDate;
    @ManyToOne
    @JoinColumn(name = "registered_user_id", nullable = false)
    private User registeredUser;
    private LocalDate modificationDate;
    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    private User modifiedUser;
}