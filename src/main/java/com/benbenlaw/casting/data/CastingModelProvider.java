package com.benbenlaw.casting.data;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.core.block.SyncableBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.apache.logging.log4j.util.Cast;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;
import static net.minecraft.client.data.models.BlockModelGenerators.*;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_270;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_90;

public class CastingModelProvider extends ModelProvider {

    public CastingModelProvider(PackOutput output) {
        super(output, Casting.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Items
        itemModels.generateFlatItem(CastingItems.BLACK_BRICK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.BLOCK_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.GEAR_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.INGOT_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.NUGGET_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.PLATE_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.ROD_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.GEM_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.DUST_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.BALL_MOLD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CastingItems.WIRE_MOLD.get(), ModelTemplates.FLAT_ITEM);


        // Bucket Models
        for (var entry : FLUIDS_MAP.entrySet()) {
            String fluidName = entry.getKey();
            if (BuiltInRegistries.ITEM.getValue(Casting.identifier(fluidName + "_bucket")) instanceof BucketItem bucketItem) {
                var type = bucketItem.content.getFluidType();
                bucketItem(itemModels, bucketItem, bucketItem.content, type.isLighterThanAir(), type.getLightLevel() > 0);
            }
        }

        //Blocks
        createMachineBlock(CastingBlocks.CONTROLLER.get(), blockModels.blockStateOutput, blockModels.modelOutput);
        createMachineBlock(CastingBlocks.SOLIDIFIER.get(), blockModels.blockStateOutput, blockModels.modelOutput);
        createMachineBlock(CastingBlocks.MIXER.get(), blockModels.blockStateOutput, blockModels.modelOutput);

        blockModels.createTrivialCube(CastingBlocks.BLACK_BRICKS.get());
        blockModels.createTrivialCube(CastingBlocks.BLACK_BRICK_GLASS.get());
        blockModels.createTrivialCube(CastingBlocks.FUEL_TANK.get());
        blockModels.createTrivialCube(CastingBlocks.COOLANT_TANK.get());

        //Fluids?
        for (var entry : FLUIDS_MAP.entrySet()) {
            Block fluidBlock = entry.getValue().getBlock();

            blockModels.createNonTemplateModelBlock(fluidBlock);
        }
    }

    public void createMachineBlock(Block block, Consumer<BlockModelDefinitionGenerator> blockStateOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        TextureMapping idleTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(Casting.identifier("block/casting_top"))).put(TextureSlot.SIDE, new Material(Casting.identifier("block/casting_side"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
        TextureMapping workingTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(Casting.identifier("block/casting_top"))).put(TextureSlot.SIDE, new Material(Casting.identifier("block/casting_side"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_working"));

        MultiVariant multivariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, idleTextureMapping, modelOutput));
        MultiVariant multivariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, idleTextureMapping, modelOutput));

        MultiVariant workingVariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));
        MultiVariant workingVariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));

        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.FACING, CastingBlock.WORKING)
                                .select(Direction.DOWN, false, multivariant1.with(X_ROT_180))
                                .select(Direction.UP, false, multivariant1)
                                .select(Direction.NORTH, false, multivariant)
                                .select(Direction.EAST, false, multivariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,false, multivariant.with(Y_ROT_180))
                                .select(Direction.WEST,false, multivariant.with(Y_ROT_270))
                                .select(Direction.DOWN, true, workingVariant1.with(X_ROT_180))
                                .select(Direction.UP, true, workingVariant1)
                                .select(Direction.NORTH, true, workingVariant)
                                .select(Direction.EAST, true, workingVariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,true, workingVariant.with(Y_ROT_180))
                                .select(Direction.WEST,true, workingVariant.with(Y_ROT_270))));

    }

    public void bucketItem(ItemModelGenerators itemModelGenerators, BucketItem item, Fluid fluid, boolean flipGas, boolean applyFluidLuminosity) {
        Material drip = new Material(Identifier.fromNamespaceAndPath(NeoForgeMod.MOD_ID, "item/mask/bucket_fluid_drip"));
        Material bucket = new Material(Identifier.withDefaultNamespace("item/bucket"));
        DynamicFluidContainerModel.Textures textures = new DynamicFluidContainerModel.Textures(Optional.empty(), Optional.of(bucket), Optional.of(drip), Optional.empty());
        itemModelGenerators.itemModelOutput.accept(item, new DynamicFluidContainerModel.Unbaked(textures, fluid, flipGas, false, applyFluidLuminosity));
    }
}
