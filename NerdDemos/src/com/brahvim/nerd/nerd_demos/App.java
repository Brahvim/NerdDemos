package com.brahvim.nerd.nerd_demos;

import java.util.LinkedHashSet;
import java.util.function.Function;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.nerd_demos.scenes.DemoScene2;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdP3dSketchBuilder;

import processing.opengl.PGraphics3D;

public class App {

    public static void main(final String[] p_args) {
        new NerdP3dSketchBuilder() {
            @Override
            protected void supplyUserDefinedModules(
                    final LinkedHashSet<Function<NerdSketch<PGraphics3D>, NerdModule<PGraphics3D>>> p_modulesSet) {
                p_modulesSet.add(NerdScenesModule<PGraphics3D>::new);
            }
        }
                .canResize()
                .closeOnPressingEscapeInitially()
                .setWindowIconPath("Images/SunglassesNerd.png")
                .setNerdModuleSettings(new NerdScenesModuleSettings<>(DemoScene2.class))
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
