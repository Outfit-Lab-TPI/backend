package com.outfitlab.project.presentation;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.exceptions.PredictionTimeoutException;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.presentation.dto.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/fashion")
public class FashnController {

    private final CombinePrendas combinePrendas;
    private static final Logger log = LoggerFactory.getLogger(FashnController.class);

    public FashnController(CombinePrendas combinePrendas) {this.combinePrendas = combinePrendas;}

    @PostMapping("/combinar-prendas")
    public ResponseEntity<GeneratedResponse> combine(@RequestBody CombineRequest request) {
        System.out.println(request.toString());

        try {
            return ResponseEntity.ok(new GeneratedResponse("OK", this.combinePrendas.execute(CombineRequest.convertToDomainModel(request))));
        } catch (PredictionFailedException e) {
            return buildHttpResponse(e.getMessage(), "FAILED", BAD_GATEWAY);
        } catch (PredictionTimeoutException e) {
            return buildHttpResponse(e.getMessage(), "TIMEOUT", GATEWAY_TIMEOUT);
        } catch (FashnApiException e) {
            return buildHttpResponse(e.getMessage(), "ERROR",  BAD_GATEWAY);
        } catch (Exception e) {
            return buildHttpResponse("Error inesperado: " + e.getMessage(), "ERROR", INTERNAL_SERVER_ERROR);
        }
    }

    @NotNull
    private static ResponseEntity<GeneratedResponse> buildHttpResponse(String message, String status, HttpStatus badGateway) {
        log.info(message);
        return ResponseEntity.status(badGateway).body(new GeneratedResponse(status, null, message));
    }
}

