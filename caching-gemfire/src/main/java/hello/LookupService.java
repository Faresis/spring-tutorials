package hello;

import org.springframework.cache.annotation.Cacheable;

public class LookupService {
    @Cacheable("hello")
    public Page findPage(String page) throws InterruptedException {
        Thread.sleep(3000);
        Page result = new Page();
        result.setName(page);
        result.setWebsite("http://" + page + ".com");
        return result;
    }
}
