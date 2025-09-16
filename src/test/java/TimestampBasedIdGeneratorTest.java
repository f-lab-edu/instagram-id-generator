import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TimestampBasedIdGeneratorTest {

    @Test
    void null_입력시_예외가_발생한다() {
        assertThatThrownBy(() -> new TimestampBasedIdGenerator(null))
                .isInstanceOf(NullPointerException.class);
    }
}
