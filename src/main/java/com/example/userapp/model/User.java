package com.example.userapp.model;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @CsvBindByName
    private @Id String name;

    @CsvBindByName
    private float salary;

    public User() {}

    User(String name, float salary) {

        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return this.name;
    }

    public float getSalary() {
        return this.salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User employee = (User) o;
        return Objects.equals(this.name, employee.name)
                && Objects.equals(this.salary, employee.salary);
    }


    @Override
    public String toString() {
        return "User{ name='" + this.name + '\'' + ", role='" + this.salary + '\'' + '}';
    }
}
