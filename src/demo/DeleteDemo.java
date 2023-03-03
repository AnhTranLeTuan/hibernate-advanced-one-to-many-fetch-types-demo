package demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.lazyFetchType.Course;
import entity.lazyFetchType.InstructorDetail;
import entity.lazyFetchType.Instructor;

// There are no difference in deleting records on both fetch types
public class DeleteDemo {
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
			Course course = new Course("Example Course");
			session2.beginTransaction();
			course.setInstructor(instructor);
			session2.save(course);
			session2.getTransaction().commit();
			session2.close();
			
			
			// Checking by rereading the instructor record on the database, then printing out its fields that will also include course objects
			Session session3 = sessionFactory.getCurrentSession();
			session3.beginTransaction();
			Instructor returnedObject = session3.get(Instructor.class, instructor.getId());
			if (returnedObject == null) {
				
			} else {
				System.out.println("The returned instructor: " + returnedObject + "\n");
				System.out.println("Course objects in the instructor:" + returnedObject.getCourses());
			}
			session3.getTransaction().commit();
			session3.close();
			
			/* Delete the just-saved course, but the associated instructor record will still be available since
			 the cascade effect on the delete operation is not specified */
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(course.getId(), Course.class, sessionFactory.getCurrentSession());
			
			// Rereading the instructor record one more time
			Session session4 = sessionFactory.getCurrentSession();
			session4.beginTransaction();
			Instructor returnedObject2 = session4.get(Instructor.class, instructor.getId());
			if (returnedObject2 == null) {
				
			} else {
				System.out.println("The returned instructor: " + returnedObject2 + "\n");
				System.out.println("Course objects in the instructor:" + returnedObject2.getCourses());
			}
			session4.getTransaction().commit();
			session4.close();
		} finally {
			sessionFactory.close();
		}
	}

}
