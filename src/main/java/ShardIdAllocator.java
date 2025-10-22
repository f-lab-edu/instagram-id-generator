@FunctionalInterface
public interface ShardIdAllocator {
    long allocate(final int maxAllowedShardId);
}
