package com.homework.demo.repository;
import com.homework.demo.entity.SeatingChart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SeatingChartRepository extends JpaRepository<SeatingChart, Integer> {
    
}
