package com.nexossoftware.inventorysystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;
    private LocalDate companyEntryDate;
}