grails.gorm.default.mapping = {
    //cache true
    id generator: 'uuid'
    autoTimestamp true
    /* version 为true时，自己new的domian类不能update，只能insert，因为version值不知道
     * 一般在高并发时使用version*/
    version false
}

//maxSize : 32在数据库中表现为字符长度，不是字节，如oracle VARCHAR2(32 CHAR)
grails.gorm.default.constraints = {
    '*'(maxSize: 32, blank: false)
}