package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin

public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    private DataSource dataSource;

    // create an Autowired controller to inject the categoryDao and ProductDao


@Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao, DataSource dataSource) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.dataSource = dataSource;
    }


    // add the appropriate annotation for a get action

@GetMapping
    public List<Category> getAll(@RequestParam(name = "categoryId", required = false) Integer categoryId,
                                 @RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "description", required = false) String  description)
    {
        // find and return all categories
        return categoryDao.getAllCategories(categoryId, name,description);


    }


    // add the appropriate annotation for a get action
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category>  getById(@PathVariable int categoryId)
    {
        try
        {
            var category = categoryDao.getById(categoryId);

            if(category == null){
                return ResponseEntity.notFound().build();
            }
                return ResponseEntity.ok(category);


        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return (List<Product>) categoryDao.getById(categoryId);
    }


    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return categoryDao.create(category);
    }

@PutMapping
@PreAuthorize("hasRole('ROLE_ADMIN')")
    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        categoryDao.update(id, category);
        // update the category by id
    }


@DeleteMapping("{id}")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@ResponseStatus(value = HttpStatus.NO_CONTENT)
    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id)
{
    try
    {
        var category = categoryDao.getById(id);

        if(category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        categoryDao.delete(id);
    }
    catch(Exception ex)
    {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
    }
}
}
