package cn.aethli.lock.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/** @author Termite */
@Slf4j
@Component
public class LockUtil {
  private final StringRedisTemplate redisTemplate;

  @Autowired
  public LockUtil(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * @param key
   * @param expire
   * @param waitTime
   * @param polling
   * @return
   */
  public boolean lock(String key, int expire, int waitTime, int polling, String value)
      throws InterruptedException {
    String luaScripts =
        "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
    List<String> keys = new ArrayList<>();
    keys.add(key);
    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScripts, Long.class);
    int i = 0;
    do {
      Long executeCount = redisTemplate.execute(redisScript, keys, value, String.valueOf(expire/1000));
      if (executeCount != null && 0 != Integer.parseInt(String.valueOf(executeCount))) {
        return true;
      }
      Thread.sleep(polling);
      i += polling;
    } while (i < waitTime);
    log.info("业务:{},已超时", key);
    return false;
  }

  /**
   * 使用Lua脚本进行解锁操纵，解锁的时候验证value值
   *
   * @param key
   * @param value
   * @return
   */
  public boolean unlock(String key, String value) {
    String luaScripts =
        "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    List<String> keys = new ArrayList<>();
    keys.add(key);
    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScripts, Long.class);
    return (Objects.requireNonNull(redisTemplate.execute(redisScript, keys, value)).compareTo(0L)
        != 0);
  }
}
