package io.github.moehreag.soundfix.mixin;

import io.github.moehreag.soundfix.subtitles.SubtitlesHud;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Inject(method = "doEvent", at = @At("HEAD"))
	private void startBlockBreak(PlayerEntity source, int type, BlockPos pos, int data, CallbackInfo ci) {
		SubtitlesHud.getInstance().breakingBlock = type == 2001;
	}

	@Inject(method = "doEvent", at = @At("RETURN"))
	private void endBlockBreak(PlayerEntity source, int type, BlockPos pos, int data, CallbackInfo ci) {
		SubtitlesHud.getInstance().breakingBlock = false;
	}
}
