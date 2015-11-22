package introsde.dsantoro.a2.model;

import introsde.dsantoro.a2.dao.HealthCoachDao;
import introsde.dsantoro.a2.model.MeasureDefinition;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.OneToOne;

/**
 * The persistent class for the "HealthProfile" database table.
 * 
 */
@Entity
@Table(name = "HealthProfile")
@NamedQuery(name = "HealthProfile.findAll", query = "SELECT l FROM HealthProfile l")
@XmlRootElement(name="healthprofile")
//@XmlType(propOrder = { "weight", "height", "BMI" })
//@XmlAccessorType(XmlAccessType.FIELD)
public class HealthProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_healthprofile")
	@TableGenerator(name="sqlite_healthprofile", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="HealthProfile")
	@Column(name = "idMeasure")
	private int idMeasure;

	@Column(name = "value")
	private String value;
	
	@OneToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef", insertable = true, updatable = true)
	private MeasureDefinition measureDefinition;
	
	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;

	public HealthProfile() {
	}

	public int getIdMeasure() {
		return this.idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
		this.measureDefinition = param;
	}

	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
//	@XmlElement(name="bmi")
//	public double getBMI() {
//		return this.weight/(Math.pow(this.height, 2));
//	}
//	
//	public String toString() {
//		return "{"+this.height+","+this.weight+","+this.getBMI()+","+"}";
//	}
	
	// Database operations
	// Notice that, for this example, we create and destroy and entityManager on each operation. 
	// How would you change the DAO to not having to create the entity manager every time? 
	public static HealthProfile getHealthProfileById(int lifestatusId) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		HealthProfile p = em.find(HealthProfile.class, lifestatusId);
		HealthCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<HealthProfile> getAll() {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
	    List<HealthProfile> list = em.createNamedQuery("HealthProfile.findAll", HealthProfile.class).getResultList();
	    HealthCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static HealthProfile saveHealthProfile(HealthProfile p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static HealthProfile updateHealthProfile(HealthProfile p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeHealthProfile(HealthProfile p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	}
}
