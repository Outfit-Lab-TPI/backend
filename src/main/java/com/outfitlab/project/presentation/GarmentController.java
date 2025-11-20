package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.model.PrendaModel;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.useCases.bucketImages.DeleteImage;
import com.outfitlab.project.domain.useCases.garment.*;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import com.outfitlab.project.presentation.dto.AllGarmentsResponse;
import com.outfitlab.project.presentation.dto.GarmentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/garments")
public class GarmentController {

    private final GetGarmentsByType getGarmentsByType;
    private final AddGarmentToFavorite addGarmentToFavourite;
    private final DeleteGarmentFromFavorite deleteGarmentFromFavorite;
    private final GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail;
    private final CreateGarment createGarment;
    private final SaveImage saveImage;
    private final DeleteGarment deleteGarment;
    private final GetGarmentByCode getGarmentByCode;
    private final DeleteImage deleteImage;
    private final UpdateGarment updateGarment;

    public GarmentController(GetGarmentsByType getGarmentsByType, AddGarmentToFavorite addGarmentToFavourite,
                             DeleteGarmentFromFavorite deleteGarmentFromFavorite, GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail,
                             CreateGarment createGarment, SaveImage saveImage, DeleteGarment deleteGarment, GarmentRepository garmentRepository,
                             GetGarmentByCode getGarmentByCode, DeleteImage deleteImage, UpdateGarment updateGarment) {
        this.getGarmentsByType = getGarmentsByType;
        this.addGarmentToFavourite = addGarmentToFavourite;
        this.deleteGarmentFromFavorite = deleteGarmentFromFavorite;
        this.getGarmentsFavoritesForUserByEmail = getGarmentsFavoritesForUserByEmail;
        this.createGarment = createGarment;
        this.saveImage = saveImage;
        this.deleteGarment = deleteGarment;
        this.getGarmentByCode = getGarmentByCode;
        this.deleteImage = deleteImage;
        this.updateGarment = updateGarment;
    }

    @GetMapping("/{typeOfGarment}")
    public ResponseEntity<?> getGarmentsByType(@RequestParam(defaultValue = "0") int page,
                                              @PathVariable String typeOfGarment){
        try {
            return ResponseEntity.ok(buildResponse(this.getGarmentsByType.execute(typeOfGarment, page)
                    .map(GarmentDTO::convertModelToDTO)));
        } catch (GarmentNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGarments(@RequestParam(defaultValue = "0") int page){
        String top = "superior", bottoms = "inferior";

        try {
            return ResponseEntity.ok(
                    new AllGarmentsResponse(
                            buildResponse(this.getGarmentsByType.execute(top, page).map(GarmentDTO::convertModelToDTO)),
                            buildResponse(this.getGarmentsByType.execute(bottoms, page).map(GarmentDTO::convertModelToDTO))
                    )
            );
        } catch (GarmentNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    private static Map<String, Object> buildResponse(Page<GarmentDTO> response) {
        Map<String, Object> pageResponse = new HashMap<>();
        pageResponse.put("content", response.getContent());
        pageResponse.put("page", response.getNumber());
        pageResponse.put("size", response.getSize());
        pageResponse.put("totalElements", response.getTotalElements());
        pageResponse.put("totalPages", response.getTotalPages());
        pageResponse.put("last", response.isLast());
        return pageResponse;
    }

    @GetMapping("/favorite/add/{garmentCode}")
    public ResponseEntity<?> addGarmentToFavorite(@PathVariable String garmentCode){
        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.addGarmentToFavourite.execute(garmentCode, userEmail));
        } catch (GarmentNotFoundException | UserNotFoundException | UserGarmentFavoriteAlreadyExistsException |
                 FavoritesException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/favorite/delete/{garmentCode}")
    public ResponseEntity<?> deleteGarmentFromFavorite(@PathVariable String garmentCode){
        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.deleteGarmentFromFavorite.execute(garmentCode, userEmail));
        } catch (UserGarmentFavoriteNotFoundException | UserNotFoundException | GarmentNotFoundException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites(@RequestParam(defaultValue = "0") int page){
        try {
            String userEmail = "german@gmail.com"; //acá hay que obtenerlo de la session, NO recibirlo por parámetro sino obtenerlo por session, ahora dejo esto pq no tenemos CRUD de user.
            return ResponseEntity.ok(this.getGarmentsFavoritesForUserByEmail.execute(userEmail, page));
        } catch (UserNotFoundException | PageLessThanZeroException | FavoritesException e) {
            return buildResponseEntityError(e.getMessage());
        }
    }

    @PostMapping(value = "/new", consumes = "multipart/form-data")
    public ResponseEntity<?> newGarment(@ModelAttribute GarmentRequestDTO request, @AuthenticationPrincipal UserDetails user) {
        String brandCode = "puma"; //user.marca.brandCode
        try{
            this.createGarment.execute(
                    request.getNombre(),
                    request.getTipo(),
                    request.getColor(),
                    request.getEvento(),
                    brandCode,
                    saveImageAndGetUrl(request.getImagen(), "garment_images")
            );

            return ResponseEntity.ok("Prenda creada correctamente.");
        }catch (BrandsNotFoundException e){
            return buildResponseEntityError(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{garmentCode}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateGarment(@PathVariable String garmentCode, @ModelAttribute GarmentRequestDTO request, @AuthenticationPrincipal UserDetails user) {
        String brandCode = "puma"; //user.marca.brandCode
        try{
            String oldImageUrl = request.getImagen() != null ? getOldImageUrlOfGarment(garmentCode) : "";

            this.updateGarment.execute(
                    request.getNombre(),
                    request.getTipo(),
                    request.getColor(),
                    request.getEvento(),
                    garmentCode,
                    brandCode,
                    checkIfImageIsEmptyToSaveAndGetUrl(request)
            );
            deleteImage(oldImageUrl);

            return ResponseEntity.ok("Prenda acctualizada correctamente.");
        }catch (GarmentNotFoundException e){
            return buildResponseEntityError(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{garmentCode}")
    public ResponseEntity<?> deleteGarment(@PathVariable String garmentCode, @AuthenticationPrincipal UserDetails user) {
        String brandCode = "puma"; //user.marca.brandCode
        try{
            tryToDeleteGarmentAndImage(garmentCode, brandCode);
            return ResponseEntity.ok("Prenda eliminada correctamente.");
        }catch (BrandsNotFoundException | DeleteGarmentException e){
            return buildResponseEntityError(e.getMessage());
        }
    }

    private void tryToDeleteGarmentAndImage(String garmentCode, String brandCode) {
        PrendaModel garment = this.getGarmentByCode.execute(garmentCode);

        deleteImage(garment.getImagenUrl());
        this.deleteGarment.execute(garment, brandCode);
    }

    private ResponseEntity<?> buildResponseEntityError(String message){
        return ResponseEntity
                .status(404)
                .body(message);
    }

    private String getOldImageUrlOfGarment(String garmentCode) {
        return this.getGarmentByCode.execute(garmentCode).getImagenUrl();
    }

    private String checkIfImageIsEmptyToSaveAndGetUrl(GarmentRequestDTO request) {
        return request.getImagen() != null ? saveImageAndGetUrl(request.getImagen(), "garment_images") : "";
    }

    private void deleteImage(String oldImageUrl) {
        if (!oldImageUrl.isEmpty()) this.deleteImage.execute(oldImageUrl);
    }

    private String saveImageAndGetUrl(MultipartFile image, String folder) {
        return this.saveImage.execute(image, folder);
    }
}
