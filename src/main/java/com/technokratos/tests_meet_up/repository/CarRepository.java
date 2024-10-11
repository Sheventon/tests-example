package com.technokratos.tests_meet_up.repository;

import com.technokratos.tests_meet_up.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
