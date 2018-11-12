package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class AddressDaoImpl implements AddressDAO
{
	private static final String create_sql = "INSERT INTO Address (address1, address2, city, state, zipcode, Customer_id) VALUES (?, ?, ?, ?, ?, ?);";
	private static final String retrieve_sql = "SELECT address1, address2, city, state, zipcode FROM Address WHERE Customer_id = ?;";
	private static final String delete_sql = "DELETE FROM Address WHERE Customer_id = ?;";
	
	@Override
	public Address create(Connection connection, Address address, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(create_sql);
			ps.setString(1, address.getAddress1());
			ps.setString(2, address.getAddress2());
			ps.setString(3, address.getCity());
			ps.setString(4, address.getState());
			ps.setString(5, address.getZipcode());
			ps.setLong(6, customerID);

			int rows = ps.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Error: Create did not update the expected number of rows\n");
			}

			return address;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Address retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieve_sql);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Address new_address = new Address();
			new_address.setAddress1(rs.getString("address1"));
			new_address.setAddress2(rs.getString("address2"));
			new_address.setCity(rs.getString("city"));
			new_address.setState(rs.getString("state"));
			new_address.setZipcode(rs.getString("zipcode"));
			
			return new_address;
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
