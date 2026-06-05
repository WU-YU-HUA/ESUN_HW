package com.homework.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  //幫忙建立空的Obj
@AllArgsConstructor //方便Schema變動
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "emp_id", length = 5)
    private String empId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "floor_seat_seq", referencedColumnName = "floor_seat_seq")
    private SeatingChart floorSeatSeq;

}
