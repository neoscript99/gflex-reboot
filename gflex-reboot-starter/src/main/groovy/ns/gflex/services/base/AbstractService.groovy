package ns.gflex.services.base

import ns.gflex.repositories.GeneralRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.flex.remoting.RemotingDestination
import org.springframework.transaction.annotation.Transactional

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * service基础类(GFLexService重构)
 * 创建时间 2017-09-06
 * @author wangchu
 * @see ns.gflex.services.base.GFlexService
 */
@RemotingDestination
@Transactional
abstract class AbstractService<T> {
    @Autowired
    GeneralRepository generalRepository

    protected Logger log = LoggerFactory.getLogger(this.getClass())
    private Class<T> domain;

    public AbstractService() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        domain = (Class) params[0];
    }

    public T get(def id) {
        generalRepository.get(domain, id)
    }
    /**
     * 根据param查询domain
     * @param param
     * @return
     */
    @Transactional(readOnly = true)
    List<T> list(Map param = null) {
        if (!(param && param.order) && defaultOrder)
            param = [order: defaultOrder] << param
        generalRepository.list domain, param
    }

    @Transactional(readOnly = true)
    List listEnabled(Map param = null) {
        log.info "listEnabled param:$param"
        List enable = ['enabled', true]
        if (param)
            param.eq = param.eq ? param.eq.toList() << enable : [enable]
        else
            param = [eq: [enable]]
        list(param)
    }

    def save(Map map) {
        log.debug("save map: {}", map)
        generalRepository.saveMap domain, map
    }

    def saveEntity(T t) {
        generalRepository.saveEntity(t)
    }

    def saveTransietEntity(T t) {
        generalRepository.saveTransietEntity(t)
    }

    @Transactional(readOnly = true)
    int count(Map param = null) {
        generalRepository.count domain, param
    }

    @Transactional(readOnly = true)
    int countDistinct(Map param = null, List<String> propertyList) {
        def projections = [projections: [countDistinct: propertyList]]
        generalRepository.findFirst(projections << param, domain).countDistinct;
    }

    /**
     * @see GeneralRepository#deleteByIds
     */
    Number deleteById(def id) {
        generalRepository.deleteById(domain, id)
    }
    /**
     * @see GeneralRepository#deleteByIds
     */
    Number deleteByIds(List idList) {
        log.info("deleteByIds: $idList")
        if (idList)
            generalRepository.deleteByIds(domain, idList)
    }

    /**
     * @see GeneralRepository#deleteMatch
     */
    Number deleteMatch(Map param) {
        generalRepository.deleteMatch(domain, param)
    }

    /**
     * @see GeneralRepository#updateMatch
     */
    Number updateMatch(Map param, Map properties) {
        generalRepository.updateMatch(domain, param, properties)
    }

    List getDefaultOrder() {
        null
    }
}