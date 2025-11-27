package com.outfitlab.project.infrastructure.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfitlab.project.domain.interfaces.port.GeminiClient;
import com.outfitlab.project.domain.model.dto.GeminiRecommendationDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class GeminiClientImpl implements GeminiClient {

    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final HttpClient httpClient;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    private static final String MODEL_NAME = "gemini-2.5-flash";
    private static final String VALID_CLIMATES = "FRIO, TEMPLADO, CALIDO, LLUVIA";
    private static final String VALID_OCASIONS = "CASUAL, FORMAL, DEPORTE, FIESTA";

    private static final String SYSTEM_PROMPT =
            "Eres un experto en moda y estilismo. Analiza la petición del usuario. " +
                    "Devuelve SÓLO un objeto JSON con los campos 'clima' y 'ocasion'. " +
                    "El campo 'clima' debe ser uno de: [" + VALID_CLIMATES + "]. " +
                    "El campo 'ocasion' debe ser uno de: [" + VALID_OCASIONS + "]. " +
                    "Ejemplo de formato: {\"clima\": \"TEMPLADO\", \"ocasion\": \"CASUAL\"}.";

    public GeminiClientImpl(ObjectMapper objectMapper, String apiKey) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    @Override
    public GeminiRecommendationDTO extractParameters(String peticionUsuario) {
        try {
            String jsonBody = buildRequestBody(peticionUsuario);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Fallo API: " + response.statusCode() + ". " + response.body());
            }

            String jsonResponseContent = extractJsonContent(response.body());

            GeminiRecommendationDTO dto = objectMapper.readValue(jsonResponseContent, GeminiRecommendationDTO.class);

            return new GeminiRecommendationDTO(dto.getClima(), dto.getOcasion());

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la petición de IA.", e);
        }
    }

    private String buildRequestBody(String peticionUsuario) throws com.fasterxml.jackson.core.JsonProcessingException {

        Map<String, Object> userContent = Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", peticionUsuario))
        );

        Map<String, Object> apiRequest = Map.of(
                "contents", List.of(userContent),

                "config", Map.of(
                        "systemInstruction", SYSTEM_PROMPT,
                        "responseMimeType", "application/json"
                )
        );

        String fullPrompt = SYSTEM_PROMPT + "\n\nSolicitud: " + peticionUsuario;

        Map<String, Object> simpleRequest = Map.of(
                "contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", fullPrompt)))),
                "model", MODEL_NAME
        );

        Map<String, Object> finalRequest = Map.of(
                "contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", peticionUsuario)))),
                "config", Map.of("systemInstruction", SYSTEM_PROMPT)
        );

        Map<String, Object> minimalRequest = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", SYSTEM_PROMPT + "\n" + peticionUsuario))))
        );

        Map<String, Object> directRequest = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", SYSTEM_PROMPT + "\n\n" + peticionUsuario)))),
                "model", "gemini-2.5-flash"
        );

        return objectMapper.writeValueAsString(directRequest);
    }

    protected String extractJsonContent(String apiResponseBody) throws Exception {
        Map<String, Object> responseMap = objectMapper.readValue(apiResponseBody, Map.class);

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
        if (candidates != null && !candidates.isEmpty()) {
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            if (content != null) {
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    String jsonText = (String) parts.get(0).get("text");
                    if (jsonText != null) {
                        return jsonText.replaceAll("```json", "").replaceAll("```", "").trim();
                    }
                }
            }
        }
        throw new RuntimeException("No se pudo extraer el JSON de los parámetros de la respuesta de Gemini.");
    }
}