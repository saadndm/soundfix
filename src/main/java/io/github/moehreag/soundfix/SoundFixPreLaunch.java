package io.github.moehreag.soundfix;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class SoundFixPreLaunch implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		FabricLoader loader = FabricLoader.getInstance();
		if (!loader.isModLoaded("pylon") && !loader.isModLoaded("legacy-lwjgl3")) {
			throw new IllegalStateException("SoundFix requires pylon or legacy-lwjgl3 to be installed");
		}
	}
}
