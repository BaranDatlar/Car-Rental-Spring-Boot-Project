package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.abstracts.ModelService;
import com.baran.rentacar.business.requests.CreateModelRequest;
import com.baran.rentacar.business.responses.GetAllModelsResponse;
import com.baran.rentacar.core.utilities.mappers.ModelMapperService;
import com.baran.rentacar.dataAccess.abstracts.BrandRepository;
import com.baran.rentacar.dataAccess.abstracts.ModelRepository;
import com.baran.rentacar.entities.concretes.Model;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ModelManager implements ModelService {

    private ModelRepository modelRepository;
    private BrandRepository brandRepository;
    private ModelMapperService modelMapperService;

    @Override
    public Page<GetAllModelsResponse> getAll(Pageable pageable) {
//       Page<Model> list = modelRepository.findAll(pageable);
//       return list.map(model -> modelMapperService.forResponse().map(model,GetAllModelsResponse.class));
        return modelRepository.findAllAsDto(pageable);
    }

    @Override
    public void add(CreateModelRequest createModelRequest) {
        Model model = modelMapperService.forRequest().map(createModelRequest, Model.class);
        model.setBrand(brandRepository.getReferenceById(createModelRequest.getBrandId()));
        modelRepository.save(model);
    }
}
