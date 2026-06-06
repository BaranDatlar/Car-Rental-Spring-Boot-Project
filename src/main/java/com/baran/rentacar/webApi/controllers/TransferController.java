package com.baran.rentacar.webApi.controllers;

import com.baran.rentacar.business.abstracts.TransferService;
import com.baran.rentacar.business.requests.CreateTransferRequest;
import com.baran.rentacar.business.responses.GetTransferResponse;
import com.baran.rentacar.business.responses.TransferResult;
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
        TransferResult result = transferService.transfer(idempotencyKey,createTransferRequest);
        var response = result.response();
        URI location = URI.create("/api/v1/transfers/" + response.getReference());
        return result.created()
                ? ResponseEntity.created(location).body(response)
                : ResponseEntity.ok().body(response);
    }

    @PostMapping("/pessimistic")
    public ResponseEntity<GetTransferResponse> transferPessimistic(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateTransferRequest createTransferRequest){
        TransferResult result = transferService.transferPessimistic(idempotencyKey,createTransferRequest);
        var response = result.response();
        URI location = URI.create("/api/v1/transfers/" + response.getReference());
        return result.created()
                ? ResponseEntity.created(location).body(response)
                : ResponseEntity.ok().body(response);
    }

    @GetMapping("/{reference}")
    public ResponseEntity<GetTransferResponse> getByReference(@Valid @PathVariable String reference){
        return ResponseEntity.ok(transferService.getByReference(reference));
    }
}
