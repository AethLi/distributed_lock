package cn.aethli.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DistributedLock {
  /** 业务锁名 */
  String key();
  /** 锁的过期秒数,默认是5秒,此处为毫秒单位，实际以秒作为单位 */
  int expire() default 5000;
  /** 尝试加锁,最多等待时间,默认0毫秒 */
  int waitTime() default 0;
  /** 轮询间隔,默认是100毫秒 */
  int polling() default 100;
}
