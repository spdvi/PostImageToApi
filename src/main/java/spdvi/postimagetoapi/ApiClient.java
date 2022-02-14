package spdvi.postimagetoapi;

import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author DevMike
 */
public class ApiClient {

    public int updateUserProfilePicture(int userId, File imageFile) throws IOException, Exception {
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        HttpPost post = new HttpPost("http://localhost:8080/usuari/profilePicture/" + userId);
        
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
