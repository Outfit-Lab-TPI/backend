package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.entities.Prenda;
import com.outfitlab.project.infrastructure.PrendaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PrendaController {
    private final PrendaService prendaService;

    @GetMapping("/prendas")
    public ResponseEntity<?> getAllPrendas() {
        try {
            List<Prenda> prendas = prendaService.getAllPrendas();
            return ResponseEntity.ok(prendas);
        } catch (IOException e) {
            return ResponseEntity
                    .status(500)
                    .body("Error al cargar las prendas: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{codigoMarca}/prendas")
    public ResponseEntity<?> getPrendasByMarca(@PathVariable String codigoMarca) {
        try {
            List<Prenda> prendas = prendaService.getPrendasByMarca(codigoMarca);
            if (prendas.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body("No se encontraron prendas para la marca: " + codigoMarca);
            }
            return ResponseEntity.ok(prendas);
        } catch (IOException e) {
            return ResponseEntity
                    .status(500)
                    .body("Error al cargar las prendas: " + e.getMessage());
        }
    }

    @GetMapping("/marcas/{codigoMarca}/prendas/{codigoPrenda}")
    public ResponseEntity<?> getPrendaByCodigo(@PathVariable String codigoMarca, @PathVariable String codigoPrenda) {
        try {
            Prenda prenda = prendaService.getPrendaByCodigo(codigoMarca, codigoPrenda);
            if (prenda == null) {
                return ResponseEntity
                        .status(404)
                        .body("No se encontró la prenda con código: " + codigoPrenda + " para la marca: " + codigoMarca);
            }
            return ResponseEntity.ok(prenda);
        } catch (IOException e) {
            return ResponseEntity
                    .status(500)
                    .body("Error al cargar la prenda: " + e.getMessage());
        }
    }
}