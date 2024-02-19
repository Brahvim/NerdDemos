package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.math.easings_old.built_in_easings_old.NerdSineEaseOld;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP3dSketch;

import processing.core.PConstants;
import processing.core.PVector;

// TODO: Implement `PShape` batching and perhaps also culling.

public class AnimatedCubesManager {

	private class AnimatedCube {

		public static final int DEFAULT_LIFETIME = 8_000;

		protected float size = 40;
		protected PVector pos, vel, acc;
		protected boolean visible = true;
		protected int startTime, endTime;
		protected NerdSineEaseOld plopWave;
		protected PVector rot, rotVel, rotAcc;
		protected float frict = 1, rotFrict = 1, dtCoef = 0.1f;

	}

	// region Fields.
	public int
	/*   */ cubesToAdd = 0,
			cubesPerClick = 7,
			cubesPerFrame = 2;

	private final List<AnimatedCube> FREE_CUBES = new ArrayList<>(6000);
	private final List<AnimatedCube> CUBES = new ArrayList<>(6000);
	private final NerdP3dSketch SKETCH;
	private final NerdP3dScene SCENE;
	// endregion

	// region Constructors.
	public AnimatedCubesManager(final NerdP3dScene p_scene) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();
	}
	// endregion

	// region Cube methods.
	private AnimatedCube createAnimatedCube() {
		final AnimatedCube toRet;

		if (this.FREE_CUBES.isEmpty())
			toRet = new AnimatedCube();
		else {
			toRet = this.FREE_CUBES.get(0);
			this.FREE_CUBES.remove(toRet);
		}

		this.CUBES.add(toRet);
		toRet.startTime = this.SKETCH.millis();
		toRet.endTime = toRet.startTime + AnimatedCube.DEFAULT_LIFETIME;

		final PVector cameraPos = this.SCENE.GRAPHICS.getCurrentCamera().POSITION;

		toRet.vel = new PVector();

		toRet.rotVel = new PVector();

		toRet.pos = new PVector(
				cameraPos.x + this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cx, this.SKETCH.GENERIC_WINDOW.cx),
				cameraPos.y + this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cy, this.SKETCH.GENERIC_WINDOW.cy),
				cameraPos.z + this.SKETCH.random(-600, 600));

		toRet.acc = new PVector(
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f));

		toRet.rot = new PVector(
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU));

		toRet.rotAcc = new PVector(
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f));

		toRet.plopWave = new NerdSineEaseOld(this.SKETCH);
		toRet.plopWave.inactValue = 1;

		return toRet;
	}

	protected void drawCube(final AnimatedCube p_cube) {
		if (!p_cube.visible)
			return;

		final float deltaTime = this.SKETCH.getFrameTime() * p_cube.dtCoef;

		// Physics calculations:
		p_cube.vel.add(p_cube.acc);
		p_cube.rotVel.add(p_cube.rotAcc);
		p_cube.pos.add(PVector.mult(p_cube.vel, p_cube.frict + deltaTime));
		p_cube.rot.add(PVector.mult(p_cube.rotVel, p_cube.rotFrict + deltaTime));

		final NerdP3dGraphics g = this.SCENE.GRAPHICS;

		try (var a = g.new Push()) {
			g.translate(p_cube.pos);
			g.rotate(p_cube.rot);
			g.scale(p_cube.size * p_cube.plopWave.get());
			g.strokeWeight(0.018f);
			g.fill(255);
			g.box(1);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected AnimatedCube beginCubePlopIn(final AnimatedCube p_cube) {
		p_cube.visible = true;
		p_cube.plopWave.parameterCoef = 0.015f;
		p_cube.plopWave.endWhenAngleIncrementsBy(PConstants.HALF_PI).start();
		return p_cube;
	}

	protected AnimatedCube beginCubePlopOut(final AnimatedCube p_cube) {
		p_cube.plopWave.parameterCoef = 0.00001f;
		p_cube.plopWave.endWhenAngleIncrementsBy(PConstants.HALF_PI)
				.start(270, () -> p_cube.visible = false);

		return p_cube;
	}
	// endregion

	public void draw() {
		this.addCubesInLimit();

		for (final AnimatedCube cube : this.CUBES)
			if (this.FREE_CUBES.contains(cube))
				System.out.println(cube);

		final ListIterator<AnimatedCube> it = this.CUBES.listIterator();

		while (it.hasNext()) {
			final AnimatedCube cube = it.next();

			// Is the cube's lifetime over? Play the plop-out animation!:
			if (cube.endTime - this.SKETCH.millis() < 0)
				this.beginCubePlopOut(cube);

			// If it actually isn't visible anymore, remove it:
			if (!cube.visible) {
				it.remove();
				this.FREE_CUBES.add(cube);
			}

			// Draw the cube!:
			this.drawCube(cube);
		}
	}

	private void addCubesInLimit() {
		if (this.cubesToAdd == 0)
			return;

		for (int i = 0; //
				i <= this.cubesPerFrame || this.cubesToAdd == 0; //
				i++, --this.cubesToAdd) { // Yes, two things at once! Not just a C++ feature!
			this.beginCubePlopIn(this.createAnimatedCube());
		}
	}

	// region Cube-collection queries.
	public int numCubes() {
		return this.CUBES.size();
	}

	public void removeAll() {
		this.CUBES.clear();
	}

	public void emitCubes(final int p_howMany) {
		this.cubesToAdd += p_howMany;
	}
	// endregion

}
