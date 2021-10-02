package gg.sbs.api.scheduler;

import gg.sbs.api.SimplifiedApi;

public abstract class ScheduledRunnable implements Runnable {

	private ScheduledTask task;

	public void cancel() {
		SimplifiedApi.getScheduler().cancel(task);
	}

}