package fr.idarkay.vertical_render.client.mixin;

import com.google.common.collect.Lists;
import fr.idarkay.vertical_render.client.VerticalRenderClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

/**
 * File <b>MixinVideoOptionsScreen</b> located on fr.idarkay.vertical_render.client.mixin
 * MixinVideoOptionsScreen is a part of Verttical_Render.
 * <p>
 * Copyright (c) 2021 Verttical_Render.
 * <p>
 *
 * @author alice. B. (IDarKay),
 * Created the 25/02/2021 at 17:07
 */

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends GameOptionsScreen
{

    @Mutable @Final @Shadow private static Option[] OPTIONS;

    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title)
    {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    protected void init(CallbackInfo ci)
    {
        if (Arrays.stream(OPTIONS).noneMatch(VerticalRenderClient.VERTICAL_RENDER_DISTANCE::equals))
        {
            List<Option> list = Lists.newArrayList(OPTIONS);
            list.add(VerticalRenderClient.VERTICAL_RENDER_DISTANCE);
            OPTIONS = list.toArray(new Option[0]);
        }
    }
}
