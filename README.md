# env-maven-plugin

## 构建

### 安装到本地仓库

- 构建maven插件,并安装到本地仓库
```
~/EvnManagePlugin>mvn install -pl mvn-plugin -am -Dmvn.test.skip=true
```

- 构建gradle插件,并安装到本地仓库
```
~/EvnManagePlugin/gradle-plugin>gradle uploadArchives
```


## 使用
### maven工程
1. 在pom.xml中做如下配置
```xml
<plugin>
    <groupId>io.jjz</groupId>
    <artifactId>mvn-plugin</artifactId> <!--引入插件-->
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>compile</phase> <!--将插件中evn的goal绑定在编译阶段-->
            <goals>
                <goal>env</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <envFlag>dev</envFlag> <!-- 指定k8s的yaml文件的profile -->
        <!--<path>env</path>-->   <!--或指定相对路径的key=value格式的文件, 优先级比envFlag高-->
    </configuration>
</plugin>
```
2. 执行程序
- 运行普通Java工程，引入exec-maven-plugin插件
```
~\EvnManagePlugin\example>mvn clean compile
```
- 运行SpringBoot工程，引入spring-boot-maven-plugin插件
```
~\EvnManagePlugin\example>mvn clean spring-boot:run 
```

在输出信息中会显示配置了哪些环境变量
[INFO] --- mvn-plugin:1.0-SNAPSHOT:env (default) @ example ---

### gradle工程
1. 在根项目的build.gradle文件的buildscript{}的dependencies{}中引入
```
classpath 'io.jjz:gradle-plugin:1.0-SNAPSHOT'
```
2. 在app module的build.gradle文件中引入插件,并配置好信息
```
apply plugin: 'io.jjz.env-plugin'

envTask {
    flag "st"
    //path "C:/Users/Jun/IdeaProjects/EnvManagePlugin/example/env"
}
```
3. 执行
- 普通Java工程
```
// 配置一个task
task run(type: JavaExec, dependsOn: 'classes') {
    classpath = sourceSets.main.runtimeClasspath
    main = 'io.jjz.MainEntry'
}

gradle envTask run
```

- SpringBoot工程
```
// 需引入插件,并配置入口函数
apply plugin: 'org.springframework.boot'
jar {
    version = '0.0.1'
    manifest {
        attributes "Manifest-Version": 1.0, 'Main-Class': 'io.jjz.Application'
    }
}

gradle envTask bootRun
```
