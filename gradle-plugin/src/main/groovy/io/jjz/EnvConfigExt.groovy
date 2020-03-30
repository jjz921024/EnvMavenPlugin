package io.jjz

class EnvConfigExt {
    String envFlag

    String path

    boolean isK8s


    @Override
    String toString() {
        return "EnvConfigExt{" +
                "envFlag='" + envFlag + '\'' +
                ", path='" + path + '\'' +
                ", isK8s=" + isK8s +
                '}'
    }
}
