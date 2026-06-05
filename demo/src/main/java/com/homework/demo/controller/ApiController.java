package com.homework.demo.controller;

import com.homework.demo.dto.EmployeeResponse;
import com.homework.demo.dto.SeatingChartResponse;
import com.homework.demo.entity.Employee;
import com.homework.demo.entity.SeatingChart;
import com.homework.demo.repository.EmployeeRepository;
import com.homework.demo.repository.SeatingChartRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允許 Vue.js 前端跨網域呼叫
public class ApiController {

    private final SeatingChartRepository seatingChartRepo;
    private final EmployeeRepository employeeRepo;

    // 建構子注入 (Constructor Injection)
    public ApiController(SeatingChartRepository seatingChartRepo, EmployeeRepository employeeRepo) {
        this.seatingChartRepo = seatingChartRepo;
        this.employeeRepo = employeeRepo;
    }

    // 取得所有座位資料
    @GetMapping("/seats")
    public Map<String, List<SeatingChartResponse>> getSeatingChart() {
        //Select *
        List<SeatingChart> seats = seatingChartRepo.findAll();
        
        List<SeatingChartResponse> dtoList = seats.stream().map(seat -> {
            SeatingChartResponse res = new SeatingChartResponse();
            res.setFloorSeatSeq(seat.getFloorSeatSeq());
            res.setFloorNo(seat.getFloorNo());
            res.setSeatNo(seat.getSeatNo());
            
            // 給座位狀態
            if (seat.getEmployee() == null) {
                res.setAvailable(true);
                res.setEmpId(null);
            } else {
                res.setAvailable(false);
                res.setEmpId(seat.getEmployee().getEmpId()); // GET員工編號
            }
            return res;
        }).collect(Collectors.toList());

        Map<String, List<SeatingChartResponse>> response = new HashMap<>();
        response.put("SeatChart", dtoList);
        return response;
    }

    // 查詢員工基本資料
    @GetMapping("/user/{id}")
    public EmployeeResponse getUser(@PathVariable("id") String id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null; 
        }
        
        EmployeeResponse res = new EmployeeResponse();
        res.setEmpId(employee.getEmpId());
        res.setName(employee.getName());
        res.setEmail(employee.getEmail());
        return res;
    }

    // 列出沒位置的員工
    @GetMapping("/user_no_seat")
    public List<EmployeeResponse> getNoSeatEmployees() {
        List<Employee> noSeatEmployees = employeeRepo.findByFloorSeatSeqIsNullOrderByEmpIdAsc();
        
        return noSeatEmployees.stream().map(emp -> {
            EmployeeResponse res = new EmployeeResponse();
            res.setEmpId(emp.getEmpId());
            res.setName(emp.getName());
            res.setEmail(emp.getEmail());
            return res;
        }).collect(Collectors.toList());
    }

    // 更新員工資料(先只更新座位)
    @PutMapping("/user/{id}")
    @Transactional // Transaction
    public String updateUser(
            @PathVariable("id") String id, 
            @RequestBody Map<String, Integer> body) {
        
        // payload: { "floorSeatSeq": 3 }
        Integer floorSeatSeq = body.getOrDefault("floorSeatSeq", null);

        // 呼叫 PostgreSQL 的預存程序
        employeeRepo.updateEmployeeSeat(id, floorSeatSeq);
        return "員工資料更新成功！";
    }
}