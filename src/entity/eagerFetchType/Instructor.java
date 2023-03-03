package entity.eagerFetchType;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="instructor")
public class Instructor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="first_name")
	private String first_name;
	@Column(name="last_name")
	private String last_name;
	@Column(name="email")
	private String email;
	/* One-to-one mapping between the instructor and the intructor_detail tables. By default, if we don't
     specify the cascade type, none of the type will be applied. In this case, I utilize the CascadeType.ALL
     that include all of the types */
	/* Cascade features of Hibernate seem like the reserve version of cascade features on MySQL. 
	 Specifically, on Hibernate, the parent table will be instructor table, and the instructor_detail table will be the child table.
	 On the other hand, on MySQL, the parent table will be instructor_detail table, and the instructor table will be the child table. */
	// This field will be equivalent to the foreign key which will match to the primary key on the instructor_detail table
	// @OneToOne have the default fetch type as FetchType.EAGER, so providing FetchType.EAGER on the property "fetch" will be optional 
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="instructor_detail_id")
	private InstructorDetail instructorDetail;
	// One-to-one mapping between the instructor and the course tables
	// As @OneToMany have the default fetch type as FetchType.LAZY, so I will provide FetchType.EAGER on the property "fetch" to adjust it 
	// In this case, I specify Cascade types, because not all of them are efficent
	@OneToMany(fetch=FetchType.EAGER, mappedBy="instructor", cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	private List<Course> courses;
	
	public Instructor() {}
	
	public Instructor(String first_name, String last_name, String email) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public InstructorDetail getInstructorDetail() {
		return instructorDetail;
	}

	public void setInstructorDetail(InstructorDetail instructorDetail) {
		this.instructorDetail = instructorDetail;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", email=" + email
				+ ", instructorDetail=" + instructorDetail + "]";
	}

	
}
