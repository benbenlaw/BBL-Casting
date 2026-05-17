package com.benbenlaw.casting.event;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.entity.multiblock.MultiblockControllerBlockEntity;
import com.benbenlaw.casting.config.EquipmentModifierConfig;
import com.benbenlaw.casting.multiblock.MultiblockData;
import com.benbenlaw.casting.util.BeheadingHeadMap;
import com.benbenlaw.casting.util.CastingTags;
import com.benbenlaw.core.block.UnbreakableResourceBlock;
import com.benbenlaw.core.util.BlockInformation;
import com.benbenlaw.core.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

import static com.benbenlaw.casting.item.EquipmentModifier.*;
import static com.benbenlaw.core.block.UnbreakableResourceBlock.RESTING;
import static com.benbenlaw.core.event.UnbreakableBlockReplaceEvent.blockInformationMap;

@EventBusSubscriber(modid = Casting.MOD_ID)

public class ToolEvents {

    public static final Map<UUID, Direction> lastHitDirectionMap = new HashMap<>();
    public static FakePlayer fakePlayer = null;

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        Direction face = event.getFace();

        if (level.isClientSide()) return;
        if (player.getMainHandItem().has(EXCAVATION.dataComponent.get())) {
            lastHitDirectionMap.put(player.getUUID(), face);
            //System.out.println("Direction: " + face);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        assert entity != null;
        Level level = entity.level();
        BlockPos pos = event.getPos();
        BlockState state = event.getPlacedBlock();

        if (!level.isClientSide()) {
            if (state.is(CastingTags.Blocks.CONTROLLER_ALL)) {
                BlockPos.betweenClosedStream(pos.offset(-16, -16, -16), pos.offset(16, 16, 16))
                        .forEach(block -> {
                            BlockEntity blockEntity = level.getBlockEntity(block);
                            if (blockEntity instanceof MultiblockControllerBlockEntity controller) {
                                controller.structureDirty = true;
                            }
                        });
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Direction face = lastHitDirectionMap.getOrDefault(player.getUUID(), Direction.DOWN);
        Level level = player.level();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        ItemStack tool = player.getMainHandItem();

        if (!level.isClientSide()) {
            if (state.is(CastingTags.Blocks.CONTROLLER_ALL)) {
                BlockPos.betweenClosedStream(pos.offset(-16, -16, -16), pos.offset(16, 16, 16))
                        .forEach(block -> {
                            BlockEntity blockEntity = level.getBlockEntity(block);
                            if (blockEntity instanceof MultiblockControllerBlockEntity controller) {
                                controller.structureDirty = true;
                            }
                        });
            }
        }

        boolean isSilkTouch = tool.getComponents().keySet().contains(SILK_TOUCH.dataComponent.get());
        boolean isFortune = tool.getComponents().keySet().contains(FORTUNE.dataComponent.get());
        boolean isAutoSmelt = tool.getComponents().keySet().contains(AUTO_SMELT.dataComponent.get());
        boolean isExcavation = tool.getComponents().keySet().contains(EXCAVATION.dataComponent.get());
        boolean isMagnet = hasMagnetArmor(player) && hasMiningItem(tool);

        boolean requiresCastingOverrides = isMagnet || isExcavation || isSilkTouch || isFortune || isAutoSmelt;


        if (!level.isClientSide() && requiresCastingOverrides) {

            if (state.getBlock() instanceof UnbreakableResourceBlock) {
                event.setCanceled(true);
                breakBlockWithCasting(level, player, pos, tool, isSilkTouch, isFortune, isAutoSmelt);
                return;
            }

            // Excavation
            if(!tool.isCorrectToolForDrops(state) && state.requiresCorrectToolForDrops()) {
                return;
            }

            if (isExcavation && isToggleableModifierActive(tool)) {
                int excavationLevel = (int) tool.getComponents().getOrDefault(EXCAVATION.dataComponent.get(), 0);

                List<BlockPos> excavationPlane = getExcavationPlane(pos, face, excavationLevel);

                // Break the original block first (once)
                breakBlockWithCasting(level, player, pos, tool, isSilkTouch, isFortune, isAutoSmelt);

                for (BlockPos targetPos : excavationPlane) {
                    if (targetPos.equals(pos)) continue; // Skip original block

                    if (!level.isLoaded(targetPos)) continue;

                    BlockState targetState = level.getBlockState(targetPos);
                    if (!targetState.isAir() && targetState.getDestroySpeed(level, targetPos) >= 0 && tool.isCorrectToolForDrops(targetState))
                    {
                        breakBlockWithCasting(level, player, targetPos, tool, isSilkTouch, isFortune, isAutoSmelt);
                    }
                }
                return;
            }
            // Other casting overrides
            breakBlockWithCasting(level, player, pos, tool, isSilkTouch, isFortune, isAutoSmelt);
        }
    }

    public static void breakBlockWithCasting(Level level, Player player, BlockPos pos, ItemStack tool, boolean isSilkTouch, boolean isFortune, boolean isAutoSmelt) {
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        ItemStack fakeItemStack = tool.copy();
        List<ItemStack> drops = new ArrayList<>();

        int blockExperience = 0;
        if (state.getBlock() instanceof DropExperienceBlock experienceBlock) {
            blockExperience = experienceBlock.getExpDrop(state, level, pos, blockEntity, player, tool);
        }

        // Requires Experience to be dropped

        // Silk Touch
        if (isSilkTouch && isToggleableModifierActive(tool)) {
            fakeItemStack.enchant(toHolder(level, Enchantments.SILK_TOUCH), 1);
            drops = getLootDrops(state, blockEntity, pos, player, fakeItemStack, level);
        } else if (isFortune) {
            int fortuneLevel = (int) tool.getComponents().getOrDefault(FORTUNE.dataComponent.get(), 0);
            fakeItemStack.enchant(toHolder(level, Enchantments.FORTUNE), fortuneLevel);
            drops = getLootDrops(state, blockEntity, pos, player, fakeItemStack, level);
        }

        if (drops.isEmpty()) {
            drops = getLootDrops(state, blockEntity, pos, player, fakeItemStack, level);
        }

        // Auto Smelt
        if (isAutoSmelt && isToggleableModifierActive(tool)) {
            List<ItemStack> smeltingDrops = new ArrayList<>();
            for (ItemStack drop : drops) {
                SingleRecipeInput container = new SingleRecipeInput(drop);
                List<RecipeHolder<SmeltingRecipe>> smeltingRecipe = level.getRecipeManager().getRecipesFor(RecipeType.SMELTING, container, level);
                if (!smeltingRecipe.isEmpty()) {
                    ItemStack result = smeltingRecipe.getFirst().value().getResultItem(level.registryAccess());
                    ItemStack smeltedStack = result.copy();
                    smeltedStack.setCount(drop.getCount());
                    smeltingDrops.add(smeltedStack);
                } else {
                    smeltingDrops.add(drop);
                }
            }
            drops = smeltingDrops;
        }

        //Deal with drops
        for (ItemStack drop : drops) {

            //Insert into player inventory only if mining, if full drop as normal
            if (hasMagnetArmor(player) && hasMiningItem(tool)) {
                boolean canAddItem = player.addItem(drop.copy());
                if (!canAddItem) {
                    Block.popResource(level, pos, drop);
                }
            } else {
                Block.popResource(level, pos, drop);
            }
        }

        if (drops.isEmpty() || drops.getFirst().is(Items.AIR)) {
            state.getBlock().playerDestroy(level, player, pos, state, blockEntity, tool);
        }

        if (state.getBlock() instanceof UnbreakableResourceBlock unbreakableResourceBlock) {
            level.setBlock(pos, state.setValue(RESTING, true), Block.UPDATE_ALL);
            level.scheduleTick(pos, unbreakableResourceBlock, 20);
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            level.destroyBlock(pos, true, player);

        }

        //Drop Experience
        if (blockExperience > 0) {
            ExperienceOrb experienceOrb = new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockExperience);
            level.addFreshEntity(experienceOrb);
        }

        //Damage Tool
        tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
    }

    public static void breakUnbreakableBlock(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        ItemStack fakeItemStack = tool.copy();

        if (state.getBlock() instanceof UnbreakableResourceBlock unbreakableResourceBlock) {
            unbreakableResourceBlock.playerDestroy(level, player, pos, state, blockEntity, fakeItemStack);

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            long delay = 10 + Objects.requireNonNull(level.getServer()).getTickCount();
            blockInformationMap.put(pos, new BlockInformation(state, level, delay));
            level.setBlock(pos, Blocks.BARRIER.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    //Magnet Modifier Check for block drops

    public static boolean isToggleableModifierActive(ItemStack tool) {
        if (!tool.getComponents().has(TOGGLEABLE_MODIFIERS.get())) {
            return true;
        }
        if(tool.getComponents().keySet().contains(TOGGLEABLE_MODIFIERS.get())) {
            if (Boolean.TRUE.equals(tool.getComponents().get(TOGGLEABLE_MODIFIERS.get()))) {
                return true;
            }
            if (Boolean.FALSE.equals(tool.getComponents().get(TOGGLEABLE_MODIFIERS.get()))) {
                return false;
            }
        }
        return false;
    }

    public static boolean hasMagnetArmor(Player player) {
        for (ItemStack armorItem : player.getArmorSlots()) {
            if (armorItem.getComponents().keySet().contains(MAGNET.dataComponent.get())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMiningItem(ItemStack tool) {
        return tool.getItem() instanceof TieredItem;
    }

    //Get loot drops from a block
    public static List<ItemStack> getLootDrops(BlockState state, BlockEntity entity, BlockPos pos, Player player, ItemStack tool, Level level) {
        LootParams.Builder lootParams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withParameter(LootContextParams.BLOCK_ENTITY, entity)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_STATE, state);

        return state.getDrops(lootParams);
    }

    public static List<BlockPos> getExcavationPlane(BlockPos origin, Direction face, int level) {
        List<BlockPos> positions = new ArrayList<>();

        Direction.Axis axis = face.getAxis();
        Direction.Axis axis1;
        Direction.Axis axis2;

        switch (axis) {
            case X -> {
                axis1 = Direction.Axis.Y;
                axis2 = Direction.Axis.Z;
            }
            case Y -> {
                axis1 = Direction.Axis.X;
                axis2 = Direction.Axis.Z;
            }
            case Z -> {
                axis1 = Direction.Axis.X;
                axis2 = Direction.Axis.Y;
            }
            default -> throw new IllegalStateException("Unexpected axis: " + axis);
        }

        for (int i = -level; i <= level; i++) {
            for (int j = -level; j <= level; j++) {
                BlockPos offset = origin;

                // Apply offset based on the perpendicular axes
                offset = offset.relative(Direction.fromAxisAndDirection(axis1, i >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE), Math.abs(i));
                offset = offset.relative(Direction.fromAxisAndDirection(axis2, j >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE), Math.abs(j));

                positions.add(offset);
            }
        }

        return positions;
    }

    @SubscribeEvent
    public static void onBreakingBlock(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = event.getPosition().orElse(BlockPos.ZERO);
        BlockState state = level.getBlockState(pos);
        ItemStack tool = player.getMainHandItem();

        float baseSpeed = event.getOriginalSpeed();

        // Efficiency still increases speed
        if (tool.getComponents().keySet().contains(EFFICIENCY.dataComponent.get())) {
            Integer efficiencyLevel = (Integer) tool.getComponents().get(EFFICIENCY.dataComponent.get());
            if (efficiencyLevel != null && efficiencyLevel > 0) {
                if (tool.isCorrectToolForDrops(state)) {
                    float bonus = efficiencyLevel * efficiencyLevel + 1;
                    baseSpeed += bonus;
                }
            }
        }

        // Excavation decreases speed
        if (tool.getComponents().keySet().contains(EXCAVATION.dataComponent.get()) && isToggleableModifierActive(tool)) {
            Integer excavationLevel = (Integer) tool.getComponents().get(EXCAVATION.dataComponent.get());
            if (excavationLevel != null && excavationLevel > 0) {
                if (tool.isCorrectToolForDrops(state)) {

                    float slowAmount = excavationLevel + (event.getOriginalSpeed() / 4);
                    baseSpeed -= slowAmount;
                    baseSpeed = Math.max(baseSpeed, 0.1f);
                }
            }
        }

        event.setNewSpeed(baseSpeed);
    }


    @SubscribeEvent
    public static void onPreLivingDamage(LivingDamageEvent.Pre event) {
        Level level = event.getEntity().level();
        Entity target = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof Player attacker)) return;
        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) return;

        boolean isSharpness = weapon.getComponents().keySet().contains(SHARPNESS.dataComponent.get());
        boolean isLifesteal = weapon.getComponents().keySet().contains(LIFESTEAL.dataComponent.get());
        boolean isKnockback = weapon.getComponents().keySet().contains(KNOCKBACK.dataComponent.get());
        boolean isIgnite = weapon.getComponents().keySet().contains(IGNITE.dataComponent.get());

        boolean requiresCastingOverrides = isIgnite || isSharpness || isLifesteal || isKnockback;

        if (!level.isClientSide() && requiresCastingOverrides) {

            //Sharpness
            if (isSharpness) {
                int sharpnessLevel = (int) weapon.getComponents().getOrDefault(SHARPNESS.dataComponent.get(), 0);
                if (sharpnessLevel <= 0) return;

                // Vanilla is: 0.5 * level + 0.5
                float bonusDamage = EquipmentModifierConfig.additionalMultiplierForSharpness.get() * sharpnessLevel + EquipmentModifierConfig.additionalAdditionForSharpness.get();
                event.setNewDamage(event.getNewDamage() + bonusDamage);
            }

            //Lifesteal
            if (isLifesteal) {
                if (target instanceof Player) return;
                if (target.isInvulnerable()) return;

                if (target instanceof Enemy) {
                    int lifestealLevel = (int) weapon.getComponents().getOrDefault(LIFESTEAL.dataComponent.get(), 0);
                    int healingAmount = (int) (event.getNewDamage() * 0.1f * lifestealLevel);
                    if (healingAmount > 0) {
                        attacker.heal(healingAmount);
                    }
                }
            }

            //Knockback
            if (isKnockback) {
                int knockbackLevel = (int) weapon.getComponents().getOrDefault(KNOCKBACK.dataComponent.get(), 0);
                if (knockbackLevel > 0) {
                    double dx = target.getX() - attacker.getX();
                    double dz = target.getZ() - attacker.getZ();
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance > 0) {
                        dx /= distance;
                        dz /= distance;
                        target.setDeltaMovement(
                                target.getDeltaMovement().add(dx * (double) knockbackLevel, 0.1, dz * (double) knockbackLevel)
                        );
                        target.hurtMarked = true;
                    }
                }
            }

            //Ignite
            if (isIgnite) {
                int igniteLevel = (int) weapon.getComponents().getOrDefault(IGNITE.dataComponent.get(), 0);
                target.igniteForSeconds(1 + igniteLevel);

            }

            // Damage Item
            weapon.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Inventory inventory = event.getEntity().getInventory();
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;


        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;

            //Repairing
            if (stack.getComponents().keySet().contains(REPAIRING.dataComponent.get())) {
                int repairingLevel = (int) stack.getComponents().getOrDefault(REPAIRING.dataComponent.get(), 0);
                int repairTickTime = getRepairTickTime(repairingLevel);

                if (event.getEntity().tickCount % repairTickTime == 0) {
                    int currentDamage = stack.getDamageValue();

                    if (currentDamage > 0) {
                        int newDamage = Math.max(currentDamage - 1, 0);
                        stack.setDamageValue(newDamage);
                        inventory.setChanged();
                    }
                }
            }

        }
    }


    private static int getRepairTickTime(int repairingLevel) {
        int repairTickTime = 220;

        switch (repairingLevel) {
            case 1 -> repairTickTime = 200;
            case 2 -> repairTickTime = 180;
            case 3 -> repairTickTime = 160;
            case 4 -> repairTickTime = 140;
            case 5 -> repairTickTime = 120;
            case 6 -> repairTickTime = 100;
            case 7 -> repairTickTime = 80;
            case 8 -> repairTickTime = 60;
            case 9 -> repairTickTime = 40;
            case 10 -> repairTickTime = 20;
        }
        return repairTickTime;
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {

        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        ItemStack tool = player.getItemInHand(hand);

        if (level.isClientSide()) return;

        //Toggled Modifiers
        boolean autoSmelt = tool.getComponents().keySet().contains(AUTO_SMELT.dataComponent.get());
        boolean excavation = tool.getComponents().keySet().contains(EXCAVATION.dataComponent.get());
        boolean silkTouch = tool.getComponents().keySet().contains(SILK_TOUCH.dataComponent.get());
        boolean containsToggleableModifier = autoSmelt || excavation || silkTouch;

        if (containsToggleableModifier && player.isCrouching()) {

            if (!tool.getComponents().has(TOGGLEABLE_MODIFIERS.get())) {
                tool.set(TOGGLEABLE_MODIFIERS.get(), true);
            }

            else if (Boolean.TRUE.equals(tool.get(TOGGLEABLE_MODIFIERS.get()))) {
                tool.set(TOGGLEABLE_MODIFIERS.get(), false);
            }
            else if (Boolean.FALSE.equals(tool.get(TOGGLEABLE_MODIFIERS.get()))) {
                tool.set(TOGGLEABLE_MODIFIERS.get(), true);
            }

            player.sendSystemMessage(Component.literal("Toggled Modifiers: " + tool.get(TOGGLEABLE_MODIFIERS.get())));
        }

        boolean isTeleporting = tool.getComponents().keySet().contains(TELEPORTING.dataComponent.get());

        boolean requiresCastingOverrides = isTeleporting;

        if (!level.isClientSide() && requiresCastingOverrides) {

            //Teleporting
            if (isTeleporting) {
                doTeleportation(tool, player, level);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        ItemStack tool = player.getItemInHand(hand);

        boolean isTorchPlacing = tool.getComponents().keySet().contains(TORCH_PLACING.dataComponent.get());

        boolean requiresCastingOverrides = isTorchPlacing;

        if (!level.isClientSide() && requiresCastingOverrides) {

            //Torch Placing
            if (isTorchPlacing) {
                Direction face = event.getFace();
                assert face != null;
                BlockPos placePos = pos.relative(face);

                if (!level.getBlockState(placePos).canBeReplaced()) {
                    return;
                }

                if (face == Direction.UP) {
                    if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP)) {
                        level.setBlockAndUpdate(placePos, Blocks.TORCH.defaultBlockState());
                        tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                } else if (face.getAxis().isHorizontal()) {
                    if (level.getBlockState(pos).isFaceSturdy(level, pos, face)) {
                        BlockState wallTorch = Blocks.WALL_TORCH.defaultBlockState()
                                .setValue(WallTorchBlock.FACING, face);
                        level.setBlockAndUpdate(placePos, wallTorch);
                        tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {

        Level level = event.getEntity().level();
        LivingEntity deadEntity = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity killer = event.getEntity().getKillCredit();

        if (killer instanceof Player) {

            ItemStack stack = killer.getWeaponItem();
            if (stack.isEmpty()) return;

            List<ItemStack> loot = new ArrayList<>();

            boolean isLooting = stack.getComponents().keySet().contains(LOOTING.dataComponent.get());
            boolean isBeheading = stack.getComponents().keySet().contains(BEHEADING.dataComponent.get());

            boolean requiresCastingOverrides = isLooting || isBeheading;

            if (!level.isClientSide() && requiresCastingOverrides) {

                boolean bossLoot = deadEntity instanceof WitherBoss || deadEntity instanceof EnderDragon;

                //Looting
                if (isLooting && !bossLoot) {

                    event.setCanceled(true);
                    LootTable lootTable = Objects.requireNonNull(level.getServer()).reloadableRegistries().getLootTable(deadEntity.getLootTable());
                    int lootingLevel = (int) stack.getComponents().getOrDefault(LOOTING.dataComponent.get(), 0);
                    ItemStack fakeItemStack = stack.copy();
                    fakeItemStack.enchant(toHolder(level, Enchantments.LOOTING), lootingLevel);

                    loot = getMobLootDrops(deadEntity, (Player) killer, damageSource, fakeItemStack, lootTable, level);
                }

                //Beheading
                if (isBeheading) {
                    Optional<ItemStack> customHead = BeheadingHeadMap.getHeadForEntity(deadEntity);
                    //System.out.println("Custom head: " + customHead);
                    customHead.ifPresent(loot::add);
                }

                for (ItemStack itemStack : loot) {
                    popOutTheItem(level, deadEntity.blockPosition(), itemStack);
                }
            }
        }
    }

    public static void doTeleportation(ItemStack tool, Player player, Level level) {

        int ADDITIONAL_BLOCKS_PER_LEVEL = EquipmentModifierConfig.blocksPerLevelForTeleporting.get();
        int range = (int) tool.getComponents().getOrDefault(TELEPORTING.dataComponent.get(), 0) * ADDITIONAL_BLOCKS_PER_LEVEL;
        Vec3 lookVec = player.getLookAngle();
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(lookVec.scale(range));

        BlockHitResult hit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hit.getBlockPos();
            Direction hitDir = hit.getDirection();

            //Adjust position based on block side hit
            Vec3 targetPos;
            if (hitDir == Direction.UP) {
                targetPos = Vec3.atCenterOf(hitPos.above()).subtract(0, 0.5, 0);
            } else if (hitDir == Direction.DOWN) {
                targetPos = Vec3.atCenterOf(hitPos.below()).subtract(0, 0.5, 0);
            } else {
                Vec3 faceOffset = Vec3.atLowerCornerOf(hitDir.getNormal()).scale(1);
                targetPos = Vec3.atCenterOf(hitPos).add(faceOffset).subtract(0, 1.5, 0);
            }

            BlockPos teleportPos = BlockPos.containing(targetPos);
            // Check if the teleport position is air
            Vec3 preTeleportPos = player.position();

            if (level.getBlockState(teleportPos).isAir() && level.getBlockState(teleportPos.above()).isAir()) {
                player.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(tool.getItem(), EquipmentModifierConfig.cooldownForTeleporting.get());
                tool.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);

                // Spawn particles
                for (int i = 0; i < 32; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5);
                    double offsetY = level.random.nextDouble() * 2.0;
                    double offsetZ = (level.random.nextDouble() - 0.5);

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SMOKE, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ, 8, 0.0, 0.5, 0.0, 0.0);
                        serverLevel.sendParticles(ParticleTypes.SMOKE, preTeleportPos.x + offsetX, preTeleportPos.y + offsetY, preTeleportPos.z + offsetZ, 8, 0.0, 0.5, 0.0, 0.0);
                    }
                }
            }
        }
    }

    public static List<ItemStack> getMobLootDrops(Entity deadEntity, Player player, DamageSource damageSource, ItemStack stack,
                                                  LootTable lootTable, Level level) {

        if (fakePlayer == null) {
            fakePlayer = FakePlayerUtil.createFakePlayer((ServerLevel) level, "FakePlayerForLooting");
        }

        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, stack);

        LootParams.Builder lootParams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.THIS_ENTITY, deadEntity)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(deadEntity.getOnPos()))
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withParameter(LootContextParams.ATTACKING_ENTITY, fakePlayer);

        LootParams lootParamsFinal = lootParams.create(LootContextParamSets.ENTITY);
        return lootTable.getRandomItems(lootParamsFinal);
    }

    //From In World Recipes // Move to BBL Core in the future
    public static void popOutTheItem(Level level, BlockPos blockPos, ItemStack itemStack) {

        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.1, 0.5).offsetRandom(level.random, 0.7F);
        ItemStack itemstack1 = itemStack.copy();
        ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack1);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }


    public static Holder<Enchantment> toHolder(Level level, ResourceKey<Enchantment> enchantment) {
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(enchantment);
    }

}