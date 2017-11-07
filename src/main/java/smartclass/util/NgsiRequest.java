/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartclass.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Pedro
 */
public class NgsiRequest {
    
    public static final String SERVER_IP = "35.203.147.23";
    public static final String SERVER_PORT = "1026";
    public static final String SERVER_PROTOCOL = "http://";

    public String sendPost(String urlString, String body) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(SERVER_PROTOCOL+SERVER_IP+":"+SERVER_PORT+urlString);
            StringEntity postingString = new StringEntity(body);
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            if (response != null) {
                InputStream in = response.getEntity().getContent();
                return IOUtils.toString(in, "UTF-8");
            }
            return null;
        } catch (IOException ex) {
            Logger.getLogger(NgsiRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
