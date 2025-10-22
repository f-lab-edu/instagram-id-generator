final class SequenceIdGenerator {
    private static final IdComponent COMPONENT = IdComponent.SEQUENCE;

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

    public long allocatedBits() {
        return COMPONENT.bitLength();
    }

    private static void verifyInstanceIdentifierCount(int instanceIdentifierCount) {
        if (instanceIdentifierCount < 0 || instanceIdentifierCount > COMPONENT.maxValue()) {
            throw new IllegalArgumentException("인스턴스 식별자는 10비트 이내이어야 한다");
        }
    }

    public long generate(final long timestamp, final long shardInstanceIdentifier) {
        return sequenceGroup.sequence(timestamp, shardInstanceIdentifier);
    }
}
