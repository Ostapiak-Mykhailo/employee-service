package dao;

import entities.Department;
import entities.Employee;
import enums.DepartmentType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.List;

public class DepartmentDao {

    public void saveDepartment(Department department) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(department);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public Department getDepartmentById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.find(Department.class, id);
        }
    }

    public List<Department> getAllDepartmentsAsList() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Department", Department.class).getResultList();
        }
    }

    public void deleteDepartment(Department department) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(department);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public List<Employee> getDepartmentEmployeeListById(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e WHERE e.department.id = :id", Employee.class)
                    .setParameter("id", id).getResultList();
        }
    }

    public List<Integer> getAllDepartmentId() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT id FROM Department", Integer.class).getResultList();
        }
    }

    public List<DepartmentType> getAllDepartmentTypes() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d.departmentType FROM Department d", DepartmentType.class).getResultList();
        }
    }
}
