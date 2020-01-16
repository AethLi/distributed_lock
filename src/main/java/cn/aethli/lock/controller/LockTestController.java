package cn.aethli.lock.controller;

import cn.aethli.lock.annotation.DistributedLock;
import cn.aethli.lock.common.emnus.ResponseStatus;
import cn.aethli.lock.model.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** @author Termite */
@RestControllerAdvice
@RequestMapping("lock/test")
public class LockTestController {

  @GetMapping
  @DistributedLock(key = "lock_test", expire = 18000, waitTime = 2000)
  public Object lockTest()  {
    return new ResponseModel(ResponseStatus.OK);
  }
}
