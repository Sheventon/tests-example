package com.technokratos.tests_meet_up.mapper;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toCar(CarDto carDto);

    @Mapping(source = "car.brand.name", target = "brandName")
    CarDto fromCar(Car car);
}
