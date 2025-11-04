package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FailedToAddFavoritesException;
import com.outfitlab.project.domain.exceptions.GarmentNotFoundException;
import com.outfitlab.project.domain.exceptions.UserGarmentFavoriteAlreadyExistsException;
import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.model.dto.GarmentDTO;
import com.outfitlab.project.domain.useCases.garment.AddGarmentToFavorite;
import com.outfitlab.project.domain.useCases.garment.GetGarmentsByType;
import com.outfitlab.project.presentation.dto.AllGarmentsResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/garments")
public class GarmentController {

    private final GetGarmentsByType getGarmentsByType;
    private final AddGarmentToFavorite addGarmentToFavourite;

    public GarmentController(GetGarmentsByType getGarmentsByType, AddGarmentToFavorite addGarmentToFavourite) {
        this.getGarmentsByType = getGarmentsByType;
        this.addGarmentToFavourite = addGarmentToFavourite;
    }

    @GetMapping("/{typeOfGarment}")
    public ResponseEntity<?> getGarmentsByType(@RequestParam(defaultValue = "0") int page,
                                              @PathVariable String typeOfGarment){
        try {
            return ResponseEntity.ok(buildResponse(this.getGarmentsByType.execute(typeOfGarment, page)
                    .map(GarmentDTO::convertModelToDTO)));
        } catch (GarmentNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
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
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
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
        } catch (GarmentNotFoundException | UserNotFoundException | UserGarmentFavoriteAlreadyExistsException | FailedToAddFavoritesException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}
