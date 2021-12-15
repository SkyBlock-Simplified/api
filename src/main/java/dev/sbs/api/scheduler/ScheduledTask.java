package dev.sbs.api.scheduler;

import lombok.Getter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled Task for the {@link Scheduler}.
 */
public final class ScheduledTask implements Runnable {

	private static volatile int currentId = 1;

	/**
	 * Get the time the task was created.
	 */
	@Getter
	private final long addedTime = System.currentTimeMillis();

	/**
	 * Get the id of the task.
	 */
	@Getter
	private final int id;

	/**
	 * Get the delay (in milliseconds) before the task will run.
	 */
	@Getter
	private final long initialDelay;

	/**
	 * Get the delay (in milliseconds) before the task will repeat.
	 */
	@Getter
	private final long repeatDelay;

	/**
	 * Is this an asynchronous task?
	 */
	@Getter
	private final boolean async;

	/**
	 * Is this task currently running?
	 */
	@Getter
	private boolean running;

	/**
	 * Will this task run repeatedly?
	 */
	@Getter
	private boolean repeating;

	/**
	 * Get the number of consecutive errors.
	 */
	@Getter
	private int consecutiveErrors = 0;

	private final Runnable runnableTask;
	private final ScheduledFuture<?> scheduledFuture;
	private final Object lock = new Object();

	/**
	 * Creates a new Scheduled Task.
	 *
	 * @param task The task to run.
	 * @param initialDelay The initialDelay (in ticks) to wait before the task is ran.
	 * @param repeatDelay The initialDelay (in ticks) to wait before calling the task again.
	 * @param async If the task should be run asynchronously.
	 */
	ScheduledTask(ScheduledExecutorService executorService, final Runnable task, long initialDelay, long repeatDelay, boolean async) {
		synchronized (this.lock) {
			this.id = currentId++;
		}

		this.runnableTask = task;
		this.initialDelay = initialDelay;
		this.repeatDelay = repeatDelay;
		this.async = async;
		this.repeating = this.repeatDelay > 0;

		// Schedule Task
		if (repeatDelay > 0)
			this.scheduledFuture = executorService.scheduleWithFixedDelay(this, initialDelay, repeatDelay, TimeUnit.MILLISECONDS);
		else
			this.scheduledFuture = executorService.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
	}

	/**
	 * Will attempt to cancel this task.
	 */
	public void cancel() {
		this.cancel(false);
	}

	/**
	 * Will attempt to cancel this task, even if running.
	 */
	public void cancel(boolean mayInterruptIfRunning) {
		// Attempt Cancellation
		this.scheduledFuture.cancel(mayInterruptIfRunning);

		if (this.scheduledFuture.isDone()) {
			this.repeating = false;
			this.running = false;
		}
	}

	/**
	 * Gets if the current task is done.
	 *
	 * @return True if the task has completed normally, encountered an exception or cancelled.
	 */
	public boolean isDone() {
		return this.scheduledFuture.isDone();
	}

	/**
	 * Gets if the current task is canceled.
	 *
	 * @return True if the task is canceled.
	 */
	public boolean isCanceled() {
		return this.scheduledFuture.isCancelled();
	}

	@Override
	public void run() {
		try {
			// Run Task
			this.running = true;

			if (this.isAsync())
				this.runnableTask.run();
			else {
				synchronized (this.lock) {
					this.runnableTask.run();
				}
			}

			this.consecutiveErrors = 0;
		} catch (Exception ignore) {
			this.consecutiveErrors++;
		} finally {
			this.running = false;
		}
	}

}
