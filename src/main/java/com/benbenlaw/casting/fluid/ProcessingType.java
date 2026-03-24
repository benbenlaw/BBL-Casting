package com.benbenlaw.casting.fluid;

import com.benbenlaw.core.tag.ResourceType;

public enum ProcessingType {
    INGOT9(ResourceType.INGOTS),
    GEMS4(ResourceType.GEMS),
    GEMS9(ResourceType.GEMS),
    DUST4(ResourceType.DUSTS),
    DUST9(ResourceType.DUSTS),
    CUSTOM(null);

    private final ResourceType resourceType;

    ProcessingType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return this.resourceType;
    }
}