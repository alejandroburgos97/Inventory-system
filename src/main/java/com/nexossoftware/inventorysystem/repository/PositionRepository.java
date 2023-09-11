package com.nexossoftware.inventorysystem.repository;

import com.nexossoftware.inventorysystem.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

}