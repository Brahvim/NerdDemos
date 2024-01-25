package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.util.ArrayList;
import java.util.Objects;

import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.processing_wrapper.sketches.NerdP3dSketch;

import processing.core.PConstants;
import processing.core.PShape;

public class CubeManager {

	// region Fields.
	public int cubesPerClick = 7,
			cubesPerFrame = 2;

	private final ArrayList<AnimatedCube> CUBES = new ArrayList<>();
	private final NerdP3dSketch SKETCH;
	private final NerdP3dScene SCENE;

	// private AlBuffer<?>[] popAudios;
	private PShape cubeShape;
	private int cubesToAdd;
	// endregion

	// region Constructors.
	public CubeManager(final NerdP3dScene p_scene) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();

		this.cubeShape = this.SKETCH.createShape(PConstants.BOX, 1);
		this.cubeShape.setStrokeWeight(0.28f);
	}

	public CubeManager(final NerdP3dScene p_scene, final PShape p_cubeShape) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();
		this.cubeShape = p_cubeShape;
	}

	// public CubeManager(final NerdP3dScene p_scene, final AlBuffer<?>[] p_buffers)
	// {
	// this.SCENE = Objects.requireNonNull(p_scene);
	// this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();
	// this.popAudios = p_buffers;
	//
	// this.cubeShape = this.SKETCH.createShape(PConstants.BOX, 1);
	// this.cubeShape.setStrokeWeight(0.28f);
	// }

	// public CubeManager(final NerdP3dScene p_scene,
	// final PShape p_cubeShape, final AlBuffer<?>[] p_buffers) {
	// this.SCENE = Objects.requireNonNull(p_scene);
	// this.SKETCH = (NerdP3dSketch) this.SCENE.getSketch();
	// this.cubeShape = p_cubeShape;
	// this.popAudios = p_buffers;
	// }
	// endregion

	public void draw() {
		if (this.cubesToAdd != 0)
			for (int i = 0; i != this.cubesPerFrame; i++) {
				if (--this.cubesToAdd == 0)
					break;

				// if (this.popAudios == null)
				this.CUBES.add(new AnimatedCube(this.SCENE).plopIn(/* null */));
				// else {
				// final AlBuffer<?> randomPop = this.popAudios[(int)
				// this.SKETCH.random(this.popAudios.length)];
				// this.CUBES.add(new AnimatedCube(this.SCENE).plopIn(randomPop));
				// }
			}

		for (int i = this.CUBES.size() - 1; i != -1; i--) {
			final AnimatedCube cube = this.CUBES.get(i);
			// final PVector pos = cube.getPos();

			// if (!CollisionAlgorithms.ptRect(
			// pos.x, pos.y,
			// -5000, -5000,
			// 5000, 5000)) { // No, I did not take the camera's position into account.
			// Whoops.
			// // this.CUBES.remove(i); // `this.cubesToRemove++;`
			// final int cubeId = i;
			// cube.plopOut(() -> this.CUBES.remove(cubeId));
			// }

			if (cube.lifetime - this.SKETCH.millis() < 0) {
				final int cubeId = i; // For clarity :>
				cube.plopOut(() -> this.CUBES.remove(cubeId));
			}
		}

		// Used to let the cubes 'plop-out', though unused right now!
		// for (int i = this.cubesToRemove; i != 0; i--) {
		// final AnimatedCube cube = this.CUBES.get(i);
		// cube.plopOut(() -> this.CUBES.remove(cube));
		// }

		// for (final AnimatedCube c : this.CUBES)
		// c.draw(this.CUBE_SHAPE);

		for (int i = this.CUBES.size() - 1; i != -1; i--) {
			final AnimatedCube cube = this.CUBES.get(i);
			// System.out.printf(
			// "Listener position: `[ %f, %f, %f ]`. Cube position:`%s`.%n",
			// App.OPENAL.getListenerPosition()[0],
			// App.OPENAL.getListenerPosition()[1],
			// App.OPENAL.getListenerPosition()[2],
			// cube.getPos());
			cube.draw(this.cubeShape);
		}
	}

	public void emitCubes(final int p_howMany) {
		// System.out.println("Emitting `" + p_howMany + "` cubes!");
		this.cubesToAdd += p_howMany;
	}

	public void removeAll() {
		this.CUBES.clear();
		// this.cubesToRemove = this.CUBES.size() - 1; // I dunno how to do this.
	}

	public int numCubes() {
		return this.CUBES.size();
	}

	public void setShape(final PShape p_cubeShape) {
		this.cubeShape = Objects.requireNonNull(p_cubeShape);
	}

}
