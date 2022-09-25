package who.whocraft.common.entity.bodycontroller;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import who.whocraft.common.entity.hostile.AbstractDalek;

public class DalekBodyController extends BodyRotationControl {

    private final Mob mob;

    public DalekBodyController(Mob mob) {
        super(mob);
        this.mob = mob;
    }
}
