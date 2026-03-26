package com.benbenlaw.casting.data;

import com.benbenlaw.casting.block.CastingBlockEntities;import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.item.CastingDataComponents;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.LootContext;import net.minecraft.world.level.storage.loot.LootParams;import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;import net.minecraft.world.level.storage.loot.parameters.LootContextParams;import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import java.util.Set;

public class CastingLootTableProvider extends VanillaBlockLoot {

    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    public CastingLootTableProvider(HolderLookup.Provider provider) {
        super(provider);
    }

    @Override
    protected void generate() {

        this.dropSelf(CastingBlocks.BLACK_BRICKS.get());
        this.dropSelf(CastingBlocks.BLACK_BRICK_GLASS.get());
        this.dropWithFluidComponent(CastingBlocks.CONTROLLER.get());
        this.dropWithFluidComponent(CastingBlocks.SOLIDIFIER.get());
        this.dropWithFluidComponent(CastingBlocks.MIXER.get());
        this.dropWithFluidComponent(CastingBlocks.TANK.get());
    }


    private void dropWithFluidComponent(Block block) {
        this.add(block, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                        .include(CastingDataComponents.FLUIDS.get())))));
    }




    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

    @NotNull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}