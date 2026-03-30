package com.benbenlaw.casting.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CastingConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Integer> amountOfExperienceFromExperienceBall;
    public static final ModConfigSpec.ConfigValue<Integer> defaultControllerSpeed;
    public static final ModConfigSpec.ConfigValue<Integer> defaultSolidifierSpeed;
    public static final ModConfigSpec.ConfigValue<Integer> defaultMixerSpeed;


    static {

        // Casting Configs
        BUILDER.comment("Casting Startup Config")
                .push("Casting");

        amountOfExperienceFromExperienceBall = BUILDER.comment("Amount of experience given when using the experience ball, default = 10")
                .define("Experience Ball Experience", 10);

        defaultControllerSpeed = BUILDER.comment("Default speed for the controller, default = 200")
                .define("Default Controller Speed", 200);

        defaultSolidifierSpeed = BUILDER.comment("Default speed for the solidifier, default = 200")
                .define("Default Solidifier Speed", 200);

        defaultMixerSpeed = BUILDER.comment("Default speed for the mixer, default = 200")
                .define("Default Mixer Speed", 200);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();
    }
}
