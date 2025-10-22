public final class ShardIdGenerator {
    private static final int SHARD_ID_BITS = 13;
    private static final int MAX_SHARD_ID = (1 << SHARD_ID_BITS) - 1;

    private final ShardIdAllocator shardIdAllocator;

    public ShardIdGenerator(final ShardIdAllocator shardIdAllocator) {
        this.shardIdAllocator = shardIdAllocator;
    }

    public long generate() {
        var result = shardIdAllocator.allocate(MAX_SHARD_ID);
        verifyShardIdRange(result);
        return result;
    }

    public long allocatedBits() {
        return SHARD_ID_BITS;
    }

    private void verifyShardIdRange(final long shardId) {
        if (shardId < 0 || shardId > MAX_SHARD_ID) {
            final var errorMessage = """
                    샤드 ID(%d)는 0부터 %d 사이의 값이어야 한다
                    """.formatted(shardId, MAX_SHARD_ID);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
