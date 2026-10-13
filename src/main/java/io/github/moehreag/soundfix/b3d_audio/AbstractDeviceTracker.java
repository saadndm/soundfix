package io.github.moehreag.soundfix.b3d_audio;

import java.util.concurrent.atomic.AtomicBoolean;
import io.github.moehreag.soundfix.SoundFix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class AbstractDeviceTracker implements DeviceTracker {
	private volatile DeviceList deviceList;
	private final AtomicBoolean updatePending = new AtomicBoolean();

	public AbstractDeviceTracker(final DeviceList deviceList) {
		this.deviceList = deviceList;
	}

	protected abstract boolean isUpdateRequested();

	protected abstract void discardUpdateRequest();

	@Override
	public DeviceList currentDevices() {
		return this.deviceList;
	}

	@Override
	public void forceRefresh() {
		this.discardUpdateRequest();
		this.deviceList = DeviceList.query();
	}

	@Override
	public void tick() {
		if (this.isUpdateRequested()) {
			this.discardUpdateRequest();
			if (this.updatePending.compareAndSet(false, true)) {
				SoundFix.IO.execute(() -> {
					this.deviceList = DeviceList.query();
					this.updatePending.set(false);
				});
			}
		}
	}
}
