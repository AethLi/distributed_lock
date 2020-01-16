package cn.aethli.lock.common.emnus;

/**
 * @author 93162
 */
public enum ResponseStatus {
  OK("正常", 0),
  FAIL("失败", 1),
  ERROR("错误", 2);
  private String desc;
  private int value;

  ResponseStatus(String desc, int value) {
      this.desc = desc;
      this.value = value;
  }

  public static ResponseStatus get(int value) {
    for (ResponseStatus status : ResponseStatus.values()) {
      if (status.value == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("argument error: " + value);
  }

  public int getValue() {
    return this.value;
  }

  public String getDesc() {
    return this.desc;
  }
}
