package me.bronyville.api.impl.jobs;

import me.bronyville.api.impl.Script;

public abstract class Job {

    public Script script;
    public Job(final Script script) {
        this.script = script;
    }

    public int priority() {
        return 0;
    }

    public abstract boolean activate();
    public abstract void execute();
}