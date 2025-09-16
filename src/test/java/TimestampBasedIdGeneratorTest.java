import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    @Test
    void ID생성_시점을_연속된_시간에는_ID값이_단조_증가한다() {
        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var id1 = sut.generate(basedEpoch.plusMillis(100));
        final var id2 = sut.generate(basedEpoch.plusMillis(200));
        final var id3 = sut.generate(basedEpoch.plusMillis(300));

        assertThat(id1).isLessThan(id2);
        assertThat(id2).isLessThan(id3);
    }

    @Test
    void ID_생성시점이_기준시점_보다_이전일경우_예외를_발생시킨다() {
        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var basedEpochBefore = basedEpoch.minusSeconds(1);

        assertThatThrownBy(() -> sut.generate(basedEpochBefore))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 시간은 기준 시간보다 이전일 수 없습니다.");
    }

    @Test
    void ID값_생성은_최대_41비트다() {
        final var maxBits = 41;
        final var maxDifferenceMillis = (1L << maxBits) - 1;

        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var basedEpochAfterMax = basedEpoch.plusMillis(maxDifferenceMillis);
        final var id = sut.generate(basedEpochAfterMax);

        assertThat(id).isEqualTo(maxDifferenceMillis);
    }

    @Test
    void 최대_사용기간_초과시_예외를_발생시킨다() {
        final var maxBits = 41;
        final var maxDifferenceMillis = (1L << maxBits) - 1;

        final var basedEpoch = Instant.parse("2025-01-01T00:00:00Z");
        final var sut = new TimestampBasedIdGenerator(basedEpoch);

        final var overMaxDifferenceMillis = basedEpoch.plusMillis(maxDifferenceMillis + 1);

        assertThatThrownBy(() -> sut.generate(overMaxDifferenceMillis))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시간 차이")
                .hasMessageContaining("최대 사용 기간")
                .hasMessageContaining("초과");
    }

    @Test
    void 기준_시간은_LocalDateTime으로_설정할_수_있다() {
        final var basedDateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        final var basedEpoch = basedDateTime.toInstant(ZoneOffset.UTC);
        final var sut = TimestampBasedIdGenerator.referenceDateTime(basedDateTime);

        var target = new TimestampBasedIdGenerator(basedEpoch);

        assertThat(sut).isEqualTo(target);
    }
}
