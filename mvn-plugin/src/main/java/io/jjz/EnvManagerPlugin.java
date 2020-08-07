package io.jjz;

import io.jjz.utils.EnvUtil;
import io.jjz.utils.ParseUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Map;

import static io.jjz.common.Constants.BASE_PATH;
import static io.jjz.common.Constants.YAML_SUFFIX;

/**
 * Author: Junzhao Jiang
 */
@Mojo(name = "env", defaultPhase = LifecyclePhase.NONE)
public class EnvManagerPlugin extends AbstractMojo {

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
        envMap = ParseUtil.parseK8sFile(file);
      } else {
        envMap = ParseUtil.parseCommonFile(file);
      }

      EnvUtil.setEnv(envMap);

      for (Map.Entry<String, String> entry : envMap.entrySet()) {
        getLog().info("set " + entry.getKey() + " = " + entry.getValue());
      }

    } catch (Exception e) {
      getLog().warn(e.getMessage());
    }
  }


  public void setEnvFlag(String envFlag) {
    this.envFlag = envFlag;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
