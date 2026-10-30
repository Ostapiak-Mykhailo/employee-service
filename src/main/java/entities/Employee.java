package entities;

import enums.Position;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Position position;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Override
    public String toString() {
        return "Employee{" +
                "id = " + id +
                "; firstName = '" + firstName + '\'' +
                "; lastName = '" + lastName + '\'' +
                "; position = " + position +
                "; department = " + department.getDepartmentType() +
                '}';
    }
}
