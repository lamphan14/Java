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

import java.sql.SQLException;
import java.util.List;

import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.util.DAOException;

/** 
 */
public interface ProductPersistenceService
{
	void create(Product product) throws SQLException, DAOException;
	Product retrieve(Long id) throws SQLException, DAOException;
	void update(Product product) throws SQLException, DAOException;
	void delete(Long id) throws SQLException, DAOException;
	
	/**
	 * Retrieve a product by its unique UPC
	 */
	Product retrieveByUPC(String upc) throws SQLException, DAOException;

	/**
	 * Retrive products in the given category
	 */
	List<Product> retrieveByCategory(int category) throws SQLException, DAOException;
	
}