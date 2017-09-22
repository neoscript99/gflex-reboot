package ns.gflex.config.data

import ns.gflex.domain.Department
import spock.lang.Specification

/**
 * Created by Neo on 2017-09-22.
 */
class InitializeDomianSpec extends Specification {
    def 'get value'() {
        given:
        Class aClass = Department

        when:
        InitializeDomian initializeDomian = Department.getAnnotation(InitializeDomian)
        println initializeDomian.value()

        then:
        true
    }
}
