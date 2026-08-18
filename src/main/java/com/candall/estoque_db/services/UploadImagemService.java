package com.candall.estoque_db.services;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImagemService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final RestTemplate restTemplate = new RestTemplate();

    public String enviarParaNuvem(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        try {
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = ".jpg";

            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            String nomeArquivo = UUID.randomUUID() + extensao.toLowerCase();

            String urlUpload =
                    supabaseUrl
                            + "/storage/v1/object/"
                            + bucketName
                            + "/"
                            + nomeArquivo;

            HttpHeaders headers = new HttpHeaders();

            headers.set(
                    "Authorization",
                    "Bearer " + supabaseKey
            );

            headers.set(
                    "apikey",
                    supabaseKey
            );

            String contentType = arquivo.getContentType();

            if (contentType != null) {
                headers.setContentType(
                        MediaType.parseMediaType(contentType)
                );
            } else {
                headers.setContentType(
                        MediaType.APPLICATION_OCTET_STREAM
                );
            }

            HttpEntity<byte[]> requestEntity =
                    new HttpEntity<>(
                            arquivo.getBytes(),
                            headers
                    );

            restTemplate.exchange(
                    urlUpload,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return supabaseUrl
                    + "/storage/v1/object/public/"
                    + bucketName
                    + "/"
                    + nomeArquivo;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao salvar imagem na nuvem: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private String limparNomeArquivo(String nomeOriginal) {
        if (nomeOriginal == null) {
            return "imagem.jpg";
        }

        String nome = nomeOriginal.toLowerCase();

        // 2. Substitui manualmente os acentos mais comuns do português
        nome = nome.replaceAll("[áàâãä]", "a");
        nome = nome.replaceAll("[éèêë]", "e");
        nome = nome.replaceAll("[íìîï]", "i");
        nome = nome.replaceAll("[óòôõö]", "o");
        nome = nome.replaceAll("[úùûü]", "u");
        nome = nome.replaceAll("[ç]", "c");
        nome = nome.replaceAll("[ñ]", "n");

        nome = nome.replace(" ", "-");

        nome = nome.replaceAll("[^a-z0-9.\\-_]", "");

        return nome;
    }
}