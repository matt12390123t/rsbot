package me.bronyville.scripts.divination.jobs;

import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.GameObject;

import java.util.concurrent.Callable;

public class ConvertMemory extends Job {

    public ConvertMemory(Script script) {
        super(script);
    }

    private final Filter<GameObject> riftFilter = new Filter<GameObject>() {
        @Override
        public boolean accept(GameObject gameObject) {
            return gameObject.getName().equals("Energy Rift");
        }
    };

    public GameObject getEnergyRift() {
        return script.ctx.objects.select().select(riftFilter).nearest().poll();
    }

    @Override
    public boolean activate() {
        return script.ctx.backpack.select().count() == 28 || script.ctx.players.local().isInCombat();
    }

    @Override
    public void execute() {
        final GameObject rift = getEnergyRift();
        if(rift != script.ctx.objects.getNil()) {
            if(rift.getLocation().distanceTo(script.ctx.players.local()) >= 7) {
                script.log.info("Walking to rift");
                if(script.ctx.movement.stepTowards(rift)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return rift.getLocation().distanceTo(script.ctx.players.local()) >= 2;
                        }
                    }, 300, 10);
                }
            } else {
                if(!rift.isInViewport()) {
                    script.setStatus("Turning camera to rift");
                    script.ctx.camera.turnTo(rift);
                } else {
                    script.setStatus("Interacting with rift");
                    if(rift.interact("Convert to experience")) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return script.ctx.players.local().getAnimation() != -1;
                            }
                        }, 200, 7);
                    } else {
                        script.setStatus("We've misclicked or failed to click");
                    }
                }
            }
        }
    }
}
