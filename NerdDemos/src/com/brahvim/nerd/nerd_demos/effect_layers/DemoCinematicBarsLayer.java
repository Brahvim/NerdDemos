package com.brahvim.nerd.nerd_demos.effect_layers;

import com.brahvim.nerd.framework.scene_layer_api.NerdScene;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.layers.NerdP3dLayer;

import processing.core.PConstants;
import processing.opengl.PGraphics3D;

public class DemoCinematicBarsLayer extends NerdP3dLayer {

	protected DemoCinematicBarsLayer(final NerdScene<PGraphics3D> p_scene) {
		super(p_scene);
	}

	public float rectHeight = 60;

	@Override
	protected void draw() {
		this.GRAPHICS.fill(0);
		this.GRAPHICS.begin2d();
		this.GRAPHICS.rectMode(PConstants.CORNER);
		this.GRAPHICS.rect(0, 0, this.WINDOW.width, this.rectHeight);
		this.GRAPHICS.rect(0, this.WINDOW.height - this.rectHeight,
				this.WINDOW.width, this.WINDOW.height);
		this.GRAPHICS.end2d();
	}

}
