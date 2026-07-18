package dev.arpit.learning.commonUtils.utils;

import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogFieldConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateUtils {
  private static final ILogger logger = LoggerFactory.getLogger(RestTemplateUtils.class);
  private static final int READ_TIMEOUT = 10;
  private static final int CONNECT_TIMEOUT = 10;
  private static RestTemplate restTemplate =
      new RestTemplateBuilder()
          .connectTimeout(Duration.ofSeconds(READ_TIMEOUT))
          .readTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
          .build();

  private static void setCustomRequestTimeout(int connectTimeout, int readTimeout) {
    restTemplate =
        new RestTemplateBuilder()
            .connectTimeout(Duration.ofSeconds(connectTimeout))
            .readTimeout(Duration.ofSeconds(readTimeout))
            .build();
  }

  private static @NonNull URI buildUri(
      @NonNull String url, @Nullable HashMap<String, String> queryParams, Object... uriVariables) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    if (queryParams != null) {
      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        params.add(entry.getKey(), entry.getValue());
      }
    }

    if (uriVariables != null) {
      url = StringUtils.getString(url, uriVariables);
    }

    return UriComponentsBuilder.fromUriString(url).queryParams(params).build().toUri();
  }

  private static @NonNull HttpHeaders getRequestHeaders(
      @NonNull HashMap<String, String> headerMap) {
    HttpHeaders headers = new HttpHeaders();
    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
      headers.add(entry.getKey(), entry.getValue());
    }
    return headers;
  }

  private static <T> @NonNull HttpEntity<T> getRequestEntity(
      @Nullable T body, @Nullable HashMap<String, String> headers) {
    if (headers != null) {
      return new HttpEntity<>(body, getRequestHeaders(headers));
    }

    return new HttpEntity<>(body, (HttpHeaders) null);
  }

  public static <R, T> @NonNull ResponseEntity<T> makeHTTPRequest(
      @NonNull URI uri,
      @NonNull HttpMethod httpMethod,
      @Nullable HttpEntity<R> requestEntity,
      @NonNull Class<T> responseType,
      int connectTimeout,
      int readTimeout) {
    if (connectTimeout == -1) {
      connectTimeout = CONNECT_TIMEOUT;
    }
    if (readTimeout == -1) {
      readTimeout = READ_TIMEOUT;
    }
    setCustomRequestTimeout(connectTimeout, readTimeout);
    logger.info(
        CommonUtilLogConstants.MAKING_HTTP_REQUEST,
        CommonUtilLogFieldConstants.URI,
        uri,
        CommonUtilLogFieldConstants.HTTP_METHOD,
        httpMethod,
        CommonUtilLogFieldConstants.HTTP_ENTITY,
        requestEntity,
        CommonUtilLogFieldConstants.CONNECTION_TIMEOUT,
        connectTimeout,
        CommonUtilLogFieldConstants.READ_TIMEOUT,
        readTimeout);
    try {
      ResponseEntity<T> httpResponse =
          restTemplate.exchange(uri, httpMethod, requestEntity, responseType);
      logger.info(
          CommonUtilLogConstants.COMPLETED_HTTP_REQUEST,
          CommonUtilLogFieldConstants.URI,
          uri,
          CommonUtilLogFieldConstants.HTTP_RESPONSE,
          httpResponse);
      return httpResponse;
    } catch (RuntimeException e) {
      logger.error(CommonUtilLogConstants.RUNTIME_EXCEPTION_EXC_WHILE_CALLING_API, e);
      throw e;
    }
  }

  public static <R, T> @NonNull ResponseEntity<T> makeHTTPRequest(
      @NonNull URI uri,
      @NonNull HttpMethod httpMethod,
      @Nullable HttpEntity<R> requestEntity,
      @NonNull Class<T> responseType) {
    return makeHTTPRequest(uri, httpMethod, requestEntity, responseType, -1, -1);
  }

  public static <T> @NonNull ResponseEntity<T> makeHttpRequest(
      @NonNull String url,
      @NonNull HttpMethod httpMethod,
      @NonNull Class<T> responseType,
      @Nullable HashMap<String, Object> bodyParams,
      @Nullable HashMap<String, String> queryParams,
      @Nullable HashMap<String, String> headers,
      int connectTimeout,
      int readTimeout,
      Object... uriVariables) {
    return makeHTTPRequest(
        buildUri(url, queryParams, uriVariables),
        httpMethod,
        getRequestEntity(bodyParams, headers),
        responseType,
        connectTimeout,
        readTimeout);
  }

  public static <T> @NonNull ResponseEntity<T> makeHttpRequest(
      @NonNull String url,
      @NonNull HttpMethod httpMethod,
      @NonNull Class<T> responseType,
      @Nullable HashMap<String, Object> bodyParams,
      @Nullable HashMap<String, String> queryParams,
      @Nullable HashMap<String, String> headers,
      Object... uriVariables) {
    return makeHTTPRequest(
        buildUri(url, queryParams, uriVariables),
        httpMethod,
        getRequestEntity(bodyParams, headers),
        responseType,
        -1,
        -1);
  }

  private static @NonNull HttpEntity<MultiValueMap<String, Object>> getCSVAsStreamRequestEntity(
      @NonNull String paramName, @NonNull String fileName, @NonNull String dataToSend) {
    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
    logger.info(
        CommonUtilLogConstants.DATA_SENT_FOR_THE_CSV,
        CommonUtilLogFieldConstants.DATA_TO_SEND,
        dataToSend);
    ByteArrayResource contentsAsResource =
        new ByteArrayResource(dataToSend.getBytes(StandardCharsets.UTF_8)) {
          @Override
          public String getFilename() {
            return fileName + ".csv";
          }
        };

    bodyMap.add(paramName, contentsAsResource);
    return new HttpEntity<>(bodyMap);
  }

  public static <T> @NonNull ResponseEntity<T> makeHttpRequestWithCSVAsStream(
      @NonNull String url,
      @NonNull Class<T> responseType,
      @NonNull String paramName,
      @NonNull String fileName,
      @NonNull String dataToSend) {
    HttpEntity<MultiValueMap<String, Object>> requestEntity =
        getCSVAsStreamRequestEntity(paramName, fileName, dataToSend);
    return makeHTTPRequest(
        buildUri(url, null), HttpMethod.POST, requestEntity, responseType, -1, -1);
  }

  public static <T> @NonNull ResponseEntity<T> makeHTTPRequestWithCSVAsStream(
      @NonNull String url,
      @NonNull Class<T> responseType,
      int connectTimeout,
      int readTimeout,
      @NonNull String paramName,
      @NonNull String fileName,
      @NonNull String dataToSend) {
    HttpEntity<MultiValueMap<String, Object>> requestEntity =
        getCSVAsStreamRequestEntity(paramName, fileName, dataToSend);
    return makeHTTPRequest(
        buildUri(url, null),
        HttpMethod.POST,
        requestEntity,
        responseType,
        connectTimeout,
        readTimeout);
  }

  public static @NonNull ResponseEntity<byte[]> makeHTTPRequestAndSaveFile(
      @NonNull String url,
      @NonNull String filepath,
      @NonNull HttpMethod httpMethod,
      @Nullable HashMap<String, Object> bodyParams,
      @Nullable HashMap<String, String> queryParams,
      @Nullable HashMap<String, String> headers) {
    ResponseEntity<byte[]> response;
    try {
      HttpEntity<HashMap<String, Object>> requestEntity = getRequestEntity(bodyParams, headers);
      response =
          makeHTTPRequest(buildUri(url, queryParams), httpMethod, requestEntity, byte[].class);
      byte[] responseBody = response.getBody();
      if (responseBody == null) {
        return new ResponseEntity<>((byte[]) null, HttpStatus.NO_CONTENT);
      }
      if (response.getStatusCode() == HttpStatus.OK) {
        logger.info(
            CommonUtilLogConstants.API_RESPONSE,
            CommonUtilLogFieldConstants.STATUS_CODE,
            HttpStatus.OK);
      } else if (response.getStatusCode() == HttpStatus.PARTIAL_CONTENT) {
        logger.debug(
            CommonUtilLogConstants.API_RESPONSE,
            CommonUtilLogFieldConstants.STATUS_CODE,
            HttpStatus.PARTIAL_CONTENT,
            CommonUtilLogFieldConstants.MSG,
            CommonUtilLogFieldConstants.PERFORM_MORE_ITERATIONS);
      }
      File file = new File(filepath);
      File parent = file.getParentFile();
      if (parent != null && !parent.exists()) {
        boolean created = parent.mkdirs();
        if (!created) {
          logger.error(
              CommonUtilLogConstants.EXCEPTION,
              CommonUtilLogFieldConstants.ERR_MSG,
              "Failed to create parent directories");
        }
      }
      Files.write(file.toPath(), responseBody);
      return response;
    } catch (Exception e) {
      logger.error(CommonUtilLogConstants.EXCEPTION, CommonUtilLogFieldConstants.ERR_MSG, e);
    }

    return new ResponseEntity<>((byte[]) null, HttpStatus.NO_CONTENT);
  }
}
