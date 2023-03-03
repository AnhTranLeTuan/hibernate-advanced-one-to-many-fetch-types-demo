package demo;
import java.util.List; 

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/* Since there are many instructions that will appear multiple times throughout Demo classes, I will utilize static functions on simple cases (use more 
on the hibernate-basic-CRUD-demo project since situations are not really complex) to make the program to be cleaner and more reusable */
//Static functions that can be reused on Demo classes for creating SessionFactory object, as well as saving, reading, updating, and deleting on the database
public class StaticFunctions {
	
	public static <T> SessionFactory createSessionFactory(String configFile, Class<T> annotatedClass) {
		// Create the Configuration object which can be used to retrieved the SessionFactory object
		/* We would need to call methods for setting the configuration file and the annotated classes (these methods will eventually 
		 return the Configuration object iteslf for easier calling the other set up methods). After having enough information, we can start build 
		 the SessionFactory object. */
		/* The SessionFactory object (long-lived object - only created once) can then be used to create the 
		 Session object (short-lived object - may be created multiple time) */
		Configuration configuration = new Configuration();
		/* In this situation, we don't have to pass the name of the configuration file "hibernate.cfg.xml" because
		 is name is coressponding with the default name of the file. If we don't pass the name to the parameter, Hibernate will automatically
		 use the default name. */
		SessionFactory sessionFactory = configuration.configure(configFile).addAnnotatedClass(annotatedClass).buildSessionFactory();
		return sessionFactory;
	}
	
	public static <T> void saveObjectToDatabase(T object, Session session){
		// In order to query to the database, we would need to begin the transaction and get, commit the transaction
		// Queries have to be in the between of the calling of transaction-related methods 
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();
		session.close();
	}
	
	public static <T> T readObjectFromDatabaseByPrimaryKey(int id, Class<T> annotatedClass,Session session) {
		session.beginTransaction();
		T object = session.get(annotatedClass, id);
		session.getTransaction().commit();
		session.close();
		return object;
	}
	
	// NOTE: When providing queries, we have to use the properties name from the class, not names from the dabase (using HQL instead of SQL)
	public static  <T> void readObjectFromDatabaseByQuery(String query, Session session) {
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<T> objects = session.createQuery(query).getResultList();
		System.out.println(objects);
		session.getTransaction().commit();
		session.close();
	}
	
	public static void updateDatabaseRecordsByQuery(String query, Session session) {
		session.beginTransaction();
		// Return the number of entities or records being updated or deleted
		session.createQuery(query).executeUpdate();
		session.getTransaction().commit();
		session.close();
}
	
	public static <T> void deleteRecordOnDatabaseByPersistentObject(int id, Class<T> annotatedClass,Session session) {
		session.beginTransaction();
		T object = session.get(annotatedClass, id);
		session.delete(object);
		session.getTransaction().commit();
		session.close();
	}
	
	public static void deleteDatabaseRecordsByQuery(String query, Session session) {
		session.beginTransaction();
		// Return the number of entities or records being updated or deleted
		session.createQuery(query).executeUpdate();
		session.getTransaction().commit();
		session.close();
}
	
}