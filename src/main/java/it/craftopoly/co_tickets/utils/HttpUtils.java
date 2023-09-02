package it.craftopoly.co_tickets.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.craftopoly.co_tickets.CO_Tickets;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {
    private static final String baseUrl = CO_Tickets.getInstance().getConfig().getString("settings.api_url");
    private static final Gson gson = new Gson();

    public static <T> T get(String endpoint, String token, Class<T> responseType){
        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            setAuthenticationHeader(connection, token);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(inputStream);
                     BufferedReader bufferedReader = new BufferedReader(reader)) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    return gson.fromJson(response.toString(), responseType);
                }
            } else {
                return gson.fromJson("{code: "+responseCode+"}", responseType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T post(String endpoint, String token, Object data, Class<T> responseType) {
        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            setAuthenticationHeader(connection, token);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = connection.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                String requestData = gson.toJson(data);
                writer.write(requestData);
                writer.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(inputStream);
                     BufferedReader bufferedReader = new BufferedReader(reader)) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    return gson.fromJson(response.toString(), responseType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return gson.fromJson("{code: " + responseCode + "}", responseType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T put(String endpoint, String token, Object data, Class<T> responseType) {
        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            setAuthenticationHeader(connection, token);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = connection.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                String requestData = gson.toJson(data);
                writer.write(requestData);
                writer.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(inputStream);
                     BufferedReader bufferedReader = new BufferedReader(reader)) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    return gson.fromJson(response.toString(), responseType);
                }
            } else {
                return gson.fromJson("{code: " + responseCode + "}", responseType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String endpoint, String token) throws IOException {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        setAuthenticationHeader(connection, token);

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new IOException("HTTP DELETE request failed with response code: " + responseCode);
        }
    }


    private static void setAuthenticationHeader(HttpURLConnection connection, String token) {
        if (token != null && !token.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }
    }
}
