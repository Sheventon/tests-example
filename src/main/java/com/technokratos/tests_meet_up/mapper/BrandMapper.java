package com.technokratos.tests_meet_up.mapper;

import com.technokratos.tests_meet_up.dto.BrandDto;
import com.technokratos.tests_meet_up.model.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toBrand(BrandDto brand);

    BrandDto fromBrand(Brand brand);
}
