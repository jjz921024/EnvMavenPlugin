package io.jjz.utils;

import java.util.*;

public class YamlUtil {

  /**
   * 读取指定的多个key
   */
  public static Object getValue(Map<String, Object> load, String... keys) {
    if (keys.length == 1) {
      return load.get(keys[0]);
    }

    for (Map.Entry<String, Object> entry : load.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      // 直到找到指定的key
      if (!keys[0].equals(key)) {
        continue;
      }

      // 递归解析每一级，分object和array
      if (keys.length > 1) {
        if (value instanceof Map) {
          HashMap<String, Object> map = new HashMap<>();
          Map<String, Object> parent = (Map<String, Object>) value;
          parent.entrySet().forEach(e -> map.put(e.getKey(), e.getValue()));
          return getValue(map, Arrays.copyOfRange(keys, 1, keys.length));

        } else if (value instanceof List) {
          List<Object> parent = (List<Object>) value;
          Map<String, Object> arrayToMap = transferArrayToMap(key, parent);
          return getValue(arrayToMap, keys);
        }
      }

    }
    return null;
  }


  public static Map<String, Object> transferArrayToMap(String key, List<Object> list) {
    HashMap<String, Object> map = new HashMap<>();
    for (Object element : list) {
      map.put(key, element);
    }
    return map;
  }

  public static Map<String, String> transferArrayToMap(List<Object> list) {
    HashMap<String, String> map = new HashMap<>();
    for (Object l : list) {
      Map<String, String> m = (Map<String, String>) l;
      map.put(m.get("name"), m.get("value"));
    }
    return map;
  }

}
