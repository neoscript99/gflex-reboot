package ns.gflex.domain

import grails.gorm.annotation.Entity

@Entity
class AttachmentInfo {
	String fileId
	String name
	Long fileSize
	Date dateCreated
	String ownerId
	static mapping = {
		id name:'fileId',generator:'assigned'
		activity fetch:'join',lazy:false
		ownerId index:'idx_attach_owner'
		dateCreated index:'idx_attach_date'
	}

	static constraints = {
		fileId maxSize:64
		ownerId maxSize:128
		name maxSize:256
	}

	@Override
	public String toString() {
		return "Attachment - $name";
	}
}
