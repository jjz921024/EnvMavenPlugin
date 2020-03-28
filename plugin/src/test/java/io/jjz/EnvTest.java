package io.jjz;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static io.jjz.utils.EnvUtils.setEnv;

public class EnvTest {

  @Test
  public void test() throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "SET");
    Map<String, String> env = pb.environment();
    env.put("MYVAR", "myValue");
    Process p = pb.start();


    InputStreamReader isr = new InputStreamReader(p.getInputStream());
    char[] buf = new char[1024];
    while (!isr.ready()) {
      ;
    }
    /*while (isr.read(buf) != -1) {
      System.out.println(buf);
    }*/

    System.out.println(System.getenv("MYVAR"));

  }

  @Test
  public void test2() throws Exception {
    HashMap<String, String> map = new HashMap<>();
    map.put("ddd", "ee");
    setEnv(map);

    System.out.println(System.getenv("ddd"));
  }


}
