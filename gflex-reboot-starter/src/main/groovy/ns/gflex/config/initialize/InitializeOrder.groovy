package ns.gflex.config.initialize

/**
 * Created by Neo on 2017-09-28.
 */
interface InitializeOrder {
    int DOMAIN_INIT = 0
    int AFTER_DOMAIN = 100
    int BEFORE_GFLEX = 200
    int GFLEX_INIT = 300
    int AFTER_GFLEX = 400
}
