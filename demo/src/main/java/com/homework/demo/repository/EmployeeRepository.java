package com.homework.demo.repository;

import com.homework.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    //SELECT * FROM employee WHERE floor_seat_seq IS NULL
    List<Employee> findByFloorSeatSeqIsNullOrderByEmpIdAsc();

    @Procedure(procedureName = "update_employee_seat")
    void updateEmployeeSeat(
        @Param("p_emp_id") String empId, 
        @Param("p_floor_seat_seq") Integer floorSeatSeq
    );
}