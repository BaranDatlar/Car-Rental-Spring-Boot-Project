package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.CreateBrandRequest;
import com.baran.rentacar.business.requests.UpdateBrandRequest;
import com.baran.rentacar.business.responses.GetAllBrandsResponse;
import com.baran.rentacar.business.responses.GetByIdBrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    public Page<GetAllBrandsResponse> getAll(Pageable pageable);
    public GetByIdBrandResponse getById(int brandId);
    public Page<GetAllBrandsResponse> search(String q, Pageable pageable);
    public void add(CreateBrandRequest createBrandRequest);
    public void update(int id, UpdateBrandRequest updateBrandRequest);
    public void delete(int id);
}
