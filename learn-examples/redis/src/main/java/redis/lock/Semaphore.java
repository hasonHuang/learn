package redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.ZParams;

import java.util.List;
import java.util.UUID;

/**
 * 计数信号量：限制一项资源能同时被多少个进程访问
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/4/14
 */
public class Semaphore {

    private static final String PREFIX = "semaphore:";
    private static final String OWNER = ":owner";
    private static final String COUNTER = ":counter";

    /**
     * 简单的计数信号量（不公平的）
     *
     * 优点：简单，运行速度快
     * 缺点：每个进程的系统时间必须相同，信号量的数量偶尔超过限制
     *
     * 步骤：
     * 1.清除过期的信号量
     * 2.生成token放进信号量集合，当前时间戳作为分值
     * 3.获取token在集合中的排名：若排名低于可获取的信号量总数（排名由0开始），表示申请成功，否则失败
     *
     * @param jedis Redis 客户端
     * @param name 被锁对象
     * @param limit （可申请的）信号量总数
     * @param lockTimeout 信号量的有效时长，毫秒
     * @return 成功时返回 UUID，失败时返回 null
     */
    public String acquireSimple(Jedis jedis, String name, int limit, long lockTimeout) {
        // 获取存放锁的集合
        String lockName = PREFIX + name;
        // 生成锁令牌
        String token = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();

        // 使用事务进行操作，保证一致性
        Transaction transaction = jedis.multi();
        // 清除过期的锁，最小有效期 = now - lockTimeout
        transaction.zremrangeByScore(lockName, "-inf", Double.toString(now - lockTimeout));
        // 添加新的锁令牌到集合中
        transaction.zadd(lockName, now, token);
        // 获取新锁的排名(由0开始)，只有排名少于 limit 时表示未超出锁数量上限
        transaction.zrank(lockName, token);
        List<Object> resultList = transaction.exec();

        int rank = ((Long) resultList.get(resultList.size() - 1)).intValue();
        if (rank < limit) {
            return token;  // 获取成功
        }

        // 获取失败。删除锁令牌
        jedis.zrem(lockName, token);
        return null;
    }

    /**
     * 释放简单信号量
     *
     * @param jedis Redis 客户端
     * @param name 被锁对象
     * @param token 信号量令牌，获取信号量时返回的内容
     * @return true 成功，false 失败
     */
    public boolean releaseSimple(Jedis jedis, String name, String token) {
        return jedis.zrem(PREFIX + name, token) == 1;
    }

    /**
     * 公平信号量
     *
     * 优点：主机间无需拥有相同时间（误差可在一两秒内）
     * 缺点：主机间的时间误差会导致信号量过早释放或太晚释放（需控制在一两秒内），信号量的数量偶尔超过限制
     *
     * 超时集合：记录信号量与获取时间
     * 计数器：  记录获取信号量的编号，每次获取时执行自增
     * 信号量拥有者集合： 记录信号量与计数（ID编号）
     *
     * 步骤：
     * 1. 在超时集合中的删除过期的信号量
     * 2. 对超时集合和信号量拥有者集合执行交集，结果覆盖到信号量拥有者集合中
     * 3. 计数器自增；把结果和新的信号量放到信号量拥有者集合中，同时把当前时间戳和新的信号量放到超时集合中
     * 4. 检查新的信号量在 信号量拥有者集合 中的排名，若排名足够低（排名由0开始），表示获取成功；否则失败
     *
     * @param jedis Redis客户端
     * @param name 被锁对象
     * @param limit （可申请的）信号量总数
     * @param lockTimeout 信号量的有效时长，毫秒
     * @return
     */
    public String acquireFair(Jedis jedis, String name, int limit, long lockTimeout) {
        String semaphoreName = PREFIX + name;  // 超时集合
        String semaphoreOwner = PREFIX + name + OWNER;  // 信号量拥有者
        String semaphoreCounter = PREFIX + name + COUNTER;  // 计数器
        String token = UUID.randomUUID().toString();  // 信号量令牌，代表一个信号量
        long now = System.currentTimeMillis();

        // 移除过期的信号量
        Transaction transaction = jedis.multi();  // 保证原子性
        transaction.zremrangeByScore(semaphoreName, "-inf", String.valueOf(now - lockTimeout));
        // 交集的结果集的聚合方式：默认 SUM
        // 交集运算前，分别对超时集合、拥有者集合的分值做乘法（为了不改变拥有者集合的分数值）
        ZParams params = new ZParams();
        params.weightsByDouble(1, 0);
        // 交集结果覆盖到信号量拥有者集合
        transaction.zinterstore(semaphoreOwner, params, semaphoreOwner, semaphoreName);
        // 计数器自增
        transaction.incr(semaphoreCounter);

        // 获取信号量
        List<Object> execList = transaction.exec();  // 为了得到计数器结果，执行事务
        Long counterNum = (Long) execList.get(execList.size() - 1);
        transaction = jedis.multi();
        transaction.zadd(semaphoreName, now, token);
        transaction.zadd(semaphoreOwner, counterNum, token);
        transaction.zrank(semaphoreOwner, token);  // 获取新的信号量的排名

        // 检查是否成功获取信号量
        execList = transaction.exec();
        Long rank = (Long) execList.get(execList.size() - 1);
        if (rank < limit) {
            return token;  // 排名足够低时表示获取成功（排名由 0 开始）
        }

        // 获取失败，移除这次添加的数据
        transaction = jedis.multi();
        transaction.zrem(semaphoreName,token);
        transaction.zrem(semaphoreOwner, token);
        transaction.exec();

        return null;
    }

    /**
     * 取消竞争条件的公平信号量。任何时候都应该使用这种信号量
     *
     * 优点：保证信号量的数量不会超过限制
     *
     * @param jedis Redis 客户端
     * @param name 被锁对象
     * @param limit 信号量的总数
     * @param lockTimeout 信号量的有效时长，毫秒
     * @return 成功时返回 UUID，失败时返回 null
     */

    public String acquireFairWithLock(
            Jedis jedis, String name, int limit, long lockTimeout) {
        // 获取信号量前，执行锁定操作，每个时刻只能有一个客户端获取信号量
        String token = DistributedLock.acquire(jedis, name, 1);
        if (token != null) {
            try {
                // 获取信号量
                return acquireFair(jedis, name, limit, lockTimeout);
            } finally {
                // 解除锁定
                DistributedLock.release(jedis, name, token);
            }
        }
        // 获取信号量失败
        return null;
    }

    /**
     * 释放公平信号量
     *
     * @param jedis Redis 客户端
     * @param name 被锁对象
     * @param token 信号量令牌，获取信号量时返回的内容
     * @return true 成功，false 失败
     */
    public boolean releaseFair(Jedis jedis, String name, String token) {
        Transaction transaction = jedis.multi();
        transaction.zrem(PREFIX + name, token);  // 超时集合
        transaction.zrem(PREFIX + name + OWNER, token);  // 信号量拥有者
        List<Object> execList = transaction.exec();
        return (Long) execList.get(execList.size() - 1) == 1;
    }

    /**
     * 刷新信号量（延长信号量有效期）
     *
     * @param jedis Redis客户端
     * @param name 被锁对象
     * @param token 信号量对应的令牌，获取信号量时的返回值
     * @return true 成功，false 失败
     */
    public boolean refreshFair(Jedis jedis, String name, String token) {
        long now = System.currentTimeMillis();
        // 新增时返回 1； 更新时返回 0
        Long result = jedis.zadd(PREFIX + name, now, token);
        // 如果是新增，表示该信号量已经过期被清除掉，告知客户端已失去信号量
        if (result == 1) {
            releaseFair(jedis, name, token);
            return false;
        }
        // 刷新成功
        return true;
    }
}
