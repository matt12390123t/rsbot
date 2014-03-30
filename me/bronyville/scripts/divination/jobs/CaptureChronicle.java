package me.bronyville.scripts.divination.jobs;

import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Npc;

import java.util.concurrent.Callable;

public class CaptureChronicle extends Job {

    public CaptureChronicle(Script script) {
        super(script);
    }

    private Filter<Npc> chronicleFilter = new Filter<Npc>() {
        @Override
        public boolean accept(Npc npc) {
            return npc.getName().toLowerCase().contains("chronicle");
        }
    };

    @Override
    public boolean activate() {
        return !script.ctx.npcs.select().select(chronicleFilter).isEmpty()
                && script.ctx.backpack.select().count() != 28;
    }

    @Override
    public void execute() {
        for(final Npc chronicle : script.ctx.npcs) {
            if(chronicle != script.ctx.npcs.getNil()) {
                if(chronicle.getLocation().distanceTo(script.ctx.players.local()) >= 4) {
                    if(script.ctx.movement.stepTowards(chronicle)) {
                        script.setStatus("Chasing chronicle");
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return chronicle.getLocation().distanceTo(script.ctx.players.local()) >= 2;
                            }
                        }, 300, 5);
                    }
                } else {
                    if(!chronicle.isInViewport()) {
                        script.setStatus("Turning camera to chronicle");
                        script.ctx.camera.turnTo(chronicle);
                    } else {
                        if(chronicle.interact("Capture")) {
                            script.setStatus("Capturing chronicle");
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                      return !chronicle.isValid();
                                }
                            }, 200, 5);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int priority() {
        return 3;
    }

}
