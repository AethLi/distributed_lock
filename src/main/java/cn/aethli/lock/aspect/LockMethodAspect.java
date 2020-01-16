package cn.aethli.lock.aspect;

import cn.aethli.lock.annotation.DistributedLock;
import cn.aethli.lock.common.emnus.ResponseStatus;
import cn.aethli.lock.model.ResponseModel;
import cn.aethli.lock.utils.LockUtil;
import java.lang.reflect.Method;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LockMethodAspect {

  private final LockUtil lockUtil;

  @Autowired
  public LockMethodAspect(LockUtil lockUtil) {
    this.lockUtil = lockUtil;
  }

  @Around("@annotation(cn.aethli.lock.annotation.DistributedLock)")
  public Object aroundDistributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock lock = method.getAnnotation(DistributedLock.class);
    String key = lock.key();
    int expire = lock.expire();
    int waitTime = lock.waitTime();
    int polling = lock.polling();
    String value = UUID.randomUUID().toString();
    if (!lockUtil.lock(key, expire, waitTime, polling, value)) {
      return new ResponseModel(ResponseStatus.ERROR, "业务超时");
    }
    Object proceed = joinPoint.proceed();
    if (!lockUtil.unlock(key, value)) {
      log.info("业务:{},解锁错误", key);
    }
    return proceed;
  }
}
