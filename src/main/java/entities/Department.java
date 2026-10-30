package entities;

import enums.DepartmentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;
    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<Employee> employees = new ArrayList<>();

    @Override
    public String toString() {
        return "Department{" +
                "id = " + id +
                "; departmentType = " + departmentType +
                '}';
    }
}
