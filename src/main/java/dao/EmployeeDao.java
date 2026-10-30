package dao;

import entities.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.List;

public class EmployeeDao {

    public void saveEmployee(Employee employee) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(employee);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public List<Employee> getAllEmployeesAsList() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e JOIN FETCH e.department", Employee.class).getResultList();
        }
    }

    public Employee getEmployeeById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    public List<Integer> getAllEmployeeId() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT id FROM Employee", Integer.class).getResultList();
        }
    }

    public void updateEmployee(Employee employee) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(employee);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public void deleteEmployee(Employee employee) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(employee);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
