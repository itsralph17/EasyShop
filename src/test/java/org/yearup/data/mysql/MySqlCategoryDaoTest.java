package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
class MySqlCategoryDaoTest extends BaseDaoTestClass {
    private MySqlCategoryDao dao;

    @BeforeEach
    public void setup() {
        dao = new MySqlCategoryDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectCategory() {
        // arrange
        int categoryId = 1;
        Category expected = new Category();
        expected.setCategoryId(1);
        expected.setName("Electronics");
        expected.setDescription("Explore the latest gadgets and electronic devices.");

        // act
        Category actual = dao.getById(categoryId);

        // assert
        assertEquals(expected.getCategoryId(), actual.getCategoryId(), "Category ID should match");
        assertEquals(expected.getName(), actual.getName(), "Name should match");
        assertEquals(expected.getDescription(), actual.getDescription(), "Description should match");
    }

    @Test
    public void Create() {

        // Arrange
        String categoryName = "Cars";
        String categoryDescription = "Fixed";
        // Act
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setDescription(categoryDescription);
        Category createdCategory = dao.create(newCategory);
        // Assert
        assertEquals(categoryName, createdCategory.getName());
        assertEquals(categoryDescription, createdCategory.getDescription());
    }




}

