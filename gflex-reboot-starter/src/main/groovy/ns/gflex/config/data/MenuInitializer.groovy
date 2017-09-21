package ns.gflex.config.data

import ns.gflex.domain.Menu
import ns.gflex.domain.Role
import ns.gflex.domain.association.RoleMenu
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(400)
class MenuInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.countAll(Menu) > 0)
    }

    void doInit() {

        def roleAdmin = generalRepository.findFirst(Role, [eq: [['roleCode', 'Administrators']]])
        def rolePublic = generalRepository.findFirst(Role, [eq: [['roleCode', 'Public']]])

        def rootMenu = save(new Menu(label: 'Root', app: ''))

        initAdminMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: roleAdmin, menu: it))
        }
        initPublicMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: rolePublic, menu: it))
        }
    }

    private List initAdminMenu(Long rootId) {
        def sys = save(new Menu(label: '系统设置', app: '', seq: 90, parentId: rootId))

        [
                new Menu(label: '用户设置', app: 'Profile', seq: 99, parentId: rootId),
                new Menu(label: '帐号管理', app: 'User', seq: 2, parentId: sys.id),
                new Menu(label: '角色管理', app: 'Role', seq: 1, parentId: sys.id),
                new Menu(label: '发布通知', app: 'Note', seq: 3, parentId: sys.id),
                new Menu(label: '参数维护', app: 'Param', seq: 4, parentId: sys.id)
        ]
    }

    private List initPublicMenu(Long rootId) {
        [
                new Menu(label: '欢迎页面', app: 'Welcome', seq: 10, parentId: rootId)
        ]
    }
}
