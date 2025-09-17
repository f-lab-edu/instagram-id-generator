import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ShardIdGeneratorTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 8191})
    void 인스턴스_식별자값이_정상_범위_이내일_경우_예외없이_생성된다(final int instanceIdentifier) {
        assertThatCode(() -> ShardIdGenerator.from(instanceIdentifier))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 8192})
    void 인스턴스_식별자값이_정상_범위_이외일_경우_예외가_발생한다(final int instanceIdentifier) {
        assertThatThrownBy(() -> ShardIdGenerator.from(instanceIdentifier))
                .isInstanceOf(IllegalArgumentException.class);
    }
}