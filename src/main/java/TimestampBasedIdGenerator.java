import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

final class TimestampBasedIdGenerator {
    private static final IdComponent ID_COMPONENT = IdComponent.TIMESTAMP;

    private final Instant basedEpoch;

    public TimestampBasedIdGenerator(final Instant basedEpoch) {
        this.basedEpoch = Objects.requireNonNull(basedEpoch);
    }

    public static TimestampBasedIdGenerator referenceDateTime(final LocalDateTime basedDateTime) {
        final var basedEpoch = basedDateTime.toInstant(ZoneOffset.UTC);
        return new TimestampBasedIdGenerator(basedEpoch);
    }

    public long generate(final Instant currentInstant) {
        verifyEarlierThanBaseEpoch(currentInstant);
        final var timeDifferenceMillis = timeDifferenceMillis(currentInstant);
        verifyMaxUsePeriod(timeDifferenceMillis);
        return timeDifferenceMillis;
    }

    public long generate(final long rawTimestamp) {
        final var currentInstant = Instant.ofEpochMilli(rawTimestamp);
        return generate(currentInstant);
    }

    public long generate() {
        return generate(Instant.now());
    }

    private long timeDifferenceMillis(final Instant currentInstant) {
        return currentInstant.toEpochMilli() - basedEpoch.toEpochMilli();
    }

    private void verifyEarlierThanBaseEpoch(final Instant currentInstant) {
        if (currentInstant.isBefore(basedEpoch)) {
            throw new IllegalArgumentException("현재 시간은 기준 시간보다 이전일 수 없습니다.");
        }
    }

    private void verifyMaxUsePeriod(final long timeDifferenceMillis) {
        long maxUsePeriod = ID_COMPONENT.maxValue();

        if (timeDifferenceMillis > maxUsePeriod) {
            final var errorMessage = """
                    시간 차이 (%, d ms)가 최대 사용 기간 (%, d ms)을 초과했습니다.
                    """.formatted(timeDifferenceMillis, maxUsePeriod);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimestampBasedIdGenerator that = (TimestampBasedIdGenerator) o;
        return Objects.equals(basedEpoch, that.basedEpoch);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(basedEpoch);
    }
}
