package com.benbenlaw.casting.recipe.custom;

import com.benbenlaw.core.recipe.NoInventoryRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record MixingRecipe(NonNullList<SizedFluidIngredient> fluids, FluidStackTemplate outputFluid) implements Recipe<NoInventoryRecipe> {

    public static final MapCodec<MixingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.list(SizedFluidIngredient.CODEC).fieldOf("inputs").flatXmap(inputFluids -> {
                        NonNullList<SizedFluidIngredient> nonNullList = NonNullList.create();
                        nonNullList.addAll(inputFluids);
                        return DataResult.success(nonNullList);
                    }, DataResult::success).forGetter(MixingRecipe::fluids),
                    FluidStackTemplate.CODEC.fieldOf("output").forGetter(MixingRecipe::outputFluid)
            ).apply(instance, MixingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MixingRecipe> STREAM_CODEC = StreamCodec.of(
            MixingRecipe::write, MixingRecipe::read);

    public static final RecipeType<MixingRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<MixingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static MixingRecipe read(RegistryFriendlyByteBuf buffer) {
        int fluidCount = buffer.readInt();
        NonNullList<SizedFluidIngredient> fluids = NonNullList.create();
        for (int i = 0; i < fluidCount; i++) {
            SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
            fluids.add(fluid);
        }

        FluidStackTemplate output = FluidStackTemplate.STREAM_CODEC.decode(buffer);
        return new MixingRecipe(fluids, output);
    }

    private static void write(RegistryFriendlyByteBuf buffer, MixingRecipe recipe) {
        buffer.writeInt(recipe.fluids.size());
        for (SizedFluidIngredient fluid : recipe.fluids) {
            SizedFluidIngredient.STREAM_CODEC.encode(buffer, fluid);
        }

        FluidStackTemplate.STREAM_CODEC.encode(buffer, recipe.outputFluid);
    }

    @Override
    public boolean matches(NoInventoryRecipe recipe, Level level) {
            return true;
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(NoInventoryRecipe recipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<NoInventoryRecipe>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<NoInventoryRecipe>> getType() {
        return TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }
}