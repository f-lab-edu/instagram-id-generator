import java.util.Arrays;

enum IdComponent {
    TIMESTAMP(41),
    SHARD(13),
    SEQUENCE(10);

    static {
        long totalBits = Arrays.stream(IdComponent.values())
                .mapToLong(IdComponent::bitLength)
                .sum();
        if (totalBits > Long.SIZE) {
            throw new IllegalStateException("ID 구성의 비트 합이 64이어야 한다");
        }
    }

    private final long bitLength;

    IdComponent(final long bitLength) {
        this.bitLength = bitLength;
    }

    public long bitLength() {
        return bitLength;
    }

    public long maxValue() {
        return (1L << bitLength) - 1;
    }
}
