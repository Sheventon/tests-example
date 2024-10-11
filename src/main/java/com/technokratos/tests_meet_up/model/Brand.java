package com.technokratos.tests_meet_up.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int maxCars;

    private int currentCars;

    @OneToMany(mappedBy = "brand")
    private List<Car> cars;
}
