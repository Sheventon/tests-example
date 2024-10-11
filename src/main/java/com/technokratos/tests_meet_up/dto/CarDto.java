package com.technokratos.tests_meet_up.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    private Long id;

    private String brandName;

    private String model;

    private int year;

    private String color;

    private float engine;

    private int horsesPower;
}
