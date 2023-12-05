package lab7.entity;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "STUDENT")
// mapping of named query
@NamedQueries({
@NamedQuery(name = "studentByLastName", 
		   query = "select st " +
				  "from Student st " +
		    		  "where st.lastName = :lastName " +
		    		  "and st.startDate > :date"),

@NamedQuery(name = "StudentsInSubject", 
		   query = "select st " +
				  "from Student st, " +
		    		  "in (st.subjects) c " +
		    		  "where c.name = :subjectName" ),

@NamedQuery(name = "AllStudents",
			query = "SELECT st\r\n" + //
					"FROM Student st\r\n" + //
					"WHERE st.id > :ID")

})
public class Student {
	// private static final TemporalType DATE = null;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	private int id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Basic
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@ManyToMany(
		cascade = { CascadeType.PERSIST, CascadeType.MERGE }
	)
	// specifying join table
	@JoinTable(
		name = "SUBJECT_STUDENT",
		joinColumns = {@JoinColumn(name = "STUDENT_ID")},
		inverseJoinColumns={@JoinColumn(name="SUBJECT_ID")}
	)
	private List<Subject> subjects;

	public void setFirstName(String firstName) {
		this.firstName = firstName;

	}

	public void setLastName(String lastName) {
		this.lastName = lastName;

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getLastName() {

		return lastName;
	}

	public int getId() {

		return id;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}
}
