import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SequenceIdGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1023})
    @DisplayName("10비트 범위 이내의 인스턴스 식별자 주입시 예외가 발생하지 않는다")
    void initialize_not_throws_exception(final int instanceIdentifierCount) {
        assertThatCode(() -> SequenceIdGenerator.from(instanceIdentifierCount))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1024})
    @DisplayName("10비트 범위 이외의 인스턴스 식별자 주입시 예외가 발생한다")
    void initialize_throws_exception(final int instanceIdentifierCount) {
        assertThatThrownBy(() -> SequenceIdGenerator.from(instanceIdentifierCount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인스턴스 식별자는 10비트 이내이어야 한다");
    }
}
