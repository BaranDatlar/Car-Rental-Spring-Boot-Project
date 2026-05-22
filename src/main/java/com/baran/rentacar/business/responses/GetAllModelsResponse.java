package com.baran.rentacar.business.responses;


import com.baran.rentacar.entities.concretes.Brand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllModelsResponse {
    private int id;
    private String name;
    private String brandName;
}
