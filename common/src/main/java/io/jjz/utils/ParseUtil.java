package io.jjz.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseUtil {

  public static Map<String, String> parseK8sFile(File file) throws IOException {
    Yaml yaml = new Yaml();
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      Iterable<Object> iterable = yaml.loadAll(fileInputStream);
      Map<String, Object> deployments = (Map<String, Object>) iterable.iterator().next();

      Object env = YamlUtil.getValue(deployments, "spec", "template", "spec", "containers", "env");
      if (env == null) {
        throw new IllegalArgumentException("can't find spec#template#spec#containers[env]");
      }

      // 得到yaml中env的环境变量
      return YamlUtil.transferArrayToMap((List<Object>) env);
    }
  }

  // 每行一个环境变量，格式 key=value
  public static Map<String, String> parseCommonFile(File file) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    HashMap<String, String> envMap = new HashMap<>();
    bufferedReader.lines().forEach(line -> {
      String[] split = line.split("=", 2);
      envMap.put(split[0], split[1]);
    });
    return envMap;
  }

}
