package com.rex.core;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.rex.backend.entity.Job;

public class DataGenerator {
	
		final static String[] groupsNames = { "Corporate Development",
				"Human Resources", "Legal", "Environment", "Quality Assurance",
				"Research and Development", "Production", "Sales", "Marketing" };
		final static String[] officeNames = { "London",
			"New York", "Tokyo", "Turku"};
		final static String[] fnames = { "Peter", "Alice", "Joshua", "Mike",
				"Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik",
				"Rene", "Lisa", "Marge" };
		final static String[] lnames = { "Smith", "Gordon", "Simpson", "Brown",
				"Clavel", "Simons", "Verne", "Scott", "Allison", "Gates",
				"Rowling", "Barks", "Ross", "Schneider", "Tate" };
		final static String cities[] = { "Amsterdam", "Berlin", "Helsinki",
				"Hong Kong", "London", "Luxemburg", "New York", "Oslo", "Paris",
				"Rome", "Stockholm", "Tokyo", "Turku" };
		final static String streets[] = { "4215 Blandit Av.", "452-8121 Sem Ave",
				"279-4475 Tellus Road", "4062 Libero. Av.", "7081 Pede. Ave",
				"6800 Aliquet St.", "P.O. Box 298, 9401 Mauris St.",
				"161-7279 Augue Ave", "P.O. Box 496, 1390 Sagittis. Rd.",
				"448-8295 Mi Avenue", "6419 Non Av.", "659-2538 Elementum Street",
				"2205 Quis St.", "252-5213 Tincidunt St.",
				"P.O. Box 175, 4049 Adipiscing Rd.", "3217 Nam Ave",
				"P.O. Box 859, 7661 Auctor St.", "2873 Nonummy Av.",
				"7342 Mi, Avenue", "539-3914 Dignissim. Rd.",
				"539-3675 Magna Avenue", "Ap #357-5640 Pharetra Avenue",
				"416-2983 Posuere Rd.", "141-1287 Adipiscing Avenue",
				"Ap #781-3145 Gravida St.", "6897 Suscipit Rd.",
				"8336 Purus Avenue", "2603 Bibendum. Av.", "2870 Vestibulum St.",
				"Ap #722 Aenean Avenue", "446-968 Augue Ave",
				"1141 Ultricies Street", "Ap #992-5769 Nunc Street",
				"6690 Porttitor Avenue", "Ap #105-1700 Risus Street",
				"P.O. Box 532, 3225 Lacus. Avenue", "736 Metus Street",
				"414-1417 Fringilla Street", "Ap #183-928 Scelerisque Road",
				"561-9262 Iaculis Avenue" };

		public static void create() {

			EntityManager em = Persistence
					.createEntityManagerFactory("reportengine")
					.createEntityManager();

			em.getTransaction().begin();
			Random r = new Random(0);
			Calendar cal = Calendar.getInstance();
			
			for (String o : officeNames) {
					Set<Job> gPersons = new HashSet<Job>();
					
					int amount = r.nextInt(15) + 1;
					for (int i = 0; i < amount; i++) {
						Job p = new Job();
						p.setJobName(fnames[r.nextInt(fnames.length)]);
						p.setJobDesc(lnames[r.nextInt(lnames.length)]);
						p.setJobMacro(cities[r.nextInt(cities.length)]);
						
						int n = r.nextInt(100000);
						if (n < 10000) {
							n += 10000;
						}
						
						gPersons.add(p);
						em.persist(p);
					}
				}
			
			em.getTransaction().commit();
		}
		
}
