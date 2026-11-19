package com.benbenlaw.casting.data;

import appeng.api.ids.AEItemIds;
import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.data.recipes.*;
import com.benbenlaw.casting.fluid.FluidData;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.casting.util.MaterialMelting;
import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.core.tag.ModdedTagBuilder;
import com.benbenlaw.core.tag.ResourceNames;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.benbenlaw.casting.data.recipes.FluidStackHelper.fluidList;
import static com.benbenlaw.casting.data.recipes.FluidStackHelper.getFluidStack;
import static com.benbenlaw.casting.fluid.CastingFluids.FLUIDS_MAP;
import static com.benbenlaw.casting.item.EquipmentModifier.*;
import static com.benbenlaw.casting.util.MaterialMelting.WIRES;

public class CastingRecipeProvider extends RecipeProvider {

    public CastingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }
    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        //Smelting
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.BRICK), RecipeCategory.MISC, new ItemStack(CastingItems.BLACK_BRICK.get()), 0.5f, 200)
                .unlockedBy("has_brick", has(Items.BRICK)).save(consumer, "casting:smelting/black_brick");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.BRICKS), RecipeCategory.MISC, new ItemStack(CastingBlocks.BLACK_BRICKS.get()), 0.5f, 200)
                .unlockedBy("has_brick", has(Items.BRICK)).save(consumer, "casting:smelting/black_bricks");

        //Shapeless
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.TANK, 1)
                .requires(CastingBlocks.TANK)
                .unlockedBy("has_brick", has(CastingBlocks.TANK)).save(consumer, "casting:clear_fluids/tank");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.CONTROLLER, 1)
                .requires(CastingBlocks.CONTROLLER)
                .unlockedBy("has_brick", has(CastingBlocks.CONTROLLER)).save(consumer, "casting:clear_fluids/controller");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.SOLIDIFIER, 1)
                .requires(CastingBlocks.SOLIDIFIER)
                .unlockedBy("has_brick", has(CastingBlocks.SOLIDIFIER)).save(consumer, "casting:clear_fluids/solidifier");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.MIXER, 1)
                .requires(CastingBlocks.MIXER)
                .unlockedBy("has_brick", has(CastingBlocks.MIXER)).save(consumer, "casting:clear_fluids/mixer");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.EQUIPMENT_MODIFIER, 1)
                .requires(CastingBlocks.EQUIPMENT_MODIFIER)
                .unlockedBy("has_brick", has(CastingBlocks.EQUIPMENT_MODIFIER)).save(consumer, "casting:clear_fluids/equipment_modifier");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.MULTIBLOCK_COOLANT_TANK, 1)
                .requires(CastingBlocks.MULTIBLOCK_COOLANT_TANK)
                .unlockedBy("has_brick", has(CastingBlocks.MULTIBLOCK_COOLANT_TANK)).save(consumer, "casting:clear_fluids/multiblock_coolant_tank");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.MULTIBLOCK_FUEL_TANK, 1)
                .requires(CastingBlocks.MULTIBLOCK_FUEL_TANK)
                .unlockedBy("has_brick", has(CastingBlocks.MULTIBLOCK_FUEL_TANK)).save(consumer, "casting:clear_fluids/multiblock_fuel_tank");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CastingBlocks.MULTIBLOCK_CONTROLLER, 1)
                .requires(CastingBlocks.MULTIBLOCK_CONTROLLER)
                .unlockedBy("has_brick", has(CastingBlocks.MULTIBLOCK_CONTROLLER)).save(consumer, "casting:clear_fluids/multiblock_controller");


        //Multiblock Crafting
        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), CastingBlocks.CONTROLLER.toStack(),
                CastingBlocks.MULTIBLOCK_CONTROLLER,"multiblock/controller", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), CastingBlocks.TANK.toStack(),
                CastingBlocks.MULTIBLOCK_FUEL_TANK,"multiblock/fuel_tank", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), CastingBlocks.SOLIDIFIER.toStack(),
                CastingBlocks.MULTIBLOCK_SOLIDIFIER,"multiblock/solidifier", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), CastingBlocks.MIXER.toStack(),
                CastingBlocks.MULTIBLOCK_MIXER,"multiblock/mixer", consumer);

        //Change this in the future
        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), CastingBlocks.MULTIBLOCK_FUEL_TANK.toStack(),
                CastingBlocks.MULTIBLOCK_VALVE,"multiblock/valve", consumer);

        //Change this in the future
        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 4000), Items.GLASS.getDefaultInstance(),
                CastingBlocks.MULTIBLOCK_COOLANT_TANK,"multiblock/coolant_tank", consumer);


        // Crafting
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.BLACK_BRICKS.get(), 1)
                .pattern("AA")
                .pattern("AA")
                .define('A', CastingItems.BLACK_BRICK)
                .unlockedBy("has_brick", has(CastingItems.BLACK_BRICK)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.BLACK_BRICK_GLASS.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', Tags.Items.GLASS_BLOCKS)
                .unlockedBy("has_brick", has(CastingItems.BLACK_BRICK)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.MULTIBLOCK_REGULATOR.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', CastingBlocks.BLACK_BRICKS)
                .unlockedBy("has_brick", has(CastingItems.BLACK_BRICK)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingItems.FLUID_MOVER.get(), 1)
                .pattern(" BB")
                .pattern(" BB")
                .pattern("B  ")
                .define('B', CastingItems.BLACK_BRICK.get())
                .unlockedBy("has_black_brick", has(CastingItems.BLACK_BRICK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.TANK.get(), 1)
                .pattern("BBB")
                .pattern("BGB")
                .pattern("BBB")
                .define('B', CastingItems.BLACK_BRICK.get())
                .define('G', Tags.Items.GLASS_BLOCKS)
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.CONTROLLER.get(), 1)
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', CastingItems.BLACK_BRICK.get())
                .define('B', CastingBlocks.BLACK_BRICKS.get())
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.SOLIDIFIER.get(), 1)
                .pattern("BBB")
                .pattern("A A")
                .pattern("BBB")
                .define('A', CastingItems.BLACK_BRICK.get())
                .define('B', CastingBlocks.BLACK_BRICKS.get())
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.MIXER.get(), 1)
                .pattern("BBB")
                .pattern("A A")
                .pattern("BBB")
                .define('B', CastingBlocks.BLACK_BRICKS.get())
                .define('A', CastingBlocks.TANK)
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.MIXER_WHISK.get(), 1)
                .pattern("BIB")
                .pattern("BSB")
                .pattern("BIB")
                .define('B', CastingBlocks.BLACK_BRICKS.get())
                .define('S', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingBlocks.EQUIPMENT_MODIFIER.get(), 1)
                .pattern("BBB")
                .pattern("STS")
                .pattern("BBB")
                .define('B', CastingBlocks.BLACK_BRICKS.get())
                .define('S', CastingBlocks.SOLIDIFIER.get())
                .define('T', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .unlockedBy("has_black_brick", has(CastingBlocks.BLACK_BRICKS.get()))
                .save(consumer);

        //Tool Modifier
        toolModifierRecipes(consumer, null, getFluidStack("molten_lapis", 1350), FORTUNE.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_redstone", 1350), EFFICIENCY.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_emerald", 720), SILK_TOUCH.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_obsidian", 8000), UNBREAKING.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_glowstone", 8000), REPAIRING.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Tags.Items.RODS_WOODEN), 64), getFluidStack("molten_coal", 5120), TORCH_PLACING.id);
        toolModifierRecipes(consumer, null, new FluidStack(Fluids.LAVA, 8000), AUTO_SMELT.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Tags.Items.GEMS_EMERALD), 8), getFluidStack("molten_lapis", 1350), LOOTING.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_quartz", 1350), SHARPNESS.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(ItemTags.SKULLS), 1), null, BEHEADING.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_soul", 3200), SOULBOUND.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.GOLDEN_APPLE), 1), getFluidStack("molten_gold", 720), LIFESTEAL.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.PISTON), 4), null,KNOCKBACK.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.FLINT_AND_STEEL), 1), new FluidStack(Fluids.LAVA, 8000),IGNITE.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_diamond", 360),EXCAVATION.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_ender", 640), TELEPORTING.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Tags.Items.INGOTS_IRON), 6), getFluidStack("molten_gold", 540), MAGNET.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_steel", 720), PROTECTION.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.STICKY_PISTON), 4), null, STEP_ASSIST.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.GOLDEN_CARROT), 8), null, NIGHT_VISION.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.PUFFERFISH), 2), null, WATER_BREATHING.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.SUGAR), 12), null, SPEED.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.MAGMA_BLOCK), 8), null, LAVA_WALKER.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.SPONGE), 8), null, WATER_WALKER.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.NETHER_STAR), 1), null, FLIGHT.id);
        toolModifierRecipes(consumer, new SizedIngredient(Ingredient.of(Items.FEATHER), 4), null, FEATHER_FALLING.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_experience", 500), EQUIPMENT_LEVEL.id);
        toolModifierRecipes(consumer, null, getFluidStack("molten_blaze", 720), JETS.id);

        // Molds
        createMoldRecipe(CastingItems.GEAR_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("gears"), consumer);
        createMoldRecipe(CastingItems.PLATE_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("plates"), consumer);
        createMoldRecipe(CastingItems.ROD_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("rods"), consumer);
        createMoldRecipe(CastingItems.NUGGET_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("nuggets"), consumer);
        createMoldRecipe(CastingItems.INGOT_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("ingots"), consumer);
        createMoldRecipeWithID(CastingItems.INGOT_MOLD.get(), Tags.Items.BRICKS, consumer, "ingot_mold_bricks");
        createMoldRecipe(CastingItems.BLOCK_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("storage_blocks"), consumer);
        createMoldRecipe(CastingItems.GEM_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("gems"), consumer);
        createMoldRecipeWithID(CastingItems.GEM_MOLD.get(), ItemTags.COALS, consumer, "gem_mold_coals");
        createMoldRecipe(CastingItems.DUST_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("dusts"), consumer);
        createMoldRecipe(CastingItems.BALL_MOLD.get(), CastingTags.Items.BALL_ITEMS, consumer);
        createMoldRecipe(CastingItems.WIRE_MOLD.get(), ModdedTagBuilder.createNeoFabricItemTag("wires"), consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CastingItems.REPAIRING_MOLD.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK)
                .define('B', Items.ANVIL)
                .unlockedBy("has_item", has(Items.ANVIL))
                .save(consumer);

        // Fuels
        FuelRecipeBuilder.fuelRecipesBuilder(new FluidStack(Fluids.LAVA, 10), 1000, 300)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        FuelRecipeBuilder.fuelRecipesBuilder(getFluidStack("molten_coal", 8), 1200, 260)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        FuelRecipeBuilder.fuelRecipesBuilder(getFluidStack("molten_obsidian", 8), 1200, 200)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        FuelRecipeBuilder.fuelRecipesBuilder(getFluidStack("molten_uranium", 20), 1400, 100)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        FuelRecipeBuilder.fuelRecipesBuilder(getFluidStack("molten_blaze", 1), 1400, 60)
                .unlockedBy("has_item", has(Items.BLAZE_POWDER)).save(consumer);

        // Coolants
        CoolantRecipeBuilder.coolantRecipesBuilder(new FluidStack(Fluids.WATER, 10), 160)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("molten_ender", 5), 100)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("molten_glowstone", 10), 140)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("molten_diamond", 20), 80)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("chilled_water", 25), 60)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("iced_water", 25), 40)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        CoolantRecipeBuilder.coolantRecipesBuilder(getFluidStack("super_coolant", 10), 20)
                .unlockedBy("has_item", has(Items.BUCKET)).save(consumer);

        // Common Melting and Solidifier Recipes
        for (String resource : ResourceNames.getAllResourceNames()) {
            // Ignore coal as it has its own melting values
            // Skip resources bblcore lists, but Casting has no molten fluid for (e.g. copper_alloy, removed in 2.3.7)
            if (!resource.equals("coal") && !resource.equals("quartz") && FLUIDS_MAP.containsKey("molten_" + resource)) {
                createCommonMeltingRecipes(resource, consumer);
                createCommonSolidifierRecipes(resource, consumer);
            }
        }

        // Quartz enriched iron (Refined Storage)
        createSimpleMeltingRecipe(getFluidStack("molten_quartz_enriched_iron", 90), BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:quartz_enriched_iron")),
                FluidData.getTempByName("molten_quartz_enriched_iron"), "quartz_enriched_iron/from_ingot", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_quartz_enriched_iron", 90), CastingTags.Items.INGOT_MOLD,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:quartz_enriched_iron")), "quartz_enriched_iron/ingot", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_iron", 270), getFluidStack("molten_quartz", 250)),
                getFluidStack("molten_quartz_enriched_iron", 360), "molten_quartz_enriched_iron", consumer);

        // Quartz enriched copper (Refined Storage)
        createSimpleMeltingRecipe(getFluidStack("molten_quartz_enriched_copper", 90), BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:quartz_enriched_copper")),
                FluidData.getTempByName("molten_quartz_enriched_copper"), "quartz_enriched_copper/from_ingot", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_quartz_enriched_copper", 90), CastingTags.Items.INGOT_MOLD,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:quartz_enriched_copper")), "quartz_enriched_copper/ingot", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 270), getFluidStack("molten_quartz", 250)),
                getFluidStack("molten_quartz_enriched_copper", 360), "molten_quartz_enriched_copper", consumer);

        // AE2 Certus Quartz
        createSimpleMeltingRecipe(getFluidStack("molten_certus_quartz", 250), BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:certus_quartz_crystal")),
                FluidData.getTempByName("molten_certus_quartz"), "certus_quartz/from_certus_quartz", consumer);

        createSimpleMeltingRecipe(getFluidStack("molten_certus_quartz", 1000), ModdedTagBuilder.createNeoFabricItemTag("storage_blocks/certus_quartz"),
                FluidData.getTempByName("molten_certus_quartz"), "certus_quartz/from_block", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_certus_quartz", 250), CastingTags.Items.GEM_MOLD,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:certus_quartz_crystal")), "certus_quartz/crystal", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_certus_quartz", 1000), CastingTags.Items.BLOCK_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("storage_blocks/certus_quartz"), "certus_quartz/block", consumer);

        // AE2 Fluix
        createSimpleMeltingRecipe(getFluidStack("molten_fluix", 250), ModdedTagBuilder.createNeoFabricItemTag("gems/fluix"),
                FluidData.getTempByName("molten_fluix"), "fluix/from_fluix", consumer);

        createSimpleMeltingRecipe(getFluidStack("molten_fluix", 1000), BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:fluix_block")),
                FluidData.getTempByName("molten_fluix"), "fluix/from_block", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_fluix", 250), CastingTags.Items.GEM_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("gems/fluix"), "fluix/crystal", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_fluix", 1000), CastingTags.Items.BLOCK_MOLD,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:fluix_block")), "fluix/block", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_quartz", 250), getFluidStack("molten_redstone", 90), getFluidStack("molten_charged_certus_quartz", 250)),
                getFluidStack("molten_fluix", 500), "fluix", consumer);

        // AE2 Charged Certus Quartz
        createSimpleMeltingRecipe(getFluidStack("molten_charged_certus_quartz", 250), BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:charged_certus_quartz_crystal")),
                FluidData.getTempByName("molten_charged_certus_quartz"), "charged_certus_quartz/from_charged_certus_quartz", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_charged_certus_quartz", 250), CastingTags.Items.GEM_MOLD,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("ae2:charged_certus_quartz_crystal")), "charged_certus_quartz/crystal", consumer);




        // Experience
        createSimpleSolidifierRecipe(getFluidStack("molten_experience", 100), Items.GLASS_BOTTLE.getDefaultInstance(),
                Items.EXPERIENCE_BOTTLE, "experience/bottle", consumer);;

        // Obsidian
        createSimpleMeltingRecipe(getFluidStack("molten_obsidian", 1000), Items.OBSIDIAN, FluidData.getTempByName("molten_obsidian"),
                "obsidian/from_block", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_obsidian", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.OBSIDIAN, "obsidian/block", consumer);

        // Glass
        createSimpleMeltingRecipe(getFluidStack("molten_glass", 1000), Items.GLASS, FluidData.getTempByName("molten_glass"),
                "glass/from_block", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_glass", 1000), ItemTags.SAND, FluidData.getTempByName("molten_glass"),
                "glass/from_sand", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_glass", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.GLASS,"glass/block", consumer);

        //Debris
        createSimpleMeltingRecipe(getFluidStack("molten_debris", 90), Items.ANCIENT_DEBRIS, FluidData.getTempByName("molten_debris"),
                "debris/from_debris", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_debris", 90), CastingTags.Items.INGOT_MOLD,
                Items.NETHERITE_SCRAP,"debris/netherite_scrap", consumer);

        //Silicon
        createSimpleMeltingRecipe(getFluidStack("molten_silicon", 250), ModdedTagBuilder.createNeoFabricItemTag("silicon"), FluidData.getTempByName("molten_silicon"),
                "silicon/from_silicon", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_silicon",  250), CastingTags.Items.BALL_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("silicon"), "silicon/block", consumer);

        //Coal
        createSimpleMeltingRecipe(getFluidStack("molten_coal", 80), Items.COAL, FluidData.getTempByName("molten_coals"),
                "coal/from_coal", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_coal", 720), Items.COAL_BLOCK, FluidData.getTempByName("molten_coals"),
                "coal/from_block", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_coal", 80), ModdedTagBuilder.createNeoFabricItemTag("ores/coal") , FluidData.getTempByName("molten_coals"),
                "coal/from_ore", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_coal", 80), CastingTags.Items.GEM_MOLD,
                Items.COAL, "coal/coal", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_coal", 720), CastingTags.Items.BLOCK_MOLD,
                Items.COAL_BLOCK, "coal/coal_block", consumer);

        //Black Quartz
        createSimpleMeltingRecipe(getFluidStack("molten_black_quartz", 250),  ModdedTagBuilder.createNeoFabricItemTag("ores/black_quartz"), FluidData.getTempByName("molten_black_quartz"),
                "black_quartz/from_ore", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_quartz", 250),  ModdedTagBuilder.createNeoFabricItemTag("dusts/black_quartz"), FluidData.getTempByName("molten_black_quartz"),
                "black_quartz/from_dust", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_quartz", 250), ModdedTagBuilder.createNeoFabricItemTag("gems/black_quartz"), FluidData.getTempByName("molten_black_quartz"),
                "black_quartz/from_black_quartz", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_quartz", 1000), ModdedTagBuilder.createNeoFabricItemTag("storage_blocks/black_quartz"), FluidData.getTempByName("molten_black_quartz"),
                "black_quartz/from_block", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_black_quartz", 250), CastingTags.Items.GEM_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("gems/black_quartz"), "black_quartz/gem", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_black_quartz", 250), CastingTags.Items.DUST_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("dusts/black_quartz"), "black_quartz/dust", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_black_quartz", 1000), CastingTags.Items.BLOCK_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("storage_blocks/black_quartz"), "black_quartz/block", consumer);

        //Quartz
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 250), ModdedTagBuilder.createNeoFabricItemTag("ores/quartz"), FluidData.getTempByName("molten_quartz"),
                "quartz/from_ore", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 250), Items.QUARTZ, FluidData.getTempByName("molten_quartz"),
                "quartz/from_quartz", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 250), ModdedTagBuilder.createNeoFabricItemTag("dusts/quartz"), FluidData.getTempByName("molten_quartz"),
                "quartz/from_dust", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 1000), Items.QUARTZ_BLOCK, FluidData.getTempByName("molten_quartz"),
                "quartz/from_block", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 1000), Items.QUARTZ_BRICKS, FluidData.getTempByName("molten_quartz"),
                "quartz/from_bricks", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 1000), Items.CHISELED_QUARTZ_BLOCK, FluidData.getTempByName("molten_quartz"),
                "quartz/from_chiseled_block", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 1000), Items.QUARTZ_PILLAR, FluidData.getTempByName("molten_quartz"),
                "quartz/from_pillar", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_quartz", 1000), Items.SMOOTH_QUARTZ, FluidData.getTempByName("molten_quartz"),
                "quartz/from_smooth_block", consumer);


        createSimpleSolidifierRecipe(getFluidStack("molten_quartz", 250), CastingTags.Items.GEM_MOLD,
                Items.QUARTZ, "quartz/gem", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_quartz", 250), CastingTags.Items.DUST_MOLD,
                ModdedTagBuilder.createNeoFabricItemTag("dusts/quartz"), "quartz/dust", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_quartz", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.QUARTZ_BLOCK, "quartz/block", consumer);

        //Ender
        createSimpleMeltingRecipe(getFluidStack("molten_ender", 250), Items.ENDER_PEARL, FluidData.getTempByName("molten_ender"),
                "ender/from_ender_pearl", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_ender", 250), CastingTags.Items.BALL_MOLD,
                Items.ENDER_PEARL, "ender/ender_pearl", consumer);

        //End Stone
        createSimpleMeltingRecipe(getFluidStack("molten_end_stone", 1000), Items.END_STONE, FluidData.getTempByName("molten_end_stone"),
                "end_stone/from_end_stone", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_end_stone", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.END_STONE, "end_stone/block", consumer);

        //Stone
        createSimpleMeltingRecipe(getFluidStack("molten_stone", 1000), Tags.Items.STONES, FluidData.getTempByName("molten_stone"),
                "stone/from_stones", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_stone", 1000), Tags.Items.COBBLESTONES, FluidData.getTempByName("molten_stone"),
                "stone/from_cobblestones", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_stone", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.STONE, "stone/block", consumer);

        //Glowstone
        createSimpleMeltingRecipe(getFluidStack("molten_glowstone", 250), Items.GLOWSTONE_DUST, FluidData.getTempByName("molten_glowstone"),
                "glowstone/from_glowstone_dust", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_glowstone", 1000), Items.GLOWSTONE, FluidData.getTempByName("molten_glowstone"),
                "glowstone/from_glowstone", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_glowstone", 250), CastingTags.Items.DUST_MOLD,
                Items.GLOWSTONE_DUST, "glowstone/dust", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_glowstone", 1000), CastingTags.Items.BLOCK_MOLD,
                Items.GLOWSTONE, "glowstone/block", consumer);

        //Black Brick
        createSimpleMeltingRecipe(getFluidStack("molten_black_brick", 1000), CastingBlocks.BLACK_BRICKS.get(), FluidData.getTempByName("molten_black_brick"),
                "black_brick/from_black_bricks", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_brick", 250), CastingItems.BLACK_BRICK.get(), FluidData.getTempByName("molten_black_brick"),
                "black_brick/from_black_brick", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_brick", 1000), Items.CLAY, FluidData.getTempByName("molten_black_brick"),
                "black_brick/from_clay_block", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_black_brick", 250), Items.CLAY_BALL, FluidData.getTempByName("molten_black_brick"),
                "black_brick/from_clay", consumer);

        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 1000), CastingTags.Items.BLOCK_MOLD,
                CastingBlocks.BLACK_BRICKS.get(), "black_brick/block", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_black_brick", 250), CastingTags.Items.INGOT_MOLD,
                CastingItems.BLACK_BRICK.get(), "black_brick/black_brick", consumer);

        //Soul
        createSimpleMeltingRecipe(getFluidStack("molten_soul", 1000), ItemTags.SOUL_FIRE_BASE_BLOCKS, FluidData.getTempByName("molten_soul"),
                "soul/from_soul_sand", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_soul", 1000), CastingTags.Items.BLOCK_MOLD,
                ItemTags.SOUL_FIRE_BASE_BLOCKS, "soul/block", consumer);

        //Blaze
        createSimpleMeltingRecipe(getFluidStack("molten_blaze", 90), Items.BLAZE_ROD, FluidData.getTempByName("molten_blaze"),
                "blaze/from_blaze_rod", consumer);
        createSimpleMeltingRecipe(getFluidStack("molten_blaze", 45), Items.BLAZE_POWDER, FluidData.getTempByName("molten_blaze"),
                "blaze/from_blaze_powder", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_blaze", 90), CastingTags.Items.DUST_MOLD,
                Items.BLAZE_POWDER, "blaze/dust", consumer);
        createSimpleSolidifierRecipe(getFluidStack("molten_blaze", 90), CastingTags.Items.ROD_MOLD,
                Items.BLAZE_ROD, "blaze/rod", consumer);

        //Chilled Water
        createSimpleMeltingRecipe(getFluidStack("chilled_water", 250), Items.SNOWBALL, FluidData.getTempByName("chilled_water"),
                "chilled_coolant/from_snow_ball", consumer);
        createSimpleMeltingRecipe(getFluidStack("chilled_water", 1000), Items.SNOW_BLOCK, FluidData.getTempByName("chilled_water"),
                "chilled_coolant/from_snow_block", consumer);

        //Iced Water
        createSimpleMeltingRecipe(getFluidStack("iced_water", 1000), Items.ICE, FluidData.getTempByName("iced_water"),
                "iced_water/from_ice", consumer);
        createSimpleMeltingRecipe(getFluidStack("iced_water", 1000 * 9), Items.PACKED_ICE, FluidData.getTempByName("iced_water"),
                "iced_water/from_packed_ice", consumer);
        createSimpleMeltingRecipe(getFluidStack("iced_water", 1000 * 9 * 9), Items.BLUE_ICE, FluidData.getTempByName("iced_water"),
                "iced_water/from_blue_ice", consumer);


        //Mixer Recipes
        createSimpleMixingRecipe(fluidList(
                        getFluidStack("iced_water", 1000), getFluidStack("chilled_water", 1000)),
                getFluidStack("super_coolant", 2000), "super_coolant", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 270), getFluidStack("molten_tin", 90)),
                getFluidStack("molten_bronze", 360), "bronze", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_gold", 90),getFluidStack("molten_soul", 1000)),
                getFluidStack("molten_soularium", 90), "soularium", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_coal", 160),getFluidStack("molten_iron", 90)),
                getFluidStack("molten_steel", 90), "steel", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 270),getFluidStack("molten_zinc", 90)),
                getFluidStack("molten_brass", 360), "brass", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_ender", 250),getFluidStack("molten_energetic_alloy", 90),getFluidStack("molten_glowstone", 250)),
                getFluidStack("molten_vibrant_alloy", 90), "vibrant_alloy", consumer);

        createSimpleMixingRecipe(fluidList(
                        new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 1000)),
                getFluidStack("molten_obsidian", 1000), "obsidian", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 90), getFluidStack("molten_redstone", 90)),
                getFluidStack("molten_redstone_alloy", 90), "redstone_alloy", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_ender", 250), getFluidStack("molten_iron", 90)),
                getFluidStack("molten_pulsating_alloy", 180), "pulsating_alloy", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_lead", 270), getFluidStack("molten_platinum", 90), getFluidStack("molten_ender", 500)),
                getFluidStack("molten_enderium", 360), "enderium", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_iron", 90), getFluidStack("molten_coal", 80), getFluidStack("molten_obsidian", 1000)),
                getFluidStack("molten_dark_steel", 90), "dark_steel", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 90), getFluidStack("molten_nickel", 90)),
                getFluidStack("molten_constantan", 180), "constantan", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_iron", 180), getFluidStack("molten_nickel", 90)),
                getFluidStack("molten_invar", 270), "invar", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_end_stone", 1000), getFluidStack("molten_dark_steel", 90), getFluidStack("molten_obsidian", 1000)),
                getFluidStack("molten_end_steel", 90), "end_steel", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 270), getFluidStack("molten_silver", 90), getFluidStack("molten_redstone", 360)),
                getFluidStack("molten_signalum", 360), "signalum", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_tin", 270), getFluidStack("molten_silver", 90), getFluidStack("molten_glowstone", 500)),
                getFluidStack("molten_lumium", 360), "lumium", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_copper", 90), getFluidStack("molten_iron", 90)),
                getFluidStack("molten_conductive_alloy", 90), "conductive_alloy", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_gold", 90), getFluidStack("molten_silver", 90)),
                getFluidStack("molten_electrum", 180), "electrum", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_redstone", 90), getFluidStack("molten_gold", 90), getFluidStack("molten_conductive_alloy", 90)),
                getFluidStack("molten_energetic_alloy", 90), "energetic_alloy", consumer);

        createSimpleMixingRecipe(fluidList(
                        getFluidStack("molten_debris", 360), getFluidStack("molten_gold", 360)),
                getFluidStack("molten_netherite", 90), "netherite", consumer);

        //Mod Specific Compat non Resource based

        //RS
        createSimpleSolidifierRecipe(getFluidStack("molten_quartz_enriched_iron", 810), Tags.Items.STONES,
                BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:machine_casing")), "compat/rs/machine_casting", consumer);

        //createSimpleSolidifierRecipe(getFluidStack("molten_quartz_enriched_iron", 810), Tags.Items.STONES,
        //        BuiltInRegistries.ITEM.get(ResourceLocation.parse("refinedstorage:machine_casing")), "compat/rs/machine_casting", consumer);

    }

    public void createSimpleMixingRecipe(NonNullList<FluidStack> fluids, FluidStack output, String recipeID, RecipeOutput consumer) {
        MixingRecipeBuilder.mixingRecipesBuilder(fluids, output)
                .unlockedBy("has_item", has(CastingBlocks.MULTIBLOCK_MIXER))
                .save(consumer, "casting:mixer/" + recipeID);
    }

    public void createSimpleMeltingRecipe(FluidStack output, ItemLike input, int temp, String recipeID, RecipeOutput consumer) {

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(input.asItem());

        MeltingRecipeBuilder.meltingRecipesBuilder(new SizedIngredient(Ingredient.of(input), 1),
                        output, temp)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new ItemExistsCondition(itemId.toString())),
                        "casting:melting/" + recipeID);
    }


    public void createSimpleMeltingRecipe(FluidStack output, TagKey<Item> input, int temp, String recipeID, RecipeOutput consumer) {

        MeltingRecipeBuilder.meltingRecipesBuilder(new SizedIngredient(Ingredient.of(input), 1),
                        output, temp)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(input))),
                        "casting:melting/" + recipeID);
    }

    public void createSimpleSolidifierRecipe(FluidStack output, TagKey<Item> mold, ItemLike input, String recipeID, RecipeOutput consumer) {

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(input.asItem());

        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                        new SizedIngredient(Ingredient.of(mold), 1),
                        new SizedIngredient(Ingredient.of(input), 1),
                        output)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new ItemExistsCondition(itemId.toString())),
                        "casting:solidifier/" + recipeID);
    }

    public void createSimpleSolidifierRecipe(FluidStack output, TagKey<Item> mold, TagKey<Item> input, String recipeID, RecipeOutput consumer) {

        SolidifierRecipeBuilder.solidifierRecipesBuilder(new SizedIngredient(Ingredient.of(mold), 1),
                        new SizedIngredient(Ingredient.of(input), 1),
                        output)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(input))),
                        "casting:solidifier/" + recipeID);
    }


    public void createSimpleSolidifierRecipe(FluidStack output, ItemStack mold, ItemLike input, String recipeID, RecipeOutput consumer) {

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(input.asItem());

        SolidifierRecipeBuilder.solidifierRecipesBuilder(
                        new SizedIngredient(Ingredient.of(mold), 1),
                        new SizedIngredient(Ingredient.of(input), 1),
                        output)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new ItemExistsCondition(itemId.toString())),
                        "casting:solidifier/" + recipeID);
    }

    public void createSimpleSolidifierRecipe(FluidStack output, ItemStack mold, TagKey<Item> input, String recipeID, RecipeOutput consumer) {

        SolidifierRecipeBuilder.solidifierRecipesBuilder(new SizedIngredient(Ingredient.of(mold), 1),
                        new SizedIngredient(Ingredient.of(input), 1),
                        output)
                .unlockedBy("has_item", has(input))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(input))),
                        "casting:solidifier/" + recipeID);
    }
    public void createCommonMeltingRecipes(String type, RecipeOutput consumer) {

        int temp = FluidData.getTempByName(type);
        Fluid fluid = FLUIDS_MAP.get("molten_" + type).getFluid();

        for (CommonTags.ResourceType resourceTypes : CommonTags.ResourceType.values()) {
            int amount = MaterialMelting.valueOf(String.valueOf(resourceTypes)).getAmount();
            TagKey<Item> tagKey = CommonTags.getTag(type, resourceTypes);

            String resourceName = resourceTypes.toString().toLowerCase(Locale.ROOT);
            if (resourceName.endsWith("s")) {
                resourceName = resourceName.substring(0, resourceName.length() - 1);
            }

            MeltingRecipeBuilder.meltingRecipesBuilder(new SizedIngredient(Ingredient.of(tagKey), 1),
                            new FluidStack(fluid, amount), temp)
                    .unlockedBy("has_item", has(tagKey))
                    .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(tagKey))),
                            "casting:melting/" + type + "/" + "from_" + resourceName);

        }
    }

    public void createCommonSolidifierRecipes(String type, RecipeOutput consumer) {

        Fluid fluid = FLUIDS_MAP.get("molten_" + type).getFluid();

        for (CommonTags.ResourceType resourceTypes : CommonTags.ResourceType.values()) {
            int amount = MaterialMelting.valueOf(String.valueOf(resourceTypes)).getAmount();
            TagKey<Item> tagKey = CommonTags.getTag(type, resourceTypes);

            String resourceName = resourceTypes.toString().toLowerCase(Locale.ROOT);
            if (resourceName.endsWith("s")) {
                resourceName = resourceName.substring(0, resourceName.length() - 1);
            }

            TagKey<Item> moldTag = getMoldTag(resourceTypes);
            if (moldTag != null) {
                SolidifierRecipeBuilder.solidifierRecipesBuilder(SizedIngredient.of(moldTag, 1),
                                new SizedIngredient(Ingredient.of(tagKey), 1),
                                new FluidStack(fluid, amount))
                        .unlockedBy("has_item", has(tagKey))
                        .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(tagKey))),
                                "casting:solidifier/" + type + "/" + resourceName);
            }

        }
    }

    public void createMoldRecipe(ItemLike output, TagKey<Item> input, RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK.get())
                .define('B', input)
                .unlockedBy("has_black_brick", has(CastingItems.BLACK_BRICK.get()))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(input))));
    }

    public void createMoldRecipeWithID(ItemLike output, TagKey<Item> input, RecipeOutput consumer, String id) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', CastingItems.BLACK_BRICK.get())
                .define('B', input)
                .unlockedBy("has_black_brick", has(CastingItems.BLACK_BRICK.get()))
                .save(consumer.withConditions(new NotCondition(new TagEmptyCondition(input))),
                        "casting:" + id);
    }

    public void toolModifierRecipes(RecipeOutput consumer, SizedIngredient ingredient, FluidStack fluid, String effect) {

        String recipeFrom = "";

        if (ingredient == null) {
            recipeFrom = "equipment_modifier/" + effect + "_from_fluid";

        }
        if (fluid == null) {
            recipeFrom = "equipment_modifier/" + effect + "_from_item";
        }

        if (fluid != null && ingredient != null) {
            recipeFrom = "equipment_modifier/" + effect + "_from_fluid_and_item";
        }

        EquipmentModifierRecipeBuilder.ToolModifierRecipesBuilder(
                        ingredient,
                        fluid,
                        effect)
                .unlockedBy("has_item", has(CastingBlocks.EQUIPMENT_MODIFIER.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(Casting.MOD_ID, recipeFrom));
    }


    public TagKey<Item> getMoldTag(CommonTags.ResourceType resourceTypes) {
        return switch (resourceTypes) {
            case INGOTS -> CastingTags.Items.INGOT_MOLD;
            case NUGGETS -> CastingTags.Items.NUGGET_MOLD;
            case STORAGE_BLOCKS -> CastingTags.Items.BLOCK_MOLD;
            case RAW_STORAGE_BLOCKS, RAW_MATERIALS, ORES -> null;
            case PLATES -> CastingTags.Items.PLATE_MOLD;
            case DUSTS -> CastingTags.Items.DUST_MOLD;
            case GEARS -> CastingTags.Items.GEAR_MOLD;
            case RODS -> CastingTags.Items.ROD_MOLD;
            case GEMS -> CastingTags.Items.GEM_MOLD;
            case WIRES -> CastingTags.Items.WIRE_MOLD;
        };
    }
}
