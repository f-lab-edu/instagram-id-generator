import java.time.Instant;
import java.util.Objects;

public final class TimestampBasedIdGenerator {
    private static final long TIMESTAMP_BITS = 41L;
    private static final long MAX_USE_PERIOD_TIME_MILLIS = (1L << TIMESTAMP_BITS) - 1;

    private final Instant basedEpoch;

    public TimestampBasedIdGenerator(final Instant basedEpoch) {
        this.basedEpoch = Objects.requireNonNull(basedEpoch);
    }

    public long generate(final Instant currentInstant) {
        verifyEarlierThanBaseEpoch(currentInstant);
        final var timeDifferenceMillis = timeDifferenceMillis(currentInstant);
        verifyMaxUsePeriod(timeDifferenceMillis);
        return timeDifferenceMillis;
    }

    private long timeDifferenceMillis(final Instant currentInstant) {
        return currentInstant.toEpochMilli() - basedEpoch.toEpochMilli();
    }

    private void verifyEarlierThanBaseEpoch(final Instant currentInstant) {
        if (currentInstant.isBefore(basedEpoch)) {
            throw new IllegalArgumentException("현재 시간은 기준 시간보다 이전일 수 없습니다.");
        }
    }

    private void verifyMaxUsePeriod(long timeDifferenceMillis) {
        if (timeDifferenceMillis > MAX_USE_PERIOD_TIME_MILLIS) {
            final var errorMessage = """
                    시간 차이 (%, d ms)가 최대 사용 기간 (%, d ms)을 초과했습니다.
                    """.formatted(timeDifferenceMillis, MAX_USE_PERIOD_TIME_MILLIS);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
