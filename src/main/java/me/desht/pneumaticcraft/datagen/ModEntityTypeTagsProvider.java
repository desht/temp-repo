package me.desht.pneumaticcraft.datagen;

import me.desht.pneumaticcraft.api.data.PneumaticCraftTags;
import me.desht.pneumaticcraft.api.lib.Names;
import me.desht.pneumaticcraft.common.registry.ModEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator.getPackOutput(), lookupProvider, Names.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(PneumaticCraftTags.EntityTypes.VACUUM_TRAP_BLACKLISTED).addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED);
        tag(PneumaticCraftTags.EntityTypes.VACUUM_TRAP_WHITELISTED);
        tag(PneumaticCraftTags.EntityTypes.OMNIHOPPER_BLACKLISTED).add(EntityType.VILLAGER);

        tag(PneumaticCraftTags.EntityTypes.BASIC_DRONES).add(ModEntityTypes.COLLECTOR_DRONE.get());
        tag(PneumaticCraftTags.EntityTypes.BASIC_DRONES).add(ModEntityTypes.GUARD_DRONE.get());
        tag(PneumaticCraftTags.EntityTypes.BASIC_DRONES).add(ModEntityTypes.HARVESTING_DRONE.get());
        tag(PneumaticCraftTags.EntityTypes.BASIC_DRONES).add(ModEntityTypes.LOGISTICS_DRONE.get());

        // no PNC entities are suitable for picking up with Carry On
        for (var entityType : ModEntityTypes.ENTITY_TYPES.getEntries()) {
            tag(PneumaticCraftTags.EntityTypes.CARRYON_BLACKLISTED).add(entityType.get());
        }
    }
}
