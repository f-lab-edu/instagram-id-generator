public final class ShardIdGenerator {
    private static final IdComponent ID_COMPONENT = IdComponent.SHARD;

    private final ShardIdAllocator shardIdAllocator;

    public ShardIdGenerator(final ShardIdAllocator shardIdAllocator) {
        this.shardIdAllocator = shardIdAllocator;
    }

    public long generate() {
        var result = shardIdAllocator.allocate(ID_COMPONENT.maxValue());
        verifyShardIdRange(result);
        return result;
    }

    public long allocatedBits() {
        return ID_COMPONENT.bitLength();
    }

    private void verifyShardIdRange(final long shardId) {
        long maxShardId = ID_COMPONENT.maxValue();
        if (shardId < 0 || shardId > maxShardId) {
            final var errorMessage = """
                    샤드 ID(%d)는 0부터 %d 사이의 값이어야 한다
                    """.formatted(shardId, maxShardId);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
