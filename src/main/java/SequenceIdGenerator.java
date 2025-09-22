public class SequenceIdGenerator {
    private static final int SEQUENCE_BITS = 10;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private final SequenceByShardIdentifierGroup sequenceGroup;

    private SequenceIdGenerator(final SequenceByShardIdentifierGroup sequenceGroup) {
        this.sequenceGroup = sequenceGroup;
    }

    public static SequenceIdGenerator from(final int instanceIdentifierCount) {
        verifyInstanceIdentifierCount(instanceIdentifierCount);
        return new SequenceIdGenerator(
                SequenceByShardIdentifierGroup.from(instanceIdentifierCount)
        );
    }

    private static void verifyInstanceIdentifierCount(int instanceIdentifierCount) {
        if (instanceIdentifierCount < 0 || instanceIdentifierCount > MAX_SEQUENCE) {
            throw new IllegalArgumentException("인스턴스 식별자는 10비트 이내이어야 한다");
        }
    }
}
