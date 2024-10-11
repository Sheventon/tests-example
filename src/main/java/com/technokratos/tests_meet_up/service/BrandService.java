package com.technokratos.tests_meet_up.service;

import com.technokratos.tests_meet_up.dto.BrandDto;
import com.technokratos.tests_meet_up.exception.model.BrandAlreadyExistsException;
import com.technokratos.tests_meet_up.exception.model.BrandNotExistsException;
import com.technokratos.tests_meet_up.mapper.BrandMapper;
import com.technokratos.tests_meet_up.model.Brand;
import com.technokratos.tests_meet_up.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandDto createBrand(BrandDto brandDto) {
        if (!brandRepository.existsByName(brandDto.getName())) {
            brandDto.setCurrentCars(0);
            return brandMapper.fromBrand(brandRepository.save(brandMapper.toBrand(brandDto)));
        } else {
            throw new BrandAlreadyExistsException(String.format("Brand with name %s already exists", brandDto.getName()));
        }
    }

    public BrandDto getByName(String name) {
        return brandMapper.fromBrand(brandRepository.findByName(name)
                .orElseThrow(() -> new BrandNotExistsException(String.format("Brand with name %s not found", name))));
    }

    public Brand getBrandByName(String name) {
        return brandRepository.findByName(name)
                .orElseThrow(() -> new BrandNotExistsException(String.format("Brand with name %s not found", name)));
    }

    public void updateBrandCurrentCars(Brand brand) {
        brandRepository.save(brand);
    }
}
