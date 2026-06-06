package com.baran.rentacar.entities.concretes;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "models")
@Entity
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "models_seq_gen")
    @SequenceGenerator(name = "models_seq_gen", sequenceName = "models_seq", allocationSize = 50)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "model")
    private List<Car> cars;
}
