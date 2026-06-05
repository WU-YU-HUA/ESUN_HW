package com.homework.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  //幫忙建立空的Obj
@AllArgsConstructor //方便Schema變動
@Entity
@Table(name = "seatingchart")
public class SeatingChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 SERIAL 自動遞增
    @Column(name = "floor_seat_seq")
    private Integer floorSeatSeq;

    @Column(name = "floor_no", nullable = false)
    private Integer floorNo;

    @Column(name = "seat_no", nullable = false)
    private Integer seatNo;

    @OneToOne(mappedBy = "floorSeatSeq") //方便找到空位
    private Employee employee;
}
