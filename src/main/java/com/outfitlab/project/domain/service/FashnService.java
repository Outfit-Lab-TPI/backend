package com.outfitlab.project.domain.service;

import com.outfitlab.project.domain.exceptions.FashnApiException;
import com.outfitlab.project.domain.exceptions.PredictionFailedException;
import com.outfitlab.project.domain.useCases.fashn.CheckRequestCombine;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.domain.useCases.fashn.PollStatusUntilCombineComplete;
import com.outfitlab.project.presentation.dto.CombineRequest;
import org.springframework.stereotype.Service;

@Service
public class FashnService {

    private final String AVATAR_HOMBRE = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/men-with-jean-menmodel.jpg";
    private final String AVATAR_MUJER = "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/models_images/avatar-mujer-real-grande.png";


    private final CheckRequestCombine checkRequestCombine;
    private final CombinePrendas combinePrendas;
    private final PollStatusUntilCombineComplete pollStatusUntilCombineComplete;

    public FashnService(CheckRequestCombine checkRequestCombine, CombinePrendas combinePrendas, PollStatusUntilCombineComplete pollStatusUntilCombineComplete) {
        this.checkRequestCombine = checkRequestCombine;
        this.combinePrendas = combinePrendas;
        this.pollStatusUntilCombineComplete = pollStatusUntilCombineComplete;
    }

    public String combine(CombineRequest req) throws FashnApiException, PredictionFailedException {
        String avatarUrl = req.getEsHombre() ? AVATAR_HOMBRE : AVATAR_MUJER;
        String superior = req.getSuperior();
        String inferior = req.getInferior();
        String category;

        String id;
        String result;

        System.out.println("---------------- Iniciando combinación de prendas...");
        System.out.println("---------------- Avatar seleccionado: " + avatarUrl);
        System.out.println("---------------- URL prenda superior: " + superior);
        System.out.println("---------------- URL prenda inferior: " + inferior);

        this.checkRequestCombine.execute(superior, inferior);

        if (superior != null && (inferior == null || inferior.isBlank())) {
            category = "tops";
            id = this.combinePrendas.execute(req, category, avatarUrl);
            result =  this.pollStatusUntilCombineComplete.execute(id);

            return result;
        }

        if (inferior != null && (superior == null || superior.isBlank())) {
            category = "bottoms";
            id = this.combinePrendas.execute(req, category, avatarUrl);
            result = this.pollStatusUntilCombineComplete.execute(id);

            return result;
        }

        // --------------- en estas 3 lineas, copio la req y armo una req para superior (con inferior null, la q viene de la firma)
        //                 y la copia que seria la inferior (a la cual le vacio superior),
        //                 porque? porque el combinePrendas valida si tiene superior, envia la superior y viceversa.
        CombineRequest reqInferior = req;
        reqInferior.setSuperior(null);
        req.setInferior(null);
        // --------------------------------------------------------------------------------------------------------------------------

        category = "tops";
        String firstPredictionId = this.combinePrendas.execute(req, category, avatarUrl); //primero te paso el avatar defaul/user en un futuro
        String firstResultUrl = this.pollStatusUntilCombineComplete.execute(firstPredictionId);

        category = "bottoms";
        String secondPredictionId = this.combinePrendas.execute(reqInferior, category, firstResultUrl); //desp paso el avatar combinado con la prenda anterior

        return this.pollStatusUntilCombineComplete.execute(secondPredictionId);
    }
}
