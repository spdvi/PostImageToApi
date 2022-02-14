package spdvi.postimagetoapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author DevMike
 */
public class ApiClient {
    private String token = null;
    
    public String getToken(String email, String password) throws IOException {
       
        String body = "{\"username\":\"" + email + "\",\"password\":\""
                + password + "\"}";
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/authenticate");
        StringEntity requestEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
        post.setEntity(requestEntity);
        
        HttpResponse response = httpClient.execute(post);
        
        if(response.getStatusLine().getStatusCode() == 200) {
            String responseString = new BasicResponseHandler().handleResponse(response);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(
                    responseString, JsonObject.class);
            token = jsonObject.get("token").getAsString();
        }
        return token;
    }
    
    public int updateUserProfilePicture(int userId, File imageFile) throws IOException, Exception {
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        HttpPost post = new HttpPost("http://localhost:8080/usuari/profilePicture/" + userId);
        post.addHeader("Authorization", "Bearer " + token);        
        FileBody fileBody = new FileBody(imageFile, ContentType.IMAGE_JPEG);        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("profileImage", fileBody);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        
        CloseableHttpResponse response = httpclient.execute(post);

        return response.getStatusLine().getStatusCode();
    }
}
