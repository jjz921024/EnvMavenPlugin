import io.jjz.EnvConfigExt
import io.jjz.common.Constants
import org.junit.Test

class EvnTest {

    @Test
    void test() {
        def evnConfig = new EnvConfigExt()
        //evnConfig.envFlag = 'st'
        evnConfig.path = 'dfdfd'

        if (!evnConfig.path) {
            evnConfig.isK8s = true
            evnConfig.envFlag = !evnConfig.envFlag ? "dev" : evnConfig.envFlag
            evnConfig.path = Constants.BASE_PATH + evnConfig.envFlag + Constants.YAML_SUFFIX
        }

        println(evnConfig.path)
    }
}
