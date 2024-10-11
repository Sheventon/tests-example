package com.technokratos.tests_meet_up.integration;

import com.technokratos.tests_meet_up.dto.CarDto;
import com.technokratos.tests_meet_up.exception.utils.ErrorMessage;
import com.technokratos.tests_meet_up.model.Brand;
import com.technokratos.tests_meet_up.model.Car;
import com.technokratos.tests_meet_up.repository.BrandRepository;
import com.technokratos.tests_meet_up.repository.CarRepository;
import com.technokratos.tests_meet_up.service.CarService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class CarFullyIntegrationTests {

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

    private final TestRestTemplate testRestTemplate;

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;

    @Autowired
    public CarFullyIntegrationTests(TestRestTemplate testRestTemplate,
                                    CarRepository carRepository,
                                    BrandRepository brandRepository) {
        this.testRestTemplate = testRestTemplate;
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
        HttpEntity<CarDto> entity = new HttpEntity<>(BMM);
        ResponseEntity<ErrorMessage> error = testRestTemplate
                .exchange("/api/v1/car", HttpMethod.POST, entity, ErrorMessage.class);
        assertAll(
                () -> assertNotNull(error.getBody()),
                () -> assertEquals(
                        String.format("Brand with name %s not found", BMM.getBrandName()),
                        error.getBody().getMessage()
                ),
                () -> assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), error.getBody().getStatusCode())
        );
    }

    @Test
    public void testCreateCarWithMaxCarsCountReached() {
        HttpEntity<CarDto> entity = new HttpEntity<>(CHERY);
        ResponseEntity<ErrorMessage> error = testRestTemplate
                .exchange("/api/v1/car", HttpMethod.POST, entity, ErrorMessage.class);
        assertAll(
                () -> assertNotNull(error.getBody()),
                () -> assertEquals("Maximum cars reached", error.getBody().getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), error.getBody().getStatusCode())
        );

    }

    @Test
    public void testCreateCarWithExistsBrand() {
        HttpEntity<CarDto> entity = new HttpEntity<>(BMW);
        ResponseEntity<CarDto> actual = testRestTemplate
                .exchange("/api/v1/car", HttpMethod.POST, entity, CarDto.class);
        assertAll(
                () -> assertNotNull(actual.getBody()),
                () -> assertNotNull(actual.getBody().getId()),
                () -> assertEquals(BMW.getBrandName(), actual.getBody().getBrandName()),
                () -> assertEquals(1, brandRepository.findByName(BMW.getBrandName()).get().getCurrentCars()),
                () -> assertEquals(2023, actual.getBody().getYear()),
                () -> assertEquals("BLACK", actual.getBody().getColor()),
                () -> assertEquals(2.0F, actual.getBody().getEngine()),
                () -> assertEquals(190, actual.getBody().getHorsesPower())
        );
    }

    @Test
    public void testGetAllCars() {
        ParameterizedTypeReference<List<CarDto>> type = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<CarDto>> actual = testRestTemplate
                .exchange("/api/v1/car/all", HttpMethod.GET, null, type);
        assertAll(
                () -> assertNotNull(actual.getBody()),
                () -> assertEquals(2, actual.getBody().size())
        );
    }
}
