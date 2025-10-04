package com.outfitlab.project.infrastructure;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Service
public class ImgBBService {

    private static final String API_KEY = "df7daf77f82c0d0391ecb0cec51d76f3"; // Reemplaza con tu key
    private static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";

    private final RestTemplate restTemplate;

    public ImgBBService() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(MultipartFile file) throws Exception {
        byte[] fileBytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(fileBytes);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("key", API_KEY);
        body.add("image", base64Image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(UPLOAD_URL, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("url");
        }

        throw new Exception("Failed to upload to ImgBB");
    }
}