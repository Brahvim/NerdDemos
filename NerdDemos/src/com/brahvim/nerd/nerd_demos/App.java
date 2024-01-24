package com.brahvim.nerd.nerd_demos;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings;
import com.brahvim.nerd.nerd_demos.scenes.net_demo_scenes.TcpDemoScene;
import com.brahvim.nerd.processing_wrapper.sketch_builders.NerdJava2dSketchBuilder;

public class App {

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

    public static void main(final String[] p_args) {
        new NerdJava2dSketchBuilder()
                .canResize()
                // .startFullscreenInitially()
                .setWindowIconPath("Images/SunglassesNerd.png")
                .setNerdModuleSettings(new NerdScenesModuleSettings(TcpDemoScene.class))
                .build(p_args);
    }

}
