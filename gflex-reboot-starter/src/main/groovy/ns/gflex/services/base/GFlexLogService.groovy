package ns.gflex.services.base

import ns.gflex.domain.Log

/**
 * 带有label和log功能的服务类
 * @date 2013-7-18
 * @author 王楚
 *
 */
abstract class GFlexLogService extends GFlexService {

    def save(Map map, boolean isMerge = false, Object domain = null) {
        def newEntity = super.save(map, isMerge, domain)
        if (newEntity.id)
            logToDB("${newEntity.class} - ${map.id ? 'Modify' : 'Create'} $newEntity", newEntity.id)
        return newEntity
    }

    void deleteByIds(List idList, Object domain = null) {
        logToDB("${this.class.name} deleteByIds $idList")
        deleteByOwnerList(idList, 'ownerId', Log)
        super.deleteByIds(idList, domain)
    }

    void deleteByOwnerList(List valueList, String ownerIdField, Object domain = null) {
        def list = []
        valueList.each { list << withPrefix(it) }
        generalRepository.deleteMatch(domain, ['in': [[ownerIdField, list]]])
    }

    void logToDB(String message, Object entityId = null) {
        new Log(ownerId: entityId ? withPrefix(entityId) : '', message: message, account: sessionAccount,
                ipAddress: httpRequest ? httpRequest.remoteHost : '@server').save()
    }

    String withPrefix(def id) {
        return "${this.class.simpleName}_$id";
    }
}
