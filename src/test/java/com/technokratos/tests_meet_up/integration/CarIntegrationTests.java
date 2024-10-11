package com.technokratos.tests_meet_up.integration;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.exception.model.BrandNotExistsException;
import com.technokratos.tests_meet_up.exception.model.MaximumCarsReachedException;
import com.technokratos.tests_meet_up.model.Brand;
import com.technokratos.tests_meet_up.model.Car;
import com.technokratos.tests_meet_up.repository.BrandRepository;
import com.technokratos.tests_meet_up.repository.CarRepository;
import com.technokratos.tests_meet_up.service.CarService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CarIntegrationTests {

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    public static void beforeAll() {
        POSTGRES.start();
    }

    @AfterAll
    public static void afterAll() {
        POSTGRES.stop();
    }

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    private final CarService carService;

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;

    @Autowired
    public CarIntegrationTests(CarService carService,
                               CarRepository carRepository,
                               BrandRepository brandRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
    }

    @BeforeEach
    public void setUp() {
        Brand brand = Brand.builder()
                .name("Chery")
                .maxCars(10)
                .currentCars(10)
                .cars(Collections.emptyList())
                .build();
        Brand brand2 = Brand.builder()
                .name("BMW")
                .maxCars(5)
                .currentCars(0)
                .cars(Collections.emptyList())
                .build();
        brandRepository.save(brand);
        brandRepository.save(brand2);

        Car car = Car.builder()
                .id(1L)
                .brand(brand)
                .model("5 Series")
                .year(2020)
                .color("WHITE")
                .engine(3.0f)
                .horsesPower(249)
                .build();
        Car car2 = Car.builder()
                .id(2L)
                .brand(brand)
                .model("7 Series")
                .year(2020)
                .color("BLACK")
                .engine(3.0f)
                .horsesPower(350)
                .build();
        carRepository.save(car);
        carRepository.save(car2);
    }

    @AfterEach
    public void clear() {
        carRepository.deleteAll();
        brandRepository.deleteAll();
    }

    private static final CarDto BMM = CarDto.builder()
            .brandName("BMM")
            .model("3 Series")
            .year(2023)
            .color("BLACK")
            .engine(2.0F)
            .horsesPower(190)
            .build();

    private static final CarDto BMW = CarDto.builder()
            .brandName("BMW")
            .model("3 Series")
            .year(2023)
            .color("BLACK")
            .engine(2.0F)
            .horsesPower(190)
            .build();

    private static final CarDto CHERY = CarDto.builder()
            .brandName("Chery")
            .model("Tiggo 4")
            .year(2024)
            .color("RED")
            .engine(1.5F)
            .horsesPower(147)
            .build();

    @Test
    public void testCreateCarWithNotExistsBrand() {
        assertThrows(BrandNotExistsException.class, () -> carService.createCar(BMM));
    }

    @Test
    public void testCreateCarWithMaxCarsCountReached() {
        assertThrows(MaximumCarsReachedException.class, () -> carService.createCar(CHERY));
    }

    @Test
    public void testCreateCarWithExistsBrand() {
        CarDto actual = carService.createCar(BMW);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(BMW.getBrandName(), actual.getBrandName()),
                () -> assertEquals(1, brandRepository.findByName(BMW.getBrandName()).get().getCurrentCars()),
                () -> assertEquals(2023, actual.getYear()),
                () -> assertEquals("BLACK", actual.getColor()),
                () -> assertEquals(2.0F, actual.getEngine()),
                () -> assertEquals(190, actual.getHorsesPower())
        );
    }

    @Test
    public void testGetAllCars() {
        List<CarDto> cars = carService.getAll();
        assertAll(
                () -> assertNotNull(cars),
                () -> assertEquals(2, cars.size())
        );
    }
}
