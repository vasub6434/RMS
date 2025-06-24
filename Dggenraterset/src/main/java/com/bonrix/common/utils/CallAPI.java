package com.bonrix.common.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;


//If you make a request to Tomcat with trailing whitespace after the HTTP version (as in your example), Tomcat will respond with 505 error
public class CallAPI {
	private static final Logger log = Logger.getLogger(CallAPI.class);
	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		CallAPI http = new CallAPI();
		//http.sendPost("https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=9173186596&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=Attention%21+Site+Name.+Kamimajra+-HRRTRYMN0134%27s+Alarm+Name+Battery+Low%3A+Status+CLEAR+at%2C+2023-06-12+04%3A46%3A57%2C.-Team+VTTPL");
		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet("https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=9173186596&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=Attention%21+Site+Name.+Kamimajra+-HRRTRYMN0134%27s+Alarm+Name+Battery+Low%3A+Status+CLEAR+at%2C+2023-06-12+04%3A46%3A57%2C.-Team+VTTPL");
		
		System.out.println("\nTesting 2 - Send Http POST request");
	//	http.sendPost();

	}

	// HTTP GET request
	public static String sendGet(String url) throws Exception {

		//String url = "http://fcmlight.saharshsolutions.co.in/sendSingleMessageAction/sendSingleMessage.do?message=Hello+Sajan+Mains_Fail+is+ON+on+2018-11-19+16%3A13%3A50.0&clientName=tvipl&password=tvipl&phNo=9173186596&senderName=tower&title=alert";
		StringBuffer response = new StringBuffer();
		log.info("CallAPI url : " + url);
		try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);

	//	int responseCode = con.getResponseCode();
		log.info("CallAPI Sending 'GET' request to URL : " + url);
	//	log.info("CallAPI Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		log.info(response.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e);
			log.error(e.getLocalizedMessage());
			log.error(e.getStackTrace());
		}catch (MalformedURLException e) {
			e.printStackTrace();
			log.error(e);
			log.error(e.getLocalizedMessage());
			log.error(e.getStackTrace());
		}
		catch (IOException e) {
			e.printStackTrace();
			log.error(e);
			log.error(e.getLocalizedMessage());
			log.error(e.getStackTrace()); 
		}
		return response.toString();
	}
	
	// HTTP POST request
	public void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());

	}    
	
	public static String sendGetWithCertificate(String urlCall) throws Exception {
		String responseData = "";
        // Load the public certificate from file or any other source
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(
            new FileInputStream("/opt/_.vispl.in.crt")
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
       // URL url = new URL("https://bulksmsapi.vispl.in/?username=vtlnoctrn&password=vtlnoctrn@123&messageType=text&mobile=9173186596&senderId=VTLNOC&ContentID=1307166134202357463&EntityID=1301157960116941398&message=Attention%21+Site+Name.+Kamimajra+-HRRTRYMN0134%27s+Alarm+Name+Battery+Low%3A+Status+CLEAR+at%2C+2023-06-12+04%3A46%3A57%2C.-Team+VTTPL");
        URL url = new URL(urlCall);
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
             responseData = response.toString();
            System.out.println(responseData);
        } else {
            System.out.println("Request failed with response code: " + responseCode);
        }
        
        connection.disconnect();
		return responseData;
    }

}