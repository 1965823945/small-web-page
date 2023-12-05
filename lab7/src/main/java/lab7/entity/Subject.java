package lab7.entity;

import java.util.List;

import java.util.Date;
import jakarta.persistence.*;
@Entity
@Table(name = "SUBJECT")

@NamedQueries({

	@NamedQuery(name = "AllSubjects", 
			   query ="select sb\n"+
			   "FROM Subject sb\r\n" + //
			   "WHERE sb.id > :ID")
	})

public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "LECTOR")
	private String lector;

	@Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;

	// mapping of many-to-many relationship in the inverse side
	@ManyToMany(
		cascade = {CascadeType.PERSIST, CascadeType.MERGE},
		mappedBy = "subjects"
	)
	private List<Student> students;

	public void setName(String name) {
		this.name=name;
		
	}

	public void setLector(String lector) {
		this.lector=lector;
		
	}

	public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

	public void setStudents(List<Student> students2) {
		this.students=students2;
		
	}

	public int getId() {
      return id;
	}
	
	public String getName() {
		
		return name;
	}

	public String getLector() {
		
		return lector;
	}

    public Date getStartDate() {
        return startDate;
    }

	public List<Student> getStudents() {
		
		return students;
	}

}

