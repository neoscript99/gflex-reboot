package ns.gflex.config

import com.google.common.collect.Sets
import groovy.util.logging.Slf4j
import ns.gflex.config.initialize.InitializeDomian
import ns.gflex.domain.Department
import ns.gflex.repositories.GeneralRepository
import ns.gflex.util.InitializerUtil
import org.apache.commons.cli.Option
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import java.lang.reflect.Field

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
    @Autowired
    OptionAccessor optionAccessor

    @Override
    @Transactional
    void run(String... args) throws Exception {
        log.debug("init ns.gflex with params: {}", args)
        if (optionAccessor.init) {
            def profiles = Sets.newHashSet('default')
            if (Collection.isAssignableFrom(optionAccessor.inits.class))
                profiles.addAll(optionAccessor.inits)
            initStaticList(profiles)
            InitializerUtil.doInit(generalRepository, "ns.gflex.config.data")
        }
    }

    /**
     * 初始化InitializeDomian注解的domain类
     * @see InitializeDomian
     */
    void initStaticList(Set profiles) {
        log.debug("initialize system with StaticList profiles: $profiles")
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        Set doneSet = new HashSet()
        configurableApplicationContext.getBeansOfType(Datastore).each { String key, Datastore datastore ->
            datastore.getMappingContext().getPersistentEntities().each {
                initDomain(it.javaClass, doneSet, profiles)
            }
        }
    }

    /**
     * 根据InitializeDomian的profiles配置，决定是否进行初始化
     * @param domain
     * @param doneSet
     * @param profiles
     */
    void initDomain(Class domain, Set doneSet, Set profiles) {
        if (doneSet.contains(domain))
            return;
        InitializeDomian initializeDomian = domain.getAnnotation(InitializeDomian)
        if (initializeDomian && initializeDomian.profiles().any { profiles.contains(it) }) {
            log.debug("$domain 初始化开始")
            //如果有依赖，先处理依赖类
            initializeDomian.depends().each { initDomain(it, doneSet, profiles) }
            //表数据为空
            if (!generalRepository.countAll(domain)) {
                Field initField = domain.getDeclaredField(initializeDomian.value())
                initField.setAccessible(true)
                initField.get(domain).each { generalRepository.saveEntity(it) }
            }
            log.debug("$domain 初始化完成")
        }
        doneSet.add(domain)
    }
}