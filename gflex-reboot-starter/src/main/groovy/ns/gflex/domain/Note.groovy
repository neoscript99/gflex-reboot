package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * 系统通知
 * @author neo
 * 2012-6-11
 */
@Entity
class Note {
	String title;
	String content;
	Integer attachNum;
	User lastUser;
	Date lastUpdated;
	Date dateCreated;
	static mapping = {
		lastUser lazy:false,fetch:'join'
	}
	static constraints = {
		title maxSize:256
		content nullable:true, maxSize:1024
	}
}
