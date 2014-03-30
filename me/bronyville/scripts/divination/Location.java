package me.bronyville.scripts.divination;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Tile;

/**
 * Created by Kenneth on 3/29/2014.
 */
public enum Location {

    DRAYNOR(-1, null, -1),
    FALADOR(-1, null, -1),
    VARROCK(-1, null, -1),
    SEERS(87306, new Tile(2746, 3427, 0), 18155, 18178);


    private final int riftId;
    private final int[] harvestableIds;
    private final Tile commonLocation;
    private Location(int riftId, Tile commonLocation, int... harvestableIds) {
        this.riftId = riftId;
        this.harvestableIds = harvestableIds;
        this.commonLocation = commonLocation;
    }

    public int getRiftId() {
        return riftId;
    }

    public Tile getCommonLocation() {
        return commonLocation;
    }

    public int[] getHarvestableIds() {
        return harvestableIds;
    }

    public static Location determine(MethodContext ctx) {
        for(Location loc : Location.values()) {
            if(!ctx.npcs.select().id(loc.getHarvestableIds()).isEmpty()) {
                return loc;
            }
        }
        return null;
    }

}
