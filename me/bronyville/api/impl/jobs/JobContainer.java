package me.bronyville.api.impl.jobs;

import org.powerbot.script.PollingScript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobContainer implements Comparator<Job> {

    private final PollingScript script;
    public JobContainer(PollingScript script) {
        this.script = script;
    }

    private final List<Job> queue = new ArrayList<Job>();

    public void submit(Job... jobs) {
        for(Job job : jobs)
            if(job != null && !queue.contains(job))
                queue.add(job);
        Collections.sort(queue, this);
    }

    public void revoke(Job... jobs) {
        for(Job job : jobs)
            if(job != null && queue.contains(job))
                queue.remove(job);
        Collections.sort(queue, this);
    }

    public Job next() {
        for(Job job : queue)
            if(job.activate())
                return job;
        return null;
    }

    @Override
    public int compare(Job o1, Job o2) {
        return o1.priority() - o2.priority();
    }

}
