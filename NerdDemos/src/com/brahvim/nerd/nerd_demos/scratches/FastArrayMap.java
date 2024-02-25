package com.brahvim.nerd.nerd_demos.scratches;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FastArrayMap<KeyT, ValueT> implements Iterable<Map.Entry<KeyT, ValueT>> {

    protected int size;
    protected Object[][] entries;

    protected Set<KeyT> keySet;

    // Dirty flags:
    protected boolean
    /*   */ keySetDirty,
            valuesDirty,
            entrySetDirty;

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(final Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsKey'");
    }

    public boolean containsValue(final Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsValue'");
    }

    @SuppressWarnings("unchecked")
    public ValueT get(final Object key) {
        for (int i = 0; i < this.size; i += 2)
            if (this.entries[i] == key)
                return (ValueT) this.entries[i + 1];

        return null;
    }

    public ValueT put(final KeyT key, final ValueT value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    public ValueT remove(final Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    public void putAll(final Map<? extends KeyT, ? extends ValueT> m) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putAll'");
    }

    public void clear() {
        this.size = 0;
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

        };
    }

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

        };
    }

}
