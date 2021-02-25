package fr.idarkay.vertical_render.client;

import fr.idarkay.vertical_render.client.option.VerticalGameOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.io.File;

/**
 * File <b>Vertical_renderClient</b> located on fr.idarkay.vertical_render.client
 * Vertical_renderClient is a part of Verttical_Render.
 * <p>
 * Copyright (c) 2021 Verttical_Render.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/02/2021 at 15:56
 */
@Environment(EnvType.CLIENT)
public class VerticalRenderClient implements ClientModInitializer
{

    private static VerticalGameOptions CONFIG;

    public static final DoubleOption VERTICAL_RENDER_DISTANCE = new DoubleOption("option.custom.vertical_render", 2.0D, 48.0D, 1.0F,
            (gameOptions) -> (double) options().verticalViewDistance, (gameOptions, viewDistance) ->
    {
        options().verticalViewDistance = viewDistance.intValue();
        options().writeChanges();
        MinecraftClient.getInstance().worldRenderer.reload();
    }, (gameOptions, option) -> new LiteralText(("V-Render Distance" + options().verticalViewDistance + " chunks")));

    @Override
    public void onInitializeClient()
    {
        // skip
    }

    public static VerticalGameOptions options()
    {
        if (CONFIG == null)
        {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    private static VerticalGameOptions loadConfig()
    {
        return VerticalGameOptions.load(new File("config/vertical_render.json"));
    }

}
