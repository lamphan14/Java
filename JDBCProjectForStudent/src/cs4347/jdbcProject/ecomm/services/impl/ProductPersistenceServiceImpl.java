package cs4347.jdbcProject.ecomm.services.impl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.services.ProductPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	private DataSource dataSource;

	public ProductPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

    /**
     * this method throw a DAOException if the
     * given Product has a non-null ID and
     * return the same Product with the ID attribute set to the
     * value set by the application's auto-increment primary key column.
     * @throws DAOException if the given Purchase has a non-null id.
     */

	public Product create(Product product) throws SQLException, DAOException {
        // create new product dao
		ProductDAO dao = new ProductDaoImpl();
        // get connection
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false); // disable auto commit
		Product prod = dao.create(connection, product); // create new product
        // throw exception if null id
		if(prod.getId() == null){
			throw new DAOException("Trying to rcreate Product with NULL Product ID");
		}
		connection.commit(); // commit transaction
		return prod;
	}

	// this method retrieves product
	public Product retrieve(Long id) throws SQLException, DAOException {
		// open connection
		Connection connection = dataSource.getConnection();
		
		// create dao and retrieve data
		ProductDAO dao = new ProductDaoImpl();
		Product product = dao.retrieve(connection, id);

		// close the connection and return the product
		connection.close();
		return product;
	}

	// this method update product data
	public int update(Product product) throws SQLException, DAOException {
        // get connection
		Connection connection = dataSource.getConnection();
        // create a new productdao object
		ProductDAO dao = new ProductDaoImpl();
        // update execution
		int rows = dao.update(connection, product);
		connection.close(); // close connection
		return rows;
	}

	// this method deletes product
	public int delete(Long id) throws SQLException, DAOException {
        // get connection
		Connection connection = dataSource.getConnection();
        // create a new productdao object
		ProductDAO dao = new ProductDaoImpl();
        // delete execution
		int rows = dao.delete(connection, id);
		connection.close(); // close connection
		return rows;
	}

	// this method retrieves product by UPC
	public Product retrieveByUPC(String upc) throws SQLException, DAOException {
        // get connection
		Connection connection = dataSource.getConnection();
        // create a new productdao object
		ProductDAO prodDao = new ProductDaoImpl();
		// retrieve product
		Product product = prodDao.retrieveByUPC(connection, upc);
		connection.close(); // close connection
		return product;
	}

	// this method retrieves products by category
	public List<Product> retrieveByCategory(int category) throws SQLException,
			DAOException {
        // get connection
		Connection connection = dataSource.getConnection();
		ProductDAO prodDao = new ProductDaoImpl();
		// create a list of products and retrieve results
		List<Product> products = prodDao.retrieveByCategory(connection, 7); 
		connection.close(); // close connection
		return products;
	}

}
