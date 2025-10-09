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
        final var sequenceId = sequenceIdGenerator.generate(timestamp.toEpochMilli(), shardId);
        return InstagramId.of(
                timestampId,
                shardId,
                sequenceId,
                (int) shardIdGenerator.allocatedBits(),
                (int) sequenceIdGenerator.allocatedBits()
        );
    }

    public long makeRawId(long rawTimestamp, long shard, long sequence) {
        final var timestampId = timestampBasedIdGenerator.generate(rawTimestamp);
        return InstagramId.makeRawId(
                timestampId,
                shard,
                sequence,
                (int) shardIdGenerator.allocatedBits(),
                (int) sequenceIdGenerator.allocatedBits()
        );
    }
}
