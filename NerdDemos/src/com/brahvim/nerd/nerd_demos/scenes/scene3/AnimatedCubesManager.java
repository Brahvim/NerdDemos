package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.math.easings_old.built_in_easings_old.NerdSineEaseOld;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP3dSketch;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

// TODO: Implement `PShape` batching and perhaps also culling.

public class AnimatedCubesManager {

	private class AnimatedCube {

		// region `static` constants:
		protected static final int
		/*   */ DEFAULT_SIZE = 40,
				DEFAULT_FRICTION = 1,
				DEFAULT_LIFETIME = 8_000,
				DEFAULT_ROTATIONAL_FRICTION = 1;
		protected static final boolean DEFAULT_VISIBILITY = true;
		protected static final float DEFAULT_DT_COEFFICIENT = 0.1f;
		// endregion

		protected final PVector
		/*   */ POSITION = new PVector(),
				VELOCITY = new PVector(),
				ROTATION = new PVector(),
				ACCELERATION = new PVector(),
				ROTATIONAL_VELOCITY = new PVector(),
				ROTATIONAL_ACCELERATION = new PVector();

		protected int endTime;
		protected boolean visible;
		protected float
		/*   */ size,
				frict,
				dtCoef,
				rotFrict;
		protected NerdSineEaseOld plopWave;

		// public AnimatedCube(final NerdSketch<?> p_sketch) {
		// this.plopWave = new NerdSineEaseOld(p_sketch);
		// this.plopWave.inactValue = 1;
		// }

	}

	// region Fields.
	public int
	/*   */ cubesToAdd = 0,
			cubesPerClick = 3,
			cubesPerFrame = 3;

	private final Queue<AnimatedCube> FREE_CUBES = new ArrayDeque<>(6000);
	private final List<AnimatedCube> CUBES = new ArrayList<>(6000);
	private final NerdP3dSketch SKETCH;
	private final NerdP3dScene SCENE;
	private final PShape CUBE_SHAPE;
	// endregion

	public AnimatedCubesManager(final NerdP3dScene p_scene) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();
		this.CUBE_SHAPE = this.SKETCH.createShape(PConstants.BOX, 1);
	}

	// region Cube methods.
	private AnimatedCube createAnimatedCube() {
		final AnimatedCube toRet = this.FREE_CUBES.isEmpty()
				? new AnimatedCube()
				: this.FREE_CUBES.poll();

		toRet.plopWave = new NerdSineEaseOld(this.SKETCH);
		toRet.plopWave.inactValue = 1;
		// toRet.plopWave.resetValues();
		this.CUBES.add(toRet);

		toRet.size = AnimatedCube.DEFAULT_SIZE;
		toRet.frict = AnimatedCube.DEFAULT_FRICTION;
		toRet.visible = AnimatedCube.DEFAULT_VISIBILITY;
		toRet.dtCoef = AnimatedCube.DEFAULT_DT_COEFFICIENT;
		toRet.rotFrict = AnimatedCube.DEFAULT_ROTATIONAL_FRICTION;
		toRet.endTime = this.SKETCH.millis() + AnimatedCube.DEFAULT_LIFETIME;

		toRet.VELOCITY.set(0, 0, 0);
		toRet.ROTATIONAL_VELOCITY.set(0, 0, 0);
		toRet.POSITION.set(this.SCENE.GRAPHICS.getCurrentCamera().POSITION);
		toRet.POSITION.add(
				this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cx, this.SKETCH.GENERIC_WINDOW.cx),
				this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cy, this.SKETCH.GENERIC_WINDOW.cy),
				this.SKETCH.random(-600, 600));

		toRet.ROTATION.set(
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU));

		toRet.ACCELERATION.set(
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f));

		toRet.ROTATIONAL_ACCELERATION.set(
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f));

		return toRet;
	}

	protected void drawCube(final AnimatedCube p_cube) {
		if (!p_cube.visible)
			return;

		final NerdP3dGraphics g = this.SCENE.GRAPHICS;

		try (var a = g.new Push()) {
			g.translate(p_cube.POSITION);
			g.rotate(p_cube.ROTATION);
			g.scale(p_cube.size * p_cube.plopWave.get());

			g.strokeWeight(0.018f);
			g.fill(255);

			g.shape(this.CUBE_SHAPE);
			// g.box(1);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCube(final AnimatedCube p_cube) {
		final float deltaTime = this.SKETCH.getFrameTime() * p_cube.dtCoef;

		// Physics calculations:

		p_cube.ROTATIONAL_VELOCITY.add(p_cube.ROTATIONAL_ACCELERATION);
		p_cube.ROTATION.add(PVector.mult(p_cube.ROTATIONAL_VELOCITY, p_cube.rotFrict + deltaTime));

		p_cube.VELOCITY.add(p_cube.ACCELERATION);
		p_cube.POSITION.add(PVector.mult(p_cube.VELOCITY, p_cube.frict + deltaTime));

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

		final List<AnimatedCube> cubesToRemove = new ArrayList<>();

		final int millis = this.SKETCH.millis();
		for (final var cube : this.CUBES) {
			// Is the cube's lifetime over? Play the plop-out animation!:

			if (!cube.visible)
				cubesToRemove.add(cube);

			if (cube.endTime - millis < 0)
				this.beginCubePlopOut(cube);

			// Draw the cube!:
			this.updateCube(cube);
			this.drawCube(cube);
		}

		// Batched removal:
		for (final var cube : cubesToRemove) {
			this.FREE_CUBES.offer(cube);
			this.CUBES.remove(cube);
		}
	}

	private void addCubesInLimit() {
		if (this.cubesToAdd == 0)
			return;

		for (int i = 0; //
				i < this.cubesPerFrame || this.cubesToAdd == 0; //
				++i, --this.cubesToAdd) // Yes, two things at once! Not just a C++ feature!
			this.beginCubePlopIn(this.createAnimatedCube());

		// if (this.cubesToAdd != 0)
		// for (int i = 0; i != this.cubesPerFrame; ++i) {
		// if (--this.cubesToAdd == 0)
		// break;

		// this.beginCubePlopIn(this.createAnimatedCube());
		// }
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
