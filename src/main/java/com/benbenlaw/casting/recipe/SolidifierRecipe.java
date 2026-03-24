package com.benbenlaw.casting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record SolidifierRecipe(SizedIngredient mold, SizedIngredient output, FluidStackTemplate fluid) implements Recipe<RecipeInput> {

    public static final MapCodec<SolidifierRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    SizedIngredient.NESTED_CODEC.fieldOf("mold").forGetter(SolidifierRecipe::mold),
                    SizedIngredient.NESTED_CODEC.fieldOf("output").forGetter(SolidifierRecipe::output),
                    FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(SolidifierRecipe::fluid)
            ).apply(instance, SolidifierRecipe::new)
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, SolidifierRecipe> STREAM_CODEC = StreamCodec.of(
            SolidifierRecipe::write, SolidifierRecipe::read);

    public static final RecipeType<SolidifierRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<SolidifierRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static SolidifierRecipe read(RegistryFriendlyByteBuf buffer) {
        SizedIngredient mold = SizedIngredient.STREAM_CODEC.decode(buffer);
        SizedIngredient output = SizedIngredient.STREAM_CODEC.decode(buffer);
        FluidStackTemplate fluid = FluidStackTemplate.STREAM_CODEC.decode(buffer);
        return new SolidifierRecipe(mold, output, fluid );
    }

    private static void write(RegistryFriendlyByteBuf buffer, SolidifierRecipe recipe) {
        SizedIngredient.STREAM_CODEC.encode(buffer, recipe.mold);
        SizedIngredient.STREAM_CODEC.encode(buffer, recipe.output);
        FluidStackTemplate.STREAM_CODEC.encode(buffer, recipe.fluid);
    }

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        return mold.test(container.getItem(0));
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(RecipeInput recipeInput) {
        return new ItemStackTemplate(output.ingredient().getValues().get(0).value()).create();
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
