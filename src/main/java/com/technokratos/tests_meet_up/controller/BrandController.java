package com.technokratos.tests_meet_up.controller;

import com.technokratos.tests_meet_up.dto.BrandDto;
import com.technokratos.tests_meet_up.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brand")
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public BrandDto createBrand(@RequestBody BrandDto brandDto) {
        return brandService.createBrand(brandDto);
    }

    @GetMapping("/{name}")
    public BrandDto getBrand(@PathVariable String name) {
        return brandService.getByName(name);
    }
}
