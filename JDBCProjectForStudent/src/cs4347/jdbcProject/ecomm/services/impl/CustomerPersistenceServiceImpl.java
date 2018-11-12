package cs4347.jdbcProject.ecomm.services.impl;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.services.CustomerPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	private static final String update_addr = "UPDATE Address SET address1 = ?, address2 = ?, city = ?, state = ?, zipcode = ? WHERE Customer_id = ?;";
	private static final String update_cc = "UPDATE CREDIT_CARD SET name = ?, ccNumber = ?, expDate = ?, securityCode = ? WHERE Customer_id = ?;";
	
	private DataSource dataSource;

	public CustomerPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	/**
	 * This method provided as an example of transaction support across multiple inserts.
	 * 
	 * Persists a new Customer instance by inserting new Customer, Address, 
	 * and CreditCard instances. Notice the transactional nature of this 
	 * method which includes turning off autocommit at the start of the 
	 * process, and rolling back the transaction if an exception 
	 * is caught. 
	 */
	
	//Method creates customer data
	@Override
	public Customer create(Customer customer) throws SQLException, DAOException
	{
		// Customer's ID must be null before being inserted into the table
		if (customer.getId() != null) {
			throw new DAOException("Error: Trying to insert a Customer with a non-null ID\n");
		}

		// create new customerDao, addressDao & creditCardDao
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		
		// get connection
		Connection connection = dataSource.getConnection();		
		
		try {
			connection.setAutoCommit(false);
		
			Customer cust = customerDAO.create(connection, customer); // create new customer
			Long custID = cust.getId();
			
			if (cust.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			Address address = cust.getAddress();
			addressDAO.create(connection, address, custID);
			
			if (cust.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			CreditCard creditCard = cust.getCreditCard();
			creditCardDAO.create(connection, creditCard, custID);
			
			connection.commit(); // commit transaction
			return cust;
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
	
	//Method retrieves customer data
	@Override
	public Customer retrieve(Long id) throws SQLException, DAOException 
	{
		Connection connection = dataSource.getConnection();
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Customer cust = customerDAO.retrieve(connection,id);
        
		AddressDAO addressDAO = new AddressDaoImpl();
        Address address = addressDAO.retrieveForCustomerID(connection, id);

        CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
        CreditCard cc = creditCardDAO.retrieveForCustomerID(connection, id);
        cust.setAddress(address);
        cust.setCreditCard(cc);
        connection.close();
        return cust;

	}
	
	//Method updates customer data
	@Override
	public int update(Customer customer) throws SQLException, DAOException 
	{
		// get connection
		Connection connection = dataSource.getConnection();
		// create a new customerdao object
		CustomerDAO dao = new CustomerDaoImpl();
		
		PreparedStatement ps = null;
		
		try {
			connection.setAutoCommit(false);
			
			// update execution
			int rows = dao.update(connection, customer);
			
			Address address = customer.getAddress();
			ps = connection.prepareStatement(update_addr);
			ps.setString(1, address.getAddress1());
			ps.setString(2, address.getAddress2());
			ps.setString(3, address.getCity());
			ps.setString(4,  address.getState());
			ps.setString(5, address.getZipcode());
			ps.setLong(6,  customer.getId());
			ps.executeUpdate();
			
			CreditCard cc = customer.getCreditCard();
			ps = connection.prepareStatement(update_cc);
			ps.setString(1, cc.getName());
			ps.setString(2, cc.getCcNumber());
			ps.setString(3, cc.getExpDate());
			ps.setString(4,  cc.getSecurityCode());
			ps.setLong(5, customer.getId());
			ps.executeUpdate();
			
			connection.commit();
			//connection.close(); // close connection
			
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
	
	//Method deletes customer data
	@Override
	public int delete(Long id) throws SQLException, DAOException 
	{
		Connection connection = dataSource.getConnection();
        CustomerDAO customerDAO = new CustomerDaoImpl();
        
        try {
        	// delete execution
        	int rows = customerDAO.delete(connection, id);
        	
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

	//Method retrieves customers by zipcode
	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException 
	{
		// get connection
		Connection connection = dataSource.getConnection();		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		List<Customer> customersByZipCodeList = customerDAO.retrieveByZipCode(connection, zipCode);
		
		AddressDAO addressDAO = new AddressDaoImpl();
        CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		for (Customer customer: customersByZipCodeList) {
			long id = customer.getId();
			
			Address address = addressDAO.retrieveForCustomerID(connection, id);
			customer.setAddress(address);
        
			CreditCard cc = creditCardDAO.retrieveForCustomerID(connection, id);
			customer.setCreditCard(cc);
		}
        
		connection.close();
		return customersByZipCodeList;
		
	}
	
	//Method retrieves customers by dateofbirth(DOB)
	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException 
	{
		// get connection
		Connection connection = dataSource.getConnection();		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		List<Customer> customersByDOBList = customerDAO.retrieveByDOB(connection, startDate, endDate);
		AddressDAO addressDAO = new AddressDaoImpl();
        CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		
		for (Customer customer: customersByDOBList) {
			long id = customer.getId();
			
			Address address = addressDAO.retrieveForCustomerID(connection, id);
			customer.setAddress(address);
        
			CreditCard cc = creditCardDAO.retrieveForCustomerID(connection, id);
			customer.setCreditCard(cc);
		}
		
		connection.close();
		return customersByDOBList;
	}
}
