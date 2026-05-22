package com.baran.rentacar.webApi.controllers;

import com.baran.rentacar.business.abstracts.ModelService;
import com.baran.rentacar.business.requests.CreateModelRequest;
import com.baran.rentacar.business.responses.GetAllModelsResponse;
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
@RequestMapping("/api/models")
public class ModelsController {

    private final ModelService modelService;

    @GetMapping()
    public ResponseEntity<Page<GetAllModelsResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(modelService.getAll(pageable));
    }

    @PostMapping()
    public ResponseEntity<Void> createBrand(@Valid @RequestBody CreateModelRequest createModelRequest) {
        modelService.add(createModelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
