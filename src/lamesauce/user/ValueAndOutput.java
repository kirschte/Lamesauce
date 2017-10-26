package lamesauce.user;

public class ValueAndOutput<V, O extends CharSequence> {

    private V value;
    private O output;

    ValueAndOutput(V value, O output) {
        this.value = value;
        this.output = output;
    }

    public V getValue() {
        return value;
    }

    public O getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueAndOutput)) return false;

        ValueAndOutput<?, ?> that = (ValueAndOutput<?, ?>) o;

        return (value != null ? value.equals(that.value) : that.value == null)
                && (output != null ? output.equals(that.output) : that.output == null);
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        return result;
    }
}
