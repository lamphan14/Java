package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{
	private static final String createSQL = "INSERT INTO Purchase (Customer_id, purchaseDate, purchaseAmount, Product_id) VALUES (?, ?, ?, ?);";
	private static final String retrieveSQL = "SELECT id, Customer_id, purchaseDate, purchaseAmount, Product_id FROM Purchase WHERE id = ?;";
	private static final String updateSQL = "UPDATE Purchase SET Customer_id = ?, purchaseDate = ?, purchaseAmount = ?, Product_id = ? WHERE id = ?;";
	private static final String deleteSQL = "DELETE FROM Purchase WHERE id = ?;";
	private static final String retrieveCustID = "SELECT id, purchaseDate, purchaseAmount, Product_id FROM Purchase WHERE Customer_id = ?;";
	private static final String retrieveProdID = "SELECT id, Customer_id, purchaseDate, purchaseAmount FROM Purchase WHERE Product_id = ?;";
	private static final String retrievePurchSumm = "SELECT MIN(purchaseAmount), MAX(purchaseAmount), AVG(purchaseAmount) FROM Purchase;";
	
	@Override
	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException {
		if (purchase.getId() != null ) {
			throw new DAOException("Trying to insert Purchase with NON-NULL ID");
		}
		
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, purchase.getCustomerID());
			ps.setDate(2, purchase.getPurchaseDate());
			ps.setDouble(3, purchase.getPurchaseAmount());
			ps.setLong(4, purchase.getProductID());
			
			int rows = ps.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Error: Create did not update the expected number of rows\n");
			}
			
			// Copy the generated auto-increment ID to the Customer's ID
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			long generated_id = rs.getLong(1);
			purchase.setId(generated_id);
			
			return purchase;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("Trying to retrieve Purchase with null ID");
		}
		
		PreparedStatement ps = null;
		
		try {
			 ps = connection.prepareStatement(retrieveSQL);
			 ps.setLong(1, id);
			 ResultSet rs = ps.executeQuery();
			 if (!rs.next()) {
				 return null;
			 }
			 
			 Purchase purchase =  new Purchase();
			 
			 purchase.setId(id);
			 purchase.setCustomerID(rs.getLong("Customer_id"));
			 purchase.setPurchaseDate(rs.getDate("purchaseDate"));
			 purchase.setPurchaseAmount(rs.getDouble("purchaseAmount"));
			 purchase.setProductID(rs.getLong("Product_id"));
			 
			 return purchase;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException {
		if (purchase.getId() == null) {
			throw new DAOException("Trying to update Purchase with null ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setLong(1, purchase.getCustomerID());
			ps.setDate(2, purchase.getPurchaseDate());
			ps.setDouble(3, purchase.getPurchaseAmount());
			ps.setLong(4, purchase.getProductID());
			ps.setLong(5, purchase.getId());

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("Trying to delete Purchase with null ID");
		}
					
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

	@Override
	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(retrieveCustID);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			
			List<Purchase> purchases = new ArrayList<Purchase>();
			
			while (rs.next()) {
				Purchase newPur = new Purchase();
				
				newPur.setId(rs.getLong("id"));
				newPur.setPurchaseDate(rs.getDate("purchaseDate"));
				newPur.setPurchaseAmount(rs.getDouble("purchaseAmount"));
				newPur.setProductID(rs.getLong("Product_id"));
				purchases.add(newPur);
			}
			
			return purchases;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}	
	}

	@Override
	public List<Purchase> retrieveForProductID(Connection connection, Long productID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieveProdID);
			ps.setLong(1, productID);
			ResultSet rs = ps.executeQuery();
			
			List<Purchase> purchases = new ArrayList<Purchase>();
			
			while (rs.next()) {
				Purchase newPur = new Purchase();
				
				newPur.setId(rs.getLong("id"));
				newPur.setCustomerID(rs.getLong("Customer_id"));
				newPur.setPurchaseDate(rs.getDate("purchaseDate"));
				newPur.setPurchaseAmount(rs.getDouble("purchaseAmount"));
				purchases.add(newPur);	
			}
			
			return purchases;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(retrievePurchSumm);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			PurchaseSummary pSummary = new PurchaseSummary();
			
			pSummary.minPurchase = rs.getFloat("MIN(purchaseAmount)");
			pSummary.maxPurchase = rs.getFloat("MAX(purchaseAmount)");
			pSummary.avgPurchase = rs.getFloat("AVG(purchaseAmount)");

			return pSummary;	
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}		
	}
}
