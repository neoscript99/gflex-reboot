package ns.gflex

import org.apache.commons.cli.Option
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
class GflexBootApplication {
    @Autowired
    ApplicationArguments applicationArguments
    CliBuilder cliBuilder = new CliBuilder(usage: this.class.name)

    GflexBootApplication() {
        buildCliOptions()
    }

    static void main(String[] args) {
        new GflexBootApplication().run(args);
    }

    void run(String[] args) {
        OptionAccessor optionAccessor = cliBuilder.parse(args)
        if (optionAccessor.help)
            cliBuilder.usage()
        else
            SpringApplication.run(this.class, args);
    }


    void buildCliOptions() {
        cliBuilder.with {
            _(longOpt: 'init', args: Option.UNLIMITED_VALUES, optionalArg: true, argName: '0~n个初始化方案', '传入本参数，执行初始化，同时如果传入profiles执行对应的多套初始化方案，[default]方案默认执行')
            _(longOpt: 'help', args: 0, '显示命令行帮助')
        }
    }

    /**
     * spring bean通过autowire获取args
     * @return
     */
    @Bean
    OptionAccessor optionAccessor() {
        cliBuilder.parse(applicationArguments.sourceArgs)
    }
}
