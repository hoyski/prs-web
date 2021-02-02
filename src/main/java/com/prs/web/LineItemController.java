package com.prs.web;

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

import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;

@CrossOrigin
@RestController
@RequestMapping("/line-items")
public class LineItemController {

	/*
	 * A controller will implement 5 CRUD methods: 1) GET ALL 2) GET BY ID 3) POST -
	 * INSERT 4) PUT - UPDATE 5) DELETE - DELETE
	 */

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;

	// Get all line items
	@GetMapping("/")
	public List<LineItem> getAll() {
		return lineItemRepo.findAll();
	}

	// Get a line item by id
	@GetMapping("/{id}")
	public Optional<LineItem> getById(@PathVariable int id) {
		return lineItemRepo.findById(id);
	}

	// Add a line item
	@PostMapping("/")
	public LineItem addLineItem(@RequestBody LineItem l) {
		l = lineItemRepo.save(l);
		recalculateTotal(l.getRequest());
		return l;
	}

	// Update a line item
	@PutMapping("/")
	public LineItem updateLineItem(@RequestBody LineItem l) {
		l = lineItemRepo.save(l);
		recalculateTotal(l.getRequest());
		return l;
	}

	// Delete a line item
	@DeleteMapping("/{id}")
	public LineItem deleteLineItem(@PathVariable int id) {
		// Optional type will wrap a line item
		Optional<LineItem> l = lineItemRepo.findById(id);
		// isPresent() will return true if a line item was found
		if (l.isPresent()) {
			lineItemRepo.deleteById(id);
			recalculateTotal(l.get().getRequest());
		} else {
			System.out.println("Error - line item not found for id " + id);
		}
		return l.get();
	}

	// List line items for a purchase request
	@GetMapping("/lines-for-pr/{id}")
	public List<LineItem> getLineItemByPr(@PathVariable int id) {
		return lineItemRepo.findByRequestId(id);
	}

	// Recalculate total
	private void recalculateTotal(Request r) {
		double newTotal = 0.0;
		
		List<LineItem> lineItems = lineItemRepo.findByRequestId(r.getId());
		for (LineItem lineItem : lineItems) {

			newTotal += lineItem.getProduct().getPrice() * lineItem.getQuantity();
		}
		r.setTotal(newTotal);
		requestRepo.save(r);
	}

}
