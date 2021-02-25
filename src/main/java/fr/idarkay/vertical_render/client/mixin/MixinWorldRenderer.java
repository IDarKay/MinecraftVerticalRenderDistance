package fr.idarkay.vertical_render.client.mixin;

import fr.idarkay.vertical_render.client.VerticalRenderClient;
import fr.idarkay.vertical_render.client.struct.EditedBuiltChunkStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * File <b>MixinWorldRenderer</b> located on fr.idarkay.vertical_render.client.mixin
 * MixinWorldRenderer is a part of Verttical_Render.
 * <p>
 * Copyright (c) 2021 Verttical_Render.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/02/2021 at 17:43
 */
@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer
{
    @Shadow private BuiltChunkStorage chunks;
    @Shadow @Final private MinecraftClient client;
    private int verticalRenderDistance = -1;

//    @Inject(method = "<init>", at = @At("HEAD"))
//    protected void init(MinecraftClient client, BufferBuilderStorage bufferBuilderStorage, CallbackInfo ci)
//    {
//        this.verticalRenderDistance = -1;
//    }

//

    @Inject(method = "reload()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/WorldRenderer;renderDistance:I"))
    protected void reload(CallbackInfo ci)
    {
        this.verticalRenderDistance = VerticalRenderClient.options().verticalViewDistance;
    }

    @Shadow private int renderDistance;

    @Shadow private ClientWorld world;

    @Inject(method = "reload()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BuiltChunkStorage;updateCameraPosition(DD)V", shift = At.Shift.BEFORE))
    protected void reload2(CallbackInfo ci)
    {
        Entity entity = this.client.getCameraEntity();
        ((EditedBuiltChunkStorage) this.chunks).updateCameraPositionXYZ(entity.getX(), entity.getY(), entity.getZ());
    }

    @Inject(method = "setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BuiltChunkStorage;updateCameraPosition(DD)V", shift = At.Shift.BEFORE))
    protected void setupTerrain(Camera camera, Frustum frustum, boolean hasForcedFrustum, int frame, boolean spectator, CallbackInfo ci)
    {
        ((EditedBuiltChunkStorage) this.chunks).updateCameraPositionXYZ(
                this.client.player.getX(),
                this.client.player.getY(),
                this.client.player.getZ());
    }

    /**
     * @author IDarKay
     * @reason edit fo Y render distance
     */
    @Overwrite
    @Nullable
    public ChunkBuilder.BuiltChunk getAdjacentChunk(BlockPos pos, ChunkBuilder.BuiltChunk chunk, Direction direction) {
        BlockPos blockPos = chunk.getNeighborPosition(direction);
//        System.out.println(this.verticalRenderDistance);
        if ((MathHelper.abs(pos.getX() - blockPos.getX()) > this.renderDistance * 16) ||
                (MathHelper.abs(pos.getZ() - blockPos.getZ()) > this.renderDistance * 16) ||
                (MathHelper.abs(pos.getY() - blockPos.getY()) > this.verticalRenderDistance * 16)) {
            return null;
        } else if (blockPos.getY() >= this.world.getBottomY() && blockPos.getY() < this.world.getTopY()) {
            return  ((EditedBuiltChunkStorage) this.chunks).publicGetRenderedChunk(blockPos);
        } else {
            return null;
        }
    }

}
