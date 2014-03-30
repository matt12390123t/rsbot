package me.bronyville.scripts.divination.jobs;

import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.Hud;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Item;

import java.util.concurrent.Callable;

public class DropJunk extends Job {

    public DropJunk(Script script) {
        super(script);
    }

    private Filter<Item> junkFilter = new Filter<Item>() {
        @Override
        public boolean accept(Item item) {
            return item.getName().toLowerCase().contains("log");
        }
    };

    @Override
    public boolean activate() {
        return !script.ctx.backpack.select().select(junkFilter).isEmpty();
    }

    @Override
    public void execute() {
        for(final Item item : script.ctx.backpack) {
            if(!script.ctx.hud.isVisible(Hud.Window.BACKPACK)) {
                script.ctx.hud.view(Hud.Window.BACKPACK);
            }
            if(item != script.ctx.backpack.getNil()) {
                if(item.interact("Drop")) {
                    script.setStatus("Dropping junk item: "+ item.getName());
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !item.isValid();
                        }
                    }, 200, 5);
                }
            }
        }
    }

    @Override
    public int priority() {
        return 2;
    }
}
