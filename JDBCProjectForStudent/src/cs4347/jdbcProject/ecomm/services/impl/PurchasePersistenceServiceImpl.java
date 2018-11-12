package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService {
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Purchase create(Purchase purchase) throws SQLException, DAOException {
		if (purchase.getId() != null) {
			throw new DAOException("Trying to create Purchase with NON-NULL ID");
		}
		
		PurchaseDAO dao = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false); 
		
			Purchase pur = dao.create(connection, purchase);
			
			connection.commit();
			return pur;
		}
		catch (Exception ex) {
			connection.rollback(); 
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
				
		PurchaseDAO dao = new PurchaseDaoImpl();
				
		Purchase purchase = dao.retrieve(connection, id);

		connection.close();
		return purchase;
	}

	@Override
	public int update(Purchase purchase) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
		
		PurchaseDAO dao = new PurchaseDaoImpl();
		
		try {
			connection.setAutoCommit(false);
			
			int rows = dao.update(connection, purchase);

			connection.commit();
			return rows;
		}
		catch (Exception ex) {
			connection.rollback(); 
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
		
		PurchaseDAO dao = new PurchaseDaoImpl();
		try {
			connection.setAutoCommit(false);
			
			int rows = dao.delete(connection, id);
			
			connection.commit();
			return rows;
		}
		catch (Exception ex) {
			connection.rollback(); 
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
		PurchaseDAO purDao = new PurchaseDaoImpl();
		
		List<Purchase> purchases = purDao.retrieveForCustomerID(connection, customerID); 
		connection.close();
		
		return purchases;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
		PurchaseDAO purDao = new PurchaseDaoImpl();
		
		PurchaseSummary purSum = purDao.retrievePurchaseSummary(connection, customerID);
		connection.close();
		
		return purSum;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException {
		Connection connection = dataSource.getConnection();
		PurchaseDAO purDao = new PurchaseDaoImpl();

		List<Purchase> purchases = purDao.retrieveForProductID(connection, productID);
		connection.close();
		
		return purchases;
	}
}
