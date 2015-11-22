package introsde.dsantoro.a2.resources;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import introsde.dsantoro.a2.model.HealthProfileHistory;
import introsde.dsantoro.a2.model.Person;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container

public class HealthProfileHistoryResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int personId;
    String measureType;
    int mid = -1;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

	public HealthProfileHistoryResource(UriInfo uriInfo, Request request, int personId, String measureType) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.personId = personId;
        this.measureType = measureType;        
        //this.entityManager = em;
        //System.out.println("sono qui. Param: " + this.measureType);
	}
	
	public HealthProfileHistoryResource(UriInfo uriInfo, Request request, int personId, String measureType, int mid) {
        this(uriInfo, request, personId, measureType);
        this.mid = mid;
	}

	// Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<HealthProfileHistory> getHelthProfileHistory() {
        Person person = this.getPersonById(personId);
        if (person == null)
            throw new RuntimeException("Get: Person with " + personId + " not found");
        if (this.mid != -1){
        	return person.getHealthProfileHistory(this.mid);
        } else {
        	return person.getHealthProfileHistory(this.measureType);	
        }        
        
    }

    // for the browser
    @GET
    @Produces(MediaType.TEXT_XML)
    public Person getPersonHTML() {
        Person person = this.getPersonById(personId);
        if (person == null)
            throw new RuntimeException("Get: Person with " + personId + " not found");
        System.out.println("Returning person... " + person.getIdPerson());
        return person;
    }


    private Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: "+personId);

        // this will work within a Java EE container, where not DAO will be needed
        //Person person = entityManager.find(Person.class, personId); 
        System.out.println("--> Checking presence of person with id: " +this.personId);
        Person person = Person.getPersonById(personId);
        if (person == null) System.out.println("--> Person not present, id: " +this.personId);
        return person;
    }

}
