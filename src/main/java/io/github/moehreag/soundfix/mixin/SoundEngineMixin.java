package io.github.moehreag.soundfix.mixin;

import io.github.moehreag.soundfix.Engine;
import io.github.moehreag.soundfix.SoundFix;
import net.minecraft.client.sound.instance.SoundInstance;
import net.minecraft.client.sound.system.SoundEngine;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

	@Inject(method = "play", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/system/SoundEngine;started:Z", opcode = Opcodes.GETFIELD, ordinal = 0), cancellable = true)
	private void play(SoundInstance sound, CallbackInfo ci) {
		if ((Object) this instanceof Engine) {
			SoundFix.engine.play(sound);
			ci.cancel();
		}
	}
}
