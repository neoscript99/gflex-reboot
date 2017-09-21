package ns.gflex.repositories

import grails.gorm.DetachedCriteria
import ns.gflex.util.JsonUtil
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.criterion.Projections
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service

import java.text.MessageFormat

/**
 * 替换原来的GForm
 * <p>通过set到业务service，符合组合优于继承的设计规范，分离业务逻辑和数据库操作，为单元测试提供支持
 * <p>不涉及业务处理
 * Created by Neo on 2017-08-25.
 */
@Service
class GormRepository implements GeneralRepository {
    static Logger log = LoggerFactory.getLogger(GormRepository.class)

    /**
     * @see GeneralRepository#get
     */
    @Override
    public <T> T get(Class<T> domain, def id) {
        domain.get(id)
    }

    /**
     * @see GeneralRepository#findFirst
     */
    @Override
    public <T> T findFirst(Class<T> domain, Map param) {
        List results = list(domain, param)
        if (results)
            results.first()
        else
            null
    }

    /**
     * @see ns.gflex.repositories.GeneralRepository#saveMap(Class, Map)
     */
    @Override
    public <T> T saveMap(Class<T> domain, Map map) {
        saveMap(domain, map, false)
    }
    /**
     * @see ns.gflex.repositories.GeneralRepository#saveMap(Class, Map, boolean)
     */
    public <T> T saveMap(Class<T> domain, Map map, boolean isMerge) {
        def newEntity = JsonUtil.mapToBean(map, domain)
        log.debug("save new entity: {}", newEntity.dump())
        if (map.id) {
            //原来的代码newEntity是离线的，会触发beforeInsert，导致dateCreated更新，其实是update
            def oldEntity = domain.get(map.id)
            BeanUtils.copyProperties(newEntity, oldEntity, ['id', 'version', 'metaClass'].toArray(new String[0]))
            saveEntity(oldEntity, isMerge)
        } else
            saveEntity(newEntity, isMerge)
    }

    /**
     * @see GeneralRepository#list
     */
    @Override
    public <T> List<T> list(Class<T> domain, Map param) {
        log.info "list - ${domain} param:$param"

        if (param?.projections) {
            log.info "resultTransformer to CriteriaSpecification.ALIAS_TO_ENTITY_MAP"
            param.put 'resultTransformer', [
                    CriteriaSpecification.ALIAS_TO_ENTITY_MAP
            ]
        }

        criteriaQuery(domain, param, false)
    }

    /**
     * @see GeneralRepository#listAll
     */
    @Override
    public <T> List<T> listAll(Class<T> domain) {
        list(domain, null)
    }
    /**
     * @see GeneralRepository#count
     */
    @Override
    int count(Class domain, Map param) {
        log.info "count - ${domain} param:$param"
        criteriaQuery(domain, param, true)
    }

    /**
     * @see GeneralRepository#countAll
     */
    @Override
    int countAll(Class domain) {
        count(domain, null)
    }

    /**
     * @see GeneralRepository#delete
     */
    @Override
    void delete(Object entity) {
        entity.delete()
    }

    /**
     * @see GeneralRepository#deleteById
     */
    @Override
    Number deleteById(Class domain, Object id) {
        deleteByIds(domain, [id])
    }

    /**
     * @see GeneralRepository#deleteByIds
     */
    @Override
    Number deleteByIds(Class domain, List idList) {
        deleteMatch(domain, ['in': [['id', idList]]])
    }

    /**
     * @see GeneralRepository#deleteMatch
     */
    @Override
    Number deleteMatch(Class domain, Map param) {
        buildDetachedCriteria(domain, param).deleteAll()
    }

    /**
     * @see GeneralRepository#updateMatch
     */
    @Override
    Number updateMatch(Class domain, Map param, Map properties) {
        buildDetachedCriteria(domain, param).updateAll(properties)
    }

    /**
     * @see GeneralRepository#saveEntity(Object)
     */
    @Override
    def saveEntity(def entity) {
        saveEntity(entity, false)
    }
    /**
     * 原先不是merge有问题，新版本先测试下，本方法先隐藏
     * @see GeneralRepository#saveEntity(Object, boolean)
     */
    def saveEntity(def entity, boolean isMerge) {
        log.info "saving  ${entity.class}"

        //错误信息可能在中间过程中产生，末状态正确时不一定为空，所以必须先清理
        entity.clearErrors()
        if (entity.validate()) {
            /* 如果存在unique限制，当save为update时导致NonUniqueObjectException异常
             * 原因是查询是否唯一时，导致session有两个id相同对象，grails目前处理不够好。
             * 如果用entity.merge()，部分功能，如autoTimeStrape无法自动完成
             * 暂时通过清空session处理
             * */
            if (!isMerge) {
                log.debug "save ${entity.class} ${entity}"
                entity.save()
            } else {
                log.debug "merge ${entity.class} ${entity}"
                entity.merge()
            }
        } else {
            StringBuilder sb = new StringBuilder()
            entity.errors.allErrors.each {
                String msg = MessageFormat.format(it.defaultMessage, it.arguments);
                log.error(msg)
                log.debug(it.toString())
                sb.append("$msg\n")
                //messageBundle配置参考log.error(it)打印的error.code，
                //如com.beeb.teller.TeTransInfo.transCode.unique.error
                //sb.append("${messageSource.getMessage(it, Locale.default)}\n")

            }
            throw new RuntimeException(sb.toString())
        }
    }

    /**
     * 使用hibernate criteria 查询
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @param domainClass
     * @param param
     * @param isCount 是否计数
     * @return
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected Object criteriaQuery(Class domainClass, Map param, boolean isCount) {
        def criteria = domainClass.createCriteria()
        if (isCount)
            criteria.count(makeCriteria(param))
        else
            criteria.list(makeCriteria(param))
    }

    /**
     * 将map参数转化为HibernateCriteriaBuilder
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected def makeCriteria(Map param) {
        return {
            param.each { k, v ->
                log.debug "invokeMethod $k($v)"
                //如果v是Map，递归makeQuery
                if (v instanceof Map)
                    invokeMethod k, makeCriteria(v)
                //否则v必须为list，将list的每个值作为参数数组，循环调用Builder的函数
                else
                    v.each {
                        def args = (it instanceof List) ? it.toArray() : it

                        //通过id查询不能自动转换Integer为Long(已通过修改org.grails.datastore.mapping.query.jpa.JpaQueryBuilder修复)
                        if (k == 'eq' && (args[0] =~ '.*[iI]d$') && args[1] instanceof Integer)
                            args[1] = args[1].longValue()
                        //HibernateCriteriaBuilder不支持sqlGroupProjection
                        if (k == 'sqlGroupProjection')
                            addProjectionToList(Projections.invokeMethod('sqlGroupProjection', args), args[2][0])
                        else if (k == 'sqlProjection')
                            addProjectionToList(Projections.invokeMethod('sqlProjection', args), args[1][0])
                        else
                            invokeMethod k, args
                    }
            }
        }
    }
    /**
     * 创建criteria并执行操作
     * <p>适合批量删除等操作
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @param domain
     * @param param
     * @return
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected DetachedCriteria buildDetachedCriteria(Class domain, Map param) {
        log.info("$domain : $param")
        return new DetachedCriteria(domain).build(makeCriteria(param))
    }
}
