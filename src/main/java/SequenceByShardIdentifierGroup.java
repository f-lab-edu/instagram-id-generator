import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public record SequenceByShardIdentifierGroup(
        Map<Long, SequenceByTimestamp> value
) {
    private static final long SEQUENCE_INITIALIZE = 0L;
    private static final long TIMESTAMP_INITIALIZE = -1L;

    public static SequenceByShardIdentifierGroup from(final long instanceIdentifierCount) {
        return new SequenceByShardIdentifierGroup(
                sequenceByShardIdentifierGroupValue(instanceIdentifierCount)
        );
    }

    private static ConcurrentHashMap<Long, SequenceByTimestamp> sequenceByShardIdentifierGroupValue(
            final long instanceIdentifierCount) {
        return LongStream.range(0, instanceIdentifierCount)
                .boxed()
                .collect(Collectors.toMap(
                        i -> i,
                        i -> SequenceByTimestamp.initial(),
                        (existing, replacement) -> existing,
                        ConcurrentHashMap::new
                ));
    }

    public long sequence(final long timestamp, long instanceIdentifier) {
        if (!value.containsKey(instanceIdentifier)) {
            throw new IllegalArgumentException("인스턴스 식별자가 포함되어 있지 않습니다.");
        }

        var sequenceByTimestamp = value.get(instanceIdentifier);
        return sequenceByTimestamp.sequence(timestamp);
    }

    private record SequenceByTimestamp(
            AtomicLong lastTimestampValue,
            AtomicLong sequenceValue
    ) {
        public static SequenceByTimestamp initial() {
            return new SequenceByTimestamp(
                    new AtomicLong(TIMESTAMP_INITIALIZE),
                    new AtomicLong(SEQUENCE_INITIALIZE)
            );
        }

        public long sequence(final long timestamp) {
            final long lastTimestamp = lastTimestampValue.get();
            verifyPastTimestamp(lastTimestamp, timestamp);
            if (lastTimestamp < timestamp) {
                this.initial(timestamp);
            }
            return sequenceValue.incrementAndGet();
        }

        private void verifyPastTimestamp(long lastTimestamp, long timestamp) {
            if (lastTimestamp > timestamp) {
                throw new IllegalArgumentException("과거의 시간대입니다.");
            }
        }

        private void initial(final long timestamp) {
            lastTimestampValue.set(timestamp);
            sequenceValue.set(SEQUENCE_INITIALIZE);
        }
    }
}
