package com.benbenlaw.casting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record FuelRecipe(FluidStackTemplate fluid, int temp, int duration) implements Recipe<RecipeInput> {

    public static final MapCodec<FuelRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(FuelRecipe::fluid),
                    Codec.INT.fieldOf("temp").forGetter(FuelRecipe::temp),
                    Codec.INT.fieldOf("duration").forGetter(FuelRecipe::duration)
            ).apply(instance, FuelRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FuelRecipe> STREAM_CODEC = StreamCodec.of(
            FuelRecipe::write, FuelRecipe::read);

    public static final RecipeType<FuelRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<FuelRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static FuelRecipe read(RegistryFriendlyByteBuf buffer) {
        FluidStackTemplate fluid = FluidStackTemplate.STREAM_CODEC.decode(buffer);
        int temp = buffer.readInt();
        int duration = buffer.readInt();
        return new FuelRecipe(fluid, temp, duration);
    }

    private static void write(RegistryFriendlyByteBuf buffer, FuelRecipe recipe) {
        FluidStackTemplate.STREAM_CODEC.encode(buffer, recipe.fluid);
        buffer.writeInt(recipe.temp);
        buffer.writeInt(recipe.duration);
    }

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        return true;
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(RecipeInput recipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<RecipeInput>> getType() {
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