package com.benbenlaw.casting.data.custom;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.recipe.custom.MixingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.NonNullList;
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

public class MixingRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected NonNullList<FluidStackTemplate> fluids;
    protected FluidStackTemplate outputFluid;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public MixingRecipeBuilder(NonNullList<FluidStackTemplate> fluids, FluidStackTemplate outputFluid) {
        this.fluids = NonNullList.create();
        this.fluids.addAll(fluids);
        this.outputFluid = outputFluid;
    }

    public static MixingRecipeBuilder mixingRecipesBuilder(NonNullList<FluidStackTemplate> fluids, FluidStackTemplate outputFluid) {
        return new MixingRecipeBuilder(fluids, outputFluid);
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
        FluidStack stack = outputFluid.create();
        return ResourceKey.create(
                Registries.RECIPE,
                Casting.identifier("mixing/" + stack.getFluid().builtInRegistryHolder().key().identifier().getPath())
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Casting.identifier("mixing/" + id)));
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", net.minecraft.advancements.criterion.RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        MixingRecipe mixingRecipe = new MixingRecipe(this.fluids, this.outputFluid);
        recipeOutput.accept(resourceKey, mixingRecipe, builder.build(resourceKey.identifier().withPrefix("recipes/mixing/")));

    }

}
