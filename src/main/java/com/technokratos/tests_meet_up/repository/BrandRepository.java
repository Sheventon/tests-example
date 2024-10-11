package com.technokratos.tests_meet_up.repository;

import com.technokratos.tests_meet_up.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    Optional<Brand> findByName(String name);
}
