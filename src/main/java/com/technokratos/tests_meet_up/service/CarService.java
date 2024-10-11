package com.technokratos.tests_meet_up.service;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.exception.model.BrandAlreadyExistsException;
import com.technokratos.tests_meet_up.exception.model.MaximumCarsReachedException;
import com.technokratos.tests_meet_up.mapper.CarMapper;
import com.technokratos.tests_meet_up.model.Brand;
import com.technokratos.tests_meet_up.model.Car;
import com.technokratos.tests_meet_up.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    private final BrandService brandService;

    public CarDto createCar(CarDto carDto) {
        Brand brand = brandService.getBrandByName(carDto.getBrandName());
        if (brand.getCurrentCars() < brand.getMaxCars()) {
            brand.setCurrentCars(brand.getCurrentCars() + 1);
            brandService.updateBrandCurrentCars(brand);
            Car car = carMapper.toCar(carDto);
            car.setBrand(brand);
            return carMapper.fromCar(carRepository.save(car));
        } else {
            throw new MaximumCarsReachedException("Maximum cars reached");
        }
    }

    public List<CarDto> getAll() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::fromCar)
                .collect(Collectors.toList());
    }
}
