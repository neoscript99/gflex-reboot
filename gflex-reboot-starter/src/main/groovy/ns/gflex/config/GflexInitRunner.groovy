package ns.gflex.config

import groovy.util.logging.Slf4j
import ns.gflex.config.data.InitializeDomian
import ns.gflex.repositories.GeneralRepository
import ns.gflex.util.InitializerUtil
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
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
@Slf4j
class GflexInitRunner implements CommandLineRunner {
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    GeneralRepository generalRepository

    @Override
    @Transactional
    void run(String... strings) throws Exception {
        log.debug("init ns.gflex with params: {}", strings)
        if (strings.contains('--init')) {
            initStaticList()
            InitializerUtil.doInit(generalRepository, "ns.gflex.config.data")
        }
    }

    /**
     * 初始化InitializeDomian注解的domain类
     * @see InitializeDomian
     */
    void initStaticList() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        configurableApplicationContext.getBeansOfType(Datastore).each { String key, Datastore datastore ->
            datastore.getMappingContext().getPersistentEntities().each {
                InitializeDomian initializeDomian = it.javaClass.getAnnotation(InitializeDomian)
                if(initializeDomian) {
                    println(initializeDomian.value())
                    it.javaClass.getDeclaredField(initializeDomian.value())
                    if (it.javaClass.hasProperty(initializeDomian.value()))
                        println(initializeDomian.value())
                }
            }
        }
    }
}