import java.time.LocalDate;
import java.time.LocalDateTime;

public class IdGeneratorFactory {
    public static IdGenerator create() {
        return new IdGenerator(
                TimestampBasedIdGenerator.referenceDateTime(LocalDateTime.of(2025, 1, 1, 0, 0)),
                new ShardIdGenerator(new RanndomNumberShardIdAllocator()),
                SequenceIdGenerator.from(IdComponent.SHARD.maxValue())
        );
    }
}
