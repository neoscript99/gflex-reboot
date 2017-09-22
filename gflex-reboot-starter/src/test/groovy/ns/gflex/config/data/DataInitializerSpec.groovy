package ns.gflex.config.data

import ns.gflex.repositories.GeneralRepository
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Neo on 2017-08-25.
 */
class DataInitializerSpec extends Specification {
    @Shared
    GeneralRepository persistenceService
    def setupSpec(){
        persistenceService  = Stub(GeneralRepository)
        persistenceService.count(_, _) >> 0
    }

    def "GFlexData init test"() {
        given:
        ParamInitializer paramInitializer = new ParamInitializer()
        paramInitializer.generalRepository = persistenceService

        when:
        paramInitializer.init()
        println(paramInitializer.entityList)

        then:
        paramInitializer.entityList.size() == 3
    }
}
