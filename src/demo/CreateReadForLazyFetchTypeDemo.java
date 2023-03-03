package demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.lazyFetchType.Course;
import entity.lazyFetchType.InstructorDetail;
import entity.lazyFetchType.Instructor;

public class CreateReadForLazyFetchTypeDemo {
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		SessionFactory sessionFactory = configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class).
				addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		
		try {
			// Create and save the instructor object into the database
			Instructor instructor = new Instructor("David", "Smith", "example@gmail.com");
			StaticFunctions.saveObjectToDatabase(instructor, session);
			System.out.println("\n\n Saved instructor: " + instructor + "\n\n\n");
			
			// Create and save course objects into the database
			/* As there is the bidirectional relationship between the classes, after saving course objects, 
			 the instructor object will automatically retrieve these objects from the database and save them into its List<Course> */
			Session session2 = sessionFactory.getCurrentSession();
			Course course1 = new Course("Course 1");
			Course course2 = new Course("Course 2");
			session2.beginTransaction();
			course1.setInstructor(instructor);
			course2.setInstructor(instructor);
			session2.save(course1);
			session2.save(course2);
			session2.getTransaction().commit();
			session2.close();
			
			// Checking by rereading the instructor record on the database, then printing out its fields that will also include course objects
			/* For lazy fetch type, the session or the connection to the database is required because course objects will only be retrieved if 
			 they are requested (either by calling getCourses() on the persistence object or passing the query to the database) */
			// Below I will try to retrieve course objects by both options
			Session session3 = sessionFactory.getCurrentSession();
			session3.beginTransaction();
			// Get the persistence object of the InstructorForLazyFetchType class, then call its getCourses()
			Instructor returnedObject = session3.get(Instructor.class, instructor.getId());
			
			// Retrieve course records by using query 
			@SuppressWarnings("unchecked")
			List<Course> courses = session3.createQuery("from CourseForLazyFetchType where instructor = " + String.valueOf(instructor.getId())).getResultList();
		
			if (returnedObject == null) {
				
			} else {
				System.out.println("\n\nThe returned instructor: " + returnedObject + "\n");
				System.out.println("Course objects from the persistence object:" + returnedObject.getCourses() + "\n\n");
				
				System.out.println("Course objects from using query: " + courses);
			}
			session3.getTransaction().commit();
			session3.close();
		} finally {
			sessionFactory.close();
		}
	}

}
