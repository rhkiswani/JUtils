package io.github.rhkiswani.javaff.http;

import io.github.rhkiswani.javaff.detector.ApiDetectorUtil;
import io.github.rhkiswani.javaff.factory.exceptions.NoImplementationFoundException;
import io.github.rhkiswani.javaff.httpclient.ApacheHttpClient;
import io.github.rhkiswani.javaff.httpclient.HttpClient;
import io.github.rhkiswani.javaff.httpclient.HttpClientFactory;
import io.github.rhkiswani.javaff.json.JsonHandler;
import io.github.rhkiswani.javaff.json.JsonHandlerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpClientTest extends WebTester{

    @Test
    public void testFactory() throws Exception {
        // check default
        HttpClient httpClient = HttpClientFactory.getHttpClient(Object.class);
        assertThat(httpClient).isNotNull();

        HttpClientFactory.instance().remove(Object.class);
        try {
            HttpClientFactory.getHttpClient(Object.class);
        }catch (Exception e){
            assertThat(e).isInstanceOf(NoImplementationFoundException.class).hasMessage("No implementation found for HttpClientFactory you need to set implementation through HttpClientFactory.instance().add or add https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient to your classpath");
        }
        HttpClientFactory.instance().add(Object.class, new ApacheHttpClient());
    }

    @Test
    public void testPost() throws Exception{
        HttpClient httpClient = HttpClientFactory.getHttpClient(HttpClientTest.class);
        Map<String, String> params = prepareParams();
        String post = httpClient.post(BASE_URL, params, null);
        assertValues("POST", params, post);
    }

    @Test
    public void testPostJson() throws Exception{
        HttpClient httpClient = HttpClientFactory.getHttpClient(HttpClientTest.class);
        String post = httpClient.postJson(BASE_URL,"{\"method\":\"POST\"}", null);
        assertJsonResponse(post, "POST");
    }

    @Test
    public void testPut() throws Exception{
        String method = "PUT";
        HttpClient httpClient = HttpClientFactory.getHttpClient(HttpClientTest.class);
        Map<String, String> params = prepareParams();
        String put = httpClient.put(BASE_URL, params, null);
        assertValues(method, params, put);
    }

    private Map<String, String> prepareParams() {
        Map<String, String> params = new HashMap<>();
        params.put("paramsEmail", "rhkiswani@gmail.com");
        return params;
    }

    private void assertValues(String method, Map<String, String> params, String post) {
        JsonHandler handler = JsonHandlerFactory.getJsonHandler(HttpClientTest.class);
        Response receivedRespond = handler.fromJson(post, Response.class);

        assertThat(receivedRespond.params).isEqualTo(params);
        assertThat(receivedRespond.method).isEqualTo(method);
        assertThat(receivedRespond.contentType).isEqualTo("application/x-www-form-urlencoded");
    }

    @Test
    public void testPutJson() throws Exception{
        HttpClient httpClient = HttpClientFactory.getHttpClient(HttpClientTest.class);
        String put = httpClient.putJson(BASE_URL,"{\"method\":\"PUT\"}", null);
        assertJsonResponse(put, "PUT");
    }

    private void assertJsonResponse(String response, String method) {
        JsonHandler handler = JsonHandlerFactory.getJsonHandler(HttpClientTest.class);
        Response receivedRespond = handler.fromJson(response, Response.class);

        assertThat(receivedRespond.jsonParams).isEqualTo("{\"method\":\""+ method +"\"}");
        assertThat(receivedRespond.method).isEqualTo(method);
        assertThat(receivedRespond.contentType).isEqualTo("application/json");
    }
}
