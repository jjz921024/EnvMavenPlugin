package io.jjz

import io.jjz.common.Constants
import io.jjz.utils.EnvUtil
import io.jjz.utils.ParseUtil
import org.gradle.api.Plugin
import org.gradle.api.Project

class EnvPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def evnConfig = project.extensions.create("envConfig", EnvConfigExt)

        project.task("env") {
            println(evnConfig.toString())

            if (!evnConfig.path) {
                evnConfig.isK8s = true
                evnConfig.envFlag = !evnConfig.envFlag ? "dev" : evnConfig.envFlag
                evnConfig.path = Constants.BASE_PATH + evnConfig.envFlag + Constants.YAML_SUFFIX
            }

            // 获取配置文件
            File file = new File(evnConfig.path)
            if (!file.exists()) {
                println(evnConfig.path + " dose not exist.")
                return
            }
            if (!file.isFile()) {
                println(evnConfig.path + " is not a file.")
                return
            }


            println("parse file: " + file.getName())
            try {
                Map<String, String> envMap = null
                if (evnConfig.isK8s) {
                    envMap = ParseUtil.parseK8sFile(file)
                } else {
                    envMap = ParseUtil.parseCommonFile(file)
                }

                EnvUtil.setEnv(envMap)

                for (Map.Entry<String, String> entry : envMap.entrySet()) {
                    println("set " + entry.getKey() + " = " + entry.getValue())
                }

            } catch (Exception e) {
                println(e.getMessage())
            }
        }


    }
}
