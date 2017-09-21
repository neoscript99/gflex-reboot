package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * Menu
 * toc 2010-12-14
 * @author wangchu
 */
@Entity
class Menu implements Comparable<Menu>{
	String app
	String label
	Integer seq = 0
	Long  parentId

	static constraints = {
		app nullable:true
		parentId nullable:true
	}

	@Override
	public int compareTo(Menu o) {
		seq.compareTo(o.seq)
	}
}
