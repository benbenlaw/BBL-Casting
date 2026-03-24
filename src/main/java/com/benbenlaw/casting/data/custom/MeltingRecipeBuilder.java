package com.benbenlaw.casting.data.custom;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.entity.MixerBlockEntity;
import com.benbenlaw.casting.recipe.MeltingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MeltingRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected SizedIngredient input;
    protected List<FluidStackTemplate> output;
    protected int meltingTemp;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public MeltingRecipeBuilder(SizedIngredient input, List<FluidStackTemplate> output, int meltingTemp) {
        this.input = input;
        this.output = output;
        this.meltingTemp = meltingTemp;
    }

    public static MeltingRecipeBuilder meltingRecipesBuilder(SizedIngredient input, List<FluidStackTemplate> output, int meltingTemp) {
        return new MeltingRecipeBuilder(input, output, meltingTemp);
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
        ItemStack stack = getStackFromSized(input);
        return ResourceKey.create(
                Registries.RECIPE,
                Casting.identifier("melting/" + stack.getItem().builtInRegistryHolder().key().identifier().getPath())
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Casting.identifier("melting/" + id)));
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        MeltingRecipe meltingRecipe = new MeltingRecipe(this.input, this.output, this.meltingTemp);
        recipeOutput.accept(resourceKey, meltingRecipe, builder.build(resourceKey.identifier().withPrefix("recipes/melting/")));

    }

    private ItemStack getStackFromSized(SizedIngredient sizedIngredient) {
        return sizedIngredient.ingredient().items()
                .findFirst()
                .map(holder -> new ItemStack(holder.value(), sizedIngredient.count()))
                .orElse(ItemStack.EMPTY);
    }

}
