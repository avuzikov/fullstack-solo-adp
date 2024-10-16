package com.example.data_service.controller;

import com.example.data_service.model.Customer;
import com.example.data_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/api")
	public ResponseEntity<Map<String, String>> getApiStatus() {
		return ResponseEntity.ok(Map.of("status", "Data service is up and running"));
	}

	@GetMapping("/api/customers")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@PostMapping("/api/customers")
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
		Customer savedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(savedCustomer);
	}

	@GetMapping("/api/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		return customerRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/api/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customerDetails) {
		return customerRepository.findById(id)
				.map(customer -> {
					customer.setName(customerDetails.getName());
					customer.setEmail(customerDetails.getEmail());
					Customer updatedCustomer = customerRepository.save(customer);
					return ResponseEntity.ok(updatedCustomer);
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/api/customers/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
		return customerRepository.findById(id)
				.map(customer -> {
					customerRepository.delete(customer);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}