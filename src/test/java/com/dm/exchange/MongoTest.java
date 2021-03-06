package com.dm.exchange;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dm.model.datamongo.Customer;
import com.dm.model.datamongo.CustomerRepository;

public class MongoTest extends AbstractIntegrationTest{
	
	@Autowired
	private CustomerRepository repository;
	
	//@Test
	public void testRepository() throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
                repository.findAll().stream().forEach((customer) -> {
                System.out.println(customer);
            });
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
                repository.findByLastName("Smith").stream().forEach((customer) -> {
                System.out.println(customer);
            });

	}

}
