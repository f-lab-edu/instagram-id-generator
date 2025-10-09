import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdGeneratorTest {
    @Test
    @DisplayName("아이디 생성")
    void id_generator() {
        final var timestampBasedIdGenerator = TimestampBasedIdGenerator.referenceDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        final var shardIdGenerator = ShardIdGenerator.from(1);
        final var sequenceIdGenerator = SequenceIdGenerator.from(1);
        final var timestamp = instant(LocalDateTime.of(2025, 2, 1, 0, 0, 0));

        final var sut = new IdGenerator(timestampBasedIdGenerator,  shardIdGenerator, sequenceIdGenerator);

        assertThat(sut.generateId(timestamp)).isEqualTo(InstagramId.from(22468047667201025L));
    }

    private Instant instant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.UTC).toInstant();
    }
}
