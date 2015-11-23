package introsde.dsantoro.a2.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.h2.util.IOUtils;

import introsde.dsantoro.a2.model.Person;


public class MyClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        
        Client client = ClientBuilder.newClient(clientConfig);
        
        //client.addFilter(new LoggingFilter(System.out));
        WebTarget service = client.target(getBaseURI());
        Response res = null;
        String url="";
        String rqst="0";
        String resBody="";
        int first_person_d=-1;
        int last_person_d=-1;
        final String XML = "APPLICATION/XML";
        final String JSON = "APPLICATION/JSON";

        
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
        
        // Step 3.1) - xml        
        url="/person";
        rqst="1";
        System.out.println("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/XML");
        service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        List<Person> pL = res.readEntity(new GenericType<List<Person>>() {});
        System.out.println("=> Result: " + ((pL.size()<3) ? "ERROR" : "OK"));
        printStatus(res);
        printResBody(res);
        //System.out.println(resBody);
        
        // Step 3.1) - json        
        System.out.println("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/JSON");
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();        
        //resBody = res.readEntity(String.class);
        pL = res.readEntity(new GenericType<List<Person>>() {});
        System.out.println("=> Result: " + ((pL.size()<3) ? "ERROR" : "OK"));        
        printStatus(res);
        printResBody(res);
        //System.out.println(resBody);
        
        first_person_d = pL.get(0).getIdPerson();
        last_person_d = pL.get(pL.size()-1).getIdPerson();
        
        // Step 3.2) - xml        
        url="/person/" + first_person_d;
        rqst="2";
        System.out.println("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/XML");
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        System.out.println("=> Result: " + ( ((res.getStatus() == 200) || (res.getStatus() == 202)) ? "OK" : "ERROR"));
        printStatus(res);
        printResBody(res);
        //System.out.println(resBody);
        
        // Step 3.2) - json        
        System.out.println("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/JSON");
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();
        System.out.println("=> Result: " + ( ((res.getStatus() == 200) || (res.getStatus() == 202)) ? "OK" : "ERROR"));
        Person p = res.readEntity(Person.class);        
        printStatus(res);
        printResBody(res);
        //System.out.println(resBody);
        
        
        // Step 3.3) - xml        
        url="/person/" + first_person_d;
        rqst="3";
        p.setName("Marco");
        System.out.println("\nRequest #"+rqst+" PUT " + url + " Accept: APPLICATION/XML Content-type: " + XML);
        res = service.path(url).request().put(Entity.xml(p));
        Response putRes = res;
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        p = res.readEntity(Person.class);
        System.out.println("=> Result: " + ((p.getFirstname().equals("Marco")) ? "OK" : "ERROR"));
        printStatus(putRes);
        printResBody(res);
        
        // Step 3.3) - json
        p.setName("Daniele");
        System.out.println("\nRequest #"+rqst+" PUT " + url + " Accept: APPLICATION/JSON  Content-type: " + JSON);                
        res = service.path(url).request().put(Entity.json(p));
        putRes = res;
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();
        p = res.readEntity(Person.class);
        System.out.println("=> Result: " + ((p.getFirstname().equals("Daniele")) ? "OK" : "ERROR"));        
        printStatus(putRes);
        printResBody(res);
        
        // Step 3.4) - xml        
        url="/person/";
        rqst="4";
        
        String newPerson = "<person><firstname>Chuck</firstname><birthdate>01/01/1945</birthdate><healthProfile><healthProfile><measureDefinition><measure>weight</measure><idMeasureDef>1</idMeasureDef><measureName>weight</measureName><measureType>double</measureType></measureDefinition><value>78.9</value></healthProfile><healthProfile><measureDefinition><measure>height</measure><idMeasureDef>2</idMeasureDef><measureName>height</measureName><measureType>double</measureType></measureDefinition><value>172</value></healthProfile></healthProfile><lastname>Norris</lastname><name>Chuck</name></person>";
        
        System.out.println("\nRequest #"+rqst+" POST " + url + " Accept: APPLICATION/XML Content-type: " + XML);
        res = service.path(url).request().post(Entity.xml(newPerson));        
        res.bufferEntity();
        //res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        //System.out.println(res.readEntity(String.class));
        p = res.readEntity(Person.class);
        int pId = p.getIdPerson();
        if ( ((res.getStatus() == 200) || (res.getStatus() == 202)) && (pId != -1) ) {
        	System.out.println("=> Result: OK");	
        }else{
        	System.out.println("=> Result: ERROR");
        }
        res.bufferEntity();
        printStatus(res);
        printResBody(res);
              
        // Step 3.4) - json
        
        newPerson = "{\"lastname\": \"Norris\",\"username\": null,\"birthdate\": \"01/01/1945\",\"email\": null,\"healthProfile\": [{\"value\": \"78.9\",\"measureDefinition\": {\"idMeasureDef\": 1,\"measureType\": \"double\",\"measureName\": \"weight\",\"measure\": \"weight\"}},{\"value\": \"172\",\"measureDefinition\": {\"idMeasureDef\": 2,\"measureType\": \"double\",\"measureName\": \"height\",\"measure\": \"height\"}}],\"firstname\": \"Chuck\"}";
        
        System.out.println("\nRequest #"+rqst+" POST " + url + " Accept: " + JSON + " Content-type: " + JSON);
        res = service.path(url).request().post(Entity.json(newPerson));
        res.bufferEntity();
        //res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        p = res.readEntity(Person.class);
        pId = p.getIdPerson();
        if ( ((res.getStatus() == 200) || (res.getStatus() == 202)) && (pId != -1) ) {
        	System.out.println("=> Result: OK");	
        }else{
        	System.out.println("=> Result: ERROR");
        }
        res.bufferEntity();
        printStatus(res);
        printResBody(res);
        
        
        //System.out.println(service.path("person").request().accept(MediaType.APPLICATION_JSON).toString());
//        res.bufferEntity();
//        String resBody = res.readEntity(String.class);
//        Person p = res.readEntity(Person.class);
        //System.out.println(p.getIdPerson());
        
        
        //InputStream is = res.readEntity(InputStream.class);
        
        
//        byte[] buffer = new byte[8 * 1024];
//        int bytesRead;
//        try {
//			while ((bytesRead = is.read(buffer)) != -1) {
//			    try {
//					os.write(buffer, 0, bytesRead);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        IOUtils.closeSilently(is);
//        IOUtils.closeSilently(os);
        
        
        
        
        
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

    private static void printResBody(Response res) {
    	System.out.println(res.readEntity(String.class));		
	}

	private static void printStatus(Response res) {
    	System.out.println("=> HTTP Status: " + res.getStatus());		
	}

	private static URI getBaseURI() {
    	String HEROKU="https://sleepy-caverns-6041.herokuapp.com/dsantoroa2/";
    	String TOMCAT="http://localhost:8080/introsde-2015-assignment-2/jersey/";
    	String LOCAL="http://192.168.5.3:5700/dsantoroa2/";
    	
        return UriBuilder.fromUri(TOMCAT).build();
    }
    
    
}