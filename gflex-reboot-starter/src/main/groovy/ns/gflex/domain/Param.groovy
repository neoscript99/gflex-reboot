package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * Functions
 * @since Dec 23, 2010
 * @author wangchu
 */
@Entity
class Param {
	String code
	String name
	String value
	ParamType type
	String validExp //属性值类型正则表达式校验
	String validDescribe //校验说明
	User lastUser
	Date lastUpdated

	static mapping = {
		type lazy:false,fetch:'join'
		lastUser lazy:false,fetch:'join'
	}

	static constraints = {
		code unique:true
		lastUser nullable:true
		value maxSize:200
		name maxSize:200
		validDescribe maxSize:200
	}

	String toString(){
		"$code - $value"
	}
}
