#include <chrono>
#include <vector>
#include <iostream>
#include <unordered_map>

#define ITR 1'000'000

class IEntity { };

template <typename EntityIdT>
class IEntityManager {
public:
    virtual EntityIdT createEntity() = 0;
    virtual float getFloat1(const EntityIdT entity) = 0;
    virtual float getFloat2(const EntityIdT entity) = 0;
    virtual float getFloat3(const EntityIdT entity) = 0;
    virtual void destroyEntity(const EntityIdT entity) = 0;
    virtual std::string getString(const EntityIdT entity) = 0;
    virtual void setFloat1(EntityIdT entity, float value) = 0;
    virtual void setFloat2(EntityIdT entity, float value) = 0;
    virtual void setFloat3(EntityIdT entity, float value) = 0;
    virtual void setString(EntityIdT entity, const std::string &value) = 0;
    virtual ~IEntityManager() = default;
};

class EntityObject : public IEntity {
public:
    float float1;
    float float2;
    float float3;
    std::string string;

    EntityObject(float p_float1, float p_float2, float p_float3, std::string p_string)
        : float1(p_float1), float2(p_float2), float3(p_float3), string(std::move(p_string)) {
    }
};

class InstanceDataManager : public IEntityManager<EntityObject*> {
public:
    EntityObject* createEntity() override {
        return new EntityObject(0, 0, 0, "");
    }

    void destroyEntity(EntityObject *p_entity) override {
        delete p_entity;
    }

    float getFloat1(EntityObject *p_entity) override {
        if (p_entity == nullptr)
            return 0;

        return p_entity->float1;
    }

    void setFloat1(EntityObject *p_entity, float p_value) override {
        p_entity->float1 = p_value;
    }

    float getFloat2(EntityObject *p_entity) override {
        if (p_entity == nullptr)
            return 0;

        return p_entity->float2;
    }

    void setFloat2(EntityObject *p_entity, float p_value) override {
        p_entity->float2 = p_value;
    }

    float getFloat3(EntityObject *p_entity) override {
        if (p_entity == nullptr)
            return 0;

        return p_entity->float3;
    }

    void setFloat3(EntityObject *p_entity, float p_value) override {
        p_entity->float3 = p_value;
    }

    std::string getString(EntityObject *p_entity) override {
        if (p_entity == nullptr)
            return "";

        return p_entity->string;
    }

    void setString(EntityObject *p_entity, const std::string& p_value) override {
        p_entity->string = p_value;
    }

};

class EntityId : IEntity {
public:
    int id;

    EntityId(int p_id): id(p_id) { }

};

class MultiMapManager : public IEntityManager<EntityId> {
private:
    static int lastId;
    std::unordered_map<int, float> FLOAT_1_MAP;
    std::unordered_map<int, float> FLOAT_2_MAP;
    std::unordered_map<int, float> FLOAT_3_MAP;
    std::unordered_map<int, std::string> STRINGS_MAP;

public:
    MultiMapManager() { }

    EntityId createEntity() override {
        const int entityId = lastId++;

        this->FLOAT_1_MAP[entityId] = 0;
        this->FLOAT_2_MAP[entityId] = 0;
        this->FLOAT_3_MAP[entityId] = 0;
        this->STRINGS_MAP[entityId] = "";

        return EntityId(entityId);
    }

    void destroyEntity(EntityId p_entity) override {
        const int entityId = p_entity.id;

        this->FLOAT_1_MAP.erase(entityId);
        this->FLOAT_2_MAP.erase(entityId);
        this->FLOAT_3_MAP.erase(entityId);
        this->STRINGS_MAP.erase(entityId);
    }

    float getFloat1(EntityId p_entity) override {
        return this->FLOAT_1_MAP[p_entity.id];
    }

    float getFloat2(EntityId p_entity) override {
        return this->FLOAT_2_MAP[p_entity.id];
    }

    float getFloat3(EntityId p_entity) override {
        return this->FLOAT_3_MAP[p_entity.id];
    }

    std::string getString(EntityId p_entity) override {
        return this->STRINGS_MAP[p_entity.id];
    }

    void setFloat1(EntityId p_entity, float p_value) override {
        this->FLOAT_1_MAP[p_entity.id] = p_value;
    }

    void setFloat2(EntityId p_entity, float p_value) override {
        this->FLOAT_2_MAP[p_entity.id] = p_value;
    }

    void setFloat3(EntityId p_entity, float p_value) override {
        this->FLOAT_3_MAP[p_entity.id] = p_value;
    }

    void setString(EntityId p_entity, const std::string& p_value) override {
        this->STRINGS_MAP[p_entity.id] = p_value;
    }

};

int MultiMapManager::lastId = 0;

template<class EntityIdT>
void testManager(IEntityManager<EntityIdT> &p_entityMan) {
    const auto startTime = std::chrono::high_resolution_clock::now();

    std::vector<EntityIdT> array;

    // Make some entities:
    for (int i = 0; i < ITR; i++) {
        EntityIdT entityId = p_entityMan.createEntity();
        array.push_back(entityId);
        p_entityMan.setFloat1(entityId, 0);
        p_entityMan.setFloat2(entityId, 0);
        p_entityMan.setFloat3(entityId, 0);
        p_entityMan.setString(entityId, "");
    }

    // "Get" the data and destroy them:
    for (int i = ITR; i > 0; i--) {
        EntityIdT entityId = array[i];
        p_entityMan.getFloat1(entityId);
        p_entityMan.getFloat2(entityId);
        p_entityMan.getFloat3(entityId);
        p_entityMan.getString(entityId);
        p_entityMan.destroyEntity(entityId);
    }

    const auto endTime = std::chrono::high_resolution_clock::now();

    const auto timeTaken = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime).count();
    std::cout << "`" << typeid(p_entityMan).name() << "` took: `" << timeTaken << "` ms." << std::endl;
}

int main() {
    InstanceDataManager man1;
    testManager(man1); // ~`150` ms.

    MultiMapManager man2; // ~`1600` ms.
    testManager(man2);
}
