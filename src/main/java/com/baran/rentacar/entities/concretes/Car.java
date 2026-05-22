package com.baran.rentacar.entities.concretes;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="plate")
    private String plate;

    @Column(name = "daily_price", precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Column(name="modelYear")
    private int modelYear;

    @Column(name="state")
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;
}
