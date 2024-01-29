package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;

import processing.core.PConstants;
import processing.opengl.PGraphics3D;

public abstract class AbstractDemoScene extends NerdP3dScene {

    protected AbstractDemoScene(final NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    protected void drawImpl() {
    }

    @Override
    protected final void draw() {
        // Faster in `draw()`:
        if (super.INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
            super.MANAGER.restartScene();

        this.drawImpl();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void mousePressed() {
        if (super.INPUT.mouseButton != PConstants.RIGHT)
            return;

        try {
            final int newSceneNumber = super.INPUT.keysGivenWerePressed(KeyEvent.VK_SHIFT)
                    ? this.getPreviousDemoSceneNumber()
                    : this.getNextDemoSceneNumber();

            final Class<? extends AbstractDemoScene> newSceneClass
            /*   */ = (Class<? extends AbstractDemoScene>)
            /*   */ Class.forName(this.getClass().getPackageName() + ".DemoScene" + newSceneNumber);
            super.MANAGER.startScene(newSceneClass);
        } catch (final Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't start the next scene :/");
        }
    }

    private int getDemoSceneNumber() {
        final String sceneName = super.SCENE_NAME;
        // This had me for about 15 minutes till I...
        // ..Till I learnt about `Character.getNumericValue()`! 😂:
        return Character.getNumericValue(sceneName.charAt(sceneName.length() - 1));
    }

    protected void mousePressedImpl() {
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

}
