import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

class SequenceByShardIdentifierGroupTest {

    @Test
    @DisplayName("지정된 수의 인스턴스 식별자로 그룹이 초기화된다")
    void init_not_throws_exception() {
        final var instanceCount = 5;
        final var sut = SequenceByShardIdentifierGroup.from(instanceCount);

        assertThatCode(() -> {
            for (int i = 0; i < instanceCount; i++) {
                sut.sequence(System.currentTimeMillis(), i);
            }
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 인스턴스 식별자로 시퀀스 요청시 예외가 발생한다")
    void if_exceed_instance_count_throws_exception() {
        final var sut = SequenceByShardIdentifierGroup.from(1);

        assertThatThrownBy(() -> sut.sequence(System.currentTimeMillis(), 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인스턴스 식별자가 포함되어 있지 않습니다.");
    }

    @Test
    @DisplayName("같은 타임스탬프에서 시퀀스가 순차적으로 증가한다")
    void increment_sequential() {
        final var sut = SequenceByShardIdentifierGroup.from(1);
        final var timestamp = System.currentTimeMillis();
        final var instanceId = 0;

        final var firstSeq = sut.sequence(timestamp, instanceId);
        final var secondSeq = sut.sequence(timestamp, instanceId);
        final var thirdSeq = sut.sequence(timestamp, instanceId);

        assertThat(firstSeq).isEqualTo(1L);
        assertThat(secondSeq).isEqualTo(2L);
        assertThat(thirdSeq).isEqualTo(3L);
    }

    @Test
    @DisplayName("과거 타임스탬프로 시퀀스 요청시 예외가 발생한다")
    void if_past_timestamp_throws_exception() {
        final var sut = SequenceByShardIdentifierGroup.from(1);
        final var instanceId = 0;

        sut.sequence(2L, instanceId);

        assertThatThrownBy(() -> sut.sequence(1L, instanceId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거의 시간대입니다.");
    }

    @Test
    @DisplayName("동시성 환경에서 중복 시퀀스가 생성되지 않는다")
    void concurrent_sequence_generation() throws InterruptedException {
        final var sut = SequenceByShardIdentifierGroup.from(1);
        final var timestamp = System.currentTimeMillis();
        final var instanceId = 0;
        final var threadCount = 10_000;
        final var sequences = ConcurrentHashMap.<Long>newKeySet();

        final var barrier = new CyclicBarrier(threadCount);
        final var latch = new CountDownLatch(threadCount);

        try (var executor = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        barrier.await();
                        sequences.add(sut.sequence(timestamp, instanceId));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }

        assertThat(sequences).hasSize(threadCount);
    }
}
