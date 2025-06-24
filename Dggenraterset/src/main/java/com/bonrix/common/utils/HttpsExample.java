package com.bonrix.common.utils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsExample {
    public static void main(String[] args) throws Exception {

        // Load the public certificate from file or any other source
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(
            new FileInputStream("E:/_.vispl.in.crt")
        );
        
        // Create a KeyStore and add the public certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("my_certificate", certificate);
        
        // Create a TrustManager that trusts the certificate
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        trustManagerFactory.init(keyStore);
        
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        
        // Create an SSLContext and set the custom TrustManager as the default
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        //        public static final String POST_URL = "https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=9173186596&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=Attention%21+Site+Name.+Kamimajra+-HRRTRYMN0134%27s+Alarm+Name+Battery+Low%3A+Status+CLEAR+at%2C+2023-06-12+04%3A46%3A57%2C.-Team+VTTPL";

        // Make the HTTPS request
        URL url = new URL("https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=9173186596&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=Attention%21+Site+Name.+Kamimajra+-HRRTRYMN0134%27s+Alarm+Name+Battery+Low%3A+Status+CLEAR+at%2C+2023-06-12+04%3A46%3A57%2C.-Team+VTTPL");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            reader.close();
            
            // Process the response data
            String responseData = response.toString();
            System.out.println(responseData);
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
        
        connection.disconnect();
    }
}
