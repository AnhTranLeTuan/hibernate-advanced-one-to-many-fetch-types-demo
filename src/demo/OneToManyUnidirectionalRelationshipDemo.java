package demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Review;
import entity.lazyFetchType.Course;
import entity.lazyFetchType.Instructor;
import entity.lazyFetchType.InstructorDetail;

public class OneToManyUnidirectionalRelationshipDemo {
	
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		SessionFactory sessionFactory = configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class).
				addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).addAnnotatedClass(Review.class).
				buildSessionFactory();
		try {
			// Create and save the course object into the database
			Course course = new Course("Example Course");
			StaticFunctions.saveObjectToDatabase(course, sessionFactory.getCurrentSession());
			System.out.println("The saved course: " + course + "\n\n\n");
			
			// Create review objects that mapped with the just-saved course object and save to the database
			Review review1 = new Review("Review 1");
			Review review2 = new Review("Review 2");
			Review review3 = new Review("Review 3");
			review1.setCourse(course);
			review2.setCourse(course);
			review3.setCourse(course);
			Session session2 = sessionFactory.getCurrentSession();
			session2.beginTransaction();
			session2.save(review1);
			session2.save(review2);
			session2.save(review3);
			session2.getTransaction().commit();
			session2.close();
			
			// Rereading saved objects the first time
			/* As I utilize the lazy fetch type for the relationships, we will need to call getter methods inside the session for
			 retrieving dependent objects */ 
			Session session3 = sessionFactory.getCurrentSession();
			session3.beginTransaction();
			Course returnedObject = session3.get(Course.class, course.getId());
			List<Review> reviews = returnedObject.getReviews();
			System.out.println("The returned course: " + returnedObject);
			System.out.println("The returned reviews: " + reviews + "\n\n\n");
			session3.getTransaction().commit();
			session3.close();
			
			// Delete a review object to see how it will affect the associated course object
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(reviews.get(0).getId(), Review.class, sessionFactory.getCurrentSession());
			
			/* After rereading the records, we can see that although a review object has been deleted, 
			 the associated coure object is still available, indicating that there is no delete cascade effect */
			Session session4 = sessionFactory.getCurrentSession();
			session4.beginTransaction();
			Course returnedObject2 = session4.get(Course.class, course.getId());
			List<Review> reviews2 = returnedObject2.getReviews();
			System.out.println("The returned course: " + returnedObject2);
			System.out.println("The returned reviews: " + reviews2 + "\n\n\n");
			session4.getTransaction().commit();
			session4.close();
			
			// Let's try to delete the coure object instead
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(course.getId(), Course.class, sessionFactory.getCurrentSession());
			
			/* As the delete cascade effect is vailable from the course object to review objects, when the course object
			 is deleted, the associated review objects also will be deleted */
			System.out.println("The review objects associated with the deleted coure object: ");
			StaticFunctions.readObjectFromDatabaseByQuery("from Review where course = " + String.valueOf(course.getId()), sessionFactory.getCurrentSession());
			
		} finally {
			sessionFactory.close();
		}
	}

}
