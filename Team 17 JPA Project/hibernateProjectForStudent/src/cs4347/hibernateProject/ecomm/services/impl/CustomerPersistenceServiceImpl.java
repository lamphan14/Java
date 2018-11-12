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

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	/**
	 */
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	 {
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        }
        catch (Exception exception) {
            em.getTransaction().rollback();
            throw exception;
        }
    }

	@Override
	public Customer retrieve(Long id) throws SQLException, DAOException
	{
        em.getTransaction().begin();
        Customer item = (Customer)em.createQuery("from Customer set to c where c.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        em.getTransaction().commit();
         
        if(item == null) {
            throw new DAOException("Customer ID does not exist: " + id);
        }
         
        return item;
    }

	@Override
	public void update(Customer c1) throws SQLException, DAOException
	 {
        try {
            em.getTransaction().begin();
            Customer cust = em.find(Customer.class, c1.getId());
            cust.setFirstName(c1.getFirstName());
            cust.setLastName(c1.getLastName());
            cust.setGender(c1.getGender());
            cust.setDob(c1.getDob());
            cust.setEmail(c1.getEmail());
            cust.setAddress(c1.getAddress());
            cust.setCreditCard(c1.getCreditCard());
            em.getTransaction().commit();
        }
        catch (Exception exception) {
            em.getTransaction().rollback();
            throw exception;
        }
    }

	@Override
	public void delete(Long id) throws SQLException, DAOException
	 {
        em.getTransaction().begin();
        Customer cust = (Customer)em.createQuery("from Customer set to c where c.id = :id")
                .setParameter("id", id)
                .getSingleResult();
         
        if(cust == null) {
            em.getTransaction().rollback();
            throw new DAOException("Product ID does not exist: " + id);
        }
        em.remove(cust);
        em.getTransaction().commit();
    }

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	 {
        em.getTransaction().begin();
        List<Customer> cust =  (List<Customer>)em.createQuery("from Customer set to c where c.zipCode = :Zipcode")
                .setParameter("Zipcode", zipCode)
                .getResultList();
        em.getTransaction().commit();
         
        if(cust == null) {
            throw new DAOException("Cusomer Zipcode does not exist: " + zipCode);
        }
         
        return cust;
    }

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	 {
        em.getTransaction().begin();
        List<Customer> cust =  (List<Customer>)em.createQuery("from Customer set to c where c.DOB = :Date of Birth")
                .setParameter("Start Date", startDate)
                .setParameter("End Date", endDate)
                .getResultList();
        em.getTransaction().commit();
         
        if(cust == null) {
            throw new DAOException("No customers with DOB between " + startDate + " and " + endDate);
        }
         
        return cust;
    }
}
