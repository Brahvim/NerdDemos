package com.brahvim.nerd.nerd_demos.debug_layers;

import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.framework.scene_layer_api.NerdScene;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.layers.NerdP3dLayer;

import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class DemoDebugFpsGizmoLayer extends NerdP3dLayer {

	protected DemoDebugFpsGizmoLayer(final NerdScene<PGraphics3D> p_scene) {
		super(p_scene);
	}

	@Override
	protected void draw() {
		try (var a = super.GRAPHICS.new TwoDimensionalPush()) {
			this.showFps();
			this.showHeldKeys();
			// this.showAxesGizmo();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void showFps() {
		super.GRAPHICS.textSize(42);
		super.GRAPHICS.centeredText(Float.toString(this.SKETCH.frameRate));
	}

	private void showHeldKeys() {
		final String heldKeys = this.INPUT.getHeldKeysDebugString();
		final float xPos = super.WINDOW.width - this.SKETCH.textWidth(heldKeys);

		super.GRAPHICS.translate(xPos, 0);
		super.GRAPHICS.centeredText(heldKeys);
		super.GRAPHICS.translate(-xPos, 0);
	}

	@SuppressWarnings("unused")
	private void showAxesGizmo() {
		super.GRAPHICS.push();
		super.GRAPHICS.noStroke();
		super.GRAPHICS.translate(super.WINDOW.cx, super.WINDOW.cy);

		final NerdFlyCamera camera = super.GRAPHICS.getCurrentCamera();
		final PVector camUp = camera.ORIENTATION;

		final PVector rotDir = camera.FRONT.copy().normalize();
		final float camRoll = camUp.x;
		// final int axesBarsGap = 50;
		super.GRAPHICS.rotateZ(camRoll);
		super.GRAPHICS.rotate(rotDir);
		super.GRAPHICS.rotate(rotDir.z, rotDir.y, rotDir.x);

		// "R-B-G" color configuration.

		super.GRAPHICS.box(200, 25, 50);
		super.GRAPHICS.pop();
	}

}
