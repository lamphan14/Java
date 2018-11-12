package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;
// Implementation of ProductDAO
public class ProductDaoImpl implements ProductDAO
{   // sql insert statement
	private static final String insertSQL = 
			"INSERT INTO Product (prodName, prodDescription, prodCategory, prodUPC) VALUES (?, ?, ?, ?);";

	// create a new product
	public Product create(Connection connection, Product product)
			throws SQLException, DAOException {
        // throw exception if invalid id
		if (product.getId() != null) {
			throw new DAOException("Trying to insert Customer with NON-NULL ID");
		}
        //
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, product.getProdName());
			ps.setString(2, product.getProdDescription());
			ps.setInt(3, product.getProdCategory());
			ps.setString(4, product.getProdUPC());	// change the length in ProductDaoTest from 12 to 11
//			ps.setString(4, "01234567890");	// 11 chars work, more does not
			
			ps.executeUpdate();

			// REQUIREMENT: Copy the auto-increment primary key to the customer ID.
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			product.setId((long) lastKey);
			
			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// this method retrieves product with provided id
	public Product retrieve(Connection connection, Long id)
			throws SQLException, DAOException {
        // sql retrieve query
		final String storeQuery = 
				"SELECT id, prodDescription, prodName, prodCategory, prodUPC "
				+ "FROM product WHERE id = ?";
        // create preparedstatement object
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(storeQuery);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			Product product = new Product();
			product.setId(id);	
			product.setProdDescription(rs.getString("prodDescription"));
			product.setProdName(rs.getString("prodName"));
			product.setProdCategory(rs.getInt("prodCategory"));
			product.setProdUPC(rs.getString("prodUPC"));
			
			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// this method updates product
	public int update(Connection connection, Product product)
			throws SQLException, DAOException {
        // sql update statement
		final String updateSQL = "UPDATE product SET prodDescription = ?, prodName = ?, prodCategory = ?, "
				+ "prodUPC = ? WHERE id = ?;";
		// throw exception if null id
		if (product.getId() == null) {
			throw new DAOException("Trying to update Product with NULL ID");
		}

        // create a preparedstatement object
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, product.getProdDescription());
			ps.setString(2, product.getProdName());
			ps.setInt(3, product.getProdCategory());
			ps.setString(4, product.getProdUPC());
			ps.setLong(5, product.getId());
			
			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	// this method deletes product with provided id
	public int delete(Connection connection, Long id) throws SQLException,
			DAOException {
        // throw exception if null id
		if (id == null) {
			throw new DAOException("Trying to delete Product with NULL ID");
		}
        // sql delete statement
		final String deleteSQL = "DELETE FROM product WHERE ID = ?;";
        // create a preparedstatement
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, id);

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		
	}

	// this method retrieves product by category
	public List<Product> retrieveByCategory(Connection connection, int category)
			throws SQLException, DAOException {
        // sql select statement
		final String storeQuery = 
				"SELECT id, prodDescription, prodName, prodCategory, prodUPC "
				+ "FROM product WHERE prodCategory = ?";
        // create a preparedstatement object
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(storeQuery);
			ps.setInt(1, category);
			ResultSet rs = ps.executeQuery();
            //create a list of product to return
			List<Product> result = new ArrayList<Product>();
			while (rs.next()) {
                // create a new product entity
				Product product = new Product();
				product.setId(rs.getLong("id"));	
				product.setProdDescription(rs.getString("prodDescription"));
				product.setProdName(rs.getString("prodName"));
				product.setProdCategory(category);
				product.setProdUPC(rs.getString("prodUPC"));
				result.add(product);
			}
			return result;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		
	}

	// this method retrieve product with UPC
	public Product retrieveByUPC(Connection connection, String upc)
			throws SQLException, DAOException {
        // sql select statement
		final String storeQuery = 
				"SELECT id, prodDescription, prodName, prodCategory, prodUPC "
				+ "FROM product WHERE prodUPC = ?";
        // create a preparestatement object
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(storeQuery);
			ps.setString(1, upc);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			// create a new product entity
			Product product = new Product();
			product.setId(rs.getLong("id"));	
			product.setProdDescription(rs.getString("prodDescription"));
			product.setProdName(rs.getString("prodName"));
			product.setProdCategory(rs.getInt("prodCategory"));
			product.setProdUPC(upc);
			
			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

}
