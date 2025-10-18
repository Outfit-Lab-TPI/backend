package com.outfitlab.project.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.entities.Marca;
import com.outfitlab.project.domain.entities.Prenda;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PrendaService {
    private final ObjectMapper objectMapper;
    private final MarcaService marcaService;

    public List<Prenda> getPrendasByMarca(String codigoMarca) throws IOException {
        ClassPathResource resource = new ClassPathResource("data/prendas.json");
        InputStream inputStream = resource.getInputStream();

        JsonNode rootNode = objectMapper.readTree(inputStream);
        JsonNode marcaNode = rootNode.get(codigoMarca);

        if (marcaNode == null) {
            return new ArrayList<>();
        }

        JsonNode prendasNode = marcaNode.get("prendas");
        List<Prenda> prendas = new ArrayList<>();

        if (prendasNode != null && prendasNode.isArray()) {
            for (int i = 0; i < prendasNode.size(); i++) {
                JsonNode prendaNode = prendasNode.get(i);
                Prenda prenda = new Prenda();
                prenda.setId((long) (i + 1));
                prenda.setNombre(prendaNode.get("nombre").asText());
                prenda.setTipo(prendaNode.get("tipo").asText());
                prenda.setImagenUrl(prendaNode.get("imagenUrl").asText());
                prenda.setCodigo(prendaNode.get("codigo").asText());

                // Crear marca básica para la relación
                Marca marca = new Marca();
                marca.setCodigoMarca(codigoMarca);
                marca.setNombre(marcaNode.get("nombre").asText());
                marca.setLogoUrl(marcaNode.get("logoUrl").asText());
                prenda.setMarca(marca);

                prendas.add(prenda);
            }
        }

        return prendas;
    }

    public List<Prenda> getAllPrendas() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/prendas.json");
        InputStream inputStream = resource.getInputStream();

        JsonNode rootNode = objectMapper.readTree(inputStream);
        List<Prenda> todasLasPrendas = new ArrayList<>();

        // Usar un AtomicLong para evitar el problema de variable final
        java.util.concurrent.atomic.AtomicLong idCounter = new java.util.concurrent.atomic.AtomicLong(1L);

        rootNode.fieldNames().forEachRemaining(codigoMarca -> {
            try {
                List<Prenda> prendasMarca = getPrendasByMarca(codigoMarca);
                for (Prenda prenda : prendasMarca) {
                    prenda.setId(idCounter.getAndIncrement());
                    todasLasPrendas.add(prenda);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar prendas de la marca: " + codigoMarca, e);
            }
        });

        return todasLasPrendas;
    }

    public Prenda getPrendaByCodigo(String codigoMarca, String codigoPrenda) throws IOException {
        List<Prenda> prendas = getPrendasByMarca(codigoMarca);
        return prendas.stream()
                .filter(prenda -> prenda.getCodigo().equals(codigoPrenda))
                .findFirst()
                .orElse(null);
    }
}