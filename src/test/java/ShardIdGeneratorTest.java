import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ShardIdGeneratorTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 8191})
    void generate_does_not_throw_when_id_within_range(final int instanceIdentifier) {
        assertThatCode(() -> new ShardIdGenerator(max -> instanceIdentifier).generate())
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("샤드 ID가 경계를 벗어나면 예외가 발생한다")
    @ValueSource(ints = {-1, 8192})
    void throw_exception_if_shard_id_exceed_boundary(final int shardId) {
        final var generator = new ShardIdGenerator(max -> shardId);

        assertThatThrownBy(generator::generate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("샤드 ID");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 8191})
    void generate_returns_supplied_value(final int instanceIdentifier) {
        final var generator = new ShardIdGenerator(max -> instanceIdentifier);

        assertThat(generator.generate()).isEqualTo(instanceIdentifier);
    }
}
