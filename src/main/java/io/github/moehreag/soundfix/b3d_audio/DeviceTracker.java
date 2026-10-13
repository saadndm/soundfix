package io.github.moehreag.soundfix.b3d_audio;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DeviceTracker {
	DeviceList currentDevices();

	void tick();

	void forceRefresh();
}
