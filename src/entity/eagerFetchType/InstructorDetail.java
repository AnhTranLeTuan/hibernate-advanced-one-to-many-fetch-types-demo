package entity.eagerFetchType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="instructor_detail")
public class InstructorDetail {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="channel")
	private String channel;
	@Column(name="hobby")
	private String hobby;
	// Bi-directional
	// Create the one-to-one mapping for the InstructorDetail object to the appropriate Instructor object  
	/* the mappedby property is used to tell the field on the Instructor class that have @JoinColumn which will contain 
     the information needed to verify the approriate Instructor object for the specific InstructorDEtail object (the same ID) */
	// We can also apply casecade to this relationship
	@OneToOne(mappedBy="instructorDetail", cascade=CascadeType.ALL)
	private Instructor instructor;
	
	public InstructorDetail() {}
	
	public InstructorDetail(String channel, String hobby) {
		this.channel = channel;
		this.hobby = hobby;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getHobby() {
		return hobby;
	}
	
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "InstructorDetail [id=" + id + ", channel=" + channel + ", hobby=" + hobby + "]";
	}
	
}
