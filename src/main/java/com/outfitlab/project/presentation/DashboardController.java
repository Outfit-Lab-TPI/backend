package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.model.dto.PrendaDashboardDTO.*;
import com.outfitlab.project.domain.useCases.dashboard.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final GetTopPrendas getTopPrendas;
    private final GetActividadPorDias getActividadPorDias;
    private final GetTopCombos getTopCombos;
    private final GetColorConversion getColorConversion;

    public DashboardController(
            GetTopPrendas getTopPrendas,
            GetActividadPorDias getActividadPorDias,
            GetTopCombos getTopCombos,
            GetColorConversion getColorConversion
    ) {
        this.getTopPrendas = getTopPrendas;
        this.getActividadPorDias = getActividadPorDias;
        this.getTopCombos = getTopCombos;
        this.getColorConversion = getColorConversion;
    }

    @GetMapping("/top-prendas")
    public ResponseEntity<List<TopPrenda>> getTopPrendas(
            @RequestParam(defaultValue = "5") int topN,
            @RequestParam(required = false) String brandCode
    ) {
        List<TopPrenda> topPrendas = getTopPrendas.execute(topN, brandCode);
        return ResponseEntity.ok(topPrendas);
    }

    @GetMapping("/actividad-por-dias")
    public ResponseEntity<List<DiaPrueba>> getActividadPorDias() {
        List<DiaPrueba> evolucion = getActividadPorDias.execute();
        return ResponseEntity.ok(evolucion);
    }

    @GetMapping("/top-combos")
    public ResponseEntity<List<ComboPopular>> getTopCombos(
            @RequestParam(defaultValue = "5") int topN,
            @RequestParam(required = false) String brandCode
    ) {
        List<ComboPopular> topCombos = getTopCombos.execute(topN, brandCode);
        return ResponseEntity.ok(topCombos);
    }

    @GetMapping("/color-conversion")
    public ResponseEntity<List<ColorConversion>> getColorConversion(
            @RequestParam(required = false) String brandCode
    ) {
        List<ColorConversion> colorConversion = getColorConversion.execute(brandCode);
        return ResponseEntity.ok(colorConversion);
    }

}
