package com.technokratos.tests_meet_up.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {

    private Long id;

    private String name;

    private int maxCars;

    private int currentCars;

    private List<CarDto> cars;
}
