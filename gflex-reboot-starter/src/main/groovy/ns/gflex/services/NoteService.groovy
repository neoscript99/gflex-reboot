package ns.gflex.services

import ns.gflex.domain.*
import ns.gflex.services.base.GFlexAttachService
import org.springframework.stereotype.Service;


/**
 * 通知维护
 * @author neo
 * 2012-7-12
 */
@Service
class NoteService extends GFlexAttachService{

	@Override
	def save(Map map){
		logHost("save Note $map.title $map.dateCreated $map.lastUpdated")
		map.lastUser=sessionUser
		map.attachNum = map[ATTACH_INFO_FIELD].fileNumber
		super.save(map)
	}

	@Override
	public Class getDomainClass() {
		Note
	}
	@Override
	protected void authorityCheck(){
		//本service执行时系统还处于未登录
	}
}
