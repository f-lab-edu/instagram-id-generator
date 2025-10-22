@FunctionalInterface
public interface ShardIdAllocator {
    long allocate(final long maxAllowedShardId);
}
