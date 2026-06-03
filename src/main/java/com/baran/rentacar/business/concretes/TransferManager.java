package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.abstracts.TransferService;
import com.baran.rentacar.business.requests.CreateTransferRequest;
import com.baran.rentacar.business.responses.GetTransferResponse;
import com.baran.rentacar.business.rules.TransferBusinessRules;
import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.core.utilities.mappers.ModelMapperService;
import com.baran.rentacar.dataAccess.abstracts.AccountRepository;
import com.baran.rentacar.dataAccess.abstracts.TransferRepository;
import com.baran.rentacar.entities.concretes.Account;
import com.baran.rentacar.entities.concretes.TransferTransaction;
import com.baran.rentacar.entities.concretes.enums.TransferStatus;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransferManager implements TransferService {

    private final TransferRepository transferRepository;
    private final ModelMapperService modelMapperService;
    private final AccountRepository accountRepository;
    private final TransferBusinessRules transferBusinessRules;

    @Override
    @Transactional
    @Retryable(retryFor = {ObjectOptimisticLockingFailureException.class,
            DataIntegrityViolationException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 50, multiplier = 2))
    public GetTransferResponse transfer(String idempotencyKey, CreateTransferRequest createTransferRequest) {

        Optional<TransferTransaction> existing = transferRepository.findByReference(idempotencyKey);
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        transferBusinessRules.accountsMustBeDifferent(createTransferRequest.getFromAccountNumber(), createTransferRequest.getToAccountNumber());

        Account from = transferBusinessRules.accountMustExist(createTransferRequest.getFromAccountNumber());
        Account to = transferBusinessRules.accountMustExist(createTransferRequest.getToAccountNumber());

        transferBusinessRules.currenciesMustMatch(from.getCurrency(), to.getCurrency(), createTransferRequest.getCurrency());
        transferBusinessRules.balanceMustBeSufficient(from, createTransferRequest.getAmount());

        from.setBalance(from.getBalance().subtract(createTransferRequest.getAmount()));
        to.setBalance(to.getBalance().add(createTransferRequest.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);


        TransferTransaction tx = new TransferTransaction();
        tx.setReference(idempotencyKey);
        tx.setFromAccount(from);
        tx.setToAccount(to);
        tx.setAmount(createTransferRequest.getAmount());
        tx.setCurrency(createTransferRequest.getCurrency());
        tx.setStatus(TransferStatus.COMPLETED);
        tx.setDescription(createTransferRequest.getDescription());
        TransferTransaction saved = transferRepository.save(tx);

        GetTransferResponse response = modelMapperService.forResponse().map(saved, GetTransferResponse.class);
        response.setFromAccountNumber(from.getAccountNumber());
        response.setToAccountNumber(to.getAccountNumber());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GetTransferResponse getByReference(String reference) {
        TransferTransaction tx = transferRepository.findByReference(reference)
                .orElseThrow(() -> new BusinessException("Transfer not found"));
        GetTransferResponse response = modelMapperService.forResponse().map(tx, GetTransferResponse.class);
        response.setFromAccountNumber(tx.getFromAccount().getAccountNumber());
        response.setToAccountNumber(tx.getToAccount().getAccountNumber());
        return response;
    }

    @Override
    @Transactional
    public GetTransferResponse transferPessimistic(CreateTransferRequest createTransferRequest) {
        transferBusinessRules.accountsMustBeDifferent(createTransferRequest.getFromAccountNumber(), createTransferRequest.getToAccountNumber());

        String firstNo  = createTransferRequest.getFromAccountNumber().compareTo(createTransferRequest.getToAccountNumber()) < 0
                ? createTransferRequest.getFromAccountNumber() : createTransferRequest.getToAccountNumber();
        String secondNo = firstNo.equals(createTransferRequest.getFromAccountNumber())
                ? createTransferRequest.getToAccountNumber() : createTransferRequest.getFromAccountNumber();

        Account first = accountRepository.findByAccountNumberForUpdate(firstNo).orElseThrow(() -> new BusinessException("Account not found" + firstNo));
        Account second = accountRepository.findByAccountNumberForUpdate(secondNo).orElseThrow(() -> new BusinessException("Account not found" + secondNo));

        Account from = firstNo.equals(createTransferRequest.getFromAccountNumber()) ? first : second;
        Account to = first == from ? second : first;

        transferBusinessRules.currenciesMustMatch(from.getCurrency(), to.getCurrency(), createTransferRequest.getCurrency());
        transferBusinessRules.balanceMustBeSufficient(from, createTransferRequest.getAmount());

        from.setBalance(from.getBalance().subtract(createTransferRequest.getAmount()));
        to.setBalance(to.getBalance().add(createTransferRequest.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        TransferTransaction tx = new TransferTransaction();
        tx.setReference(UUID.randomUUID().toString());
        tx.setFromAccount(from);
        tx.setToAccount(to);
        tx.setAmount(createTransferRequest.getAmount());
        tx.setCurrency(createTransferRequest.getCurrency());
        tx.setStatus(TransferStatus.COMPLETED);
        tx.setDescription(createTransferRequest.getDescription());
        TransferTransaction saved = transferRepository.save(tx);

        GetTransferResponse response = modelMapperService.forResponse().map(saved, GetTransferResponse.class);
        response.setFromAccountNumber(from.getAccountNumber());
        response.setToAccountNumber(to.getAccountNumber());
        return response;

    }

    @Recover
    public GetTransferResponse recover(ObjectOptimisticLockingFailureException ex,
                                       String idempotencyKey,
                                       CreateTransferRequest createTransferRequest) {
        throw new BusinessException("Transfer failed due to high concurrency, please retry");
    }

    private GetTransferResponse toResponse(TransferTransaction tx) {
        GetTransferResponse response = modelMapperService.forResponse().map(tx, GetTransferResponse.class);
        response.setFromAccountNumber(tx.getFromAccount().getAccountNumber());
        response.setToAccountNumber(tx.getToAccount().getAccountNumber());
        return response;
    }
}