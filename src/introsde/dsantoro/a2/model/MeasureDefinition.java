package introsde.dsantoro.a2.model;

import introsde.dsantoro.a2.dao.HealthCoachDao;
//import introsde.rest.ehealth.model.MeasureDefaultRange;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the "MeasureDefinition" database table.
 * 
 */
@Entity
@Table(name="MeasureDefinition")
@NamedQuery(name="MeasureDefinition.findAll", query="SELECT m FROM MeasureDefinition m")
@XmlRootElement
public class MeasureDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measuredef")
	@TableGenerator(name="sqlite_measuredef", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureDefinition")
	@Column(name="idMeasureDef")
	@XmlTransient
	private int idMeasureDef;

	@XmlElement(name="measure")
	@Column(name="measureName")
	private String measure;

	@Column(name="measureType")
	private String measureType;

//	@OneToMany(mappedBy="measureDefinition")
//	private List<MeasureDefaultRange> measureDefaultRange;
//
//	public MeasureDefinition() {
//	}

	public int getIdMeasureDef() {
		return this.idMeasureDef;
	}

	public void setIdMeasureDef(int idMeasureDef) {
		this.idMeasureDef = idMeasureDef;
	}

	public String getMeasureName() {
		return this.measure;
	}

	public void setMeasureName(String measureName) {
		this.measure = measureName;
	}

	public String getMeasureType() {
		return this.measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

//	public List<MeasureDefaultRange> getMeasureDefaultRange() {
//	    return measureDefaultRange;
//	}
//
//	public void setMeasureDefaultRange(List<MeasureDefaultRange> param) {
//	    this.measureDefaultRange = param;
//	}

	// database operations
	public static MeasureDefinition getMeasureDefinitionById(int personId) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		MeasureDefinition p = em.find(MeasureDefinition.class, personId);
		HealthCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<MeasureDefinition> getAll() {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
	    List<MeasureDefinition> list = em.createNamedQuery("MeasureDefinition.findAll", MeasureDefinition.class).getResultList();
	    HealthCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static MeasureDefinition saveMeasureDefinition(MeasureDefinition p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static MeasureDefinition updateMeasureDefinition(MeasureDefinition p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeMeasureDefinition(MeasureDefinition p) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    HealthCoachDao.instance.closeConnections(em);
	}
	
	public static MeasureDefinition getMeasureDefinitionIdByName(String name) {
		EntityManager em = HealthCoachDao.instance.createEntityManager();
		MeasureDefinition p = em.find(MeasureDefinition.class, name);
		HealthCoachDao.instance.closeConnections(em);
		return p;
	}
}
