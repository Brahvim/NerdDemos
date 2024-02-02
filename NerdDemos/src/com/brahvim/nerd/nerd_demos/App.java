package com.brahvim.nerd.nerd_demos;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.nerd_demos.scenes.DemoScene1;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdP3dSketchBuilder;

import processing.opengl.PGraphics3D;

public class App {

    public static void main(final String[] p_args) {
        new NerdP3dSketchBuilder()
                .addNerdModule((Class<? extends NerdModule<PGraphics3D>>) NerdScenesModule.class)
                .setNerdModuleSettings(new NerdScenesModuleSettings<>(DemoScene1.class))
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
