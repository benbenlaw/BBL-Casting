package com.benbenlaw.casting.recipe;

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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record MeltingRecipe(SizedIngredient input, List<FluidStackTemplate> output, int meltingTemp) implements Recipe<RecipeInput> {

    public static final MapCodec<MeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    SizedIngredient.NESTED_CODEC.fieldOf("input").forGetter(MeltingRecipe::input),
                    FluidStackTemplate.CODEC.listOf().fieldOf("output").forGetter(MeltingRecipe::output),
                    Codec.INT.fieldOf("meltingTemp").forGetter(MeltingRecipe::meltingTemp)
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
        return new MeltingRecipe(input, output, meltingTemp);
    }

    private static void write(RegistryFriendlyByteBuf buffer, MeltingRecipe recipe) {
        SizedIngredient.STREAM_CODEC.encode(buffer, recipe.input);
        FluidStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.output);
        buffer.writeInt(recipe.meltingTemp);
    }
    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        for (int i = 0; i < 15; i++) {
            if (input.test(container.getItem(i))) {
                return true;
            }
        }
        return false;
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
