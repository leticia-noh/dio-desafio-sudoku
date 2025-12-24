package br.com.dio.model;

public class Space {

    private Integer current;

    private final int expected;

    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            current = expected;
        }
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (fixed) return;
        this.current = current;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void clearSpace() {
        setCurrent(null);
    }
}
