package com.baran.rentacar.business.rules;


import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.dataAccess.abstracts.BrandRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class BrandBusinessRules {
    private BrandRepository brandRepository;

    public void checkIfBrandNameExists(String brandName) {
        if(brandRepository.existsByName(brandName)) {
            throw new BusinessException("Brand with name " + brandName + " already exists");
        }
    }
}
