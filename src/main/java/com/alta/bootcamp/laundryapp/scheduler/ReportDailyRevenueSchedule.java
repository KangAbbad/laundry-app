package com.alta.bootcamp.laundryapp.scheduler;

import com.alta.bootcamp.laundryapp.dto.ResponseDTO;
import com.alta.bootcamp.laundryapp.dto.TodayRevenueDTO;
import com.alta.bootcamp.laundryapp.entities.SummaryRevenue;
import com.alta.bootcamp.laundryapp.repositories.SummaryRevenueRepository;
import com.alta.bootcamp.laundryapp.services.ISummaryRevenueService;
import com.alta.bootcamp.laundryapp.websocket.dto.MessageDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
@EnableAsync
public class ReportDailyRevenueSchedule {
  private static final Logger logger = LoggerFactory.getLogger(ReportDailyRevenueSchedule.class);

  @Autowired
  ISummaryRevenueService summaryRevenueService;

  @Autowired
  SummaryRevenueRepository summaryRevenueRepository;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Async
  // @Scheduled(cron = "*/3 * * * * *") // every 3 seconds - [DEVELOPMENT PURPOSE]
  @Scheduled(cron = "0 0 0 * * *") // every 12:00 AM
  public void scheduleGetTodayRevenue() {
    List<SummaryRevenue> todayRevenueList = new ArrayList<>();

    ResponseDTO<List<TodayRevenueDTO>> todayRevenue = summaryRevenueService.getTodayRevenue();

    if (todayRevenue.getData().size() > 0) {
      for (TodayRevenueDTO revenue : todayRevenue.getData()) {
        MessageDTO message = new MessageDTO();
        message.setAdminId(revenue.getAdminId());
        message.setNotes("");
        message.setTotalRevenue(revenue.getTotalRevenue());

        SummaryRevenue summaryRevenue = convertToEntity(revenue);
        todayRevenueList.add(summaryRevenue);

        logger.info("[SCHEDULE] Save Today Revenue");

        messagingTemplate.convertAndSend("/topic/messages", message);
      }

      summaryRevenueRepository.saveAll(todayRevenueList);
    } else {
      MessageDTO message = new MessageDTO();
      message.setAdminId(null);
      message.setNotes("There is no transaction today");
      message.setTotalRevenue(BigDecimal.valueOf(0));

      logger.warn("[SCHEDULE] There is no transaction today");

      messagingTemplate.convertAndSend("/topic/messages", message);
    }

  }

  private SummaryRevenue convertToEntity(TodayRevenueDTO request) {
    return modelMapper.map(request, SummaryRevenue.class);
  }
}
