package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.CreateTransferRequest;
import com.baran.rentacar.business.responses.GetTransferResponse;

public interface TransferService {
    GetTransferResponse transfer(String idempotencyKey, CreateTransferRequest createTransferRequest);
    GetTransferResponse getByReference(String reference);
    GetTransferResponse transferPessimistic(CreateTransferRequest createTransferRequest);
}
