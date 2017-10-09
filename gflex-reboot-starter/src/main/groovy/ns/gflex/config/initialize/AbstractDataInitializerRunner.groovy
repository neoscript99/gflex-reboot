package ns.gflex.config.initialize

import groovy.util.logging.Slf4j
import ns.gflex.repositories.GeneralRepository
import ns.gflex.util.InitializerUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Neo on 2017-09-28.
 */
@Slf4j
abstract class AbstractDataInitializerRunner implements CommandLineRunner {
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    OptionAccessor optionAccessor

    @Override
    @Transactional
    void run(String... args) throws Exception {
        log.debug("$basePackage 中DataInitializer方式数据初始化开始")
        if (optionAccessor.init) {
            InitializerUtil.doInit(generalRepository, applicationContext, basePackage)
        }
        log.debug("$basePackage 中DataInitializer方式数据初始化完成")
    }

    abstract String getBasePackage()

}
