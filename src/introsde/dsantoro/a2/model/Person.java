package introsde.dsantoro.a2.model;

import introsde.dsantoro.a2.dao.HealthCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whole table must be persisted 
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
@XmlType (propOrder={"firstname","lastname","birthdate"}) // TODO Add HelthProfile in sortOrder
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="Person")
    @Column(name="idPerson")
    private int idPerson;
    @Column(name="lastname")
    private String lastname;
    @XmlElement(name="firstname")
    @Column(name="name")
    private String name;
    @Column(name="username")
    private String username;
    @Temporal(TemporalType.DATE) // defines the precision of the date attribute
    @Column(name="birthdate")
    private Date birthdate; 
    @Column(name="email")
    private String email;
    
    // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
    @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<LifeStatus> lifeStatus;

 // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
    @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<HealthProfile> healthProfile;
    
    @XmlElementWrapper(name = "Measurements")
    public List<LifeStatus> getLifeStatus() {
        return lifeStatus;
    }
    
    @XmlElementWrapper(name = "healthProfile")
    public List<HealthProfile> getHealthProfile() {
        return healthProfile;
    }
    
    public void setHealthProfile(List<HealthProfile> hp) {
        this.healthProfile = hp;
    }
    
    // add below all the getters and setters of all the private attributes
    
    // getters
    public int getIdPerson(){
        return idPerson;
    }

    public String getLastname(){
        return lastname;
    }
    public String getName(){
        return name;
    }
    public String getUsername(){
        return username;
    }
    public String getBirthdate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        // Get the date today using Calendar object.
        return df.format(birthdate);
    }
    public String getEmail(){
        return email;
    }
    
    // setters
    public void setIdPerson(int idPerson){
        this.idPerson = idPerson;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setBirthdate(String bd) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = format.parse(bd);
        this.birthdate = date;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public static Person getPersonById(int personId) {
        EntityManager em = HealthCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        HealthCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static List<Person> getAll() {
        EntityManager em = HealthCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        HealthCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
        EntityManager em = HealthCoachDao.instance.createEntityManager();        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        HealthCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = HealthCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        HealthCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
        EntityManager em = HealthCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        HealthCoachDao.instance.closeConnections(em);
    }
    
}