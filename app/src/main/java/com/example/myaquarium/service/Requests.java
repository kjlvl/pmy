package com.example.myaquarium.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Requests {
    public final String urlRequest = "https://f376-37-112-232-215.ngrok-free.app/";
    public final String urlRequestImg = "https://f376-37-112-232-215.ngrok-free.app/img/";

    public JSONArray setRequest(
            String url,
            List<NameValuePair> params
    ) throws IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost http = new HttpPost(url);

        http.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
        HttpResponse httpResponse = httpclient.execute(http);
        HttpEntity httpEntity = httpResponse.getEntity();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(httpEntity.getContent(), StandardCharsets.UTF_8)
        );
        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.readLine() != null) {
            stringBuilder.append(bufferedReader.readLine());
        }
        String str = stringBuilder.toString().replace("</html>", "");
        str = str.replace("<br />", "");
        return new JSONArray(str);
    }

}