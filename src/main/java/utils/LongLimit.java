package utils;

public class LongLimit {

    private long offset;
    private long size;

    public LongLimit(long offset, long size) {
        this.offset = offset;
        this.size = size;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }
}
