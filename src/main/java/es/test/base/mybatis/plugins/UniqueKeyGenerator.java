package es.test.base.mybatis.plugins;

import java.util.Random;

/**
 * @author 旺旺小学酥
 * @Time 2017/9/22
 */
public class UniqueKeyGenerator {
    private static final long datacenterIdShift = 10L;
    private static final long workerIdShift = 8L;
    private static final long timestampLeftShift = 14L;
    private static final long maxWorkerId = 4L;
    private static final long maxDatacenterId = 16L;
    private static final long sequenceMask = 4095L;
    private static final Random r = new Random();
    private final long workerId;
    private final long datacenterId;
    private final long idepoch;
    private final long defaultTimestamp;
    private long sequence;
    private long lastTimestamp;

    public UniqueKeyGenerator() {
        this(System.currentTimeMillis());
    }

    public UniqueKeyGenerator(final long idepoch) {
        this((long) r.nextInt(4), (long) r.nextInt(16), 0L, idepoch);
    }

    public UniqueKeyGenerator(final long workerId, final long datacenterId) {
        this(workerId, datacenterId, 0L, System.currentTimeMillis());
    }

    public UniqueKeyGenerator(final long workerId, final long datacenterId, final long sequence) {
        this(workerId, datacenterId, sequence, System.currentTimeMillis());
    }

    public UniqueKeyGenerator(final long workerId, final long datacenterId, final long sequence, final long idepoch) {
        this.lastTimestamp = -1L;
        this.defaultTimestamp = 1490945276722L;
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
        this.idepoch = idepoch;
        if (workerId >= 0L && workerId <= maxWorkerId) {
            if (datacenterId < 0L || datacenterId > maxDatacenterId) {
                throw new IllegalArgumentException(
                    String.format("非法datacenterId 节点(5位)应大于0而小于%d，而当前值为:%d ", maxDatacenterId, datacenterId));
            }
        } else {
            throw new IllegalArgumentException(
                String.format("非法workerId 数据中心(5位)应大于0而小于%d，而当前值为:%d", maxWorkerId, workerId));
        }
    }

    public long getDatacenterId() {
        return this.datacenterId;
    }

    public long getWorkerId() {
        return this.workerId;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public long getId() {
        return this.nextId();
    }

    public void setLastTimestamp(final long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    private synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new IllegalStateException(String.format("时间早于最低时间:%d【%d】", this.lastTimestamp, timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & sequenceMask;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            //            long id = timestamp - this.defaultTimestamp << 22 | this.datacenterId << 17 | this.workerId
            // << 12 | this.sequence;
            return timestamp - this.defaultTimestamp << timestampLeftShift | this.datacenterId << datacenterIdShift
                   | this.workerId << workerIdShift | this.sequence;
        }
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UniqueKeyGenerator{");
        sb.append("workerId=").append(this.workerId);
        sb.append(", datacenterId=").append(this.datacenterId);
        sb.append(", idepoch=").append(this.idepoch);
        sb.append(", lastTimestamp=").append(this.lastTimestamp);
        sb.append(", sequence=").append(this.sequence);
        sb.append('}');
        return sb.toString();
    }
}
