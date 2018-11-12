package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO {
	private static final String create_sql = "INSERT INTO Customer (firstName, lastName, gender, dob, email) VALUES (?, ?, ?, ?, ?);";
	private static final String retrieve_sql = "SELECT id, firstName, lastName, gender, dob, email FROM Customer WHERE id = ?;";
	private static final String update_sql = "UPDATE Customer SET firstName = ?, lastName = ?, gender = ?, dob = ?, email = ? WHERE id = ?;";
	private static final String delete_sql = "DELETE FROM Customer WHERE id = ?;";
	private static final String retrieve_zip_sql = "SELECT id, firstName, lastName, gender, dob, email "
			+ "FROM Customer JOIN Address ON id = Customer_id WHERE zipcode = ?";
	private static final String retrieve_dob_sql = "SELECT id, firstName, lastName, gender, dob, email FROM Customer "
			+ "WHERE dob BETWEEN ? AND ?";

	@Override
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException {
		// Customer's ID must be null before being inserted into the table
		if (customer.getId() != null) {
			throw new DAOException("Error: Trying to insert a Customer with a non-null ID\n");
		}

		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(create_sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, String.valueOf(customer.getGender()));
			ps.setDate(4, customer.getDob());
			ps.setString(5, customer.getEmail());

			int rows = ps.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Error: Create did not update the expected number of rows\n");
			}

			// Copy the generated auto-increment ID to the Customer's ID
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			long generated_id = rs.getLong(1);
			customer.setId(generated_id);

			return customer;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		// Customer's ID must be non-null to be selected from the table
		if (id == null) {
			throw new DAOException("Error: Trying to retrieve a Customer with a null ID\n");
		}
		
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieve_sql);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Customer new_customer = new Customer();
			new_customer.setId(rs.getLong("id"));
			new_customer.setFirstName(rs.getString("firstName"));
			new_customer.setLastName(rs.getString("lastName"));
			new_customer.setGender(rs.getString("gender").charAt(0));
			new_customer.setDob(rs.getDate("dob"));
			new_customer.setEmail(rs.getString("email"));

			return new_customer;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {
		// Customer's ID must be non-null to be updated in the table
		if (customer.getId() == null) {
			throw new DAOException("Error: Trying to update a Customer with a null ID\n");
		}

		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(update_sql);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, String.valueOf(customer.getGender()));
			ps.setDate(4, customer.getDob());
			ps.setString(5, customer.getEmail());
			ps.setLong(6, customer.getId());

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
		// Customer's ID must be non-null to be deleted from the table
		if (id == null) {
			throw new DAOException("Error: Trying to delete a Customer with a null ID\n");
		}

		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(delete_sql);
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
	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieve_zip_sql);
			ps.setString(1, zipCode);
			ResultSet rs = ps.executeQuery();

			List<Customer> result = new ArrayList<Customer>();
			while (rs.next()) {
				Customer current_customer = new Customer();
				current_customer.setId(rs.getLong("id"));
				current_customer.setFirstName(rs.getString("firstName"));
				current_customer.setLastName(rs.getString("lastName"));
				current_customer.setGender(rs.getString("gender").charAt(0));
				current_customer.setDob(rs.getDate("dob"));
				current_customer.setEmail(rs.getString("email"));
				result.add(current_customer);
			}
			
			return result;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate) throws SQLException, DAOException {
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(retrieve_dob_sql);
			ps.setDate(1, startDate);
			ps.setDate(2, endDate);
			ResultSet rs = ps.executeQuery();

			List<Customer> result = new ArrayList<Customer>();
			while (rs.next()) {
				Customer current_customer = new Customer();
				current_customer.setId(rs.getLong("id"));
				current_customer.setFirstName(rs.getString("firstName"));
				current_customer.setLastName(rs.getString("lastName"));
				current_customer.setGender(rs.getString("gender").charAt(0));
				current_customer.setDob(rs.getDate("dob"));
				current_customer.setEmail(rs.getString("email"));
				result.add(current_customer);
			}
			
			return result;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}	
}