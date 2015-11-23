package introsde.dsantoro.a2.test.model;

import static org.junit.Assert.*;
import introsde.dsantoro.a2.model.Person;

import java.util.Calendar;
import java.util.List;
import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonTest {

	static final String JPA_NAME="introsde-2015-assignment-2";
	
	@Test
	public void readPersonListTest() {
		System.out.println("--> TEST: readPersonList");
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();
		for (Person person : list) {
			System.out.println("--> Person = "+person.toString());
		}
		assertTrue(list.size()>10);
	}

	// test for adding person without using the DAO object, but isntead using the entity manager 
	// created in the testing unit by the beforetest method
	@Test
	public void addPersonTest() throws ParseException {
		System.out.println("--> TEST: addPerson");
		// count people before starting
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();
		int originalCount = list.size();
		
		// Arrange
		Person at = new Person();
		at.setName("test");
		at.setLastname("Palleto");
		at.setBirthdate("02/03/1987");

		System.out.println("--> TEST: addPerson => persisting the new person");
		// Act
		tx.begin();
		em.persist(at);
		tx.commit();
		
		// Assert
		assertNotNull("Id should not be null", at.getIdPerson());
		
		// Query using CriteriaBuilder and CriteriaQuery Example
		int idNewPerson = at.getIdPerson();
		
		System.out.println("--> TEST: addPerson => querying for the new person");
		// 1. Prepare the criteria builder and the criteria query to work with the model you want
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		
		// setup a metamodel for the root of the query
		Metamodel m = em.getMetamodel();
		EntityType<Person> Person_ = m.entity(Person.class);
		Root<Person> person = cq.from(Person_);
		
		// prepare a parameter expression to setup parameters for the query
		ParameterExpression<Integer> p = cb.parameter(Integer.class);
		
		// prepare the query
		cq.select(person).where(cb.equal(person.get("idPerson"), p));
		
		// execute the query and set the parameters with values
		TypedQuery<Person> tq = em.createQuery(cq);
		tq.setParameter(p, idNewPerson);
		
		// get the results
		List<Person> newPersonList = tq.getResultList();
		
		assertEquals("Table has one entities", 1, newPersonList.size());
		assertEquals("Table has correct name", "test", newPersonList.get(0).getFirstname());

		System.out.println("--> TEST: addPerson => deleting the new person");
		em.getTransaction().begin();
		em.remove(newPersonList.get(0));
		em.getTransaction().commit();

		list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();

		assertEquals("Table has no entity", originalCount, list.size());
	}

	// same adding person test, but using the DAO object
	@Test
	public void addPersonWithDaoTest() throws ParseException {
		System.out.println("--> TEST: addPersonWithDao");

		List<Person> list = Person.getAll();
		int personOriginalCount = list.size();
		
		Person p = new Person();
		p.setName("Pinco");
		p.setLastname("Pallino");
		p.setBirthdate("02/03/1987");

		System.out.println("--> TEST: addPersonWithDao ==> persisting person");
		Person.savePerson(p);
		assertNotNull("Id should not be null", p.getIdPerson());

		System.out.println("--> TEST: addPersonWithDao ==> getting the list");
		list = Person.getAll();
		assertEquals("Table has two entities", personOriginalCount+1, list.size());
		
		Person newPerson = Person.getPersonById(p.getIdPerson());

		System.out.println("--> TEST: addPersonWithDao ==> removing new person");
		Person.removePerson(newPerson);
		list = Person.getAll();
		assertEquals("Table has two entities", personOriginalCount, list.size());
	}

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Testing JPA on lifecoach database using '"+JPA_NAME+"' persistence unit");
		emf = Persistence.createEntityManagerFactory(JPA_NAME);
		em = emf.createEntityManager();
	}

	@AfterClass
	public static void afterClass() {
		em.close();
		emf.close();
	}

	@Before
	public void before() {
		tx = em.getTransaction();
	}

	private static EntityManagerFactory emf;
	private static EntityManager em;
	private EntityTransaction tx;
}