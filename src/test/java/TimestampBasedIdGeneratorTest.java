import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimestampBasedIdGeneratorTest {

    @Test
    void null_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> new TimestampBasedIdGenerator(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 기준시간에_ID값을_생성할_경우_0이_생성된다() {
        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var id = sut.generate(basedEpoch);
        assertThat(id).isZero();
    }

    @Test
    void 기준시간으로부터_1s_후_ID는_1000ms다() {
        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var basedEpochAfterOneSecond = basedEpoch.plusSeconds(1);
        final var id = sut.generate(basedEpochAfterOneSecond);

        assertThat(id).isEqualTo(1000L);
    }
}
