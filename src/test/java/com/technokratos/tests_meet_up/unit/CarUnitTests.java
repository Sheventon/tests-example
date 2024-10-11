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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TODO: дополнительно про extensions: https://habr.com/ru/articles/589135/
@ExtendWith(MockitoExtension.class)
public class CarUnitTests {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private CarMapper carMapper;

    private static final CarDto CAR_DTO = CarDto.builder()
            .brandName("BMM")
            .model("3 Series")
            .year(2023)
            .color("BLACK")
            .engine(2.0F)
            .horsesPower(190)
            .build();

    @Test
    public void testCreateCarWithNotExistsBrand() {
        when(brandService.getBrandByName(CAR_DTO.getBrandName())).thenThrow(BrandNotExistsException.class);
        assertThrows(BrandNotExistsException.class, () -> carService.createCar(CAR_DTO));
    }

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
}
