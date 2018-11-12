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
 
package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public ProductPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	//create the product by passing in a product
	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(product);
			em.getTransaction().commit();
		}
		catch (Exception exception) {
			em.getTransaction().rollback();
			throw exception;
		}
	}

	//get a product based on its id number
	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product item = (Product)em.createQuery("from Product as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		em.getTransaction().commit();
		
		if(item == null) {
			throw new DAOException("Product ID does not exist: " + id);
		}
		
		return item;
	}

	//update the attributes in the product
	@Override
	public void update(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product item = em.find(Product.class, product.getId());
			item.setProdName(product.getProdName());
			item.setProdDescription(product.getProdDescription());
			item.setProdCategory(product.getProdCategory());
			item.setProdUPC(product.getProdUPC());
			em.getTransaction().commit();
		}
		catch (Exception exception) {
			em.getTransaction().rollback();
			throw exception;
		}
	}

	//delete a product based on the id number
	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product item = (Product)em.createQuery("from Product as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		
		if(item == null) {
			em.getTransaction().rollback();
			throw new DAOException("Product ID does not exist: " + id);
		}
		///unsure if getTransaction is required after em.remove()
		//if errors happening, alternative deletes available at http://www.baeldung.com/delete-with-hibernate
		em.remove(item);
		em.getTransaction().commit();
	}

	//get a product by its UPC
	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product item = (Product)em.createQuery("from Product as p where p.upc = :upc")
				.setParameter("upc", upc)
				.getSingleResult();
		em.getTransaction().commit();
		
		if(item == null) {
			throw new DAOException("Product UPC does not exist: " + upc);
		}
		
		return item;
	}

	//Get a list of all o f the items in the list.
	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Product> item =  (List<Product>)em.createQuery("from Product as p where p.category = :category")
				.setParameter("category", category)
				.getResultList();
		em.getTransaction().commit();
		
		if(item == null) {
			throw new DAOException("Product category does not exist: " + category);
		}
		
		return item;
	}

}
