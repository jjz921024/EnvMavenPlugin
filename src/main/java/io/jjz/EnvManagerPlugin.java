package io.jjz;

import io.jjz.utils.EnvUtils;
import io.jjz.utils.YamlUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Junzhao Jiang
 */
@Mojo(name = "env", defaultPhase = LifecyclePhase.NONE)
public class EnvManagerPlugin extends AbstractMojo {

  private static final String BASE_PATH = "BuildScript/deploy_";
  private static final String YAML_SUFFIX = ".yaml";

  @Parameter(property = "env", defaultValue = "dev")
  private String envFlag;


  @Parameter(property = "path")
  private String path;

  private boolean isK8s;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (path == null) {
      isK8s = true;
      path = BASE_PATH + envFlag + YAML_SUFFIX;
    }

    // 获取配置文件
    File file = new File(path);
    if (!file.exists()) {
      getLog().warn(path + " dose not exist.");
      return;
    }
    if (!file.isFile()) {
      getLog().warn(path + " is not a file.");
      return;
    }


    try {
      Map<String, String> envMap = null;
      if (isK8s) {
        envMap = parseK8sFile(file);
      } else {
        envMap = parseCommonFile(file);
      }

      EnvUtils.setEnv(envMap);

      for (Map.Entry<String, String> entry : envMap.entrySet()) {
        getLog().info("set " + entry.getKey() + " = " + entry.getValue());
      }

    } catch (Exception e) {
      getLog().warn(e.getMessage());
    }
  }


  private Map<String, String> parseK8sFile(File file) throws IOException {
    Yaml yaml = new Yaml();
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      Map<String, Object> root = yaml.load(fileInputStream);

      Object env = YamlUtil.getValue(root, "spec", "template", "spec", "containers", "env");
      if (env == null) {
        throw new IllegalArgumentException("can't find spec#template#spec#containers[env]");
      }

      // 得到yaml中env的环境变量
      return YamlUtil.transferArrayToMap((List<Object>) env);
    }
  }

  // 每行一个环境变量，格式 key=value
  private Map<String, String> parseCommonFile(File file) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    HashMap<String, String> envMap = new HashMap<>();
    bufferedReader.lines().forEach(line -> {
      String[] split = line.split("=", 2);
      envMap.put(split[0], split[1]);
    });

    return envMap;
  }


  public void setEnvFlag(String envFlag) {
    this.envFlag = envFlag;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
