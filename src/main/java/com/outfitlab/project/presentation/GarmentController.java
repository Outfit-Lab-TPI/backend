package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.*;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.useCases.garment.*;
import com.outfitlab.project.domain.useCases.tripo.SaveImage;
import com.outfitlab.project.presentation.dto.AllGarmentsResponse;
import com.outfitlab.project.presentation.dto.GarmentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    public GarmentController(GetGarmentsByType getGarmentsByType, AddGarmentToFavorite addGarmentToFavourite, DeleteGarmentFromFavorite deleteGarmentFromFavorite, GetGarmentsFavoritesForUserByEmail getGarmentsFavoritesForUserByEmail, CreateGarment createGarment, SaveImage saveImage) {
        this.getGarmentsByType = getGarmentsByType;
        this.addGarmentToFavourite = addGarmentToFavourite;
        this.deleteGarmentFromFavorite = deleteGarmentFromFavorite;
        this.getGarmentsFavoritesForUserByEmail = getGarmentsFavoritesForUserByEmail;
        this.createGarment = createGarment;
        this.saveImage = saveImage;
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
    public ResponseEntity<?> crearPrenda(@ModelAttribute GarmentRequestDTO request, @AuthenticationPrincipal UserDetails user) {
        System.out.println("Nombre: " + request.getNombre());
        System.out.println("Tipo: " + request.getTipo());
        System.out.println("Color: " + request.getColor());
        System.out.println("Evento: " + request.getEvento());
        System.out.println("Imagen: " + request.getImagen().getOriginalFilename());

        String brandCode = "puma"; //user.marca.brandCode
        try{
            this.createGarment.execute(
                    request.getNombre(),
                    request.getTipo(),
                    request.getColor(),
                    request.getEvento(),
                    brandCode,
                    this.saveImage.execute(request.getImagen(), "garment_images")
            );

            return ResponseEntity.ok("Prenda creada correctamente.");
        }catch (BrandsNotFoundException e){
            return buildResponseEntityError(e.getMessage());
        }
    }

    private ResponseEntity<?> buildResponseEntityError(String message){
        return ResponseEntity
                .status(404)
                .body(message);
    }
}
