package ns.gflex.services.base

import ns.gflex.domain.Label

/**
 * 带有label和log功能的服务类
 * @date 2013-7-18
 * @author 王楚
 *
 */
abstract class GFlexLabelService extends GFlexLogService {
    static public String LABEL_FIELD = 'gflexLabelField';


    def save(Map map, boolean isMerge = false, Object domain = null) {

        def newEntity = super.save(map, isMerge, domain)

        if (newEntity.id && map[LABEL_FIELD])
            saveLables(newEntity.id, map[LABEL_FIELD])
        return newEntity
    }

    void saveLables(Object entityId, Object labels) {
        log.info "saveLables $labels"
        String oid = withPrefix(entityId);
        generalRepository.deleteMatch(Label, [eq: [['ownerId', oid]]])
        labels.each {
            def map = [ownerId: oid, label: (it.length() < 33 ? it : it[0..31])]
            new Label(map).save();
        }
    }

    List getLabels(Map map) {
        if (map.id)
            Label.findAllByOwnerId(withPrefix(map.id))*.label
    }

    void deleteByIds(List idList, Object domain = null) {
        deleteByOwnerList(idList, 'ownerId', Label)
        super.deleteByIds(idList, domain)
    }

}
