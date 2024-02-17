package com.brahvim.nerd.nerd_demos.scratches;

import java.util.HashMap;
import java.util.Map;

/*
 * ```bash
 * `MultiMapManager` took: `537.399849` ms, using `1811939328` bytes.
 * `InstanceDataManager` took: `86.243845` ms, using `0` bytes.
 * ```
 *
 * ```bash
 * `MultiMapManager` took: `638.996537` ms, using `1811939328` bytes.
 * `InstanceDataManager` took: `33.883417` ms, using `0` bytes.
 * ```
 * 
 * ..Yeah...
 */

// Moral of the story: DON'T blindly follow patterns. I wrote this example just to show that haha.
public class MultiMapVsArrayId {

    public static final int ITR = 1_000_000;

    public static void main(final String[] p_args) {
        MultiMapVsArrayId.testManager(new MultiMapManager());
        MultiMapVsArrayId.testManager(new InstanceDataManager());
    }

    private static <EntityT> void testManager(final EntityManager<EntityT> p_entityMan) {
        final long startMem = Runtime.getRuntime().totalMemory();
        final long startTime = System.nanoTime();

        for (int i = 0; i < MultiMapVsArrayId.ITR; i++) {
            final EntityT entityId = p_entityMan.createEntity();
            p_entityMan.setFloat1(entityId, 0);
            p_entityMan.setFloat2(entityId, 0);
            p_entityMan.setFloat3(entityId, 0);
            p_entityMan.setString(entityId, "");
        }

        for (int i = MultiMapVsArrayId.ITR; i > 0; i--) {
            final EntityT entityId = p_entityMan.createEntity();
            p_entityMan.getFloat1(entityId);
            p_entityMan.getFloat2(entityId);
            p_entityMan.getFloat3(entityId);
            p_entityMan.getString(entityId);
        }

        final long endTime = System.nanoTime();
        final long endMem = Runtime.getRuntime().totalMemory();

        System.out.printf(
                "`%s` took: `%s` ms, using `%s` bytes.%n",
                p_entityMan.getClass().getSimpleName(),
                (endTime - startTime) / 1e6, endMem - startMem);
    }

}

interface Entity {

}

interface EntityManager<EntityIdT> {

    public EntityIdT createEntity();

    public float getFloat1(final EntityIdT p_entity);

    public float getFloat2(final EntityIdT p_entity);

    public float getFloat3(final EntityIdT p_entity);

    public String getString(final EntityIdT p_entity);

    public void setFloat1(final EntityIdT p_entity, final float p_value);

    public void setFloat3(final EntityIdT p_entity, final float p_value);

    public void setFloat2(final EntityIdT p_entity, final float p_value);

    public void setString(final EntityIdT p_entity, final String p_value);

}

class InstanceDataManager implements EntityManager<InstanceDataManager.EntityInstance> {

    static class EntityInstance implements Entity {

        private float float1;
        private float float2;
        private float float3;
        private String string;

        protected EntityInstance(
                final float p_float1, final float p_float2, final float p_float3, final String p_string) {
            this.float1 = p_float1;
            this.float2 = p_float2;
            this.float3 = p_float3;
            this.string = p_string;
        }

    }

    // private final List<EntityObject> entities = new ArrayList<>(0);

    @Override
    public InstanceDataManager.EntityInstance createEntity() {
        return new InstanceDataManager.EntityInstance(0, 0, 0, "");
    }

    @Override
    public float getFloat1(final InstanceDataManager.EntityInstance p_entity) {
        return p_entity.float1;
    }

    @Override
    public void setFloat1(final InstanceDataManager.EntityInstance p_entity, final float p_value) {
        p_entity.float1 = p_value;
    }

    @Override
    public float getFloat2(final InstanceDataManager.EntityInstance p_entity) {
        return p_entity.float2;
    }

    @Override
    public void setFloat2(final InstanceDataManager.EntityInstance p_entity, final float p_value) {
        p_entity.float2 = p_value;
    }

    @Override
    public float getFloat3(final InstanceDataManager.EntityInstance p_entity) {
        return p_entity.float3;
    }

    @Override
    public void setFloat3(final InstanceDataManager.EntityInstance p_entity, final float p_value) {
        p_entity.float3 = p_value;
    }

    @Override
    public String getString(final InstanceDataManager.EntityInstance p_entity) {
        return p_entity.string;
    }

    @Override
    public void setString(final InstanceDataManager.EntityInstance p_entity, final String p_value) {
        p_entity.string = p_value;
    }

}

class MultiMapManager implements EntityManager<MultiMapManager.EntityId> {

    static class EntityId {

        public final int ID;

        public EntityId(final int p_id) {
            this.ID = p_id;
        }

    }

    private static int lastId = 0;
    private static final float FLOAT_ZERO = Float.parseFloat("0");
    private final Map<Integer, Float> FLOAT_1_MAP = new HashMap<>();
    private final Map<Integer, Float> FLOAT_2_MAP = new HashMap<>();
    private final Map<Integer, Float> FLOAT_3_MAP = new HashMap<>();
    private final Map<Integer, String> STRINGS_MAP = new HashMap<>();

    @Override
    public synchronized EntityId createEntity() {
        final int entityId = MultiMapManager.lastId++;

        this.FLOAT_1_MAP.put(entityId, MultiMapManager.FLOAT_ZERO);
        this.FLOAT_2_MAP.put(entityId, MultiMapManager.FLOAT_ZERO);
        this.FLOAT_3_MAP.put(entityId, MultiMapManager.FLOAT_ZERO);
        this.STRINGS_MAP.put(entityId, "");

        return new EntityId(entityId);
    }

    @Override
    public float getFloat1(final EntityId p_entity) {
        return this.FLOAT_1_MAP.getOrDefault(p_entity.ID, 0.0f);
    }

    @Override
    public float getFloat2(final EntityId p_entity) {
        return this.FLOAT_2_MAP.getOrDefault(p_entity.ID, 0.0f);
    }

    @Override
    public float getFloat3(final EntityId p_entity) {
        return this.FLOAT_3_MAP.getOrDefault(p_entity.ID, 0.0f);
    }

    @Override
    public String getString(final EntityId p_entity) {
        return this.STRINGS_MAP.getOrDefault(p_entity.ID, "");
    }

    @Override
    public void setFloat1(final EntityId p_entity, final float p_value) {
        this.FLOAT_1_MAP.put(p_entity.ID, p_value);
    }

    @Override
    public void setFloat2(final EntityId p_entity, final float p_value) {
        this.FLOAT_2_MAP.put(p_entity.ID, p_value);
    }

    @Override
    public void setFloat3(final EntityId p_entity, final float p_value) {
        this.FLOAT_3_MAP.put(p_entity.ID, p_value);
    }

    @Override
    public void setString(final EntityId p_entity, final String p_value) {
        this.STRINGS_MAP.put(p_entity.ID, p_value);
    }

}
