package com.alta.bootcamp.laundryapp.repositories;

import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRevenueRepository extends JpaRepository<SummaryRevenue, Long> {
  @Procedure("today_revenue")
  List<Object[]> getTodayRevenue();
}
