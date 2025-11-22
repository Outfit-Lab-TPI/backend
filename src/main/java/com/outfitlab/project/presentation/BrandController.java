package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.useCases.brand.ActivateBrand;
import com.outfitlab.project.domain.useCases.brand.GetAllBrands;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.useCases.brand.GetBrandAndGarmentsByBrandCode;
import com.outfitlab.project.domain.useCases.user.ConvertToAdmin;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BrandController {

    private final GetAllBrands getAllMarcas;
    private final GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode;
    private final ActivateBrand activateBrand;

    public BrandController(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode, ActivateBrand activateBrand){
        this.getAllMarcas = getAllMarcas;
        this.getBrandAndGarmentsByBrandCode = getBrandAndGarmentsByBrandCode;
        this.activateBrand = activateBrand;
    }

    @GetMapping("/marcas")
    public ResponseEntity<?> getMarcas(@RequestParam(defaultValue = "0") int page) {
        try {
            return ResponseEntity.ok(buildResponse(this.getAllMarcas.execute(page)));
        } catch (BrandsNotFoundException | PageLessThanZeroException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/marcas/{brandCode}")
    public ResponseEntity<?> getBrandAndGarmentsByBrandCode(@PathVariable String brandCode,
                                               @RequestParam(defaultValue = "0") int page) {
        try {
            return ResponseEntity.ok(this.getBrandAndGarmentsByBrandCode.execute(brandCode, page));
        } catch (BrandsNotFoundException | PageLessThanZeroException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/activate/{brandCode}")
    public ResponseEntity<?> activateUser(@PathVariable String brandCode) {
        try {
            return ResponseEntity.ok(this.activateBrand.execute(brandCode));
        } catch (BrandsNotFoundException | UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    private static Map<String, Object> buildResponse(Page<BrandDTO> response) {
        Map<String, Object> pageResponse = new HashMap<>();
        pageResponse.put("content", response.getContent());
        pageResponse.put("page", response.getNumber());
        pageResponse.put("size", response.getSize());
        pageResponse.put("totalElements", response.getTotalElements());
        pageResponse.put("totalPages", response.getTotalPages());
        pageResponse.put("last", response.isLast());
        return pageResponse;
    }
}
