package com.technokratos.tests_meet_up.unit;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.exception.model.BrandNotExistsException;
import com.technokratos.tests_meet_up.exception.model.MaximumCarsReachedException;
import com.technokratos.tests_meet_up.mapper.CarMapper;
import com.technokratos.tests_meet_up.model.Brand;
import com.technokratos.tests_meet_up.model.Car;
import com.technokratos.tests_meet_up.repository.CarRepository;
import com.technokratos.tests_meet_up.service.BrandService;
import com.technokratos.tests_meet_up.service.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarUnitTests {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @Mock
    private BrandService brandService;

    private static final CarDto CAR_DTO = CarDto.builder()
            .brandName("BMM")
            .model("3 Series")
            .year(2023)
            .color("BLACK")
            .engine(2.0F)
            .horsesPower(190)
            .build();

    @DisplayName("Тест на создание автомобиля с несуществующим брендом")
    @Test
    public void testCreateCarWithNotExistsBrand() {
        when(brandService.getBrandByName(CAR_DTO.getBrandName())).thenThrow(BrandNotExistsException.class);
        assertThrows(BrandNotExistsException.class, () -> carService.createCar(CAR_DTO));
    }

    @DisplayName("Тест на макс число автомобилей")
    @Test
    public void testCreateCarWithMaxCarsCountReached() {
        Brand brand = Brand.builder()
                .id(1L)
                .name("BMW")
                .maxCars(10)
                .currentCars(10)
                .cars(Collections.emptyList())
                .build();
        when(brandService.getBrandByName(CAR_DTO.getBrandName())).thenReturn(brand);
        assertThrows(MaximumCarsReachedException.class, () -> carService.createCar(CAR_DTO));
    }

    @DisplayName("Тест на создание автомобиля с существующим брендом")
    @Test
    public void testCreateCarWithExistsBrand() {
        Brand brand = Brand.builder()
                .id(1L)
                .name("BMW")
                .maxCars(10)
                .currentCars(2)
                .cars(Collections.emptyList())
                .build();

        when(brandService.getBrandByName(CAR_DTO.getBrandName())).thenReturn(brand);
        doNothing().when(brandService).updateBrandCurrentCars(brand);
        when(carMapper.toCar(any(CarDto.class)))
                .thenReturn(new Car(null, null, "3 Series", 2023, "BLACK", 2.0F, 190));
        when(carRepository.save(any(Car.class)))
                .thenReturn(new Car(1L, brand, "3 Series", 2023, "BLACK", 2.0F, 190));
        when(carMapper.fromCar(any(Car.class)))
                .thenReturn(new CarDto(1L, "BMW", "3 Series", 2023, "BLACK", 2.0F, 190));

        CarDto actual = carService.createCar(CAR_DTO);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1L, actual.getId()),
                () -> assertEquals(brand.getName(), actual.getBrandName()),
                () -> assertEquals(3, brand.getCurrentCars()),
                () -> assertEquals(2023, actual.getYear()),
                () -> assertEquals("BLACK", actual.getColor()),
                () -> assertEquals(2.0F, actual.getEngine()),
                () -> assertEquals(190, actual.getHorsesPower())
        );
    }

    @DisplayName("Тест на создание получение всех автомобилей в гараже")
    @Test
    public void testGetAllCars() {
        when(carRepository.findAll())
                .thenReturn(List.of(
                        Car.builder()
                                .id(1L)
                                .brand(Brand.builder()
                                        .name("Lada")
                                        .build()
                                )
                                .model("Granta")
                                .year(2020)
                                .color("WHITE")
                                .engine(1.6f)
                                .horsesPower(106)
                                .build(),
                        Car.builder()
                                .id(2L)
                                .brand(Brand.builder()
                                        .name("Lada")
                                        .build()
                                )
                                .model("Vesta")
                                .year(2023)
                                .color("WHITE")
                                .engine(1.6f)
                                .horsesPower(120)
                                .build()
                        )
                );

        when(carMapper.fromCar(any(Car.class)))
                .thenReturn(new CarDto(1L, "Lada", "Granta", 2020, "WHITE", 1.6F, 106))
                .thenReturn(new CarDto(1L, "Lada", "Vesta", 2023, "WHITE", 1.6F, 120));

        List<CarDto> cars = carService.getAll();
        assertNotNull(cars);
        assertEquals(2, cars.size());
    }
}
