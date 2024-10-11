package com.technokratos.tests_meet_up.controller;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/car")
public class CarController {

    private final CarService carService;

    @PostMapping
    public CarDto createCar(@RequestBody CarDto carDto) {
        return carService.createCar(carDto);
    }

    @GetMapping("/all")
    public List<CarDto> getAllCars() {
        return carService.getAll();
    }
}
