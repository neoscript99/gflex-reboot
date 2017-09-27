package ns.gflex.util

import ns.gflex.config.initialize.DataInitializer
import ns.gflex.repositories.GeneralRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.type.ClassMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.util.ClassUtils
import org.springframework.util.SystemPropertyUtils

/**
 * 使用spring工具类扫描包，运行实现了DataInitializer接口的init方法，进行数据初始化
 * Created by Neo on 2017-08-22.
 */
class InitializerUtil {
    static private Logger log = LoggerFactory.getLogger(InitializerUtil.class);

    static void doInit(GeneralRepository generalRepository, String basePackage, String pattern = "**/*.class") {

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + pattern;
        try {
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            List<Class<DataInitializer>> initClasses = []
            for (Resource resource : resources) {
                ClassMetadata metadata = metadataReaderFactory.getMetadataReader(resource).getClassMetadata()
                if (metadata && metadata.isConcrete()
                        && metadata.getInterfaceNames().contains('ns.gflex.config.initialize.DataInitializer')) {
                    String cls = metadata.getClassName()
                    initClasses << Class.forName(cls)
                }
            }
            initClasses.sort(AnnotationAwareOrderComparator.INSTANCE)
            initClasses.each {
                log.info("data initialize by {}", it)
                DataInitializer dataInitializer = it.newInstance()
                dataInitializer.generalRepository = generalRepository
                dataInitializer.init()
            }
        } catch (Exception e) {
            log.error("doInit失败", e);
        }
    }
}
