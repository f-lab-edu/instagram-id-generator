import java.util.concurrent.atomic.AtomicReference;

public final class InstagramId {
    private final long timestamp;
    private final long shardId;
    private final long sequence;
    private final int shardIdBits;
    private final int sequenceBits;
    private final AtomicReference<Long> idValue = new AtomicReference<>();

    private InstagramId(
            final long timestamp,
            final long shardId,
            final long sequence,
            final int shardIdBits,
            final int sequenceBits
    ) {
        this.timestamp = timestamp;
        this.shardId = shardId;
        this.sequence = sequence;
        this.shardIdBits = shardIdBits;
        this.sequenceBits = sequenceBits;
    }

    public static InstagramId of(
            final long timestamp,
            final long shardId,
            final long sequence,
            final int shardIdBits,
            final int sequenceBits
    ) {
        return new InstagramId(timestamp, shardId, sequence, shardIdBits, sequenceBits);
    }

    public long idValue() {
        Long result = idValue.get();
        if (result == null) {
            result = combine(timestamp, shardId, sequence, shardIdBits, sequenceBits);
            idValue.compareAndSet(null, result);
            result = idValue.get();
        }
        return result;
    }

    public static long makeRawId(
            final long timestamp,
            final long shardId,
            final long sequence,
            final int shardIdBits,
            final int sequenceBits
    ) {
        return combine(timestamp, shardId, sequence, shardIdBits, sequenceBits);
    }

    private static long combine(
            final long timestamp,
            final long shardId,
            final long sequence,
            final int shardIdBits,
            final int sequenceBits
    ) {
        final int timestampShift = shardIdBits + sequenceBits;

        var id = timestamp << timestampShift;
        id |= shardId << sequenceBits;
        id |= sequence;
        return id;
    }
}
