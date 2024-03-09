package com.brahvim.nerd.nerd_demos.scratches;

import java.util.HashMap;
import java.util.function.IntConsumer;

import com.brahvim.nerd.utils.NerdBufferUtils;

// TODO: Add hashing for speed (it's too easy LOL)!
public class SadlyNotFastArrayMap<KeyT, ValueT> {
    // implements Iterable<SadlyNotFastArrayMap<KeyT, ValueT>.Entry<KeyT, ValueT>> {

    public static class MapBenchmark {

        public static void main(final String[] p_args) {
            final int iterations = 1_000_000;

            final HashMap<String, String> hashMap = new HashMap<>(iterations);
            final SadlyNotFastArrayMap<String, String> arrayMap = new SadlyNotFastArrayMap<>(iterations);

            // Benchmark `HashMap`:
            final long hashMapInsertTime = MapBenchmark.benchmark(
                    iterations, i -> hashMap.put("Key" + i, "Value" + i));
            System.out.println("`HashMap` insertion time: " + hashMapInsertTime + " ms");

            final long hashMapGetTime = MapBenchmark.benchmark(
                    iterations, i -> hashMap.get("Key" + i));
            System.out.println("`HashMap` get time: " + hashMapGetTime + " ms");

            final long hashMapRemovalTime = MapBenchmark.benchmark(
                    iterations, i -> hashMap.remove("Key" + i));
            System.out.println("`HashMap` removal time: " + hashMapRemovalTime + " ms");

            System.out.println();

            // Benchmark `SadlyNotFastArrayMap`:
            final long arrayMapInsertTime = MapBenchmark.benchmark(
                    iterations, i -> arrayMap.put("Key" + i, "Value" + i));
            System.out.println("`FastArrayMap` insertion time: " + arrayMapInsertTime + " ms");

            final long arrayMapGetTime = MapBenchmark.benchmark(
                    iterations, i -> arrayMap.get("Key" + i));
            System.out.println("`FastArrayMap` get time: " + arrayMapGetTime + " ms");

            final long arrayMapRemovalTime = MapBenchmark.benchmark(
                    iterations, i -> arrayMap.remove("Key" + i));
            System.out.println("`FastArrayMap` removal time: " + arrayMapRemovalTime + " ms");
        }

        // Not an efficient way to do a benchmark, but anyway:
        private static long benchmark(final int p_iterations, final IntConsumer p_runnable) {
            final long startTime = System.currentTimeMillis();

            for (int i = 0; i < p_iterations; i++)
                p_runnable.accept(i);

            return System.currentTimeMillis() - startTime;
        }

    }

    // These could be cached by each map using them and then be reused.
    // ...or just be stored in a `static` array (per thread...?) and then be reused
    // as needed haha:
    public class Entry<EntryKeyT, EntryValueT> {

        EntryKeyT key;
        EntryValueT value;

        public Entry(final EntryKeyT p_key, final EntryValueT p_value) {
            this.key = p_key;
            this.value = p_value;
        }

    }

    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_ALLOCATION_MULTIPLIER = 2;

    // region Instance fields.
    // ...
    // "Store key-value pairs!~", they said.
    // ...Why stress the JIT!? I'm not doin' that, people!:
    protected Object[][] entries;

    protected int size, initialCapacity;

    // How filled is the array that we should de-allocate?
    protected float allocMult = SadlyNotFastArrayMap.DEFAULT_ALLOCATION_MULTIPLIER;
    // endregion

    // region Constructors.
    public SadlyNotFastArrayMap() {
        this(SadlyNotFastArrayMap.DEFAULT_CAPACITY, SadlyNotFastArrayMap.DEFAULT_ALLOCATION_MULTIPLIER);
    }

    public SadlyNotFastArrayMap(final float p_allocationMultiplier) {
        this(SadlyNotFastArrayMap.DEFAULT_CAPACITY, p_allocationMultiplier);
    }

    public SadlyNotFastArrayMap(final int p_initialCapacity) {
        this(p_initialCapacity, SadlyNotFastArrayMap.DEFAULT_ALLOCATION_MULTIPLIER);
    }

    public SadlyNotFastArrayMap(final int p_initialCapacity, final float p_allocationMultiplier) {
        // Check everything. Just like `HashMap` does:
        if (p_initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + p_initialCapacity);

        if (p_allocationMultiplier <= 0 || Float.isNaN(p_allocationMultiplier))
            throw new IllegalArgumentException("Illegal load factor: " + p_allocationMultiplier);

        // We're free now!:
        this.allocMult = p_allocationMultiplier;
        this.initialCapacity = p_initialCapacity;
        this.entries = new Object[2 * this.initialCapacity][0];
    }
    // endregion

    // region Internal methods.
    public void trimToSize() {
        this.entries = NerdBufferUtils.arrayCopy(this.entries, new Object[this.size][0], l -> new Object[l]);
    }

    protected void expandArrayIfNeeded() {
        // if ((this.entries.length - this.size) * 0.01f < this.allocFact)
        // return;

        if (this.entries.length - 2 != this.size)
            return;

        final int newSize = (int) (this.size * this.allocMult);

        if (newSize < this.size)
            throw new IllegalStateException("This `FastArrayMap` is already `Integer.MAX_VALUE` large!");

        this.entries = NerdBufferUtils.arrayCopy(this.entries, new Object[newSize][0], l -> new Object[l]);
    }

    protected int hashKey(final KeyT p_key) {
        return p_key == null ? 0 : p_key.hashCode();
    }

    protected int getKeyId(final KeyT p_key) {
        return this.hashKey(p_key) % this.size;
    }

    protected int getHashId(final int p_hash) {
        return p_hash % this.size;
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
        assert this.size % 2 == 0;

        for (int i = 0; i < this.size; i += 2)
            if (this.entries[i].equals(p_key))
                return true;

        return false;
    }

    public boolean containsValue(final ValueT p_value) {
        for (int i = 1; i < this.size; i += 2)
            if (this.entries[i].equals(p_value))
                return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public KeyT getKeyForValue(final ValueT p_value) {
        for (int i = 1; i < this.size; i += 2)
            if (this.entries[i].equals(p_value))
                return (KeyT) this.entries[i - 1];

        return null;
    }

    @SuppressWarnings("unchecked")
    public ValueT get(final KeyT p_key) {
        assert this.size % 2 == 0;

        for (int i = 0; i < this.size; i += 2)
            if (this.entries[i].equals(p_key))
                return (ValueT) this.entries[i + 1];

        return null;
    }

    public ValueT put(final KeyT p_key, final ValueT p_value) {
        this.expandArrayIfNeeded();

        this.entries[this.size][0] = p_key;
        this.entries[this.size + 1][0] = p_value;

        this.size += 2;

        return p_value;
    }

    @SuppressWarnings("unchecked")
    public ValueT remove(final KeyT p_key) {
        int keyId = 0, valueId = -1;
        ValueT toRet = null;

        for (; keyId < this.size; keyId += 2)
            if (this.entries[keyId].equals(p_key)) { // Not having a `continue` is faster haha.
                valueId = keyId + 1;

                this.entries[valueId] = this.entries[this.size - 1];
                toRet = (ValueT) this.entries[valueId];

                this.entries[keyId] = this.entries[this.size - 2];

                this.size -= 2; // Pop!
                break;
            }

        if (valueId == -1)
            return null;

        return toRet;
    }

    protected void remove(final int p_keyId) {
        this.entries[p_keyId + 1] = this.entries[this.size];
        this.entries[p_keyId] = this.entries[this.size - 1];
        --this.size; // Pop!
    }

    // public void putAll(final Map<? extends KeyT, ? extends ValueT> m) { }

    public void clear() {
        this.size = 0;
        // I've benchmarked this before. **It's faster**.
        // Java loops are a not fast enough for a `memset()`!
        // this.entries = NerdBufferUtils.arrayCopy(this.entries, new
        // Object[this.size]);
    }
    // endregion

    // region Custom iteration helpers.
    /*
     * @SuppressWarnings("unchecked")
     * public void forEachKey(final Consumer<KeyT> p_action) {
     * assert this.size % 2 == 0;
     *
     * for (int i = 0; i < this.size; i += 2)
     * p_action.accept((KeyT) this.entries[i]);
     * }
     *
     * @SuppressWarnings("unchecked")
     * public void forEachValue(final Consumer<ValueT> p_action) {
     * for (int i = 1; i < this.size; i += 2)
     * p_action.accept((ValueT) this.entries[i + 1]);
     * }
     *
     * @SuppressWarnings("unchecked")
     * public void forEach(final BiConsumer<KeyT, ValueT> p_action) {
     * assert this.size % 2 == 0;
     *
     * for (int i = 0; i < this.size; i += 2)
     * p_action.accept(
     * (KeyT) this.entries[i],
     * (ValueT) this.entries[i + 1]);
     * }
     *
     * @Override
     * public Iterator<SadlyNotFastArrayMap<KeyT, ValueT>.Entry<KeyT, ValueT>>
     * iterator() {
     * return new Iterator<>() {
     *
     * protected int currentId;
     *
     * @Override
     * public boolean hasNext() {
     * return this.currentId != SadlyNotFastArrayMap.this.size;
     * }
     *
     * @Override
     *
     * @SuppressWarnings("unchecked")
     * public Entry<KeyT, ValueT> next() {
     * if (this.currentId == SadlyNotFastArrayMap.this.size)
     * throw new NoSuchElementException();
     *
     * return SadlyNotFastArrayMap.this.new Entry<>(
     * (KeyT) SadlyNotFastArrayMap.this.entries[this.currentId],
     * (ValueT) SadlyNotFastArrayMap.this.entries[this.currentId + 1]);
     * }
     *
     * @Override
     * public void remove() {
     * SadlyNotFastArrayMap.this.remove(this.currentId);
     * }
     *
     * };
     * }
     *
     * public Iterable<KeyT> keysIterator() {
     * return () -> new Iterator<>() {
     *
     * @Override
     * public boolean hasNext() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
     * }
     *
     * @Override
     * public KeyT next() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'next'");
     * }
     *
     * @Override
     * public void remove() {
     *
     * }
     *
     * };
     * }
     *
     * public Iterable<ValueT> valuesIterator() {
     * return () -> new Iterator<ValueT>() {
     *
     * @Override
     * public boolean hasNext() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
     * }
     *
     * @Override
     * public ValueT next() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'next'");
     * }
     *
     * @Override
     * public void remove() {
     *
     * }
     *
     * };
     * }
     *
     * public Iterable<ValueT> keysIteratorBackwards() {
     * return () -> new Iterator<ValueT>() {
     *
     * @Override
     * public boolean hasNext() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
     * }
     *
     * @Override
     * public ValueT next() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'next'");
     * }
     *
     * @Override
     * public void remove() {
     *
     * }
     *
     * };
     * }
     *
     * public Iterable<ValueT> valuesIteratorBackwards() {
     * return () -> new Iterator<ValueT>() {
     *
     * @Override
     * public boolean hasNext() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
     * }
     *
     * @Override
     * public ValueT next() {
     * // TODO Auto-generated method stub
     * throw new UnsupportedOperationException("Unimplemented method 'next'");
     * }
     *
     * @Override
     * public void remove() {
     *
     * }
     *
     * };
     * }
     */
    // endregion

}
