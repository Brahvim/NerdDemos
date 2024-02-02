package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;

import processing.opengl.PGraphics3D;

public class DemoScene5 extends AbstractDemoScene {

	private final String TEXT = String.format(
			"Welcome to `%s`!", super.SCENE_NAME);

	protected DemoScene5(final NerdScenesModule<PGraphics3D> p_sceneMan) {
		super(p_sceneMan);
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		System.out.println(this.TEXT);
		// TODO: Reset the camera's settings when the scene is changed.
		// super.GRAPHICS.getCurrentCamera().POSITION.x = 500;
		super.GRAPHICS.getCurrentCamera().setClearColor(0x006699);
	}

	@Override
	protected void drawImpl() {
		// super.GRAPHICS.background(0x006699,
		// PApplet.sin(super.getMillisSinceStart()));

		// try (var a = super.GRAPHICS.new TwoDimensionalPush()) {
		super.GRAPHICS.scale(2);
		super.GRAPHICS.fill(233);
		super.GRAPHICS.textAt2dCenter(this.TEXT);
		// } catch (final Exception e) {
		// e.printStackTrace();
		// }

		// super.GRAPHICS.circle(
		// super.INPUT.mouseX - this.WINDOW.cx,
		// super.INPUT.mouseY - this.WINDOW.cy, 20);
		// super.GRAPHICS.circle(super.GRAPHICS.getMouseInWorld(), 20);
	}

}
