package com.baran.rentacar.webApi.controllers;

import com.baran.rentacar.business.abstracts.TransferService;
import com.baran.rentacar.business.requests.CreateTransferRequest;
import com.baran.rentacar.business.responses.GetTransferResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/transfers")
@AllArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<GetTransferResponse> transfer(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateTransferRequest createTransferRequest){
        GetTransferResponse response = transferService.transfer(idempotencyKey,createTransferRequest);
        URI location = URI.create("/api/v1/transfers" + response.getReference());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/pessimistic")
    public ResponseEntity<GetTransferResponse> transferPessimistic(@Valid @RequestBody CreateTransferRequest createTransferRequest){
        GetTransferResponse response = transferService.transferPessimistic(createTransferRequest);
        URI location = URI.create("/api/v1/transfers" + response.getReference());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{reference}")
    public ResponseEntity<GetTransferResponse> getByReference(@Valid @PathVariable String reference){
        return ResponseEntity.ok(transferService.getByReference(reference));
    }
}
