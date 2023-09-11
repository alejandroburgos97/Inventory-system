package com.nexossoftware.inventorysystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "positions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String positionName;
}