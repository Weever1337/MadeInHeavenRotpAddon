package com.weever.rotp_mih.client.render.entity.model.stand;

import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPoseTransition;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPoseTransitionMultiple;
import com.github.standobyte.jojo.client.render.entity.pose.RotationAngle;
import com.github.standobyte.jojo.client.render.entity.pose.anim.PosedActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandPose;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MihModel extends HumanoidStandModel<MihEntity> {
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer horse;
    private final ModelRenderer line;
    private final ModelRenderer leftLeg2;
    private final ModelRenderer leftLegJoint2;
    private final ModelRenderer leftLowerLeg2;
    private final ModelRenderer rightLeg2;
    private final ModelRenderer rightLegJoint2;
    private final ModelRenderer rightLowerLeg2;
    private final ModelRenderer saddle;

    public MihModel() {
        super();

        addHumanoidBaseBoxes(null);
        texWidth = 128;
        texHeight = 128;

        head.texOffs(32, 12).addBox(-3.0F, -3.8F, -4.0F, 6.0F, 2.0F, 1.0F, 0.1F, false);
        head.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.1F, false);
        head.texOffs(118, 0).addBox(-2.0F, -6.8F, -4.5F, 4.0F, 4.0F, 1.0F, 0.1F, false);
        head.texOffs(56, 0).addBox(-3.0F, -9.2F, -3.0F, 6.0F, 1.0F, 6.0F, 0.1F, false);
        head.texOffs(98, 0).addBox(-4.0F, -5.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.2F, false);


        torso.texOffs(0, 52).addBox(-4.0F, 1.0F, -2.5F, 8.0F, 4.0F, 1.0F, -0.1F, false);
        torso.texOffs(0, 57).addBox(-4.0F, 0.2F, -2.5F, 8.0F, 2.0F, 5.0F, 0.1F, false);
        torso.texOffs(29, 59).addBox(-5.0F, 8.0F, -6.0F, 10.0F, 10.0F, 11.0F, 0.0F, false);
        torso.texOffs(108, 4).addBox(-1.5F, 13.0F, 4.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        torso.texOffs(94, 0).addBox(-0.5F, 5.0F, 7.5F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        torso.texOffs(100, 4).addBox(-0.5F, 4.0F, 2.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        torso.texOffs(108, 4).addBox(-1.5F, 3.0F, 1.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        torso.texOffs(98, 4).addBox(-0.5F, 14.0F, 5.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        torso.texOffs(0, 36).addBox(-5.0F, 8.0F, -3.5F, 10.0F, 9.0F, 7.0F, 0.5F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(3.0F, 49.0F, 4.0F);
        torso.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
        cube_r1.texOffs(118, 0).addBox(-6.0F, -38.8F, -3.5F, 4.0F, 4.0F, 1.0F, 0.1F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(7.0F, 31.0F, 0.0F);
        torso.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2618F);
        cube_r2.texOffs(0, 16).addBox(-5.0F, -38.4F, 1.5F, 1.0F, 8.0F, 1.0F, 0.1F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(5.0F, 31.0F, 0.0F);
        torso.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, -0.1745F);
        cube_r3.texOffs(0, 16).addBox(-5.0F, -37.8F, 1.5F, 1.0F, 8.0F, 1.0F, 0.1F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-3.0F, 49.0F, -4.0F);
        torso.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 1.5708F, 0.0F);
        cube_r4.texOffs(118, 0).addBox(-6.0F, -38.8F, -3.5F, 4.0F, 4.0F, 1.0F, 0.1F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(3.0F, 31.0F, 0.0F);
        torso.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.0873F);
        cube_r5.texOffs(0, 16).addBox(-5.0F, -37.8F, 1.6F, 1.0F, 8.0F, 1.0F, 0.1F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(-3.0F, 31.0F, 0.0F);
        torso.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.0873F);
        cube_r6.texOffs(0, 16).addBox(4.0F, -37.8F, 1.6F, 1.0F, 8.0F, 1.0F, 0.1F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-5.0F, 31.0F, 0.0F);
        torso.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.1745F);
        cube_r7.texOffs(0, 16).addBox(4.0F, -37.8F, 1.5F, 1.0F, 8.0F, 1.0F, 0.1F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-7.0F, 31.0F, 0.0F);
        torso.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, 0.2618F);
        cube_r8.texOffs(0, 16).addBox(4.0F, -38.4F, 1.5F, 1.0F, 8.0F, 1.0F, 0.1F, false);



        horse = new ModelRenderer(this);
        horse.setPos(0.0F, 14.0F, -3.0F);
        torso.addChild(horse);
        horse.texOffs(34, 35).addBox(-2.0F, -12.0F, -3.0F, 4.0F, 12.0F, 7.0F, 0.0F, false);
        horse.texOffs(28, 52).addBox(-1.0F, -17.0F, 4.0F, 2.0F, 16.0F, 2.0F, 0.0F, false);
        horse.texOffs(16, 80).addBox(-3.0F, -17.0F, -3.0F, 6.0F, 5.0F, 7.0F, 0.0F, false);
        horse.texOffs(0, 4).addBox(0.5F, -19.0F, 2.99F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        horse.texOffs(0, 4).addBox(-2.5F, -19.0F, 2.99F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        horse.texOffs(112, 5).addBox(-2.0F, -17.5F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        horse.texOffs(4, 16).addBox(-2.0F, -17.0F, -8.0F, 4.0F, 5.0F, 5.0F, 0.0F, false);
        horse.texOffs(27, 35).addBox(-2.0F, -17.0F, -5.0F, 4.0F, 5.0F, 2.0F, 0.25F, false);
        horse.texOffs(20, 64).addBox(2.0F, -15.0F, -6.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        horse.texOffs(20, 64).addBox(-3.0F, -15.0F, -6.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        line = new ModelRenderer(this);
        line.setPos(0.0F, -14.5F, -4.0F);
        horse.addChild(line);
        line.texOffs(0, 10).addBox(3.1F, -1.5F, -1.5F, 0.0F, 3.0F, 16.0F, 0.0F, false);
        line.texOffs(0, 10).addBox(-3.1F, -1.5F, -1.5F, 0.0F, 3.0F, 16.0F, 0.0F, false);


        saddle = new ModelRenderer(this);
        saddle.setPos(0.0F, 29.0F, 0.0F);
        torso.addChild(saddle);


        rightLeg2 = new ModelRenderer(this);
        rightLeg2.setPos(-1.9F, -11.0F, 0.0F);
        saddle.addChild(rightLeg2);
        rightLeg2.texOffs(48, 108).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        rightLegJoint2 = new ModelRenderer(this);
        rightLegJoint2.setPos(0.0F, 6.0F, 0.0F);
        rightLeg2.addChild(rightLegJoint2);
        rightLegJoint2.texOffs(48, 102).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, false);

        rightLowerLeg2 = new ModelRenderer(this);
        rightLowerLeg2.setPos(0.0F, 6.0F, 0.0F);
        rightLeg2.addChild(rightLowerLeg2);
        rightLowerLeg2.texOffs(48, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.001F, false);

        leftLeg2 = new ModelRenderer(this);
        leftLeg2.setPos(1.9F, -11.0F, 0.0F);
        saddle.addChild(leftLeg2);
        leftLeg2.texOffs(80, 108).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        leftLegJoint2 = new ModelRenderer(this);
        leftLegJoint2.setPos(0.0F, 6.0F, 0.0F);
        leftLeg2.addChild(leftLegJoint2);
        leftLegJoint2.texOffs(80, 102).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, -0.1F, true);

        leftLowerLeg2 = new ModelRenderer(this);
        leftLowerLeg2.setPos(0.0F, 6.0F, 0.0F);
        leftLeg2.addChild(leftLowerLeg2);
        leftLowerLeg2.texOffs(80, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.001F, false);

    }


    @Override
    protected RotationAngle[][] initSummonPoseRotations() {
        return new RotationAngle[][] {
                new RotationAngle[] {
                        RotationAngle.fromDegrees(body, 0f, 42.5f, -7.5f),
                        RotationAngle.fromDegrees(horse,63.06f, 5.74f, 11.13f),
                        RotationAngle.fromDegrees(line,-37.61f, -3.96f, 3.05f),
                        RotationAngle.fromDegrees(leftArm,-64.47f, -11.31f, -5.35f),
                        RotationAngle.fromDegrees(leftForeArm,0f, 0f, 32.5f),
                        RotationAngle.fromDegrees(rightArm,-58.99f, 26.33f, 14.93f),
                        RotationAngle.fromDegrees(rightForeArm,0f, 0f, -72.5f),
                        RotationAngle.fromDegrees(leftLeg2,-35.79f, -12.02f, -16.11f),
                        RotationAngle.fromDegrees(leftLowerLeg2,57.5f, 0f, 0f),
                        RotationAngle.fromDegrees(rightLeg2,-46.59f, 20.72f, 18.5f),
                        RotationAngle.fromDegrees(rightLowerLeg2,95f, 0f, 0f)
                }
        };
    }



    @Override
    protected ModelPose<MihEntity> initIdlePose() {
        return new ModelPose<>(new RotationAngle[] {
                RotationAngle.fromDegrees(horse,50f, 0f, 0f),
                RotationAngle.fromDegrees(line,-42.5f, 0f, 0f),
                RotationAngle.fromDegrees(leftArm,-53.31f, -16.27f, -11.79f),
                RotationAngle.fromDegrees(leftForeArm,0f, 0f, 45f),
                RotationAngle.fromDegrees(rightArm,-45.72f, 14.61f, 13.81f),
                RotationAngle.fromDegrees(rightForeArm,0f, 0f, -60f),
                RotationAngle.fromDegrees(leftLeg2,-65f, 0f, 0f),
                RotationAngle.fromDegrees(leftLowerLeg2,82.5f, 0f, 0f),
                RotationAngle.fromDegrees(rightLeg2,-17.5f, 0f, 0f),
                RotationAngle.fromDegrees(rightLowerLeg2,27.5f, 0f, 2.5f)
        });
    }




}