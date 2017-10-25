package cs4310.fulfillment.program.Model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author diana
 */
public class EmployeeUtility {
    public static void addUser(Integer employee_id, String first_name, String last_name, String emp_username, String emp_password, String emp_role) {
        Employee employee = new Employee(employee_id, first_name, last_name, emp_username, emp_password, emp_role);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CS4310_Fulfillment_ProgramPU");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
    }
}
