package com.softstepsenchant;

import com.softstepsenchant.config.SoftStepsConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftStepsEnchant implements ModInitializer {
	public static final String MOD_ID = "soft-steps-enchant";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<Enchantment> SOFT_STEPS_KEY = ResourceKey.create(Registries.ENCHANTMENT, id("soft_steps"));
	private static final Identifier ANCIENT_CITY_CHEST = Identifier.fromNamespaceAndPath("minecraft", "chests/ancient_city");

	@Override
	public void onInitialize() {
		SoftStepsConfig.load();

		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (SoftStepsConfig.INSTANCE.obtainInLootChests && source.isBuiltin()) {
				if (ANCIENT_CITY_CHEST.equals(key.identifier())) {
					LootPool.Builder poolBuilder = LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1.0f))
							.add(LootItem.lootTableItem(Items.BOOK)
									.setWeight(2)
									.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
											.withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(SOFT_STEPS_KEY))))
							.add(LootItem.lootTableItem(Items.AIR).setWeight(8));

					tableBuilder.pool(poolBuilder.build());
				}
			}
		});

		LOGGER.info("Soft Steps Enchant loaded!");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}