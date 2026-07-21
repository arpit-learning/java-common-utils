package dev.arpit.learning.commonUtils.utils.restclient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class RestTemplateUtilsTest {

  private RestTemplate defaultTemplate;
  private MockRestServiceServer server;
  private MockedConstruction<RestTemplateBuilder> mockedBuilder;

  @BeforeEach
  void setUp() throws Exception {
    Field field = RestTemplateUtils.class.getDeclaredField("defaultRestTemplate");
    field.setAccessible(true);
    defaultTemplate = (RestTemplate) field.get(null);
    server = MockRestServiceServer.bindTo(defaultTemplate).build();

    mockedBuilder =
        mockConstruction(
            RestTemplateBuilder.class,
            (mock, context) -> {
              when(mock.connectTimeout(any())).thenReturn(mock);
              when(mock.readTimeout(any())).thenReturn(mock);
              when(mock.build()).thenReturn(defaultTemplate);
            });
  }

  @AfterEach
  void tearDown() {
    mockedBuilder.close();
    server.reset();
  }

  @Test
  void test_makeHTTPRequest_withTimeouts() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHTTPRequest(
            URI.create("http://test.com/api"), HttpMethod.GET, null, String.class, 5, 5);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHTTPRequest_withoutTimeouts() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHTTPRequest(
            URI.create("http://test.com/api"),
            HttpMethod.POST,
            new HttpEntity<>("body"),
            String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHTTPRequest_exception() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

    assertThrows(
        RuntimeException.class,
        () -> {
          RestTemplateUtils.makeHTTPRequest(
              URI.create("http://test.com/api"), HttpMethod.GET, null, String.class);
        });
    server.verify();
  }

  @Test
  void test_makeHttpRequest_url_timeouts_params_headers_variables() {
    server
        .expect(requestTo("http://test.com/api/123?q=search"))
        .andExpect(method(HttpMethod.PUT))
        .andExpect(header("X-Custom", "val"))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    Map<String, Object> body = new HashMap<>();
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("q", "search");
    Map<String, String> headers = new HashMap<>();
    headers.put("X-Custom", "val");

    ResponseEntity<String> response =
        RestTemplateUtils.makeHttpRequest(
            "http://test.com/api/#",
            HttpMethod.PUT,
            String.class,
            body,
            queryParams,
            headers,
            5,
            5,
            new Object[] {"123"});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHttpRequest_url_params_headers_variables() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    // Force exact match by supplying empty varargs explicitly
    ResponseEntity<String> response =
        RestTemplateUtils.makeHttpRequest(
            "http://test.com/api",
            HttpMethod.GET,
            String.class,
            (Map<String, Object>) null,
            (Map<String, String>) null,
            (Map<String, String>) null,
            new Object[0]);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHttpRequestWithCSVAsStream_withTimeouts() {
    server
        .expect(requestTo("http://test.com/upload"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
            "http://test.com/upload", String.class, 5, 5, "fileParam", "myFile", "a,b,c\n1,2,3");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHttpRequestWithCSVAsStream_withoutTimeouts() {
    server
        .expect(requestTo("http://test.com/upload"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHttpRequestWithCSVAsStream(
            "http://test.com/upload", String.class, "fileParam", "myFile", "a,b,c\n1,2,3");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("response", response.getBody());
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_success() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "file content".getBytes(),
                org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));

    String filepath = "build/tmp/test_download_success/file.txt";
    File file = new File(filepath);
    if (file.exists()) {
      file.delete();
    }
    File parent = file.getParentFile();
    if (parent.exists()) {
      for (File child : parent.listFiles()) {
        child.delete();
      }
      parent.delete();
    }

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(file.exists());
    assertEquals("file content", new String(Files.readAllBytes(file.toPath())));
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_partialContent() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.PARTIAL_CONTENT).body("partial".getBytes()));

    String filepath = "build/tmp/test_download/partial.txt";

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_noContent() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess()); // Returns null body

    String filepath = "build/tmp/test_download/nocontent.txt";

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_exception() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError()); // Throws exception

    String filepath = "build/tmp/test_download/error.txt";

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_mkdirFail() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "file content".getBytes(),
                org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));

    String parentPath = "build/tmp/test_download_fail";
    File parentFile = new File(parentPath);
    parentFile.mkdirs();

    // Create a file where dir1 should be
    File dir1File = new File(parentPath + "/dir1");
    dir1File.createNewFile();

    // The parent of our target file will be dir1/dir2
    String filepath = parentPath + "/dir1/dir2/file.txt";

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    // Because parent dir creation fails, it throws exception later or logs error
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    server.verify();

    dir1File.delete(); // Cleanup
  }

  @Test
  void test_objectCreation() {
    RestTemplateUtils utils = new RestTemplateUtils();
    assertNotNull(utils);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void test_nullParameters_throwsNPE() {
    URI uri = URI.create("http://test.com");
    HttpMethod method = HttpMethod.GET;
    Class<String> cls = String.class;

    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequest(null, method, null, cls, 5, 5));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequest(uri, null, null, cls, 5, 5));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequest(uri, method, null, null, 5, 5));

    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequest(null, method, null, cls));
    assertThrows(
        NullPointerException.class, () -> RestTemplateUtils.makeHTTPRequest(uri, null, null, cls));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequest(uri, method, null, null));

    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequest(
                null, method, cls, null, null, null, 5, 5, new Object[0]));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequest(
                "url", null, cls, null, null, null, 5, 5, new Object[0]));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequest(
                "url", method, null, null, null, null, 5, 5, new Object[0]));

    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequest(null, method, cls, null, null, null, new Object[0]));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHttpRequest("url", null, cls, null, null, null, new Object[0]));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequest(
                "url", method, null, null, null, null, new Object[0]));

    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHttpRequestWithCSVAsStream(null, cls, "param", "file", "data"));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHttpRequestWithCSVAsStream("url", null, "param", "file", "data"));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHttpRequestWithCSVAsStream("url", cls, null, "file", "data"));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHttpRequestWithCSVAsStream("url", cls, "param", null, "data"));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHttpRequestWithCSVAsStream("url", cls, "param", "file", null));

    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
                null, cls, 5, 5, "param", "file", "data"));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
                "url", null, 5, 5, "param", "file", "data"));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
                "url", cls, 5, 5, null, "file", "data"));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
                "url", cls, 5, 5, "param", null, "data"));
    assertThrows(
        NullPointerException.class,
        () ->
            RestTemplateUtils.makeHTTPRequestWithCSVAsStream(
                "url", cls, 5, 5, "param", "file", null));

    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequestAndSaveFile(null, "path", method, null, null, null));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequestAndSaveFile("url", null, method, null, null, null));
    assertThrows(
        NullPointerException.class,
        () -> RestTemplateUtils.makeHTTPRequestAndSaveFile("url", "path", null, null, null, null));
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_parentNull() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "file content".getBytes(),
                org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));

    // File without parent dir
    String filepath = "test_file_no_parent.txt";
    File file = new File(filepath);
    if (file.exists()) {
      file.delete();
    }

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(file.exists());
    file.delete();
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_parentExists() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "file content".getBytes(),
                org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));

    String parentPath = "build/tmp/test_download_exists";
    File parentFile = new File(parentPath);
    parentFile.mkdirs();

    String filepath = parentPath + "/file.txt";
    File file = new File(filepath);
    if (file.exists()) {
      file.delete();
    }

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(file.exists());
    file.delete();
    server.verify();
  }

  @Test
  void test_makeHttpRequest_nullUriVariables() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHttpRequest(
            "http://test.com/api", HttpMethod.GET, String.class, null, null, null, (Object[]) null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHttpRequest_withTimeouts_nullUriVariables() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHttpRequest(
            "http://test.com/api",
            HttpMethod.GET,
            String.class,
            null,
            null,
            null,
            5,
            5,
            (Object[]) null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHTTPRequest_partialTimeout() {
    server
        .expect(requestTo("http://test.com/api"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("response", org.springframework.http.MediaType.TEXT_PLAIN));

    ResponseEntity<String> response =
        RestTemplateUtils.makeHTTPRequest(
            URI.create("http://test.com/api"), HttpMethod.GET, null, String.class, 5, -1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    server.verify();
  }

  @Test
  void test_makeHTTPRequestAndSaveFile_otherStatus() throws Exception {
    server
        .expect(requestTo("http://test.com/download"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.CREATED).body("created".getBytes()));

    String filepath = "build/tmp/test_download/created.txt";
    File file = new File(filepath);
    if (file.exists()) {
      file.delete();
    }

    ResponseEntity<byte[]> response =
        RestTemplateUtils.makeHTTPRequestAndSaveFile(
            "http://test.com/download", filepath, HttpMethod.GET, null, null, null);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(file.exists());
    file.delete();
    server.verify();
  }
}
