package com.hason.service;

import com.hason.vo.SnowFlakeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * <p>
 * Snowflake算法是带有时间戳的全局唯一ID生成算法。它有一套固定的ID格式，如下：
 * <p>
 * 41位的时间序列（精确到毫秒，41位的长度可以使用69年）
 * 10位的机器标识（10位的长度最多支持部署1024个节点）
 * 12位的Sequence序列号（12位的Sequence序列号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 *
 * 优点是：整体上按照时间自增排序，且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)
 * <br>
 * 缺点：
 * 1. 依赖计算机的时钟，不同计算机之间存在时间误差
 * 2. 需要依赖机器ID
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/3
 */
public class SnowFlakeWorker {

    private static final Logger logger = LoggerFactory.getLogger(SnowFlakeWorker.class);

    /** 算法的开始时间，基于 2017-09-01 开始 */
    private static final long START_TIME = 1504195200000L;

    /** 时间占的位数 */
    private static final long TIME_BITS = 41;
    /** 数据中心数量占的位数 */
    private static final long DATA_CENTER_BITS = 5;
    /** 计算机数量占的位数 */
    private static final long WORKER_BITS = 5;
    /** 序列占的位数 */
    private static final long SEQUENCE_BITS = 12;

    /** 时间的位偏移量 */
    private static final long TIME_BIT_OFFSET = DATA_CENTER_BITS + WORKER_BITS + SEQUENCE_BITS;
    /** 数据中心数量的位偏移量 */
    private static final long DATA_CENTER_BIT_OFFSET = WORKER_BITS + SEQUENCE_BITS;
    /** 计算机数量的位偏移量 */
    private static final long WORKER_BIT_OFFSET = SEQUENCE_BITS;

    /** 时间戳的最大数值: 31 */
    private static final long MAX_TIME = ~(-1L << TIME_BITS);
    /** 数据中心的最大数值: 31 */
    private static final long MAX_DATA_CENTER = ~(-1L << DATA_CENTER_BITS);
    /** 计算机的最大数值: 31 */
    private static final long MAX_WORKER = ~(-1L << WORKER_BITS);
    /** 序列号的最大数值: 4095 */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /** 最后获取ID时的时间戳 */
    private static volatile long lastTimestamp = -1L;
    /** 序列号 */
    private static volatile long sequence = 0L;

    private final long dataCenterId;
    private final long workerId;

    /**
     * SnowFlake算法构造器，指定数据中心和计算机ID，初始化算法
     *
     * @param dataCenterId 数据中心ID
     * @param workerId 计算机ID
     */
    public SnowFlakeWorker(long dataCenterId, long workerId) {
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER) {
            throw new IllegalArgumentException(String.format(
                    "DataCenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER));
        }
        if (workerId < 0 || workerId > MAX_WORKER) {
            throw new IllegalArgumentException(String.format(
                    "Worker Id can't be greater than %d or less than 0", MAX_WORKER));
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 生成ID（线程安全）
     *
     * @return id
     */
    public synchronized long nextId() {
        long timestamp = timestamp();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟被修改过，回退在上一次ID生成时间之前应当抛出异常！！！
        if (timestamp < lastTimestamp) {
            logger.error(String.format("Clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
            throw new IllegalStateException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果同一毫秒内并发获取ID，则递增计算序列号
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 如果序列号溢出（每毫秒并发数大于 4095），阻塞到下一毫秒获取时间戳
            if (sequence == 0) {
                timestamp = waitToNextMillis(lastTimestamp);
            }
        } else {  // 时间戳改变，重置序列号
            // 注意：如果分库分表，需要依赖ID，为避免分布不均衡，ID最后一位可以随机生成0-9
            sequence = 0L;
        }

        // 上一次生成ID的时间
        lastTimestamp = timestamp;

        return ((timestamp - START_TIME) << TIME_BIT_OFFSET)
                | (dataCenterId << DATA_CENTER_BIT_OFFSET)
                | (workerId << WORKER_BIT_OFFSET)
                | sequence;
    }

    /**
     * 解析ID
     *
     * @param id id
     * @return SnowFlakeId
     */
    public SnowFlakeId convert(long id) {
        SnowFlakeId snowFlakeId = new SnowFlakeId();
        snowFlakeId.setTimestamp(getTimestamp(id));
        snowFlakeId.setDataCenterId(getDataCenterId(id));
        snowFlakeId.setWorkerId(getWorkerId(id));
        snowFlakeId.setSequence(getSequence(id));
        return snowFlakeId;
    }

    /**
     * 从ID中解析出时间戳
     *
     * @param id id
     * @return 时间戳
     */
    public long getTimestamp(long id) {
        return (id >>> TIME_BIT_OFFSET & MAX_TIME) + START_TIME;
    }

    /**
     * 从ID中解析出数据中心ID
     *
     * @param id id
     * @return 数据中心ID
     */
    public long getDataCenterId(long id) {
        return (id >>> DATA_CENTER_BIT_OFFSET) & MAX_DATA_CENTER;
    }

    /**
     * 从ID中解析出计算机ID
     *
     * @param id id
     * @return 计算机ID
     */
    public long getWorkerId(long id) {
        return (id >>> WORKER_BIT_OFFSET) & MAX_WORKER;
    }

    /**
     * 从ID中解析出序列号
     *
     * @param id id
     * @return 序列号
     */
    public long getSequence(long id) {
        return id & MAX_SEQUENCE;
    }

    /**
     * 阻塞到 lastTimestamp 的下一毫秒
     *
     * @param lastTimestamp 阻塞的最后时间
     * @return timestamp
     */
    private long waitToNextMillis(long lastTimestamp) {
        long timestamp = timestamp();
        while (timestamp <= lastTimestamp)
            timestamp = timestamp();
        return timestamp;
    }

    /**
     * 获取时间戳
     */
    private long timestamp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlakeWorker snowFlakeWorker = new SnowFlakeWorker(0, 0);
        long length = 20 * 1;
        long start = System.currentTimeMillis();
        long end = start + length;
        long sum = 0;
        while (end > System.currentTimeMillis()) {
//            snowFlakeWorker.nextId();
            System.out.println(snowFlakeWorker.nextId());
            sum++;
        }
        System.out.println("一秒生成ID数量" + sum);
    }
}
