package me.desht.pneumaticcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.desht.pneumaticcraft.client.model.entity.semiblocks.ModelLogisticsFrame;
import me.desht.pneumaticcraft.client.util.RenderUtils;
import me.desht.pneumaticcraft.common.entity.semiblock.EntityLogisticsFrame;
import me.desht.pneumaticcraft.common.entity.semiblock.EntityTransferGadget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderLogisticsFrame extends RenderSemiblockBase<EntityLogisticsFrame> {
    public static final IRenderFactory<EntityLogisticsFrame> FACTORY = RenderLogisticsFrame::new;

    private final ModelLogisticsFrame model = new ModelLogisticsFrame();

    private RenderLogisticsFrame(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void render(EntityLogisticsFrame entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float alpha = entity.getAlpha() / 255F;
        if (alpha == 0f) return;

        if (entity.isAir()) {
            return;
        }

        matrixStackIn.push();

        if (entity.getTimeSinceHit() > 0) {
            wobble(entity, partialTicks, matrixStackIn);
        }

        Direction side = entity.getSide();
        matrixStackIn.translate(0, side.getAxis() == Direction.Axis.Y ? 0.5 : -0.5, 0);
        switch (side) {
            case UP:
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90));
                matrixStackIn.translate(0, -1, 0);
                break;
            case DOWN:
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-90));
                matrixStackIn.translate(0, -1, 0);
                break;
            case NORTH:
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
                break;
            case SOUTH:
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90));
                break;
            case WEST:
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
                break;
            case EAST:
                break;
        }

        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutout(getEntityTexture(entity)));
        model.render(matrixStackIn, builder, kludgeLightingLevel(entity, packedLightIn), OverlayTexture.getPackedUV(0F, false), 1f, 1f, 1f, alpha);

        matrixStackIn.pop();
    }

    @Override
    public Vector3d getRenderOffset(EntityLogisticsFrame entityIn, float partialTicks) {
        VoxelShape shape = entityIn.getBlockState().getShape(entityIn.getWorld(), entityIn.getBlockPos());
        double yOff = (shape.getEnd(Direction.Axis.Y) - shape.getStart(Direction.Axis.Y)) / 2.0;
        switch (entityIn.getSide()) {
            case DOWN: return new Vector3d(0, shape.getStart(Direction.Axis.Y), 0);
            case UP: return new Vector3d(0, shape.getEnd(Direction.Axis.Y) - 1, 0);
            case NORTH: return new Vector3d(0, yOff - 0.5, shape.getStart(Direction.Axis.Z));
            case SOUTH: return new Vector3d(0, yOff - 0.5, shape.getEnd(Direction.Axis.Z) - 1);
            case WEST: return new Vector3d(shape.getStart(Direction.Axis.X), yOff - 0.5, 0);
            case EAST: return new Vector3d(shape.getEnd(Direction.Axis.X) - 1, yOff - 0.5, 0);
            default: return Vector3d.ZERO;
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLogisticsFrame entityLogisticsFrame) {
        return entityLogisticsFrame.getTexture();
    }
}
