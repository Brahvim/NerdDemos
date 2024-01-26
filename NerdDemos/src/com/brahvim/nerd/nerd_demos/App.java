package com.brahvim.nerd.nerd_demos;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.nerd_demos.scenes.TestGlScene;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdP3dSketchBuilder;

public class App {

    public static void main(final String[] p_args) {
        new NerdP3dSketchBuilder()
                .canResize()
                .closeOnPressingEscapeInitially()
                .setWindowIconPath("Images/SunglassesNerd.png")
                .setNerdModuleSettings(new NerdScenesModuleSettings(TestGlScene.class))
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
