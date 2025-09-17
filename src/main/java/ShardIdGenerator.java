public final class ShardIdGenerator {
    private static final long SHARD_ID_BITS = 13L;
    private static final long MAX_SHARD_ID = (1L << SHARD_ID_BITS) - 1;

    private final int instanceIdentifier;

    private ShardIdGenerator(final int instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public static ShardIdGenerator from(final int instanceIdentifier) {
        verifyShardIdRange(instanceIdentifier);
        return new ShardIdGenerator(instanceIdentifier);
    }

    private static void verifyShardIdRange(final int shardId) {
        if (shardId < 0 || shardId > MAX_SHARD_ID) {
            final var errorMessage = """
                    샤드 ID(%d)는 0부터 %d 사이의 값이어야 한다
                    """.formatted(shardId, MAX_SHARD_ID);
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
