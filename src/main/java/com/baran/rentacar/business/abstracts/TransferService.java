package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.CreateTransferRequest;
import com.baran.rentacar.business.responses.GetTransferResponse;
import com.baran.rentacar.business.responses.TransferResult;

public interface TransferService {
    TransferResult transfer(String idempotencyKey, CreateTransferRequest createTransferRequest);
    GetTransferResponse getByReference(String reference);
    TransferResult transferPessimistic(String idempotencyKey,CreateTransferRequest createTransferRequest);
}
