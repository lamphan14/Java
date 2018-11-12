package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CreditCardDaoImpl implements CreditCardDAO {
	private static final String create_sql = "INSERT INTO CREDIT_CARD (name, ccNumber, expDate, securityCode, Customer_id) VALUES (?, ?, ?, ?, ?);";
	private static final String retrieve_sql = "SELECT name, ccNumber, expDate, securityCode, Customer_id FROM CREDIT_CARD WHERE Customer_id = ?;";
	private static final String delete_sql = "DELETE FROM CREDIT_CARD WHERE Customer_id = ?;";
	
	@Override
	public CreditCard create(Connection connection, CreditCard creditCard, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(create_sql);
			ps.setString(1, creditCard.getName());
			ps.setString(2, creditCard.getCcNumber());
			ps.setString(3, creditCard.getExpDate());
			ps.setString(4, creditCard.getSecurityCode());
			ps.setLong(5, customerID);

			int rows = ps.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Error: Create did not update the expected number of rows\n");
			}

			return creditCard;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieve_sql);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			CreditCard new_cc = new CreditCard();
			new_cc.setName(rs.getString("name"));
			new_cc.setCcNumber(rs.getString("ccNumber"));
			new_cc.setExpDate(rs.getString("expDate"));
			new_cc.setSecurityCode(rs.getString("securityCode"));
			
			return new_cc;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}

	}

	@Override
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(delete_sql);
			ps.setLong(1, customerID);

			int rows = ps.executeUpdate();
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
}