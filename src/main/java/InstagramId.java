import java.util.Objects;

public class InstagramId {
    private final long value;

    private InstagramId(long value) {
        this.value = value;
    }

    public static InstagramId from(long value) {
        return new InstagramId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstagramId that = (InstagramId) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
