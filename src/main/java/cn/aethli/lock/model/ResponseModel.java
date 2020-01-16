package cn.aethli.lock.model;

import cn.aethli.lock.common.emnus.ResponseStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModel {

  private LocalDate date = LocalDate.now();
  private ResponseStatus status;
  private String msg;
  private Object data;

  public ResponseModel(ResponseStatus status) {
    this.status = status;
  }

  public ResponseModel(ResponseStatus status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public ResponseModel(ResponseStatus status, Object data) {
    this.status = status;
    this.data = data;
  }
}
