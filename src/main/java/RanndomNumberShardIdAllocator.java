import java.util.concurrent.ThreadLocalRandom;

class RanndomNumberShardIdAllocator implements ShardIdAllocator {
    @Override
    public long allocate(final long maxAllowedShardId) {
        return ThreadLocalRandom.current().nextLong(maxAllowedShardId);
    }
}
