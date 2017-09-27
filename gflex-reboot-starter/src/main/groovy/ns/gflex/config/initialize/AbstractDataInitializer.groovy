package ns.gflex.config.initialize

import groovy.util.logging.Slf4j
import ns.gflex.repositories.GeneralRepository

/**
 * Created by Neo on 2017-08-25.
 */
@Slf4j
abstract class AbstractDataInitializer implements DataInitializer {
    GeneralRepository generalRepository
    List entityList = Collections.synchronizedList(new ArrayList())

    /**
     * 判断并执行初始
     */
    @Override
    void init() {
        if (!isInited()) {
            def className=this.class.simpleName
            log.debug("$className 初始化开始")
            doInit();
            log.debug("$className 初始化完成")
        }
    }

    /**
     * 保存实例到数据库，使用本方法方便mock
     *
     * @param entity
     * @param < D >
     * @return
     */
    @Override
    def save(def entity) {
        entityList.add(entity)
        generalRepository.saveEntity(entity)
        entity
    }
}
