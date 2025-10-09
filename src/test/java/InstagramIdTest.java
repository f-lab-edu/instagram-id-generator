import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class InstagramIdTest {
    @Test
    @DisplayName("id를 호출하면 비트 연산으로 조합된 ID를 반환한다")
    void id_combined_value() {
        final var timestamp = 2678400000L;
        final var shardId = 1L;
        final var sequence = 1L;

        final var id = InstagramId.of(timestamp, shardId, sequence, 13, 10);

        assertThat(id.idValue()).isEqualTo(22468047667201025L);
    }

    @Test
    @DisplayName("timestamp, shardId, sequence를 비트 연산하여 raw ID를 반환한다")
    void generator_id_as_raw_id() {
        final var timestamp = 2678400000L;
        final var shardId = 1L;
        final var sequence = 1L;

        final var result = InstagramId.makeRawId(timestamp, shardId, sequence, 13, 10);

        assertThat(result).isEqualTo(22468047667201025L);
    }

}
