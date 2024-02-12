package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.io.class_loaders.NerdSceneClassLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PConstants;
import processing.opengl.PGraphics3D;

public abstract class AbstractDemoScene extends NerdP3dScene {

    protected AbstractDemoScene(final NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    protected final void draw() {
        // Faster in `draw()`:
        if (super.INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
            super.MANAGER.restartScene();

        this.drawImpl();
    }

    protected void drawImpl() {
    }

    @Override
    public final void mousePressed() {
        if (super.INPUT.mouseButton != PConstants.RIGHT)
            return;

        final int newSceneNumber = super.INPUT.keysGivenWerePressed(KeyEvent.VK_SHIFT)
                ? this.getPreviousDemoSceneNumber()
                : this.getNextDemoSceneNumber();

        try {

            final Class<? extends AbstractDemoScene> newSceneClass
            /*   */ = switch (newSceneNumber) {
                // case 5 -> this.obtainDemoScene5FromData();
                default -> this.obtainDemoSceneFromSource(newSceneNumber);
            };

            super.MANAGER.startScene(newSceneClass);

        } catch (final Exception e) {
            e.printStackTrace();
            System.err.printf("(Current scene number: %d).%n", this.getPreviousDemoSceneNumber());
            System.err.println("Couldn't start the next scene :/");
        }
    }

    protected void mousePressedImpl() {
    }

    // region Scene numberers.
    private int getDemoSceneNumber() {
        final String sceneName = super.SCENE_NAME;
        // This had me for about 15 minutes till I...
        // ..Till I learnt about `Character.getNumericValue()`! 😂:
        return Character.getNumericValue(sceneName.charAt(sceneName.length() - 1));
    }

    private int getNextDemoSceneNumber() {
        int sceneNumberToRet = this.getDemoSceneNumber();
        return switch (sceneNumberToRet) {
            case 5 -> 1;
            default -> ++sceneNumberToRet;
        };
    }

    private int getPreviousDemoSceneNumber() {
        int sceneNumberToRet = this.getDemoSceneNumber();
        return switch (sceneNumberToRet) {
            case 1 -> 5;
            default -> --sceneNumberToRet;
        };
    }
    // endregion

    // region Obtaining scenes.
    @SuppressWarnings("unchecked")
    private Class<? extends AbstractDemoScene> obtainDemoSceneFromSource(
            final int p_newSceneNumber) throws ClassNotFoundException {
        return (Class<? extends AbstractDemoScene>)
        /*   */ Class.forName(this.getClass().getPackageName() + ".DemoScene" + p_newSceneNumber);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private Class<? extends AbstractDemoScene> obtainDemoScene5FromData() throws MalformedURLException {
        return (Class<? extends AbstractDemoScene>) new NerdSceneClassLoader(
                /*   */ new URL("file", "", NerdSketch.fromDataDir("DemoScene5.class")).toString(),
                this.getClass().getPackageName() + ".DemoScene5").getLoadedClass();
    }
    // endregion

}
