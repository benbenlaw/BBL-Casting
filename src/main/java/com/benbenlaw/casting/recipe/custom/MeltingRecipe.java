package com.benbenlaw.casting.recipe.custom;

import com.benbenlaw.casting.recipe.MeltingRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public record MeltingRecipe(SizedIngredient input, List<FluidStackTemplate> output, int meltingTemp, Optional<Double> durationModifier) implements Recipe<MeltingRecipeInput> {

    public static final MapCodec<MeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    SizedIngredient.NESTED_CODEC.fieldOf("input").forGetter(MeltingRecipe::input),
                    FluidStackTemplate.CODEC.listOf().fieldOf("output").forGetter(MeltingRecipe::output),
                    Codec.INT.fieldOf("meltingTemp").forGetter(MeltingRecipe::meltingTemp),
                    Codec.DOUBLE.optionalFieldOf("durationModifier").forGetter(MeltingRecipe::durationModifier)
            ).apply(instance, MeltingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MeltingRecipe> STREAM_CODEC = StreamCodec.of(
            MeltingRecipe::write, MeltingRecipe::read);

    public static final RecipeType<MeltingRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<MeltingRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static MeltingRecipe read(RegistryFriendlyByteBuf buffer) {
        SizedIngredient input = SizedIngredient.STREAM_CODEC.decode(buffer);
        List<FluidStackTemplate> output = FluidStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        int meltingTemp = buffer.readInt();
        Optional<Double> durationModifier = buffer.readBoolean() ? Optional.of(buffer.readDouble()) : Optional.empty();
        return new MeltingRecipe(input, output, meltingTemp, durationModifier);
    }

    private static void write(RegistryFriendlyByteBuf buffer, MeltingRecipe recipe) {
        SizedIngredient.STREAM_CODEC.encode(buffer, recipe.input);
        FluidStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.output);
        buffer.writeInt(recipe.meltingTemp);
        if (recipe.durationModifier.isPresent()) {
            buffer.writeBoolean(true);
            buffer.writeDouble(recipe.durationModifier.get());
        } else {
            buffer.writeBoolean(false);
        }
    }
    @Override
    public boolean matches(@NotNull MeltingRecipeInput container, @NotNull Level level) {
        for (int i = 0; i < 15; i++) {
            if (input.test(container.getItem(i)) && container.temp() >= meltingTemp) {
                return true;
            }
        }
        return false;
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(MeltingRecipeInput recipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<MeltingRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<MeltingRecipeInput>> getType() {
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
