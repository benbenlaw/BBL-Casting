package com.benbenlaw.casting.data.custom;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SolidifierRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected SizedIngredient mold;
    protected SizedIngredient output;
    protected SizedFluidIngredient fluid;
    protected int meltingTemp;
    protected Optional<Double> durationModifier;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SolidifierRecipeBuilder(SizedIngredient mold, SizedIngredient output, SizedFluidIngredient fluid, int meltingTemp, Optional<Double> durationModifier) {
        this.mold = mold;
        this.output = output;
        this.fluid = fluid;
        this.meltingTemp = meltingTemp;
        this.durationModifier = durationModifier;
    }

    public static SolidifierRecipeBuilder solidifierRecipesBuilder(SizedIngredient mold, SizedIngredient output, SizedFluidIngredient fluid, int meltingTemp, Optional<Double> durationModifier) {
        return new SolidifierRecipeBuilder(mold, output, fluid, meltingTemp, durationModifier);
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
        ItemStackTemplate stack = getStackFromSized(output);
        return ResourceKey.create(
                Registries.RECIPE,
                Casting.identifier("solidifier/" + stack.item().value().builtInRegistryHolder().key().identifier().getPath())
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Casting.identifier("solidifier/" + id)));
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        SolidifierRecipe solidifierRecipe = new SolidifierRecipe(this.mold, this.output, this.fluid, this.meltingTemp, this.durationModifier);
        recipeOutput.accept(resourceKey, solidifierRecipe, builder.build(resourceKey.identifier().withPrefix("recipes/solidifier/")));

    }

    private ItemStackTemplate getStackFromSized(SizedIngredient sizedIngredient) {
        return sizedIngredient.ingredient().items()
                .findFirst()
                .map(holder -> new ItemStackTemplate(holder.value(), sizedIngredient.count()))
                .orElseThrow();
    }

}
