package fr.idarkay.vertical_render.client.mixin;

import fr.idarkay.vertical_render.client.VerticalRenderClient;
import fr.idarkay.vertical_render.client.struct.EditedBuiltChunkStorage;
import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * File <b>MixinBuiltChunkStorage</b> located on fr.idarkay.vertical_render.client.mixin
 * MixinBuiltChunkStorage is a part of Verttical_Render.
 * <p>
 * Copyright (c) 2021 Verttical_Render.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/02/2021 at 17:53
 */
@Mixin(BuiltChunkStorage.class)
public abstract class MixinBuiltChunkStorage implements EditedBuiltChunkStorage
{

    @Shadow protected int sizeY;

    @Shadow protected int sizeX;

    @Shadow protected int sizeZ;

    @Shadow public ChunkBuilder.BuiltChunk[] chunks;

    private int builderSize = 0;

    @Inject(method = "setViewDistance(I)V", at = @At("RETURN"))
    protected void setViewDistance(int viewDistance, CallbackInfo ci)
    {
        this.sizeY = Math.min(this.sizeY, VerticalRenderClient.options().verticalViewDistance * 2 + 1);
    }

    @Inject(method = "updateCameraPosition", at = @At("HEAD"), cancellable = true)
    protected void updateCameraPosition(double x, double y, CallbackInfo ci)
    {
        ci.cancel();
    }


    @Inject(method = "createChunks(Lnet/minecraft/client/render/chunk/ChunkBuilder;)V", at = @At("HEAD"))
    protected void createChunks(ChunkBuilder chunkBuilder, CallbackInfo ci)
    {
        this.builderSize = this.sizeX * this.sizeY * this.sizeZ;
    }

    public ChunkBuilder.BuiltChunk publicGetRenderedChunk(BlockPos pos)
    {
        return this.getRenderedChunk(pos);
    }

    /**
     * @author IDarKay
     * @reason edit fo Y render distance
     */
    @Nullable
    @Overwrite
    public ChunkBuilder.BuiltChunk getRenderedChunk(BlockPos pos) {
        int i = MathHelper.floorDiv(pos.getX(), 16);
        int j = MathHelper.floorDiv(pos.getY(), 16);
        int k = MathHelper.floorDiv(pos.getZ(), 16);
        i = MathHelper.floorMod(i, this.sizeX);
        j = MathHelper.floorMod(j, this.sizeY);
        k = MathHelper.floorMod(k, this.sizeZ);
        int index = this.getChunkIndex(i, j, k);
        if (index >= this.builderSize)
        {
            return null;
        }
        return this.chunks[index];
    }

    /**
     * @author IDarKay
     * @reason edit fo Y render distance
     */
    @Overwrite
    public void scheduleRebuild(int x, int y, int z, boolean important) {
        int i = Math.floorMod(x, this.sizeX);
        int j = Math.floorMod(y, this.sizeY);
        int k = Math.floorMod(z, this.sizeZ);
        int index = this.getChunkIndex(i, j, k);
        if (index >= this.builderSize)
        {
            return;
        }
        ChunkBuilder.BuiltChunk builtChunk = this.chunks[index];
        builtChunk.scheduleRebuild(important);
    }

    /**
     * @author IDarKay
     * @reason edit fo Y render distance
     */
    @Overwrite
    private int getChunkIndex(int x, int y, int z)
    {
        return this.sizeX * this.sizeX * y + z * this.sizeX + x;
    }

    @Override
    public void updateCameraPositionXYZ(double x, double y, double z)
    {
        int r_x = MathHelper.floor(x);
        int r_z = MathHelper.floor(z);
        int r_y = MathHelper.floor(y);

        for(int c_x = 0; c_x < this.sizeX; ++c_x) {
            int r_sizeX = this.sizeX * 16;
            int m = r_x - 8 - r_sizeX / 2;
            int n = m + Math.floorMod(c_x * 16 - m, r_sizeX);

            for(int c_z = 0; c_z < this.sizeZ; ++c_z) {
                int p = this.sizeZ * 16;
                int q = r_z - 8 - p / 2;
                int r = q + Math.floorMod(c_z * 16 - q, p);

                for(int c_y = 0; c_y < this.sizeY; ++c_y) {
                    int r_sizeY = this.sizeY * 16;
                    int w = r_y - 8 - r_sizeY / 2;
                    int origin_y = w + Math.floorMod(c_y * 16 - w, r_sizeY);
                    ChunkBuilder.BuiltChunk builtChunk = this.chunks[this.getChunkIndex(c_x, c_y, c_z)];
                    builtChunk.setOrigin(n, origin_y, r);
                }
            }
        }
    }
}
