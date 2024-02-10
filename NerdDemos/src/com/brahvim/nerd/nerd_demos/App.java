package com.brahvim.nerd.nerd_demos;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.nerd_demos.scenes.DemoScene1;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdP3dSketchBuilder;

public class App {

    @SuppressWarnings("unchecked")
    public static void main(final String[] p_args) {
        new NerdP3dSketchBuilder()
                .addNerdModule(NerdScenesModule.class, new NerdScenesModuleSettings<>(DemoScene1.class))
                // .setNerdModuleSettings()
                .setWindowIconPath("Images/SunglassesNerd.png")
                .closeOnPressingEscapeInitially()
                .canResize()
                .build(p_args);
    }

    // Ye' Old Big TODOs!:
    /*
     * - OpenAL *enumerated* wrapper!
     * - The `NerdEasingFunction` rewrite.
     * - Versioned serialization packets containing ECS components.
     * - Input mappings API like the other, 'real' engines using `Predicate`s.
     * - Complete the ECS's networking API.
     * - Stop screwing up with how to use `PGraphics`, cameras etc.
     * - Let JAR assets be in the `data` folder (each JAR carries a folder).
     *
     * Longer tasks:
     * - Android port!
     * - ECS wrapper for Processing!
     * - Dyn4j / Javacpp LiquidFun ECS wrapper!
     */

}
