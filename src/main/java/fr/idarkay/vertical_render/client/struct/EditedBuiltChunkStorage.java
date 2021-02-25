package fr.idarkay.vertical_render.client.struct;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;

/**
 * File <b>EditedBuiltChunkStorage</b> located on fr.idarkay.vertical_render.client.struct
 * EditedBuiltChunkStorage is a part of Verttical_Render.
 * <p>
 * Copyright (c) 2021 Verttical_Render.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/02/2021 at 18:13
 */
public interface EditedBuiltChunkStorage
{

    void updateCameraPositionXYZ(double x, double y, double z);

    ChunkBuilder.BuiltChunk publicGetRenderedChunk(BlockPos pos);

}
