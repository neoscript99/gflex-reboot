package ns.gflex.config.data

import ns.gflex.repositories.GeneralRepository
import ns.gflex.repositories.GormRepository

/**
 * Created by Neo on 2017-08-25.
 */
abstract class AbstractDataInitializer implements DataInitializer {
    GeneralRepository generalRepository = new GormRepository()
    List entityList = Collections.synchronizedList(new ArrayList())

    /**
     * 判断并执行初始
     */
    @Override
    void init() {
        if (!isInited())
            doInit();
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
