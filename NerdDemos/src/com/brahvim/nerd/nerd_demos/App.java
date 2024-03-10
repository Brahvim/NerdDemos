package com.brahvim.nerd.nerd_demos;

import com.brahvim.nerd.framework.ecs.NerdEcsModule;
import com.brahvim.nerd.framework.ecs.NerdEcsModuleSettings;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.framework.sketch_builders.NerdP3dSketchBuilder;
import com.brahvim.nerd.nerd_demos.scenes.DemoScene1;

public class App {

    @SuppressWarnings("unchecked")
    public static void main(final String[] p_args) {
        new NerdP3dSketchBuilder()
                .addNerdModule(NerdScenesModule.class, new NerdScenesModuleSettings<>(DemoScene1.class))
                .addNerdModule(NerdEcsModule.class, new NerdEcsModuleSettings<>())
                .setWindowIconPath("Images/SunglassesNerd.png")
                .closeOnPressingEscapeInitially()
                .canResizeWindow()
                .build(p_args);
    }

    // Ye' old big TODOs!:
    /*
     * - OpenAL *enumerated* wrapper!
     * - The `NerdEasingFunction` rewrite.
     * - Complete the ECS's networking API.
     * - Versioned serialization packets containing ECS components.
     * - Input mappings API like the other, 'real' engines using `Predicate`s.
     * - Let JAR assets be in the `data` folder (each JAR carries a folder).
     *
     * Longer tasks:
     * - Android port!
     * - ECS wrapper for Processing!
     * - Dyn4j / Javacpp LiquidFun ECS wrapper!
     */

}
