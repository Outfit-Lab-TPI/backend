package com.outfitlab.project.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.entities.Marca;
import com.outfitlab.project.domain.exceptions.MarcasNotFoundException;
import com.outfitlab.project.domain.repositories.MarcaRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MarcaService {
    private final MarcaRepository marcaRepository;
    private final ObjectMapper objectMapper;

    // Método original comentado - usando base de datos
    /*
    public List<Marca> getAllMarcas() throws MarcasNotFoundException {
        List<Marca> marcas = marcaRepository.findAll();
        System.out.println(marcas);
        if (!marcas.isEmpty()) {
            return marcas;
        }
        throw new MarcasNotFoundException("No encontramos marcas");
    }
    */

    // Nuevo método - usando archivo JSON
    public List<Marca> getAllMarcas() throws MarcasNotFoundException {
        try {
            ClassPathResource resource = new ClassPathResource("data/marcas.json");
            InputStream inputStream = resource.getInputStream();

            List<Marca> marcas = objectMapper.readValue(inputStream, new TypeReference<List<Marca>>() {});

            // Asignar IDs y timestamps para mantener consistencia con la entidad
            for (int i = 0; i < marcas.size(); i++) {
                Marca marca = marcas.get(i);
                marca.setId((long) (i + 1));
                marca.setCreatedAt(LocalDateTime.now());
                marca.setUpdatedAt(LocalDateTime.now());
            }

            System.out.println(marcas);
            return marcas;
        } catch (IOException e) {
            throw new MarcasNotFoundException("Error al cargar las marcas desde el archivo JSON: " + e.getMessage());
        }
    }

    // Método original comentado - usando base de datos
    /*
    public Marca getMarcaAtributesByCodigoMarca(String codigoMarca) throws MarcasNotFoundException {
        Marca marca = marcaRepository.findByCodigoMarca(codigoMarca);
        if (marca != null) {
            return marca;
        }
        throw new MarcasNotFoundException("No encontramos la marca: " + codigoMarca);
    }
    */

    // Nuevo método - usando archivo JSON
    public Marca getMarcaAtributesByCodigoMarca(String codigoMarca) throws MarcasNotFoundException {
        List<Marca> marcas = getAllMarcas();
        return marcas.stream()
                .filter(marca -> marca.getCodigoMarca().equals(codigoMarca))
                .findFirst()
                .orElseThrow(() -> new MarcasNotFoundException("No encontramos la marca: " + codigoMarca));
    }
}
