package com.brahvim.nerd.nerd_demos.scratches;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.SupportedAnnotationTypes;

import com.brahvim.nerd.utils.NerdBufferUtils;

public class FastArrayMap<KeyT, ValueT> implements Iterable<Map.Entry<KeyT, ValueT>> {

    public static void main(final String[] p_args) {
        // final HashMap<String, String> hashMap = new HashMap<>(1, 1);
        // final FastArrayMap<String, String> arrayMap = new FastArrayMap<>();
    }

    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_ALLOCATION_FACTOR = 0.75f;

    // region Instance fields.
    protected int size, initialCapacity;

    // ...
    // "Store key-value pairs!~", they said.
    // ...Why stress the JIT!? I'm not doin' that, people!:
    protected Object[] entries;

    // How filled is the array that we should de-allocate?
    protected float allocMult = FastArrayMap.DEFAULT_ALLOCATION_FACTOR;
    // endregion

    // region Constructors.
    public FastArrayMap() {
        this(FastArrayMap.DEFAULT_CAPACITY, FastArrayMap.DEFAULT_ALLOCATION_FACTOR);
    }

    public FastArrayMap(final float p_allocationMultiplier) {
        this(FastArrayMap.DEFAULT_CAPACITY, p_allocationMultiplier);
    }

    public FastArrayMap(final int p_initialCapacity) {
        this(p_initialCapacity, FastArrayMap.DEFAULT_ALLOCATION_FACTOR);
    }

    public FastArrayMap(final int p_initialCapacity, final float p_allocationMultiplier) {
        // Check everything. Just like `HashMap` does:
        if (p_initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + p_initialCapacity);

        if (p_allocationMultiplier <= 0 || Float.isNaN(p_allocationMultiplier))
            throw new IllegalArgumentException("Illegal load factor: " + p_allocationMultiplier);

        // We're free now!:
        this.allocMult = p_allocationMultiplier;
        this.initialCapacity = p_initialCapacity;
        this.entries = new Object[2 * this.initialCapacity];
    }
    // endregion

    // region Array-checking methods.
    protected void expandArrayIfNeeded() {
        // if ((this.entries.length - this.size) * 0.01f < this.allocFact)
        // return;

        if (this.entries.length != this.size)
            return;

        final int newSize = (int) (this.size * this.allocMult);

        if (newSize < this.size)
            throw new IllegalStateException("This `FastArrayMap` is already `Integer.MAX_VALUE` large!");

        this.entries = NerdBufferUtils.arrayCopy(this.entries, new Object[newSize]);
    }

    public void removeDuplicates() {
    }

    public void trimToSize() {
        this.entries = NerdBufferUtils.arrayCopy(this.entries, new Object[this.size]);
    }
    // endregion

    // region `Map` interface methods.
    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(final KeyT p_key) {
        for (int i = 0; i < this.size; i += 2)
            if (this.entries[i] == p_key)
                return true;

        return false;
    }

    public boolean containsValue(final ValueT p_value) {
        for (int i = 1; i < this.size; i += 2)
            if (this.entries[i] == p_value)
                return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public KeyT getKeyForValue(final ValueT p_value) {
        for (int i = 1; i < this.size; i += 2)
            if (this.entries[i] == p_value)
                return (KeyT) this.entries[i - 1];

        return null;
    }

    @SuppressWarnings("unchecked")
    public ValueT get(final KeyT p_key) {
        for (int i = 0; i < this.size; i += 2)
            if (this.entries[i] == p_key)
                return (ValueT) this.entries[i + 1];

        return null;
    }

    public ValueT put(final KeyT p_key, final ValueT p_value) {
        this.expandArrayIfNeeded();
        this.entries[this.size] = p_key;
        this.entries[this.size + 1] = p_value;

        return p_value;
    }

    @SuppressWarnings("unchecked")
    public ValueT remove(final KeyT p_key) {
        int keyId = -1, valueId = -1;
        ValueT toRet = null;

        for (; keyId < this.size; keyId += 2)
            if (this.entries[keyId] == p_key) {
                valueId = keyId + 1;
                toRet = (ValueT) this.entries[valueId];
            }

        if (valueId == -1)
            return null;

        // Swap the removed item with one that we'll put out of bounds!:
        this.entries[valueId] = this.entries[this.size];
        this.entries[keyId] = this.entries[this.size - 1];
        --this.size; // Pop!

        return toRet;
    }

    // public void putAll(final Map<? extends KeyT, ? extends ValueT> m) { }

    public void clear() {
        this.size = 0;
        // I've benchmarked this before. **It's faster**.
        // Java loops are a not fast enough for a `memset()`!
        this.entries = NerdBufferUtils.arrayCopy(this.entries, new Object[this.size]);
    }
    // endregion

    // region Custom iteration helpers.
    @Override
    public Iterator<Entry<KeyT, ValueT>> iterator() {
        return new Iterator<Map.Entry<KeyT, ValueT>>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public Entry<KeyT, ValueT> next() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'next'");
            }

            @Override
            public void remove() {

            }

        };
    }

    public Iterable<KeyT> keysIterator() {
        return () -> new Iterator<KeyT>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public KeyT next() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'next'");
            }

            @Override
            public void remove() {

            }

        };
    }

    public Iterable<ValueT> valuesIterator() {
        return () -> new Iterator<ValueT>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public ValueT next() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'next'");
            }

            @Override
            public void remove() {

            }

        };
    }

    public Iterable<ValueT> keysIteratorBackwards() {
        return () -> new Iterator<ValueT>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public ValueT next() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'next'");
            }

            @Override
            public void remove() {

            }

        };
    }

    public Iterable<ValueT> valuesIteratorBackwards() {
        return () -> new Iterator<ValueT>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
            }

            @Override
            public ValueT next() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'next'");
            }

            @Override
            public void remove() {

            }

        };
    }
    // endregion

}
