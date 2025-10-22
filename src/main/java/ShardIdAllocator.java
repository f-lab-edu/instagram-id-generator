@FunctionalInterface
interface ShardIdAllocator {
    long allocate(final long maxAllowedShardId);
}
