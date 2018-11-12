/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package cs4347.hibernateProject.ecomm.services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.util.DAOException;

/** 
 */
public interface CustomerPersistenceService
{
	void create(Customer customer) throws SQLException, DAOException;
	Customer retrieve(Long id) throws SQLException, DAOException;
	void update(Customer customer) throws SQLException, DAOException;
	void delete(Long id) throws SQLException, DAOException;
	
	/**
	 * Retrieve all Customers whose address is in the given zipcode.
	 */
	List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException;
	
	/**
	 * Retrieve all Customer whose DOB is in the given date range.
	 */
	List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException;
}