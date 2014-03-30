package me.bronyville.api.impl;

import me.bronyville.api.impl.jobs.JobContainer;
import org.powerbot.script.PollingScript;
import org.powerbot.script.methods.MethodContext;

public abstract class Script extends PollingScript {

    public MethodContext ctx;
    private final JobContainer container;
    private String status = "Starting up.";

    public JobContainer getContainer() {
        return container;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Script() {
        this.ctx = super.ctx;
        container = new JobContainer(this);
    }

    @Override
    public void start() {
        super.start();
        onStart();
    }

    @Override
    public int poll() {
        loop();
        return 300;
    }

    public abstract void onStart();
    public abstract void loop();
}
