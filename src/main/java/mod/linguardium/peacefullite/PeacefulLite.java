package mod.linguardium.peacefullite;

import mod.linguardium.peacefullite.mixin.SpawnHelperAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.SpawnHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PeacefulLite implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "peacefullite";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

//		BiomeModifications.create(id("removespawns")).add(ModificationPhase.POST_PROCESSING, BiomeSelectors.all(),(biomeSelectionContext, biomeModificationContext) -> {
//			biomeModificationContext.getSpawnSettings().clearSpawns(SpawnGroup.MONSTER); // No More Monsters
//		});
		SpawnGroup[] groups = SpawnHelperAccessor.peacefullite$getSpawnableGroups();
		if (groups != null){
			SpawnHelperAccessor.peacefullite$setSpawnableGroups( Arrays.stream(groups).filter(group -> !group.equals(SpawnGroup.MONSTER)).toArray(SpawnGroup[]::new));
		}
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.isOf(Items.ROTTEN_FLESH) && entity instanceof VillagerEntity villagerEntity) {
					if (!world.isClient()) {
						ZombieVillagerEntity zombieVillagerEntity = villagerEntity.convertTo(EntityType.ZOMBIE_VILLAGER, false);
						zombieVillagerEntity.initialize((ServerWorld) world, world.getLocalDifficulty(zombieVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true), null);
						zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
						zombieVillagerEntity.setGossipData(villagerEntity.getGossip().serialize(NbtOps.INSTANCE).getValue());
						zombieVillagerEntity.setOfferData(villagerEntity.getOffers().toNbt());
						zombieVillagerEntity.setXp(villagerEntity.getExperience());
						stack.decrement(1);
					}
			}
			return ActionResult.success(world.isClient);
		});
	}

	public static Identifier id(String path) { return new Identifier(MOD_ID,path);}
}
