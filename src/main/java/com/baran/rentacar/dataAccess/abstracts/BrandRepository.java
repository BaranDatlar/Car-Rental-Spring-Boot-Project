package com.baran.rentacar.dataAccess.abstracts;

import com.baran.rentacar.entities.concretes.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
        public boolean existsByName(String name);
        Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
