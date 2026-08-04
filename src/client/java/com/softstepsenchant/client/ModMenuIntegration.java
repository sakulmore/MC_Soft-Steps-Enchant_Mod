package com.softstepsenchant.client;

import com.softstepsenchant.config.SoftStepsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("config.soft-steps-enchant.title"));

            builder.setSavingRunnable(SoftStepsConfig::save);

            ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.soft-steps-enchant.category.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.soft-steps-enchant.option.obtainInLootChests"), SoftStepsConfig.INSTANCE.obtainInLootChests)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("config.soft-steps-enchant.tooltip.obtainInLootChests"))
                    .setSaveConsumer(newValue -> SoftStepsConfig.INSTANCE.obtainInLootChests = newValue)
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.soft-steps-enchant.option.obtainInEnchantingTable"), SoftStepsConfig.INSTANCE.obtainInEnchantingTable)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("config.soft-steps-enchant.tooltip.obtainInEnchantingTable"))
                    .setSaveConsumer(newValue -> SoftStepsConfig.INSTANCE.obtainInEnchantingTable = newValue)
                    .build());

            return builder.build();
        };
    }
}