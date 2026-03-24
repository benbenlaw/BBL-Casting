package com.benbenlaw.casting.screen;

import com.benbenlaw.casting.Casting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CastingMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Casting.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ControllerMenu>> CONTROLLER_MENU =
            MENUS.register("controller_menu", () -> IMenuTypeExtension.create(ControllerMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SolidifierMenu>> SOLIDIFIER_MENU =
            MENUS.register("solidifier_menu", () -> IMenuTypeExtension.create(SolidifierMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<MixerMenu>> MIXER_MENU =
            MENUS.register("mixer_menu", () -> IMenuTypeExtension.create(MixerMenu::new));

;

}