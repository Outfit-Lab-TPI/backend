package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.useCases.brand.ActivateBrand;
import com.outfitlab.project.domain.useCases.brand.DesactivateBrand;
import com.outfitlab.project.domain.useCases.brand.GetAllBrands;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.useCases.brand.GetBrandAndGarmentsByBrandCode;
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
    private final DesactivateBrand desactivateBrand;

    public BrandController(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode,
                           ActivateBrand activateBrand, DesactivateBrand desactivateBrand){
        this.getAllMarcas = getAllMarcas;
        this.getBrandAndGarmentsByBrandCode = getBrandAndGarmentsByBrandCode;
        this.activateBrand = activateBrand;
        this.desactivateBrand = desactivateBrand;
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
    public ResponseEntity<?> activateUserByBrandCode(@PathVariable String brandCode) {
        try {
            return ResponseEntity.ok(this.activateBrand.execute(brandCode));
        } catch (BrandsNotFoundException | UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/desactivate/{brandCode}")
    public ResponseEntity<?> desactivateUserByBrandCode(@PathVariable String brandCode) {
        try {
            return ResponseEntity.ok(this.desactivateBrand.execute(brandCode));
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
