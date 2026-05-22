package com.baran.rentacar.dataAccess.abstracts;

import com.baran.rentacar.business.responses.GetAllModelsResponse;
import com.baran.rentacar.entities.concretes.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Override
    @EntityGraph(attributePaths = {"brand"})
    Page<Model> findAll(Pageable pageable);

    @Query("""
      SELECT new com.baran.rentacar.business.responses.GetAllModelsResponse(
          m.id, m.name, m.brand.name
      )
      FROM Model m
  """)
    Page<GetAllModelsResponse> findAllAsDto(Pageable pageable);
}
