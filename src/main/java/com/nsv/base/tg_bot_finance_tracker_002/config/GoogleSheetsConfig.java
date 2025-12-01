package com.nsv.base.tg_bot_finance_tracker_002.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleSheetsConfig {

    @Bean
    public Sheets sheetsService() throws GeneralSecurityException, IOException {
        String credentialsPath = "google/directed-tracer-479608-v8-a32bc9dd8d9a.json";

        InputStream in = getClass().getClassLoader().getResourceAsStream(credentialsPath);
        if (in == null) {
            throw new RuntimeException("Не найден файл credentials: " + credentialsPath);
        }

        var credentials = ServiceAccountCredentials.fromStream(in)
                .createScoped(java.util.List.of("https://www.googleapis.com/auth/spreadsheets"));

        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Telegram Bot Sheets Integration")
                .build();
    }
}