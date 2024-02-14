package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.window_management.NerdInputModule;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class SmoothCamera extends NerdFlyCamera {

    // region Fields.
    public static final float
    /*   */ DEFAULT_ACC_FRICT = 0.9f,
            DEFAULT_VEL_FRICT = 0.9f;

    public static final float
    /*   */ SLOW_SPEED = 0.05f,
            DEFAULT_SPEED = 0.5f;

    public static final float
    /*   */ ROLL_MAX = 5,
            ROLL_SPEED = 0.1f;

    public float
    /*   */ accFrict = SmoothCamera.DEFAULT_ACC_FRICT,
            velFrict = SmoothCamera.DEFAULT_VEL_FRICT;

    private final NerdInputModule<PGraphics3D> INPUT;

    private PVector circumAmbPos = new PVector();
    private PVector accVec = new PVector(), velVec = new PVector();
    // endregion

    // region Construction.
    public SmoothCamera(final NerdP3dGraphics p_graphics) {
        super(p_graphics);
        this.INPUT = super.SKETCH.getNerdInputModule();
    }

    public SmoothCamera(final NerdP3dGraphics p_graphics, final PVector p_defaultFront) {
        super(p_graphics, p_defaultFront);
        this.INPUT = super.SKETCH.getNerdInputModule();
    }
    // endregion

    // region Getters and setters.
    public PVector getVelVec() {
        return this.velVec;
    }

    public PVector getAccVec() {
        return this.accVec;
    }

    public PVector setVelVec(final PVector p_vec) {
        return this.velVec = Objects.requireNonNull(p_vec);
    }

    public PVector setAccVec(final PVector p_vec) {
        return this.accVec = Objects.requireNonNull(p_vec);
    }

    public PVector getCircumAmbPos() {
        return this.circumAmbPos;
    }

    public PVector setCircumAmbPos(final PVector p_vec) {
        return this.circumAmbPos = p_vec;
    }
    // endregion

    @Override
    public void apply() {
        this.controlCamera();
        super.apply();
    }

    private void controlCamera() {
        // Increase speed when holding `Ctrl`:
        final float accMultiplier;

        // Reset these so holding `ALT` changes them:
        this.accFrict = SmoothCamera.DEFAULT_ACC_FRICT;
        this.velFrict = SmoothCamera.DEFAULT_VEL_FRICT;

        // if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_CONTROL)) {
        // accMultiplier = SmoothCamera.FAST_SPEED;
        // } else
        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_ALT)) {
            accMultiplier = SmoothCamera.SLOW_SPEED;
            this.accFrict = this.velFrict = 0.95f;
        } else {
            accMultiplier = SmoothCamera.DEFAULT_SPEED;
        }

        // region Roll.
        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_Z))
            super.ORIENTATION.z += this.ROLL_SPEED * 0.5f;

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_C))
            super.ORIENTATION.z -= this.ROLL_SPEED * 0.5f;

        // TODO: Set the camera roll's boundary!
        // if (super.ORIENTATION.z > ROLL_MAX)
        // super.ORIENTATION.z = -ROLL_MAX;

        // if (super.ORIENTATION.z < -ROLL_MAX)
        // super.ORIENTATION.z = ROLL_MAX;

        // if (super.up.x > PConstants.TAU || super.up.x < -PConstants.TAU)
        // super.up.x -= super.up.x;
        // endregion

        // region Elevation.
        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_SPACE))
            this.accVec.y += -accMultiplier;

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_SHIFT))
            this.accVec.y += accMultiplier;
        // endregion

        // region Circumambulation, id est "moving in circles".
        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_Q))

        {
            if (!this.INPUT.keyGivenWasPressed(KeyEvent.VK_Q))
                this.circumAmbPos.set(super.FRONT); // PVector.sub(super.front, super.pos));

            // super.front.set(this.circumAmbPos);

            super.POSITION.x += PApplet.sin(this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
            super.POSITION.z += PApplet.cos(this.SKETCH.millis() * 0.01f * accMultiplier) * 50;

            super.FRONT.x = PApplet.sin(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
            super.FRONT.z = PApplet.cos(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
        }

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_E)) {
            if (!this.INPUT.keyGivenWasPressed(KeyEvent.VK_E))
                this.circumAmbPos.set(super.FRONT); // PVector.sub(super.front, super.pos));

            // super.front.set(this.circumAmbPos);

            super.POSITION.x += PApplet.sin(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
            super.POSITION.z += PApplet.cos(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;

            super.FRONT.x = PApplet.sin(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
            super.FRONT.z = PApplet.cos(-this.SKETCH.millis() * 0.01f * accMultiplier) * 50;
        }
        // endregion

        // region `W`-`A`-`S`-`D` controls.
        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_W))
            this.accVec.z += -accMultiplier;

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_A))
            this.accVec.x += -accMultiplier;

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_S))
            this.accVec.z += accMultiplier;

        if (this.INPUT.keyGivenIsPressed(KeyEvent.VK_D))
            this.accVec.x += accMultiplier;
        // endregion

        this.accVec.mult(this.accFrict);
        this.velVec.add(this.accVec);
        this.velVec.mult(this.velFrict);

        final float deltaTime = super.SKETCH.getFrameTime() * 0.15f;
        // this.velVec.mult(deltaTime);

        super.moveX(this.velVec.x * deltaTime);
        super.moveY(this.velVec.y * deltaTime);
        super.moveZ(this.velVec.z * deltaTime);
    }

}
