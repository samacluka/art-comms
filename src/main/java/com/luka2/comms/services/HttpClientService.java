package com.luka2.comms.services;

import java.net.http.HttpResponse;

public interface HttpClientService {
    <T> HttpResponse<T> get(String url, Class<T> responseType);
    <T> HttpResponse<T> post(String url, String requestBody, Class<T> responseType);
    <T> HttpResponse<T> put(String url, String requestBody, Class<T> responseType);
}
