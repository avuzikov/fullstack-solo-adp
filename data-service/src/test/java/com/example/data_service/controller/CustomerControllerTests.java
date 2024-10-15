package com.example.data_service.controller;

import com.example.data_service.model.Customer;
import com.example.data_service.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(TestSecurityConfig.class)
public class CustomerControllerTests {

	private static final Logger logger = LoggerFactory.getLogger(CustomerControllerTests.class);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser
	public void testGetAllCustomers() throws Exception {
		Customer customer1 = new Customer("John Doe", "john@example.com");
		Customer customer2 = new Customer("Jane Doe", "jane@example.com");
		when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

		MvcResult result = mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andReturn();

		logger.info("GetAllCustomers Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testCreateCustomer() throws Exception {
		Customer customer = new Customer("John Doe", "john@example.com");
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		MvcResult result = mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(customer)))
				.andExpect(status().isOk())
				.andReturn();

		logger.info("CreateCustomer Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testGetCustomerById() throws Exception {
		Customer customer = new Customer("John Doe", "john@example.com");
		customer.setId(1L);
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		MvcResult result = mockMvc.perform(get("/api/customers/1"))
				.andExpect(status().isOk())
				.andReturn();

		logger.info("GetCustomerById Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testUpdateCustomer() throws Exception {
		Customer customer = new Customer("John Doe", "john@example.com");
		customer.setId(1L);
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Customer updatedCustomer = new Customer("John Updated", "johnupdated@example.com");
		MvcResult result = mockMvc.perform(put("/api/customers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCustomer)))
				.andExpect(status().isOk())
				.andReturn();

		logger.info("UpdateCustomer Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testDeleteCustomer() throws Exception {
		Customer customer = new Customer("John Doe", "john@example.com");
		customer.setId(1L);
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		MvcResult result = mockMvc.perform(delete("/api/customers/1"))
				.andExpect(status().isOk())
				.andReturn();

		logger.info("DeleteCustomer Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testGetCustomerByIdNotFound() throws Exception {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(get("/api/customers/1"))
				.andExpect(status().isNotFound())
				.andReturn();

		logger.info("GetCustomerByIdNotFound Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testUpdateCustomerNotFound() throws Exception {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Customer updatedCustomer = new Customer("John Updated", "johnupdated@example.com");
		MvcResult result = mockMvc.perform(put("/api/customers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCustomer)))
				.andExpect(status().isNotFound())
				.andReturn();

		logger.info("UpdateCustomerNotFound Response: " + result.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser
	public void testDeleteCustomerNotFound() throws Exception {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(delete("/api/customers/1"))
				.andExpect(status().isNotFound())
				.andReturn();

		logger.info("DeleteCustomerNotFound Response: " + result.getResponse().getContentAsString());
	}
}
