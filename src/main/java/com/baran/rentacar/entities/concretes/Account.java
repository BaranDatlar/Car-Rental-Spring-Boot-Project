package com.baran.rentacar.entities.concretes;

import com.baran.rentacar.entities.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq_gen")
    @SequenceGenerator(name = "accounts_seq_gen", sequenceName = "accounts_seq", allocationSize = 50)
    private Long id;

    @Column(name = "account_number",unique = true,nullable = false,length = 34)
    private String accountNumber;

    @Column(name = "owner_name",nullable = false)
    private String ownerName;

    @Column(name = "balance",nullable = false,precision = 19,scale = 4)
    private BigDecimal balance;

    @Column(name = "currency",nullable = false,length = 3)
    private String currency;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
