package lab7.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lab7.entity.Student;

import lab7.entity.Subject;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SubjectsDAO {
	private static final String ENTITY_MANAGER_FACTORY_NAME = "simpleFactory2";
	private EntityManagerFactory factory;

	private static SubjectsDAO instance = null;

	public static SubjectsDAO getInstance() {
		if (instance == null) {
			System.out.println(
					"Creating Singleton");
			instance = new SubjectsDAO();
		}
		return instance;

	}

	public SubjectsDAO() {
		factory = Persistence.createEntityManagerFactory(ENTITY_MANAGER_FACTORY_NAME);
	}

	public void addStudentToSubject(Student student, Subject subject) {
		EntityManager entityManager = null;
		EntityTransaction transaction = null;
		try {
			entityManager = factory.createEntityManager();
			transaction = entityManager.getTransaction();
			transaction.begin();

			// 获取已存在的学生和课程
			Student managedStudent = entityManager.find(Student.class, student.getId());
			Subject managedSubject = entityManager.find(Subject.class, subject.getId());

			// 将学生关联到课程
			List<Student> students = managedSubject.getStudents();
			students.add(managedStudent);
			managedSubject.setStudents(students);

			// 保存更新后的课程到数据库
			entityManager.merge(managedSubject);
			transaction.commit();

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

	public List<Subject> loadAllSubjects(int id) {

		EntityManager entityManager = null;
		try {
			entityManager = factory.createEntityManager();
			// create named query
			Query query = entityManager.createNamedQuery("AllSubjects");
			// set parameter lastName
			query.setParameter("ID", id);
			// execute query
			List<Subject> LectorList = (List<Subject>) query.getResultList();
			return LectorList;
		} finally {
			entityManager.close();
		}
	}
}