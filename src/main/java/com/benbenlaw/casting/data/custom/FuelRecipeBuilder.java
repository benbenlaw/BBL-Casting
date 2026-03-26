package com.benbenlaw.casting.data.custom;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.recipe.custom.FuelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class FuelRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected FluidStackTemplate fluid;
    protected int temp;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public FuelRecipeBuilder(FluidStackTemplate fluid, int temp) {
        this.fluid = fluid;
        this.temp = temp;
    }

    public static FuelRecipeBuilder fuelRecipesBuilder(FluidStackTemplate fluid, int temp) {
        return new FuelRecipeBuilder(fluid, temp);
    }


    @Override
    public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        FluidStack stack = fluid.create();
        return ResourceKey.create(
                Registries.RECIPE,
                Casting.identifier("fuel/" + stack.getFluid().builtInRegistryHolder().key().identifier().getPath())
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Casting.identifier("fuel/" + id)));
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        FuelRecipe fuelRecipe = new FuelRecipe(this.fluid, this.temp);
        recipeOutput.accept(resourceKey, fuelRecipe, builder.build(resourceKey.identifier().withPrefix("recipes/fuel/")));

    }

}
