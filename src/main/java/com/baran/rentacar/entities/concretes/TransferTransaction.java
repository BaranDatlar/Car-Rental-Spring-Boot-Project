package com.baran.rentacar.entities.concretes;

import com.baran.rentacar.entities.abstracts.BaseEntity;
import com.baran.rentacar.entities.concretes.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer_transactions")
public class TransferTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reference",unique = true, nullable = false, length = 36)
    private String reference;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    @Column(name = "currency",nullable = false, length = 3)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransferStatus status;
    @Column(name = "description")
    private String description;
}
