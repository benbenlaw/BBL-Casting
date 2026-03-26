package com.benbenlaw.casting.block.entity.renderer;

import com.benbenlaw.casting.block.entity.TankBlockEntity;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import com.benbenlaw.core.util.FluidRendererUtil;
import com.benbenlaw.core.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nullable;

public class TankBlockEntityRenderer implements BlockEntityRenderer<TankBlockEntity, TankBlockEntityRenderState> {
    public TankBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public TankBlockEntityRenderState createRenderState() {
        return new TankBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TankBlockEntity blockEntity, TankBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition,
        ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.fluidStack = FluidUtil.getStack(blockEntity.getInputFluidHandler(), 0);
        renderState.tankCapacity = blockEntity.getInputFluidHandler().getCapacityAsInt(0, FluidResource.of(FluidUtil.getStack(blockEntity.getInputFluidHandler(), 0)));
    }

    @Override
    public void submit(TankBlockEntityRenderState fluidTankRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

        float fillRatio = fluidTankRenderState.fluidStack.getAmount() / (float) fluidTankRenderState.tankCapacity;
        FluidRendererUtil.submitFluid(poseStack, Sheets.translucentBlockItemSheet(), submitNodeCollector, fluidTankRenderState.fluidStack, fillRatio, fluidTankRenderState.lightCoords);
    }
}