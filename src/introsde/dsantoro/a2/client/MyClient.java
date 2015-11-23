package introsde.dsantoro.a2.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

import introsde.dsantoro.a2.model.Person;

public class MyClient {
	File jsonFile=null;
	PrintWriter jsonOut=null;
	File xmlFile=null;
	PrintWriter xmlOut=null;
	
	public MyClient() {
		jsonFile = new File("./client-server-json.log");
		try {
			jsonOut = new PrintWriter(jsonFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlFile = new File("./client-server-xml.log");
		try {
			xmlOut = new PrintWriter(xmlFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
    public static void main(String[] args) {
    	new MyClient().start();
    }

    private String printResBody(Response res) {
    	return res.readEntity(String.class);		
	}

	private String printStatus(Response res) {
    	return "=> HTTP Status: " + res.getStatus();		
	}

	private URI getBaseURI() {
    	String HEROKU="https://sleepy-caverns-6041.herokuapp.com/dsantoroa2/";
    	String TOMCAT="http://localhost:8080/introsde-2015-assignment-2/jersey/";
    	String LOCAL="http://192.168.5.3:5700/dsantoroa2/";    	
        return UriBuilder.fromUri(HEROKU).build();
    }
	
	private void pJson(String string) {
		// Print to file
		jsonOut.println(string);		
		// Print to STDOUT
		System.out.println(string);		
	}
	
	private void pXml(String string) {
		// Print to file
		xmlOut.println(string);		
		// Print to STDOUT
		System.out.println(string);
	}
 
	private void start(){
		ClientConfig clientConfig = new ClientConfig();        
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget service = client.target(getBaseURI());
        Response res = null;
        String url="";
        String rqst="0";
        int first_person_d=-1;
        int last_person_d=-1;
        final String XML = "APPLICATION/XML";
        final String JSON = "APPLICATION/JSON";

        //Step 1) - Print URL of server
        System.out.println("TESTING SERVER AT URL: " + getBaseURI());
        jsonOut.println("TESTING SERVER AT URL: " + getBaseURI());
        xmlOut.println("TESTING SERVER AT URL: " + getBaseURI());
        
        // Step 3.1) - xml        
        url="/person";
        rqst="1";
        pXml("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/XML");
        service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        List<Person> pL = res.readEntity(new GenericType<List<Person>>() {});
        pXml("=> Result: " + ((pL.size()<3) ? "ERROR" : "OK"));
        pXml(printStatus(res));
        pXml(printResBody(res));
        //pXml(resBody);
        
        // Step 3.1) - json        
        pJson("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/JSON");
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();        
        //resBody = res.readEntity(String.class);
        pL = res.readEntity(new GenericType<List<Person>>() {});
        pJson("=> Result: " + ((pL.size()<3) ? "ERROR" : "OK"));        
        pJson(printStatus(res));
        pJson(printResBody(res));
        //pJson(resBody);
        
        first_person_d = pL.get(0).getIdPerson();
        last_person_d = pL.get(pL.size()-1).getIdPerson();
        
        // Step 3.2) - xml        
        url="/person/" + first_person_d;
        rqst="2";
        pXml("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/XML");
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        pXml("=> Result: " + ( ((res.getStatus() == 200) || (res.getStatus() == 202)) ? "OK" : "ERROR"));
        pXml(printStatus(res));
        pXml(printResBody(res));
        //pXml(resBody);
        
        // Step 3.2) - json        
        pJson("\nRequest #"+rqst+" GET " + url + " Accept: APPLICATION/JSON");
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();
        pJson("=> Result: " + ( ((res.getStatus() == 200) || (res.getStatus() == 202)) ? "OK" : "ERROR"));
        Person p = res.readEntity(Person.class);        
        pJson(printStatus(res));
        pJson(printResBody(res));
        //pJson(resBody);
        
        
        // Step 3.3) - xml        
        url="/person/" + first_person_d;
        rqst="3";
        p.setName("Marco");
        pXml("\nRequest #"+rqst+" PUT " + url + " Accept: APPLICATION/XML Content-type: " + XML);
        res = service.path(url).request().put(Entity.xml(p));
        Response putRes = res;
        res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        res.bufferEntity();
        p = res.readEntity(Person.class);
        pXml("=> Result: " + ((p.getFirstname().equals("Marco")) ? "OK" : "ERROR"));
        pXml(printStatus(putRes));
        pXml(printResBody(res));
        
        // Step 3.3) - json
        p.setName("Daniele");
        pJson("\nRequest #"+rqst+" PUT " + url + " Accept: APPLICATION/JSON  Content-type: " + JSON);                
        res = service.path(url).request().put(Entity.json(p));
        putRes = res;
        res = service.path(url).request().accept(MediaType.APPLICATION_JSON).get();
        res.bufferEntity();
        p = res.readEntity(Person.class);
        pJson("=> Result: " + ((p.getFirstname().equals("Daniele")) ? "OK" : "ERROR"));        
        pJson(printStatus(putRes));
        pJson(printResBody(res));
        
        // Step 3.4) - xml        
        url="/person/";
        rqst="4";
        
        String newPerson = "<person><firstname>Chuck</firstname><birthdate>01/01/1945</birthdate><healthProfile><healthProfile><measureDefinition><measure>weight</measure><idMeasureDef>1</idMeasureDef><measureName>weight</measureName><measureType>double</measureType></measureDefinition><value>78.9</value></healthProfile><healthProfile><measureDefinition><measure>height</measure><idMeasureDef>2</idMeasureDef><measureName>height</measureName><measureType>double</measureType></measureDefinition><value>172</value></healthProfile></healthProfile><lastname>Norris</lastname><name>Chuck</name></person>";
        
        pXml("\nRequest #"+rqst+" POST " + url + " Accept: APPLICATION/XML Content-type: " + XML);
        res = service.path(url).request().post(Entity.xml(newPerson));        
        res.bufferEntity();
        //res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        //pXml(res.readEntity(String.class));
        p = res.readEntity(Person.class);
        int pId = p.getIdPerson();
        if ( ((res.getStatus() == 200) || (res.getStatus() == 202)) && (pId != -1) ) {
        	pXml("=> Result: OK");	
        }else{
        	pXml("=> Result: ERROR");
        }
        res.bufferEntity();
        pXml(printStatus(res));
        pXml(printResBody(res));
              
        // Step 3.4) - json
        
        newPerson = "{\"lastname\": \"Norris\",\"username\": null,\"birthdate\": \"01/01/1945\",\"email\": null,\"healthProfile\": [{\"value\": \"78.9\",\"measureDefinition\": {\"idMeasureDef\": 1,\"measureType\": \"double\",\"measureName\": \"weight\",\"measure\": \"weight\"}},{\"value\": \"172\",\"measureDefinition\": {\"idMeasureDef\": 2,\"measureType\": \"double\",\"measureName\": \"height\",\"measure\": \"height\"}}],\"firstname\": \"Chuck\"}";
        
        pJson("\nRequest #"+rqst+" POST " + url + " Accept: " + JSON + " Content-type: " + JSON);
        res = service.path(url).request().post(Entity.json(newPerson));
        res.bufferEntity();
        //res = service.path(url).request().accept(MediaType.APPLICATION_XML).get();
        p = res.readEntity(Person.class);
        pId = p.getIdPerson();
        if ( ((res.getStatus() == 200) || (res.getStatus() == 202)) && (pId != -1) ) {
        	pJson("=> Result: OK");	
        }else{
        	pJson("=> Result: ERROR");
        }
        res.bufferEntity();
        pJson(printStatus(res));
        pJson(printResBody(res));
        this.jsonOut.close();
        this.xmlOut.close();
	}
    
}