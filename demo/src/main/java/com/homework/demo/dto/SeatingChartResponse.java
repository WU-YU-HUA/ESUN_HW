package com.homework.demo.dto;
import lombok.Data;

@Data
public class SeatingChartResponse {
    private Integer floorSeatSeq;
    private Integer floorNo;
    private Integer seatNo;
    private Boolean available;
    private String empId;
}
