package com.benbenlaw.casting.config;

import com.benbenlaw.casting.item.EquipmentModifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;
import java.util.stream.Collectors;


public class ModifierSetsConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.ConfigValue<List<? extends String>> CUSTOM_MODIFIER_SETS;
    public static ModConfigSpec.ConfigValue<Boolean> DISABLE_ALL_DEFAULT_MODIFIER_SETS;

    private static final Map<String, String> GROUP_DISPLAY_NAMES = new HashMap<>();

    // NEW: Store items/tags per groupKey
    public static final Map<String, List<String>> GROUP_ITEMS_OR_TAGS = new HashMap<>();

    static {
        BUILDER.comment("Custom Modifier Sets Config")
                .push("Modifier Sets");

        DISABLE_ALL_DEFAULT_MODIFIER_SETS = BUILDER.comment("Disable all default modifier sets,...\n" +
                        "If enabled, only custom modifier sets defined below will be used.")
                .define("disable_all_default_modifier_sets", false);

        CUSTOM_MODIFIER_SETS = BUILDER
                .comment("Define custom modifier groups as groupKey|displayName=MODIFIER1,MODIFIER2,...;item1,item2,#tag1,...\n" +
                        "Example: diamond|Diamond Tools=EFFICIENCY,SILK_TOUCH,TORCH_PLACING;minecraft:diamond,#minecraft:planks \n" +
                        "This defines a group 'diamond' with display name 'Diamond Tools' containing modifiers EFFICIENCY, SILK_TOUCH, TORCH_PLACING\n" +
                        "and applicable items 'minecraft:diamond' and all items with tag 'minecraft:planks'")
                .defineListAllowEmpty(
                        List.of("modifier_groups"),
                        Collections.emptyList(),
                        () -> "",
                        obj -> obj instanceof String && ((String) obj).contains("=")
                );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    /**
     * Returns a map of groupKey -> list of EquipmentModifiers.
     * Also parses and stores display names and applicable items/tags.
     */
    public static Map<String, List<EquipmentModifier>> getCustomModifierGroups() {
        Map<String, List<EquipmentModifier>> groups = new HashMap<>();
        GROUP_DISPLAY_NAMES.clear();
        GROUP_ITEMS_OR_TAGS.clear();

        for (String entry : CUSTOM_MODIFIER_SETS.get()) {
            String[] parts = entry.split("=", 2); // limit split to 2 parts
            if (parts.length != 2) {
                System.err.println("[ModifierSetsConfig] Invalid entry: " + entry);
                continue;
            }

            // Parse groupKey and optional displayName
            String[] groupAndName = parts[0].split("\\|", 2);
            String groupKey = groupAndName[0].trim();
            String displayName = groupAndName.length > 1 ? groupAndName[1].trim() : groupKey;

            // Parse modifiers and optional item/tag list separated by ';'
            String[] modifierAndItems = parts[1].split(";", 2);
            String modifierPart = modifierAndItems[0].trim();
            String itemsPart = modifierAndItems.length > 1 ? modifierAndItems[1].trim() : "";

            String[] modifierNames = modifierPart.split(",");

            List<EquipmentModifier> modifiers = Arrays.stream(modifierNames)
                    .map(String::trim)
                    .map(ModifierSetsConfig::getModifierByName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Parse items/tags into list of strings
            List<String> applicableItemsOrTags = itemsPart.isEmpty() ? Collections.emptyList()
                    : Arrays.stream(itemsPart.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            groups.put(groupKey, modifiers);
            GROUP_DISPLAY_NAMES.put(groupKey, displayName);
            GROUP_ITEMS_OR_TAGS.put(groupKey, applicableItemsOrTags);
        }

        return groups;
    }

    /**
     * Get the display name for a group key.
     * Returns the key itself if no display name was defined.
     */
    public static String getDisplayNameForGroup(String groupKey) {
        return GROUP_DISPLAY_NAMES.getOrDefault(groupKey, groupKey);
    }

    /**
     * Get the list of items or tags applicable for a group key.
     * Returns empty list if none set.
     */
    public static List<String> getItemsOrTagsForGroup(String groupKey) {
        return GROUP_ITEMS_OR_TAGS.getOrDefault(groupKey, Collections.emptyList());
    }

    private static EquipmentModifier getModifierByName(String name) {
        try {
            return EquipmentModifier.valueOf(name);
        } catch (IllegalArgumentException e) {
            System.err.println("[ModifierSetsConfig] Unknown modifier name: " + name);
            return null;
        }
    }
}
