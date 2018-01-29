package ns.gflex.config

import groovy.util.logging.Slf4j
import ns.gflex.util.Int2LongConverter
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.core.convert.support.GenericConversionService

import javax.annotation.PostConstruct

@Configuration
@Slf4j
class GFlexConfig {


    @PostConstruct
    void initConverters() {
        log.info("DefaultConversionService add Int2LongConverter")
        GenericConversionService conversionService = (GenericConversionService) DefaultConversionService.getSharedInstance();
        conversionService.addConverter(new Int2LongConverter());
    }

}