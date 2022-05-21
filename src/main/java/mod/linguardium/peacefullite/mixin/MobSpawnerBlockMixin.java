package mod.linguardium.peacefullite.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerBlockMixin {
    @Shadow private int spawnRange;

    @Redirect(at=@At(value="INVOKE", target="Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"),method="serverTick")
    private boolean peacefullite$MobDropsNotMobs(ServerWorld world, Entity entity) {
        BlockPos pos = entity.getBlockPos();
        PlayerEntity player = world.getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(),spawnRange,true);
        if (player != null && entity instanceof LivingEntityAccessor mob) {
            mob.peacefullite$dropLoot(DamageSource.player(player));
            world.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);
        }
        return false;
    }
}
