package top.lzxu.javadb.backend.common;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import top.lzxu.javadb.backend.utils.Panic;
import top.lzxu.javadb.common.Error;

public class CacheTest {

    static Random random = new SecureRandom();

    private CountDownLatch cdl;
    private top.guoziyang.mydb.backend.common.MockCache cache;
    
    @Test
    public void testCache() {
        cache = new top.guoziyang.mydb.backend.common.MockCache();
        cdl = new CountDownLatch(200);
        for(int i = 0; i < 200; i ++) {
            Runnable r = () -> work();
            new Thread(r).run();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void work() {
        for(int i = 0; i < 1000; i++) {
            long uid = random.nextInt();
            long h = 0;
            try {
                h = cache.get(uid);
            } catch (Exception e) {
                if(e == Error.CacheFullException) continue;
                Panic.panic(e);
            }
            assert h == uid;
            cache.release(h);
        }
        cdl.countDown();
    }
}
