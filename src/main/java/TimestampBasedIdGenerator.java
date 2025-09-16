import java.time.Instant;
import java.util.Objects;

public final class TimestampBasedIdGenerator {
    private final Instant basedEpoch;

    public TimestampBasedIdGenerator(final Instant basedEpoch) {
        Objects.requireNonNull(Objects.requireNonNull(basedEpoch));
        this.basedEpoch = basedEpoch;
    }

    public Long generate(final Instant baseTime) {
        return baseTime.toEpochMilli() - basedEpoch.toEpochMilli();
    }
}
