package io.jjz;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import java.io.IOException;

public class PropertiesTest {

  @Test
  public void testParse() throws MojoFailureException, MojoExecutionException, IOException {
    EnvManagerPlugin plugin = new EnvManagerPlugin();
    plugin.setPath("C:\\Users\\Jun\\IdeaProjects\\EvnManagePlugin\\env");

    plugin.execute();
  }
}
