#include <iostream>
#include <random>
#include <chrono>
#include <typeinfo>

class NerdNanosTimer {

public:
    NerdNanosTimer(const std::string& p_tracked_event_name)
        : tracked_event_name(p_tracked_event_name) {
        start_timepoint = std::chrono::high_resolution_clock::now();
        // std::cout << label << " started." << std::endl;
    }

    ~NerdNanosTimer() {
        using namespace std::chrono;

        auto end_timepoint = high_resolution_clock::now();
        auto start = time_point_cast<nanoseconds>(start_timepoint).time_since_epoch().count();
        auto end = time_point_cast<nanoseconds>(end_timepoint).time_since_epoch().count();
        auto duration = end - start;

        std::cout << "Took `" << duration << "` ns." << std::endl;
    }

private:
    std::string tracked_event_name;
    std::chrono::time_point<std::chrono::high_resolution_clock> start_timepoint;
};

class Flagger {
public:
    virtual bool getFlagA() const = 0;
    virtual bool getFlagB() const = 0;
    virtual bool getFlagC() const = 0;
    virtual bool getFlagD() const = 0;

    virtual void setFlagA(const bool p_status) = 0;
    virtual void setFlagB(const bool p_status) = 0;
    virtual void setFlagC(const bool p_status) = 0;
    virtual void setFlagD(const bool p_status) = 0;
};

class FlagsClass : public Flagger {
public:
    static const int FLAG_A = 1;
    static const int FLAG_B = 2;
    static const int FLAG_C = 4;
    static const int FLAG_D = 8;

    int flags;

    bool getFlagA() const override {
        return (flags & FlagsClass::FLAG_A) != 0;
    }

    bool getFlagB() const override {
        return (flags & FlagsClass::FLAG_B) != 0;
    }

    bool getFlagC() const override {
        return (flags & FlagsClass::FLAG_C) != 0;
    }

    bool getFlagD() const override {
        return (flags & FlagsClass::FLAG_D) != 0;
    }

    void setFlagA(const bool p_status) override {
        flags = p_status ? (flags | FlagsClass::FLAG_A) : (flags & ~FlagsClass::FLAG_A);
    }

    void setFlagB(const bool p_status) override {
        flags = p_status ? (flags | FlagsClass::FLAG_B) : (flags & ~FlagsClass::FLAG_B);
    }

    void setFlagC(const bool p_status) override {
        flags = p_status ? (flags | FlagsClass::FLAG_C) : (flags & ~FlagsClass::FLAG_C);
    }

    void setFlagD(const bool p_status) override {
        flags = p_status ? (flags | FlagsClass::FLAG_D) : (flags & ~FlagsClass::FLAG_D);
    }
};

class BooleansClass : public Flagger {
public:
    bool flagA;
    bool flagB;
    bool flagC;
    bool flagD;

    bool getFlagA() const override {
        return flagA;
    }

    bool getFlagB() const override {
        return flagB;
    }

    bool getFlagC() const override {
        return flagC;
    }

    bool getFlagD() const override {
        return flagD;
    }

    void setFlagA(const bool p_status) override {
        flagA = p_status;
    }

    void setFlagB(const bool p_status) override {
        flagB = p_status;
    }

    void setFlagC(const bool p_status) override {
        flagC = p_status;
    }

    void setFlagD(const bool p_status) override {
        flagD = p_status;
    }
};

class BooleansVsFlagsScratch {
public:
    static constexpr bool TEST_VALUES[] = { true, false, true, false };

    static void benchFlagger(Flagger *p_flagger) {
        NerdNanosTimer nanosTimer("Flagger");

        for (int i = 0; i < 1'000'000; i++) {
            switch (i % 4) {
                case 0: p_flagger->setFlagA(TEST_VALUES[0]); break;
                case 1: p_flagger->setFlagB(TEST_VALUES[1]); break;
                case 2: p_flagger->setFlagC(TEST_VALUES[2]); break;
                case 3: p_flagger->setFlagD(TEST_VALUES[3]); break;
            }
        }
    }
};

int main() {
    const int NUM_TESTS = 8;

    std::cout << "Booleans:" << std::endl;

    for (int i = 0; i < NUM_TESTS; i++) {
        BooleansClass booleansClass;
        BooleansVsFlagsScratch::benchFlagger(&booleansClass);
    }

    std::cout << "Flags:" << std::endl;

    for (int i = 0; i < NUM_TESTS; i++) {
        FlagsClass flagsClass;
        BooleansVsFlagsScratch::benchFlagger(&flagsClass);
    }

    return 0;
}
