package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.abstracts.BrandService;
import com.baran.rentacar.business.requests.CreateBrandRequest;
import com.baran.rentacar.business.requests.UpdateBrandRequest;
import com.baran.rentacar.business.responses.GetAllBrandsResponse;
import com.baran.rentacar.business.responses.GetByIdBrandResponse;
import com.baran.rentacar.business.rules.BrandBusinessRules;
import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.core.utilities.mappers.ModelMapperService;
import com.baran.rentacar.dataAccess.abstracts.BrandRepository;
import com.baran.rentacar.entities.concretes.Brand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class BrandManager implements BrandService {

    private BrandRepository brandRepository;
    private ModelMapperService modelMapperService;
    private BrandBusinessRules brandBusinessRules;

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllBrandsResponse> getAll(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        return brands.map(brand -> modelMapperService.forResponse().map(brand, GetAllBrandsResponse.class));
    }

    @Override
    @Transactional()
    public void add(CreateBrandRequest createBrandRequest) {
        brandBusinessRules.checkIfBrandNameExists(createBrandRequest.getName());
        Brand brand = modelMapperService.forRequest().map(createBrandRequest, Brand.class);
        brandRepository.save(brand);
    }

    @Override
    @Transactional()
    public void update(int id, UpdateBrandRequest updateBrandRequest) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BusinessException("Brand not found"));
        modelMapperService.forRequest().map(updateBrandRequest, brand);
        //brandRepository.save(brand);
    }

    @Override
    @Transactional()
    public void delete(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BusinessException("Brand not found" + id));
        brandRepository.delete(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public GetByIdBrandResponse getById(int brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BusinessException("Brand not found" + brandId));
        return modelMapperService.forResponse().map(brand,GetByIdBrandResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllBrandsResponse> search(String q, Pageable pageable) {
        Page<Brand> brands = brandRepository.findByNameContainingIgnoreCase(q, pageable);
        return brands.map(brand -> modelMapperService.forResponse().map(brand, GetAllBrandsResponse.class));
    }
}
