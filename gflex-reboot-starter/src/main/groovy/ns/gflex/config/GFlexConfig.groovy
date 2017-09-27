package ns.gflex.config

import flex.messaging.HttpFlexSession
import groovy.util.logging.Slf4j
import ns.gflex.util.Int2LongConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.core.convert.support.GenericConversionService
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

import javax.annotation.PostConstruct

@Configuration
@Slf4j
class GFlexConfig {
    //@Autowired
    //SessionFactory sessionFactory

    //OpenSessionInViewInterceptor暂时没用，如果需要再设置
    void addInterceptorsBak(InterceptorRegistry registry) {
        OpenSessionInViewInterceptor openSessionInViewInterceptor = new OpenSessionInViewInterceptor()
        openSessionInViewInterceptor.setSessionFactory(sessionFactory)
        registry.addWebRequestInterceptor(openSessionInViewInterceptor)
    }

    //@Bean
    HttpFlexSession httpFlexSession() {
        return new HttpFlexSession();
    }

    @PostConstruct
    void initConverters() {
        log.info("DefaultConversionService add Int2LongConverter")
        GenericConversionService conversionService = (GenericConversionService) DefaultConversionService.getSharedInstance();
        conversionService.addConverter(new Int2LongConverter());
    }

}