package com.vmware.talentboost.ics.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Tag;
import org.springframework.data.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageAnalyzer {
    public static String sendRequestToAnalyzer(String image_url) throws IOException {
        String credentialsToEncode = "acc_c223ba209ea5b63" + ":" + "1bf1c64a17b90a04b2c135467eae4831";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v2/tags";
        //String image_url = "http://playground.imagga.com/static/img/example_photos/japan-605234_1280.jpg";

        String url = endpoint_url + "?image_url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        System.out.println(jsonResponse);

        return jsonResponse;
    }


}
