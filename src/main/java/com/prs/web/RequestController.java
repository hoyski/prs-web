package com.prs.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.Request;

import com.prs.db.RequestRepo;

@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {

	/*
	 * A controller will implement 5 CRUD methods: 1) GET ALL 2) GET BY ID 3) POST -
	 * INSERT 4) PUT - UPDATE 5) DELETE - DELETE
	 */

	@Autowired
	private RequestRepo requestRepo;

	// Get all requests
	@GetMapping("/")
	public List<Request> getAll() {
		return requestRepo.findAll();
	}

	// Get all requests in review status and userId != id
	@GetMapping("/list-review/{id}")
	public List<Request> getRequestsInReview(@PathVariable int id) {
		return requestRepo.findByUserIdNotAndStatus(id, "review");
	}

	// Get a request by id
	@GetMapping("/{id}")
	public Optional<Request> getById(@PathVariable int id) {
		return requestRepo.findById(id);
	}

	// Add a request
	@PostMapping("/")
	public Request addRequest(@RequestBody Request r) {
		// Set status and submittedDate
		r.setStatus("New");
		r.setSubmittedDate(LocalDateTime.now());
		r = requestRepo.save(r);
		return r;
	}

	// Update a request
	@PutMapping("/")
	public Request updateRequest(@RequestBody Request r) {
		r = requestRepo.save(r);
		return r;
	}

	// Submit request for review
	@PutMapping("/submit-review")
	public Request submitRequestForReview(@RequestBody Request r) {
		// Change status based on requirements
		if (r.getTotal() <= 50.0) {
			r.setStatus("Approved");
		} else {
			r.setStatus("Review");
		}
		// Set requests submittedDate to the current date
		r.setSubmittedDate(LocalDateTime.now());
		r = requestRepo.save(r);
		return r;
	}

	// Request approved
	@PutMapping("/approve")
	public Request approveRequest(@RequestBody Request r) {
		r.setStatus("Approved");
		r = requestRepo.save(r);
		return r;
	}

	// Request rejected
	@PutMapping("/reject")
	public Request rejectRequest(@RequestBody Request r) {
		r.setStatus("Rejected");
		r = requestRepo.save(r);
		return r;

	}

	// Delete a request
	@DeleteMapping("/{id}")
	public Request deleteRequest(@PathVariable int id) {
		// Optional type will wrap a request
		Optional<Request> r = requestRepo.findById(id);
		// isPresent() will return true if a request was found
		if (r.isPresent()) {
			requestRepo.deleteById(id);
		} else {
			System.out.println("Error - request not found for id " + id);
		}
		return r.get();
	}

}
