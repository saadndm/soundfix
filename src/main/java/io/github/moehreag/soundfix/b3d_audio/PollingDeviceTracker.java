package io.github.moehreag.soundfix.b3d_audio;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class PollingDeviceTracker extends AbstractDeviceTracker {
	private static final long DEFAULT_DEVICE_CHECK_INTERVAL_MS = 1000L;
	private long lastDeviceCheckTime;

	public PollingDeviceTracker(final DeviceList deviceList) {
		super(deviceList);
	}

	@Override
	protected boolean isUpdateRequested() {
		return Minecraft.getTime() - this.lastDeviceCheckTime >= 1000L;
	}

	@Override
	protected void discardUpdateRequest() {
		this.lastDeviceCheckTime = Minecraft.getTime();
	}
}
