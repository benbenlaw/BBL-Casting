package com.benbenlaw.casting.recipe;

import com.benbenlaw.casting.Casting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CastingRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Casting.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Casting.MOD_ID);


    //Melting
    public static final Supplier<RecipeSerializer<MeltingRecipe>> MELTING_SERIALIZER =
            SERIALIZER.register("melting", () -> MeltingRecipe.SERIALIZER);
    public static final Supplier<RecipeType<MeltingRecipe>> MELTING_TYPE =
            TYPES.register("melting", () -> MeltingRecipe.TYPE);

    //Fuel
    public static final Supplier<RecipeSerializer<FuelRecipe>> FUEL_SERIALIZER =
            SERIALIZER.register("fuel", () -> FuelRecipe.SERIALIZER);
    public static final Supplier<RecipeType<FuelRecipe>> FUEL_TYPE =
            TYPES.register("fuel", () -> FuelRecipe.TYPE);

    //Coolant
    public static final Supplier<RecipeSerializer<CoolantRecipe>> COOLANT_SERIALIZER =
            SERIALIZER.register("coolant", () -> CoolantRecipe.SERIALIZER);
    public static final Supplier<RecipeType<CoolantRecipe>> COOLANT_TYPE =
            TYPES.register("coolant", () -> CoolantRecipe.TYPE);

    //Solidifier
    public static final Supplier<RecipeSerializer<SolidifierRecipe>> SOLIDIFIER_SERIALIZER =
            SERIALIZER.register("solidifier", () -> SolidifierRecipe.SERIALIZER);
    public static final Supplier<RecipeType<SolidifierRecipe>> SOLIDIFIER_TYPE =
            TYPES.register("solidifier", () -> SolidifierRecipe.TYPE);

    //Mixer
    public static final Supplier<RecipeSerializer<MixingRecipe>> MIXING_SERIALIZER =
            SERIALIZER.register("mixing", () -> MixingRecipe.SERIALIZER);
    public static final Supplier<RecipeType<MixingRecipe>> MIXING_TYPE =
            TYPES.register("mixing", () -> MixingRecipe.TYPE);


}
