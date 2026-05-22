package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.responses.GetAllBrandsResponse;
import com.baran.rentacar.business.rules.BrandBusinessRules;
import com.baran.rentacar.core.utilities.mappers.ModelMapperManager;
import com.baran.rentacar.core.utilities.mappers.ModelMapperService;
import com.baran.rentacar.dataAccess.abstracts.BrandRepository;
import com.baran.rentacar.entities.concretes.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandManagerTest {
    @Mock BrandRepository brandRepository;
    @Mock BrandBusinessRules brandBusinessRules;
    @Spy ModelMapperService modelMapperService = new ModelMapperManager(new ModelMapper());
    @InjectMocks BrandManager brandManager;

    @Test
    void getAll_shouldReturnPageOfBrands() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Brand brand1 = new Brand(2, "aud", null);
        Brand brand2 = new Brand(5, "fiat", null);
        Page<Brand> brandsPage = new PageImpl<>(List.of(brand1, brand2), pageable, 2);
        when(brandRepository.findAll(pageable)).thenReturn(brandsPage);

        // Act
        Page<GetAllBrandsResponse> result = brandManager.getAll(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals("aud", result.getContent().get(0).getName());
        assertEquals("fiat", result.getContent().get(1).getName());
        verify(brandRepository).findAll(pageable);
    }
}