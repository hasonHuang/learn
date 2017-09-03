package com.hason;

import com.hason.service.SnowFlakeWorker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SnowFlakeTest {

    private SnowFlakeWorker snowFlakeWorker;

    @Before
    public void init() {
        snowFlakeWorker = new SnowFlakeWorker(0, 1);
    }

    // 生成ID的唯一性
    @Test
    public void testNextId() {
        int length = 2_000_000;
        Set<Long> set = new HashSet<>(length);
        int i = 0;
        long start = System.currentTimeMillis();
        while (i++ < length) {
            set.add(snowFlakeWorker.nextId());
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("总共生成ID数量：%d", set.size()));
        System.out.println(String.format("耗时：%d ms", (end - start)));
        Assert.assertTrue("ID出现重复", set.size() == length);
    }

    // 测试生成ID的速度
    @Test
    public void testSpeed() {
        int count = 0;
        while (count++ < 10) {
            long length = 1000 * 1;
            long start = System.currentTimeMillis();
            long end = start + length;
            long sum = 0;
            while (end > System.currentTimeMillis()) {
                snowFlakeWorker.nextId();
                sum++;
            }
            System.out.println("一秒生成ID数量" + sum);
        }
    }

    // 解析ID的时间戳
    @Test
    public void testConvertTimestamp() {
        long id = snowFlakeWorker.nextId();
        System.out.println("id: " + id);
        System.out.println("时间戳： " + snowFlakeWorker.getTimestamp(id));
        System.out.println("数据中心ID： " + snowFlakeWorker.getDataCenterId(id));
        System.out.println("计算机ID： " + snowFlakeWorker.getWorkerId(id));
        System.out.println("序列号： " + snowFlakeWorker.getSequence(id));
    }

}
