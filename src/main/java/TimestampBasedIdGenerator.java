import java.time.Instant;
import java.util.Objects;

public final class TimestampBasedIdGenerator {
    private final Instant basedEpoch;

    public TimestampBasedIdGenerator(Instant basedEpoch) {
        Objects.requireNonNull(Objects.requireNonNull(basedEpoch));
        this.basedEpoch = basedEpoch;
    }
}
