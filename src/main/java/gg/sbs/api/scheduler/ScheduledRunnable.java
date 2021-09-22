package gg.sbs.api.scheduler;

import gg.sbs.api.SimplifiedAPI;

public abstract class ScheduledRunnable implements Runnable {

	private ScheduledTask task;

	public void cancel() {
		SimplifiedAPI.getScheduler().cancel(task);
	}

}