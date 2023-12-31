package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{

@Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);

    }

    @Override
    public List<Category> getAllCategories(Integer categoryId, String name, String description)
    {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";



        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);


            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Category category = mapRow(row);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // get all categories
        return categories;
    }

    @Override
    public Category getById(int id)
    {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    Category category = new Category(categoryId, name, description);
                    return category;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // get category by id
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories (category_id, name, description) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    category.setCategoryId(generatedId);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
