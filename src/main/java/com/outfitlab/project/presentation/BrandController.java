package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.useCases.marca.GetAllBrands;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.useCases.marca.GetBrandAndGarmentsByBrandCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BrandController {

    private final GetAllBrands getAllMarcas;
    private final GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode;

    public BrandController(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode){
        this.getAllMarcas = getAllMarcas;
        this.getBrandAndGarmentsByBrandCode = getBrandAndGarmentsByBrandCode;
    }

    @GetMapping("/marcas")
    public ResponseEntity<?> getMarcas(@RequestParam(defaultValue = "0") int page) {
        try {
            return ResponseEntity.ok(this.getAllMarcas.execute(page));
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
}
