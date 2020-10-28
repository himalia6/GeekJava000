package http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpProxyClient {
    private final CloseableHttpClient httpClient;

    public HttpProxyClient() {
        this.httpClient = HttpClients.createDefault();
    }

    public String request(HttpUriRequest request) {
        final ResponseHandler<String> handler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                System.out.println("response status => " + httpResponse.getStatusLine());
                final int status = httpResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    final HttpEntity entity = httpResponse.getEntity();
                    final String result = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    System.out.println("body => " + result);
                    return result;
                } else {
                    throw new ClientProtocolException("Unexpected response status => " + status);
                }
            }
        };


        // final HttpGet get = new HttpGet("http://localhost:8801");

        // request headers & remote auth
        request.addHeader("Proxy-Type", "Gateway");
        request.addHeader("Proxy-Auth", "AuthorizedKeys");
        String result = "";
        try {
            result = httpClient.execute(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
