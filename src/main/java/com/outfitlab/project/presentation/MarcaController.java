package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.MarcaModel;
import com.outfitlab.project.infrastructure.model.MarcaEntity;
import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.service.MarcaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping("/marcas")
    public ResponseEntity<?> getMarcas() {
        try {
            List<MarcaModel> marcas = marcaService.getAllMarcas();
            return ResponseEntity.ok(marcas);
        } catch (MarcasNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/marcas/{codigoMarca}")
    public ResponseEntity<?> getMarcaAtributes(@PathVariable String codigoMarca) {
        try {
            MarcaModel marca = marcaService.getMarcaByCodigoMarca(codigoMarca);
            return ResponseEntity.ok(marca);
        } catch (MarcasNotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}
