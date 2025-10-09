import java.time.Instant;

public class IdGenerator {
    private final TimestampBasedIdGenerator timestampBasedIdGenerator;
    private final ShardIdGenerator shardIdGenerator;
    private final SequenceIdGenerator sequenceIdGenerator;

    public IdGenerator(
            final TimestampBasedIdGenerator timestampBasedIdGenerator,
            final ShardIdGenerator shardIdGenerator,
            final SequenceIdGenerator sequenceIdGenerator
    ) {
        this.timestampBasedIdGenerator = timestampBasedIdGenerator;
        this.shardIdGenerator = shardIdGenerator;
        this.sequenceIdGenerator = sequenceIdGenerator;
    }

    public InstagramId generateId(final Instant timestamp) {
        final var timestampId = timestampBasedIdGenerator.generate(timestamp);
        final var shardId = shardIdGenerator.generate();
        final var sequenceId = sequenceIdGenerator.generate(timestamp.toEpochMilli(), shardId - 1);
        return InstagramId.from(combine(timestampId, shardId, sequenceId));
    }

    public long makeRawId(long rawTimestamp, long shard, long sequence) {
        final var timestampId = timestampBasedIdGenerator.generate(rawTimestamp);
        return combine(timestampId, shard, sequence);
    }

    private long combine(final long timestampId, final long shardId, final long sequenceId) {
        final var allocatedShardIdBits = shardIdGenerator.allocatedBits();
        final var allocatedSequenceIdBits = sequenceIdGenerator.allocatedBits();

        var id = timestampId << (allocatedShardIdBits + allocatedSequenceIdBits);
        id |= shardId << allocatedSequenceIdBits;
        id |= sequenceId;
        return id;
    }
}
