package com.deepslice.utilities;

public abstract class ResumableTask {

	private int id;
	protected Object data;
	private boolean dataReceived;
	private boolean finished;
	private boolean paused;
	private BackGroundThread thread;
	protected ResumableTaskStarter starter;

	public ResumableTask(ResumableTaskStarter starter, int id) {
		this.starter = starter;
		this.id = id;
	}

	public ResumableTask(ResumableTaskStarter starter) {
		this.starter = starter;
	}

	public void start() {
		// TODO Auto-generated method stub
		dataReceived = false;
		finished = false;
		paused = false;
		data = null;
		thread = new BackGroundThread();
		thread.start();
	}

	public void pause() {
		this.paused = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void resume(ResumableTaskStarter starter) {
		this.starter = starter;
		paused = false;
		if (dataReceived) {
			returnData();
		}
	}

	private void dataReceived() {
		dataReceived = true;
		if (!paused) {
			returnData();
		}
	}

	private void returnData() {
		if (starter != null) {
			starter.ResumableTaskDone(id, data);
			starter = null;
			finished = true;
		}
	}

	public abstract void doStuff();

	private class BackGroundThread extends Thread {
		public void run() {
			doStuff();
			dataReceived();
		}
	}
}