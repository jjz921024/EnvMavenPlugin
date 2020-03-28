package io.jjz;

import io.jjz.utils.YamlUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class YamlTest {

  //@Test
  public void test() throws MojoFailureException, MojoExecutionException {
    EnvManagerPlugin plugin = new EnvManagerPlugin();
    plugin.setEnvFlag("dev");

    plugin.execute();
  }


  //@Test
  public void test2() throws FileNotFoundException {
    File file = new File("BuildScript/deploy_dev.yaml");
    FileInputStream fileInputStream = new FileInputStream(file);
    Yaml yaml = new Yaml();
    Map<String, Object> root = yaml.load(fileInputStream);

    Object value = YamlUtil.getValue(root, "spec", "template", "spec", "containers", "env");
    System.out.println(value);

  }
}
