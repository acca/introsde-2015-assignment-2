package introsde.dsantoro.a2.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;    
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.h2.util.IOUtils;

import introsde.dsantoro.a2.model.Person;


public class MyClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());

        File jsonFile = new File("./client-server-json.log");
        OutputStream os = null;
		try {
			os = new FileOutputStream(jsonFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        
        //Step 1) - Print URL of server
        System.out.println("TESTING SERVER AT URL: " + getBaseURI());
        
        // // GET BASEURL/rest/salutation
        // // Accept: text/plain
        
        Response res = service.path("person/1").request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();
        System.out.println(res.readEntity(String.class));
        InputStream is = res.readEntity(InputStream.class);
        
        
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        try {
			while ((bytesRead = is.read(buffer)) != -1) {
			    try {
					os.write(buffer, 0, bytesRead);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        IOUtils.closeSilently(is);
        IOUtils.closeSilently(os);
        
        
        Person p = res.readEntity(Person.class);
        System.out.println(p.getIdPerson());
        
        
//        // // Get plain text
//        System.out.println(service.path("salutation")
//                .request().accept(MediaType.TEXT_PLAIN).get().readEntity(String.class));
//        // Get XML 
//        System.out.println(service.path("salutation")
//                .request()
//                .accept(MediaType.TEXT_XML).get().readEntity(String.class));
//        // // The HTML
//        System.out.println(service.path("salutation").request()
//                .accept(MediaType.TEXT_HTML).get().readEntity(String.class));

    }

    private static URI getBaseURI() {
    	String HEROKU="https://sleepy-caverns-6041.herokuapp.com/dsantoroa2/";
    	String TOMCAT="http://localhost:8080/introsde-2015-assignment-2/jersey/";
    	String LOCAL="http://192.168.5.3:5700/dsantoroa2/";
    	
        return UriBuilder.fromUri(TOMCAT).build();
    }
}