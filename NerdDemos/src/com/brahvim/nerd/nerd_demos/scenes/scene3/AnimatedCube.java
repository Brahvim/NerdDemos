package com.brahvim.nerd.nerd_demos.scenes.scene3;

import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.math.easings_old.built_in_easings_old.NerdSineEaseOld;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class AnimatedCube extends TestEulerBody {

	// region Fields.
	public static final int DEFAULT_LIFETIME = 8_000;

	public float size = 40;
	public int startTime, lifetime;
	public int fillColor = Integer.MAX_VALUE, strokeColor = 0;

	private boolean visible = true;
	private final NerdSineEaseOld PLOP_WAVE;
	// private AlSource popSrc;
	// endregion

	public AnimatedCube(final NerdP3dScene p_scene) {
		super(p_scene.getSketch());

		this.startTime = this.SKETCH.millis();
		this.lifetime = this.startTime + AnimatedCube.DEFAULT_LIFETIME;

		final PVector cameraPos = p_scene.GRAPHICS.getCurrentCamera().getPos();

		super.pos.set(
				cameraPos.x + super.SKETCH.random(-super.SKETCH.GENERIC_WINDOW.cx, super.SKETCH.GENERIC_WINDOW.cx),
				cameraPos.y + super.SKETCH.random(-super.SKETCH.GENERIC_WINDOW.cy, super.SKETCH.GENERIC_WINDOW.cy),
				cameraPos.z + super.SKETCH.random(-600, 600));

		super.acc.set(
				super.SKETCH.random(-0.01f, 0.01f),
				super.SKETCH.random(-0.01f, 0.01f),
				super.SKETCH.random(-0.01f, 0.01f));

		super.rot.set(
				super.SKETCH.random(PConstants.TAU),
				super.SKETCH.random(PConstants.TAU),
				super.SKETCH.random(PConstants.TAU));

		super.rotAcc.set(
				super.SKETCH.random(-0.0001f, 0.0001f),
				super.SKETCH.random(-0.0001f, 0.0001f),
				super.SKETCH.random(-0.0001f, 0.0001f));

		this.PLOP_WAVE = new NerdSineEaseOld(super.SKETCH);
		this.PLOP_WAVE.inactValue = 1;
	}

	// region Getters and setters for superclass stuff!:
	public float getFrict() {
		return super.frict;
	}

	public void setFrict(final float p_frict) {
		super.frict = p_frict;
	}

	public float getRotFrict() {
		return super.rotFrict;
	}

	public void setRotFrict(final float p_rotFrict) {
		super.rotFrict = p_rotFrict;
	}

	public PVector getPos() {
		return super.pos;
	}

	public PVector getVel() {
		return super.vel;
	}

	public PVector getAcc() {
		return super.acc;
	}

	public PVector getRot() {
		return super.rot;
	}

	public PVector getRotVel() {
		return super.rotVel;
	}

	public PVector getRotAcc() {
		return super.rotAcc;
	}
	// endregion

	// public AnimatedCube plopIn(final AlBuffer<?> p_popAudioBuffer) {
	// if (p_popAudioBuffer == null)
	// return this;
	//
	// // this.popSrc = new AlSource(App.openAl, p_popAudioBuffer);
	// // this.popSrc.setPosition(super.pos.array());
	// // this.popSrc.setGain(500);
	// // this.popSrc.playThenDispose();
	//
	// return this.plopIn();
	// }

	public AnimatedCube plopIn() {
		this.visible = true;
		this.PLOP_WAVE.parameterCoef = 0.015f;
		this.PLOP_WAVE.endWhenAngleIncrementsBy(PConstants.HALF_PI).start();
		return this;
	}

	public AnimatedCube plopOut(final Runnable p_runnable) {
		this.PLOP_WAVE.parameterCoef = 0.00001f;
		this.PLOP_WAVE.endWhenAngleIncrementsBy(PConstants.HALF_PI)
				.start(270, () -> {
					p_runnable.run();
					this.visible = false;
					// this.popSrc.dispose();
				});
		return this;
	}

	// public AlSource getAudioSource() {
	// return this.popSrc;
	// }

	public boolean isVisible() {
		return this.visible;
	}

	public void draw(final PShape p_shape) {
		if (!this.visible)
			return;

		// if (!this.popSrc.isDisposed())
		// this.popSrc.setPosition(super.pos.array());

		final NerdP3dGraphics g = (NerdP3dGraphics) super.SKETCH.getNerdGenericGraphics();

		super.integrate();
		g.push();
		g.translate(super.pos);
		g.rotate(super.rot);
		g.scale(this.size * this.PLOP_WAVE.get());

		// Performance drop + doesn't work!:
		// for (int i = 0; i < p_shape.getVertexCount(); i++)
		// p_shape.setFill(i, SKETCH.color(255, PApplet.map(SKETCH.millis(),
		// this.startTime, this.lifetime, 0, 255)));

		g.shape(p_shape);
		g.pop();
	}

}
