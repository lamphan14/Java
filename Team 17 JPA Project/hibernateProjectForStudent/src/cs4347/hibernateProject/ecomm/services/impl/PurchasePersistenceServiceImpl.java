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

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch(Exception ex) {
			em.getTransaction().rollback();
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase purchase = (Purchase)em.createQuery("from Purchase as p where p.id = :id")
					.setParameter("id", id)
					.getSingleResult();
			em.getTransaction().commit();
			em.close();
			return purchase;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase p2 = em.find(Purchase.class, purchase.getId());
			p2.setPurchaseDate(purchase.getPurchaseDate());
			p2.setPurchaseAmount(purchase.getPurchaseAmount());
			p2.setCustomer(purchase.getCustomer());
			p2.setProduct(purchase.getProduct());
			em.getTransaction().commit();
			em.close();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Purchase purchase = (Purchase)em.createQuery("from Purchase as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		
		if(purchase == null) {
			em.getTransaction().rollback();
			throw new DAOException("Purchase ID does not exist: " + id);
		}
		
		em.remove(purchase);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
        List<Purchase> purchases =  em.createQuery("select from Purchase as p where p.customer.id = :customerID", Purchase.class)
                .setParameter("customerID", customerID)
                .getResultList();
        em.getTransaction().commit();
        em.close();
        
        if(purchases == null) {
        	em.getTransaction().rollback();
        	em.close();
            throw new DAOException("Customer ID does not exist: " + customerID);
        }
          
        return purchases;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		PurchaseSummary summary = new PurchaseSummary();
		double min;
		double max;
		double avg;
		
		em.getTransaction().begin();
        Purchase purchase  = (Purchase)em.createQuery("SELECT p FROM Purchase p WHERE p.purchaseAmount=(SELECT MIN(p.purchaseAmount) FROM p WHERE p.customer.id = :customerID)")
                .setParameter("customerID", customerID)
                .getSingleResult();
        
        if(purchase == null) {
        	em.getTransaction().rollback();
        	em.close();
        	throw new DAOException("Customer ID does not exist: " + customerID);
        }
      
        min = purchase.getPurchaseAmount();
        
        purchase  = (Purchase)em.createQuery("SELECT p FROM Purchase p WHERE p.purchaseAmount=(SELECT MAX(p.purchaseAmount) FROM p WHERE p.customer.id = :customerID)")
                .setParameter("customerID", customerID)
                .getSingleResult();
        max = purchase.getPurchaseAmount();
        
        avg = (Double)em.createQuery("select avg(p.purchaseAmount) from Purchase p where p.customer.id = :customerID)")
        		.setParameter("customerID", customerID)
        		.getSingleResult();
        
        summary.minPurchase = min;
        summary.maxPurchase = max;
        summary.avgPurchase = avg;
        em.close();
        return summary;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
        List<Purchase> purchases = em.createQuery("select from Purchase as p where p.product.id = :productID", Purchase.class)
                .setParameter("productID", productID)
                .getResultList();
        em.getTransaction().commit();
        em.close();
        
        if(purchases == null) {
        	em.getTransaction().rollback();
        	em.close();
            throw new DAOException("Product ID does not exist: " + productID);
        }
          
        return purchases;
	}
}
