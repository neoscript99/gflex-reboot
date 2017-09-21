package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * 把附件文件信息和属性分开，附件最大20M
 * @author neo
 *
 */
@Entity
class AttachmentFile {
	String fileId
	byte[] data;
	static mapping = {
		id name:'fileId',generator:'assigned'
	}
	
	static constraints = {
		fileId maxSize:64
		data maxSize:1024*1024*20
	}

	@Override
	public String toString() {
		return "AttachmentFile $fileId";
	}
}
