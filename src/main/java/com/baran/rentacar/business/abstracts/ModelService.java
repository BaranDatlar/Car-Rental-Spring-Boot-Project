package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.CreateModelRequest;
import com.baran.rentacar.business.responses.GetAllModelsResponse;
import com.baran.rentacar.entities.concretes.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService {

    public Page<GetAllModelsResponse> getAll(Pageable pageable);
    public void add(CreateModelRequest createModelRequest);
}
