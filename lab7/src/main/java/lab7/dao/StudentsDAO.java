package lab7.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
//import java.sql.Date;
import java.util.Date;
import java.util.List;

import org.eclipse.persistence.jpa.PersistenceProvider;

import lab7.entity.Student;
import lab7.entity.Subject;
import jakarta.persistence.*;

public class StudentsDAO {
	private static final String ENTITY_MANAGER_FACTORY_NAME = "simpleFactory2";
	private EntityManagerFactory factory;

	private static StudentsDAO instance = null;

	public static StudentsDAO getInstance() {
		if (instance == null) {
			System.out.println(
					"Creating Singleton");
			instance = new StudentsDAO();
		}
		return instance;

	}

	public StudentsDAO() {

		factory = new PersistenceProvider().createEntityManagerFactory("simpleFactory2", null);
	}

	public StudentsDAO(EntityManagerFactory factory) {
		this.factory = factory;
		;
		System.out.println("factory =" + factory);
	}

	public void createDemoStudent() {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = factory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();

			// create student
			Student student = new Student();
			student.setFirstName("Sergey");
			student.setLastName("Sidorov");
			student.setStartDate((java.util.Date) Calendar.getInstance().getTime());
			// create Subject
			Subject Subject = new Subject();
			Subject.setLector("Ivanov");
			Subject.setName("Geometry");

			List<Student> students = new ArrayList<Student>();
			List<Subject> Subjects = new ArrayList<Subject>();
			// associate student with Subject
			students.add(student);
			Subjects.add(Subject);
			Subject.setStudents(students);
			student.setSubjects(Subjects);

			// save student to database
			entityManager.persist(student);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				// entityManager.close();
			}
		}
	}

	/**
	 * Demonstrates named query.
	 * Display student list with the specified last name
	 * 
	 * @param lastName the last name of students to be displayed
	 */
	@SuppressWarnings("unchecked")
	public List<Student> loadStudentsByLastName(String lastName, Date d) {
		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("studentByLastName");
			// set parameter lastName
			query.setParameter("lastName", lastName);
			query.setParameter("date", d);
			// execute query
			List<Student> studentList = (List<Student>) query.getResultList();
			return studentList;
		} finally {
			entityManager.close();
		}
	}

	public List<Student> loadStudentsInSubject(String subjectName) {
		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("StudentsInSubject");
			// set parameter lastName
			query.setParameter("subjectName", subjectName);
			// execute query
			List<Student> studentList = (List<Student>) query.getResultList();
			return studentList;
		} finally {
			entityManager.close();
		}
	}

	public List<Student> loadAllStudents(int id) {
		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("AllStudents");
			// set parameter lastName
			query.setParameter("ID", id);
			// execute query
			List<Student> StudentsList = (List<Student>) query.getResultList();
			return StudentsList;
		} finally {
			entityManager.close();
		}
	}

	//////////////////////////////////////
	public Long loadNumberStudentOfSubject(String Name) {
		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("NumberStudentOfSubject");
			// set parameter lastName
			query.setParameter("subjectName", Name);
			// execute query
			Long num = (Long) query.getSingleResult();
			return num;
		} finally {
			entityManager.close();
		}
	}

	public List<String> loadFacultySubjectLectors() {
		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("FacultySubjectLectors");
			// set parameter lastName
			// execute query
			List<String> LectorList = (List<String>) query.getResultList();
			return LectorList;
		} finally {
			entityManager.close();
		}
	}

	public void deleteSubject(Subject subject) {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = factory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();

			// Find the subject in the database
			Subject existingSubject = entityManager.find(Subject.class, subject.getId());
			if (existingSubject != null) {
				// Remove the subject from associated students
				List<Student> students = existingSubject.getStudents();
				for (Student student : students) {
					student.getSubjects().remove(existingSubject);
				}
				// Remove the subject from the database
				entityManager.remove(existingSubject);
				transaction.commit();
				System.out.println("Subject \"" + existingSubject.getName() + "\" has beendeleted.");
			} else {
				System.out.println("Subject not found.");
			}
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

	public void deleteSubjectByName(String name) {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = factory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();

			// Find the subject in the database by name
			TypedQuery<Subject> query = entityManager.createQuery("SELECT s FROM Subject s WHERE s.name = :name",
					Subject.class);
			query.setParameter("name", name);
			Subject existingSubject = query.getSingleResult();

			if (existingSubject != null) {
				// Remove the subject from associated students
				List<Student> students = existingSubject.getStudents();
				for (Student student : students) {
					student.getSubjects().remove(existingSubject);
				}
				// Remove the subject from the database
				entityManager.remove(existingSubject);
				transaction.commit();
				System.out.println("Subject \"" + existingSubject.getName() + "\" has been deleted.");
			} else {
				System.out.println("Subject not found.");
			}
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}
}
