package ns.gflex.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@Entity
@ToString(includes = 'message')
@EqualsAndHashCode(includes = 'id')
class Log {
    String ownerId;
    String message;
    String ipAddress;
    String account
    Date dateCreated
    static mapping = {
        id generator: 'increment'
        autoTimestamp true
    }

    static constraints = {
        message maxSize: 1024
        ownerId nullable: true
        account nullable: true
    }
}