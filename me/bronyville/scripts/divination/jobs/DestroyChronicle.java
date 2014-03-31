package me.bronyville.scripts.divination.jobs;

import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import org.powerbot.script.methods.Hud;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Item;

import java.util.concurrent.Callable;

public class DestroyChronicle extends Job {

    public DestroyChronicle(Script script) {
        super(script);
    }

    @Override
    public boolean activate() {
        return !script.ctx.backpack.select().id(29293).isEmpty();
    }

    @Override
    public void execute() {
        if(script.ctx.hud.isVisible(Hud.Window.BACKPACK)) {
            final Item chronicle = script.ctx.backpack.poll();
            final Component destroyWidget = script.ctx.widgets.get(1183, 6);
            if(destroyWidget.isVisible()) {
                if(destroyWidget.click()) {
                    script.setStatus("Clicking destroy");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !destroyWidget.isVisible();
                        }
                    }, 200, 5);
                }
            } else {
                if(chronicle.interact("Destroy")) {
                    script.setStatus("Destroying chronicles");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return destroyWidget.isVisible();
                        }
                    }, 200, 5);
                }
            }
        }
    }
}
