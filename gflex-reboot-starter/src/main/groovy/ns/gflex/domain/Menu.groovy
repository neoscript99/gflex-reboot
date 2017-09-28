package ns.gflex.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Menu
 * toc 2010-12-14
 * @author wangchu
 */
@Entity
@ToString(includePackage = false, includes = ['app', 'label'])
@EqualsAndHashCode(includes = 'id')
class Menu implements Comparable<Menu> {
    String app
    String label
    Integer seq = 0
    Long parentId

    static constraints = {
        app nullable: true
        parentId nullable: true
    }

    @Override
    public int compareTo(Menu o) {
        seq <=> o.seq
    }
}
