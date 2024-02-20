package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Queue;

import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.math.easings_old.built_in_easings_old.NerdSineEaseOld;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP3dSketch;

import processing.core.PConstants;
import processing.core.PVector;

// TODO: Implement `PShape` batching and perhaps also culling.

public class AnimatedCubesManager {

	private class AnimatedCube {

		protected static final int
		/*   */ DEFAULT_SIZE = 40,
				DEFAULT_FRICTION = 1,
				DEFAULT_LIFETIME = 8_000,
				DEFAULT_ROTATIONAL_FRICTION = 1;

		protected static final boolean DEFAULT_VISIBILITY = true;
		protected static final float DEFAULT_DT_COEFFICIENT = 0.1f;

		protected final NerdSineEaseOld PLOP_WAVE;
		protected final PVector
		/*   */ POSITION = new PVector(),
				VELOCITY = new PVector(),
				ROTATION = new PVector(),
				ACCELERATION = new PVector(),
				ROTATION_VELOCITY = new PVector(),
				ROTATIONAL_ACCELERATION = new PVector();

		protected int endTime;
		protected boolean visible = AnimatedCube.DEFAULT_VISIBILITY;
		protected float
		/*   */ size = AnimatedCube.DEFAULT_SIZE,
				frict = AnimatedCube.DEFAULT_FRICTION,
				dtCoef = AnimatedCube.DEFAULT_DT_COEFFICIENT,
				rotFrict = AnimatedCube.DEFAULT_ROTATIONAL_FRICTION;

		public AnimatedCube(final NerdSketch<?> p_sketch) {
			this.PLOP_WAVE = new NerdSineEaseOld(p_sketch);
			this.PLOP_WAVE.inactValue = 1;
		}

	}

	// region Fields.
	public int
	/*   */ cubesToAdd = 0,
			cubesPerClick = 7,
			cubesPerFrame = 2;

	private final Queue<AnimatedCube> FREE_CUBES = new ArrayDeque<>(3000);
	private final List<AnimatedCube> CUBES = new ArrayList<>(3000);
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
			toRet = new AnimatedCube(this.SKETCH);
		else {
			toRet = this.FREE_CUBES.poll();
			this.FREE_CUBES.remove(toRet);
		}

		this.CUBES.add(toRet);

		final int cubeStartTime = this.SKETCH.millis();
		toRet.endTime = cubeStartTime + AnimatedCube.DEFAULT_LIFETIME;

		final PVector cameraPos = this.SCENE.GRAPHICS.getCurrentCamera().POSITION;

		toRet.POSITION.set(
				cameraPos.x + this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cx, this.SKETCH.GENERIC_WINDOW.cx),
				cameraPos.y + this.SKETCH.random(-this.SKETCH.GENERIC_WINDOW.cy, this.SKETCH.GENERIC_WINDOW.cy),
				cameraPos.z + this.SKETCH.random(-600, 600));

		toRet.ACCELERATION.set(
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f),
				this.SKETCH.random(-0.01f, 0.01f));

		toRet.ROTATION.set(
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU),
				this.SKETCH.random(PConstants.TAU));

		toRet.ROTATIONAL_ACCELERATION.set(
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f),
				this.SKETCH.random(-0.0001f, 0.0001f));

		// toRet.PLOP_WAVE.resetSettings();

		return toRet;
	}

	protected void drawCube(final AnimatedCube p_cube) {
		if (!p_cube.visible)
			return;

		final float deltaTime = this.SKETCH.getFrameTime() * p_cube.dtCoef;

		// Physics calculations:
		p_cube.VELOCITY.add(p_cube.ACCELERATION);
		p_cube.ROTATION_VELOCITY.add(p_cube.ROTATIONAL_ACCELERATION);
		p_cube.POSITION.add(PVector.mult(p_cube.VELOCITY, p_cube.frict + deltaTime));
		p_cube.ROTATION.add(PVector.mult(p_cube.ROTATION_VELOCITY, p_cube.rotFrict + deltaTime));

		final NerdP3dGraphics g = this.SCENE.GRAPHICS;

		try (var a = g.new Push()) {
			g.translate(p_cube.POSITION);
			g.rotate(p_cube.ROTATION);
			g.scale(p_cube.size * p_cube.PLOP_WAVE.get());
			g.box(1);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected AnimatedCube beginCubePlopIn(final AnimatedCube p_cube) {
		p_cube.visible = true;
		p_cube.PLOP_WAVE.parameterCoef = 0.015f;
		p_cube.PLOP_WAVE.endWhenAngleIncrementsBy(PConstants.HALF_PI).start();
		return p_cube;
	}

	protected AnimatedCube beginCubePlopOut(final AnimatedCube p_cube) {
		p_cube.PLOP_WAVE.parameterCoef = 0.00001f;
		p_cube.PLOP_WAVE.endWhenAngleIncrementsBy(PConstants.HALF_PI)
				.start(270, () -> p_cube.visible = false);

		return p_cube;
	}
	// endregion

	public void draw() {
		this.addCubesInLimit();

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
			this.SCENE.GRAPHICS.strokeWeight(0.018f);
			this.SCENE.GRAPHICS.fill(255);
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
