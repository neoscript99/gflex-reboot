package ns.gflex.services

import ns.gflex.services.base.GFlexService
import ns.gflex.domain.Department;
import ns.gflex.domain.Role;
import ns.gflex.domain.User;
import ns.gflex.domain.association.UserRole;
import ns.gflex.util.EncoderUtil
import org.springframework.stereotype.Service;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class UserService extends GFlexService{

	def save(Map map){
		if(map.password)
			map.password=EncoderUtil.md5(map.password)
		else {
			User oldUser = User.get(map.id);
			oldUser.discard()
			map.password = oldUser.password
		}
		map.dept=Department.get(map.dept.id)

		super.save(map,true)
	}
	void addUserRole(Long userId, Long roleId){
		if(!count([role:[idEq:[roleId]],user:[idEq:[userId]]],UserRole))
			saving(new UserRole(user:User.get(userId),role:Role.get(roleId)))
	}
	void deleteUserRoles(Long userId, List roles){
		log.info "delete User Roles ${userId}"
		if(roles){
			UserRole.executeUpdate("delete UserRole where user.id = $userId and role.id in (${roles*.id.join(',')})")
		}
	}

	List getRoleUsers(Long roleId,String account){
		Map param = [user:[eq:[['editable', true]]],role:[idEq : [roleId]]]
		if(account)
			param.user.like = [['account', "%$account%"]]
		list(param,UserRole)*.user
	}

	List getUserRoles(Long userId){
		list([user:[idEq:[userId]]],UserRole)*.role
	}

	User login(String account,String password){
		def user  = User.findByAccountAndPassword(account,EncoderUtil.md5(password))
		if(user){
			if(!user.enabled)
				throw new RuntimeException('用户帐号失效')
			flexSession.setAttribute( GFlexService.SESSION_ACCOUNT_ID, user.account)
			return user
		}
		else
			throw new RuntimeException('无法登录，请检查用户名密码')
	}

	void changePassword(String account,String oriPassword,String newPassword){
		def user  = User.findByAccountAndPassword(account,EncoderUtil.md5(oriPassword))
		if(user)
			user.password = EncoderUtil.md5(newPassword)
		else
			throw new RuntimeException('原密码错误，无法修改')
	}

	@Override
	public Class getDomainClass() {
		User.class;
	}
}
