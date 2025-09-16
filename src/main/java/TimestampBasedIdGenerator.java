import java.time.Instant;
import java.util.Objects;

public final class TimestampBasedIdGenerator {
    private final Instant basedEpoch;

    public TimestampBasedIdGenerator(final Instant basedEpoch) {
        Objects.requireNonNull(Objects.requireNonNull(basedEpoch));
        this.basedEpoch = basedEpoch;
    }

    public long generate(final Instant currentInstant) {
        verifyEarlierThanBaseEpoch(currentInstant);
        return timeDifferenceMillis(currentInstant);
    }

    private long timeDifferenceMillis(final Instant currentInstant) {
        return currentInstant.toEpochMilli() - basedEpoch.toEpochMilli();
    }

    private void verifyEarlierThanBaseEpoch(final Instant currentInstant) {
        if (currentInstant.isBefore(basedEpoch)) {
            throw new IllegalArgumentException("현재 시간은 기준 시간보다 이전일 수 없습니다.");
        }
    }
}
