package com.baran.rentacar.webApi.controllers;

import com.baran.rentacar.business.abstracts.BrandService;
import com.baran.rentacar.business.requests.CreateBrandRequest;
import com.baran.rentacar.business.requests.UpdateBrandRequest;
import com.baran.rentacar.business.responses.GetAllBrandsResponse;
import com.baran.rentacar.business.responses.GetByIdBrandResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandService brandService;

    @GetMapping()
    public ResponseEntity<Page<GetAllBrandsResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(brandService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public GetByIdBrandResponse getById(@PathVariable int id) {
        return brandService.getById(id);
    }

    @PostMapping()
    public ResponseEntity<Void> createBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest) {
        brandService.add(createBrandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable int id, @Valid @RequestBody UpdateBrandRequest updateBrandRequest) {
        brandService.update(id,updateBrandRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable int id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GetAllBrandsResponse>> searchBrand(@RequestParam String search, Pageable pageable) {
        return ResponseEntity.ok(brandService.search(search, pageable));
    }
}
