package io.github.moehreag.soundfix.sounds;

import java.util.concurrent.locks.LockSupport;

import io.github.moehreag.soundfix.util.BlockableEventLoop;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.crash.CrashReport;

/**
 * The SoundEngineExecutor class is responsible for executing sound-related tasks in a separate thread.
 * <p>
 * It extends the BlockableEventLoop class, providing an event loop for managing and executing tasks.
 */
@Environment(EnvType.CLIENT)
public class SoundEngineExecutor extends BlockableEventLoop<Runnable> {
	private Thread thread = this.createThread();
	private volatile boolean shutdown;

	public SoundEngineExecutor() {
		super("Sound executor");
	}

	/**
	 * Creates and starts a new thread for executing sound-related tasks.
	 * <p>
	 * @return The created thread
	 */
	private Thread createThread() {
		Thread thread = new Thread(this::run);
		thread.setDaemon(true);
		thread.setName("Sound engine");
		thread.setUncaughtExceptionHandler(
			(t, throwable) -> Minecraft.getInstance().integratedServerCrashed(CrashReport.of(throwable, "Uncaught exception on thread: " + t.getName()))
		);
		thread.start();
		return thread;
	}

	@Override
	public Runnable wrapRunnable(Runnable runnable) {
		return runnable;
	}

	@Override
	public void schedule(Runnable runnable) {
		if (!this.shutdown) {
			super.schedule(runnable);
		}
	}

	@Override
	protected boolean shouldRun(Runnable runnable) {
		return !this.shutdown;
	}

	@Override
	protected Thread getRunningThread() {
		return this.thread;
	}

	/**
	 * The main run loop of the SoundEngineExecutor.
	 * It continuously blocks until the shutdown state is true.
	 */
	private void run() {
		while (!this.shutdown) {
			this.managedBlock(() -> this.shutdown);
		}
	}

	@Override
	protected void waitForTasks() {
		LockSupport.park("waiting for tasks");
	}

	/**
	 * Shuts down the SoundEngineExecutor by dropping all pending tasks, interrupting the thread and joining it.
	 * Tasks scheduled while shut down are ignored.
	 */
	public void shutDown() {
		this.shutdown = true;
		this.dropAllTasks();
		this.thread.interrupt();

		try {
			this.thread.join();
		} catch (InterruptedException var2) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Starts a new thread after {@link #shutDown()}, allowing new tasks to be scheduled again.
	 */
	public void startUp() {
		this.shutdown = false;
		this.thread = this.createThread();
	}
}
