package com.benbenlaw.casting;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.block.CastingCapabilities;
import com.benbenlaw.casting.block.entity.renderer.TankBlockEntityRenderer;
import com.benbenlaw.casting.block.entity.renderer.TankSpecialRenderer;
import com.benbenlaw.casting.config.CastingConfig;
import com.benbenlaw.casting.fluid.CastingFluids;
import com.benbenlaw.casting.item.CastingCreativeModeTab;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.CastingItems;
import com.benbenlaw.casting.recipe.CastingRecipeTypes;
import com.benbenlaw.casting.screen.CastingMenuTypes;
import com.benbenlaw.casting.screen.ControllerScreen;
import com.benbenlaw.casting.screen.MixerScreen;
import com.benbenlaw.casting.screen.SolidifierScreen;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Casting.MOD_ID)
public class Casting {
    public static final String MOD_ID = "casting";

    public Casting(IEventBus modEventBus, final ModContainer modContainer) {

        //Equipment Modifiers
        //EquipmentModifier.registerAllDataComponents();
        //EquipmentModifier.COMPONENTS.register(modEventBus);
        //EquipmentModifier.registerAllItemModifiers();
        //EquipmentModifier.ITEMS.register(modEventBus);

        CastingBlocks.BLOCKS.register(modEventBus);
        CastingBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        CastingCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);
        CastingItems.ITEMS.register(modEventBus);
        CastingDataComponents.COMPONENTS.register(modEventBus);
        CastingFluids.FLUIDS.register(modEventBus);
        CastingMenuTypes.MENUS.register(modEventBus);
        CastingRecipeTypes.TYPES.register(modEventBus);
        CastingRecipeTypes.SERIALIZER.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CastingConfig.SPEC, "bbl/casting/common.toml");

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::commonSetup);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        CastingCapabilities.registerCapabilities(event);
    }

    public void commonSetup(RegisterPayloadHandlersEvent event) {
        //CastingMessages.registerNetworking(event);
    }

    @EventBusSubscriber(modid = Casting.MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerSpecialModel(RegisterSpecialModelRendererEvent event) {
            event.register(Casting.identifier("block/tank_fluid"), TankSpecialRenderer.Unbaked.CODEC);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(CastingBlockEntities.TANK_BLOCK_ENTITY.get(), TankBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(CastingMenuTypes.CONTROLLER_MENU.get(), ControllerScreen::new);
            event.register(CastingMenuTypes.SOLIDIFIER_MENU.get(), SolidifierScreen::new);
            event.register(CastingMenuTypes.MIXER_MENU.get(), MixerScreen::new);
        }

        @SubscribeEvent
        public static void onClientExtensions(RegisterClientExtensionsEvent event) {
            CastingFluids.FLUIDS_MAP.values().forEach(fluid -> {
                var fluidType = fluid.getFluidType();
                var extensions = IClientFluidTypeExtensions.of(fluidType);
                event.registerFluidType(extensions, fluidType);
            });
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            event.enqueueWork(() -> {
                // Only Needed if i need a translucent fluid otherwise should be ok? //
                //     ItemBlockRenderTypes.setRenderLayer(ModFluids.MOLTEN_URANIUM_FLOWING.get(), RenderType.translucent());

            });
        }

        @SubscribeEvent
        public static void onKeyInput(RegisterKeyMappingsEvent event) {
            //event.register(KeyBinds.HELMET_HOTKEY);
            //event.register(KeyBinds.CHESTPLATE_HOTKEY);
            //event.register(KeyBinds.LEGGINGS_HOTKEY);
            //event.register(KeyBinds.BOOTS_HOTKEY);
        }
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}
