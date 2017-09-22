package ns.gflex.config.data

/**
 * 根据静态list进行初始化
 * <p>满足如下条件进行初始化：
 * <ul>
 *     <li>如果domain类标记了InitializeDomian
 *     <li>且value对应的静态变量存在，并为list
 *     <li>数据库中不存在任何记录
 * </ul>
 * Created by Neo on 2017-09-22.
 */
@interface InitializeDomian {
    String value() default 'initList';
}
