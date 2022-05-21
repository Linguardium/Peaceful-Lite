package mod.linguardium.peacefullite.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnHelper.class)
public interface SpawnHelperAccessor {
    @Accessor("SPAWNABLE_GROUPS")
    @Mutable
    static void peacefullite$setSpawnableGroups(SpawnGroup[] groups) { };

    @Accessor("SPAWNABLE_GROUPS")
    static SpawnGroup[] peacefullite$getSpawnableGroups() {
        return null;
    };
}
