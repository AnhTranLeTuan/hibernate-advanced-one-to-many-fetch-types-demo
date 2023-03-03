package demo;

import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.eagerFetchType.Course;
import entity.eagerFetchType.InstructorDetail;
import entity.eagerFetchType.Instructor;

public class CreateReadForEagerFetchTypeDemo {
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
			/* As there is the bidirectional relationship between the classes and the eager fetch type, after saving course objects, 
			 the instructor object returned from the database will automatically retrieve these course objects from the database and save 
			 them into its List<Course> (no session required) */
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
			// Checking by rereading the instructor record on the database, then printing out its data
			Session session3 = sessionFactory.getCurrentSession();
			session3.beginTransaction();
			// Get the persistence object of the InstructorForLazyFetchType class, then call its getCourses()
			Instructor returnedObject = session3.get(Instructor.class, instructor.getId());
			session3.getTransaction().commit();
			session3.close();
			
			if (returnedObject == null) {
				
			} else {
				System.out.println("\n\nThe returned instructor: " + returnedObject + "\n");
				System.out.println("The course objects: " + returnedObject.getCourses());
			}
		} finally {
			sessionFactory.close();
		}
	}

}
