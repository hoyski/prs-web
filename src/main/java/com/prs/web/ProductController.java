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

import com.prs.business.Product;

import com.prs.db.ProductRepo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

	/*
	 * A controller will implement 5 CRUD methods: 1) GET ALL 2) GET BY ID 3) POST -
	 * INSERT 4) PUT - UPDATE 5) DELETE - DELETE
	 */

	@Autowired
	private ProductRepo productRepo;

	// Get all products
	@ApiOperation("Returns all of the Products in the database")
	@GetMapping("/")
	public List<Product> getAll() {
		return productRepo.findAll();
	}

	// Get a product by id
	@ApiOperation("Returns the Product with the given ID")
	@GetMapping("/{id}")
	public Optional<Product> getById(@PathVariable @ApiParam("Unique ID of the Product") int id) {
		return productRepo.findById(id);
	}

	// Add a product
	@ApiOperation("Adds the given Product to the database. Returns the Product with the ID populated")
	@PostMapping("/")
	public Product addProduct(@RequestBody @ApiParam("The Product to add") Product p) {
		p = productRepo.save(p);
		return p;
	}

	// Update a product
	@ApiOperation("Updates the given Product in the database to match the given Product. Returns the updated Product")
	@PutMapping("/")
	public Product updateProduct(@RequestBody @ApiParam("The Product to update") Product p) {
		p = productRepo.save(p);
		return p;
	}

	// Delete a product
	@ApiOperation("Deletes the Product with the given ID if it exists from the database")
	@DeleteMapping("/{id}")
	public Product deleteProduct(@PathVariable @ApiParam("Unique ID of the Product to delete") int id) {
		// Optional type will wrap a product
		Optional<Product> p = productRepo.findById(id);
		// isPresent() will return true if a product was found
		if (p.isPresent()) {
			productRepo.deleteById(id);
		} else {
			System.out.println("Error - product not found for id " + id);
		}
		return p.get();
	}

}
