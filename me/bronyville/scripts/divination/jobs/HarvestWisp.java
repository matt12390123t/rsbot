package me.bronyville.scripts.divination.jobs;

import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.LocalPath;
import org.powerbot.script.wrappers.Npc;

import java.util.concurrent.Callable;

public class HarvestWisp extends Job {

    public HarvestWisp(Script script) {
        super(script);
    }

    private final Filter<Npc> spotFilter = new Filter<Npc>() {
        @Override
        public boolean accept(Npc npc) {
            return npc.getName().contains("wisp") || npc.getName().contains("spring");
        }
    };

    private Npc getHarvestableSpot() {
        return script.ctx.npcs.select().select(spotFilter).nearest().poll();
    }

    @Override
    public boolean activate() {
        return script.ctx.backpack.select().count() != 28
                && script.ctx.players.local().getAnimation() != 21228
                && safeToHarvest();
    }

    private long harvestTimer = System.currentTimeMillis();
    private boolean safeToHarvest() {
        if(script.ctx.players.local().getAnimation() == 21234 || script.ctx.players.local().getAnimation() == 21232) {
            harvestTimer = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - harvestTimer >= 3000;
    }

    @Override
    public void execute() {
        final Npc currentSpot = getHarvestableSpot();
        if(currentSpot != script.ctx.npcs.getNil()) {
            if(currentSpot.getLocation().distanceTo(script.ctx.players.local()) >= 7) {
                script.setStatus("Walking towards spot");
                final LocalPath path = script.ctx.movement.findPath(currentSpot);
                if(path != null && path.traverse()) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !script.ctx.players.local().isInMotion()
                                    && currentSpot.getLocation().distanceTo(script.ctx.players.local()) < 3;
                        }
                    }, 300, 5);
                }
            } else {
                if(!currentSpot.isInViewport()) {
                    script.setStatus("Turning camera to spot");
                    script.ctx.camera.turnTo(currentSpot);
                } else {
                    script.setStatus("Harvesting wisp");
                    if(currentSpot.interact("Harvest")) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return script.ctx.players.local().getAnimation() != -1;
                            }
                        }, 300, 5);
                    }
                }
            }
        }
    }
}
