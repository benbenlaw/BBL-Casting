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

public record CoolantRecipe(FluidStackTemplate fluid, int duration) implements Recipe<RecipeInput> {

    public static final MapCodec<CoolantRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(CoolantRecipe::fluid),
                    Codec.INT.fieldOf("duration").forGetter(CoolantRecipe::duration)
            ).apply(instance, CoolantRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CoolantRecipe> STREAM_CODEC = StreamCodec.of(
            CoolantRecipe::write, CoolantRecipe::read);

    public static final RecipeType<CoolantRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<CoolantRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static CoolantRecipe read(RegistryFriendlyByteBuf buffer) {
        FluidStackTemplate fluid = FluidStackTemplate.STREAM_CODEC.decode(buffer);
        int duration = buffer.readInt();
        return new CoolantRecipe(fluid, duration);
    }

    private static void write(RegistryFriendlyByteBuf buffer, CoolantRecipe recipe) {
        FluidStackTemplate.STREAM_CODEC.encode(buffer, recipe.fluid);
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
