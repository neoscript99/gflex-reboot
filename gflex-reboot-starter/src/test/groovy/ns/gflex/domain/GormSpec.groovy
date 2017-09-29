package ns.gflex.domain

import ns.gflex.GflexBootApplication
import ns.gflex.repositories.GeneralRepository
import ns.gflex.util.JsonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by Neo on 2017-09-29.
 */
@SpringBootTest(classes = GflexBootApplication.class)
@Transactional
@Commit
@Stepwise
class GormSpec extends Specification {
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    ApplicationArguments applicationArguments


    static Map savedMap

    def 'new role'() {
        given:
        Role role = generalRepository.saveEntity(Role.PUBLIC)
        savedMap = JsonUtil.beanToMap(role)
        println role.dump()
        expect:
        role.lastUpdated == role.dateCreated
    }

    def 'check id'() {
        given:
        println(savedMap)
        println(savedMap.dateCreated)
        expect:
        savedMap.id != null
    }

    def 'update lastUpdated'() {
        given:
        Role resultRole = generalRepository.saveMap(Role, savedMap)

        expect:'插入数据库时更新lastUpdated，entity中不会改变'
        resultRole.lastUpdated == resultRole.dateCreated
    }

    def 'check autoTimestamp'() {
        given:
        Role resultRole = generalRepository.get(Role,savedMap.id)

        expect:
        resultRole.lastUpdated > resultRole.dateCreated
    }
}
