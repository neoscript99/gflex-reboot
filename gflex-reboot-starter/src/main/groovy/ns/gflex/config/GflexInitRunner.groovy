package ns.gflex.config

import ns.gflex.util.InitializerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * 基础数据初始化
 * <p> 基础系统初始化为最高优先级，如果其它模块初始化依赖基础数据（User、Role、Menu）等，优先级必须低于本类
 * <p> 是否允许依赖命令行参数"--init"
 * Created by Neo on 2017-07-01.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class GflexInitRunner implements CommandLineRunner {
    static private Logger log = LoggerFactory.getLogger(GflexInitRunner.class);

    @Override
    @Transactional
    void run(String... strings) throws Exception {
        log.debug("init ns.gflex with params: {}", strings)
        if (strings.contains('--init'))
            InitializerUtil.doInit("ns.gflex.config.data")
    }
}
