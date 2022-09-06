package com.alta.bootcamp.laundryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWithMetaDTO<T> implements Serializable {
  private T data;
  private Meta meta;
  private int status;
  private String message;

  @Data
  public static class Meta {
    int page;
    @JsonProperty("per_page")
    int perPage;
    @JsonProperty("total_page")
    int totalPage;
    @JsonProperty("total_data")
    long totalData;
  }
}
