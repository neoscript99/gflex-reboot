package ns.gflex.domain

import grails.gorm.annotation.Entity


@Entity
class Log {
	String ownerId;
	String message;
	String ipAddress;
	String account
	Date dateCreated
	static mapping = {
		id generator:'increment'
	}

	static constraints = {
		message maxSize:1024
		ownerId nullable:true
		account nullable:true
	}
}