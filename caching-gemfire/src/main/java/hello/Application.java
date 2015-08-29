package hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.GemfireCacheManager;

import com.gemstone.gemfire.cache.Cache;

@SpringBootApplication
@EnableCaching
@EnableGemfireRepositories
public class Application implements CommandLineRunner {

        @Bean
        LookupService lookupService() {
            return new LookupService();
        }

        @Bean
        CacheFactoryBean cacheFactoryBean() {
            return new CacheFactoryBean();
        }

        @Bean
        LocalRegionFactoryBean<Integer, Integer> localRegionFactoryBean(final Cache cache) {
            LocalRegionFactoryBean<Integer, Integer> factory = new LocalRegionFactoryBean<>();
            factory.setCache(cache);
            factory.setName("hello");
            return factory;
        }

        @Bean
        GemfireCacheManager cacheManager(final Cache gemfireCache) {
            GemfireCacheManager manager = new GemfireCacheManager();
            manager.setCache(gemfireCache);
            return manager;
        }

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

        @Override
        public void run(String... args) throws Exception {
            lookupPageAndTimeIt(lookupService(), "SpringSource");
            lookupPageAndTimeIt(lookupService(), "SpringSource");
            lookupPageAndTimeIt(lookupService(), "PivotalSoftware");
        }

        private void lookupPageAndTimeIt(LookupService srv, String page) throws InterruptedException {
            long start = System.currentTimeMillis();
            Page results = srv.findPage(page);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("Found " + results + ", and it only took " + elapsed + " ms to find out!\n");
        }
}
