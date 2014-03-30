package me.bronyville.scripts.divination;

import me.bronyville.api.graphics.DynamicPaint;
import me.bronyville.api.graphics.LinkedProperties;
import me.bronyville.api.impl.Script;
import me.bronyville.api.impl.jobs.Job;
import me.bronyville.scripts.divination.jobs.CaptureChronicle;
import me.bronyville.scripts.divination.jobs.ConvertMemory;
import me.bronyville.scripts.divination.jobs.DropJunk;
import me.bronyville.scripts.divination.jobs.HarvestWisp;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.methods.Skills;
import org.powerbot.script.util.SkillData;

import java.awt.*;

@Manifest(
        name = "Ultra Divination",
        description = "Trains divination at any of the locations, just start it at the correct location"
)
public class Divination extends Script implements PaintListener {

    private final SkillData skillData = new SkillData(this.ctx);
    private final LinkedProperties props = new LinkedProperties();
    private final DynamicPaint paint = new DynamicPaint(this);

    private CaptureChronicle captureChronicle;
    private ConvertMemory convertMemory;
    private DropJunk dropJunk;
    private HarvestWisp harvestWisp;

    private Location location;

    @Override
    public void onStart() {
        location = Location.determine(ctx);

        captureChronicle = new CaptureChronicle(this);
        convertMemory = new ConvertMemory(this);
        dropJunk = new DropJunk(this);
        harvestWisp = new HarvestWisp(this);

        getContainer().submit(captureChronicle, convertMemory, dropJunk, harvestWisp);
        log.info(getName() + " is starting!");
    }

    public String formatTime(final long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    @Override
    public void repaint(Graphics graphics) {
        props.put("Runtime", formatTime(getRuntime()));
        props.put("Status", getStatus());
        props.put("Divination", ctx.skills.getLevel(Skills.DIVINATION) + "(+" + skillData.level(Skills.DIVINATION) + ")");
        props.put("Experience", skillData.experience(SkillData.Rate.HOUR, Skills.DIVINATION) + "(+" + skillData.experience(Skills.DIVINATION) + ")");
        props.put("Time to level", formatTime(skillData.timeToLevel(SkillData.Rate.HOUR, Skills.DIVINATION)));
        props.put("Harvesting at", location == null ? "Unknown" : location.name());
        paint.properties(props).draw(graphics);
    }

    @Override
    public void loop() {
        final Job best = getContainer().next();
        if(best != null)
            best.execute();
    }
}
