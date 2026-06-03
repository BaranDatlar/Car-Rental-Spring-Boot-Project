package com.baran.rentacar.dataAccess.abstracts;


import com.baran.rentacar.entities.concretes.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferTransaction, Long> {
    Optional<TransferTransaction> findByReference(String reference);
    boolean existsByReference(String reference);
}
