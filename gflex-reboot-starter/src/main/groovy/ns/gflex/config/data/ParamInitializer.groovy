package ns.gflex.config.data

import ns.gflex.domain.Param
import ns.gflex.domain.ParamType
import ns.gflex.domain.User
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(500)
class ParamInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.count(Param, null) > 0)
    }

    void doInit() {
        def type = save(new ParamType(code: 'system', name: '系统参数', seq: 1))
        def admin = generalRepository.findFirst(User, [eq: [['account', 'admin']]])
        save(new Param(code: 'AllowAnonymousLogin', name: '允许匿名登录', value: 'false', type: type, validExp: '^(true|false)$', validDescribe: 'true或者false', lastUser: admin))
        save(new Param(code: 'SpareMintues', name: '空闲多少分钟后自动退出（不能大于web容器session过期时间）', value: '20', type: type, validExp: '^\\d+$', validDescribe: '整数', lastUser: admin))
        save(new Param(code: 'HoldHeartBeat', name: '保持登录状态', value: 'false', type: type, validExp: '^(true|false)$', validDescribe: 'true或者false', lastUser: admin))
    }
}
