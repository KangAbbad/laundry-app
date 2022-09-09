package com.alta.bootcamp.laundryapp.scheduler;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TodayRevenueDTO;
import com.alta.bootcamp.laundryapp.entities.Admin;
import com.alta.bootcamp.laundryapp.repositories.AdminRepository;
import com.alta.bootcamp.laundryapp.services.ITransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleConfig {
  private static Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

  @Autowired
  AdminRepository adminRepository;

  @Autowired
  ITransactionService transactionService;

  @Async
  // @Scheduled(cron = "*/3 * * * * *") // every 3 seconds
  @Scheduled(cron = "0 0 0 * * *") // every 12:00 AM
  public void scheduleGetTodayRevenue() {
    ResponseDTO<List<TodayRevenueDTO>> response = transactionService.getTodayRevenue();
    if (response.getData().size() > 0) {
      for (TodayRevenueDTO revenue : response.getData()) {
        Optional<Admin> admin = adminRepository.findById(revenue.getAdminId());

        if (admin.isPresent()) {
          logger.info("========== Daily Revenue ==========");
          String adminMsg = "Admin ID: " + revenue.getAdminId() + " (" + admin.get().getUsername() + ")";
          logger.info(adminMsg);
          String totalRevenueMsg = "Total Revenue: " + revenue.getTodayRevenue();
          logger.info(totalRevenueMsg);
        } else {
          logger.error("[CRON] Transaction Invalid - Admin ID not found");
        }
      }
    }
  }
}
