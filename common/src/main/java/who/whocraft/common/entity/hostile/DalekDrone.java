package who.whocraft.common.entity.hostile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DalekDrone extends AbstractDalek{

    public DalekDrone(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}
