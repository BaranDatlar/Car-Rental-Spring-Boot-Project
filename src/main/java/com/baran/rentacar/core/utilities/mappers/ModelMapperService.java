package com.baran.rentacar.core.utilities.mappers;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {
    public ModelMapper forRequest();
    public ModelMapper forResponse();
}
