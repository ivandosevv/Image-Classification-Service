package com.vmware.talentboost.ics.data;

public class Pair<U, V> {
    public final U name;       // the name field of a pair
    public final V confidence;      // the confidence field of a pair

    // Constructs a new pair with specified values
    private Pair(U tag, V second) {
        this.name = tag;
        this.confidence = second;
    }

    @Override
    // Checks specified object is "equal to" the current object or not
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        // call `equals()` method of the underlying objects
        if (!name.equals(pair.name)) {
            return false;
        }
        return confidence.equals(pair.confidence);
    }

    @Override
    // Computes hash code for an object to support hash tables
    public int hashCode() {
        // use hash codes of the underlying objects
        return 31 * name.hashCode() + confidence.hashCode();
    }

    @Override
    public String toString() {
        return "(" + name + ", " + confidence + ")";
    }

    // Factory method for creating a typed Pair immutable instance
    public static <U, V> Pair<U, V> of(U a, V b) {
        // calls private constructor
        return new Pair<>(a, b);
    }
}

