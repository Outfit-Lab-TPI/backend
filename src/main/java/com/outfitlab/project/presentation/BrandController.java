package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.helper.CodeFormatter;
import com.outfitlab.project.domain.model.dto.BrandDTO;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import com.outfitlab.project.domain.useCases.brand.*;
import com.outfitlab.project.domain.exceptions.BrandsNotFoundException;
import com.outfitlab.project.domain.useCases.user.ConvertToAdmin;
import com.outfitlab.project.presentation.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BrandController {

    private final GetAllBrands getAllMarcas;
    private final GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode;
    private final ActivateBrand activateBrand;
    private final DesactivateBrand desactivateBrand;
    private final GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers;
    private final GetNotificationsNewBrands getNotificationsNewBrands;

    public BrandController(GetAllBrands getAllMarcas, GetBrandAndGarmentsByBrandCode getBrandAndGarmentsByBrandCode,
                           ActivateBrand activateBrand, DesactivateBrand desactivateBrand, GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers, GetNotificationsNewBrands getNotificationsNewBrands){
        this.getAllMarcas = getAllMarcas;
        this.getBrandAndGarmentsByBrandCode = getBrandAndGarmentsByBrandCode;
        this.activateBrand = activateBrand;
        this.desactivateBrand = desactivateBrand;
        this.getAllBrandsWithRelatedUsers = getAllBrandsWithRelatedUsers;
        this.getNotificationsNewBrands = getNotificationsNewBrands;
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

    @GetMapping("/marcas/all")
    public ResponseEntity<?> getBrandsWithRelatedUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            return ResponseEntity.ok(buildResponseUserWithBrands(this.getAllBrandsWithRelatedUsers.execute(page)));
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

    @PatchMapping("/marcas/activate/{brandCode}")
    public ResponseEntity<?> activateUserByBrandCode(@PathVariable String brandCode) {
        try {
            return ResponseEntity.ok(this.activateBrand.execute(brandCode));
        } catch (BrandsNotFoundException | UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/marcas/desactivate/{brandCode}")
    public ResponseEntity<?> desactivateUserByBrandCode(@PathVariable String brandCode) {
        try {
            return ResponseEntity.ok(this.desactivateBrand.execute(brandCode));
        } catch (BrandsNotFoundException | UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/marcas/notifications-new-brands")
    public ResponseEntity<?> getNotificationsNewBrands() {
        try {

            return ResponseEntity.ok(getBodyResponseForNotifications());
        } catch (BrandsNotFoundException | UserNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    private Map<String, Object> getBodyResponseForNotifications() {
        List<UserWithBrandsDTO> records = this.getNotificationsNewBrands.execute();;
        Map<String, Object> body = new HashMap<>();
        body.put("totalElements", records.size());
        body.put("notifications", records);
        return body;
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

    private static Map<String, Object> buildResponseUserWithBrands(Page<UserWithBrandsDTO> response) {
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
