package com.benbenlaw.casting.item.util;

import com.benbenlaw.core.block.entity.handler.fluid.OutputFluidHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record FluidListComponent(List<FluidStack> fluids) {

    public static final FluidListComponent EMPTY = new FluidListComponent(Collections.emptyList());

    public static final Codec<FluidListComponent> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                FluidStack.OPTIONAL_CODEC.listOf().fieldOf("fluids").forGetter(FluidListComponent::fluids)
        ).apply(instance, FluidListComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidListComponent> STREAM_CODEC =
            FluidStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(FluidListComponent::new, FluidListComponent::fluids);

    public FluidListComponent(List<FluidStack> fluids) {
        this.fluids = Collections.unmodifiableList(fluids);
    }

    // Inside FluidListComponent record

    public static FluidListComponent fromHandlers(FluidStacksResourceHandler... handlers) {
        List<FluidStack> list = new ArrayList<>();
        for (FluidStacksResourceHandler handler : handlers) {
            for (int i = 0; i < handler.size(); i++) {
                FluidStack stack = FluidUtil.getStack(handler, i);
                if (!stack.isEmpty()) {
                    list.add(stack.copy());
                } else {
                    list.add(FluidStack.EMPTY);
                }
            }
        }
        return new FluidListComponent(list);
    }

    public void applyToHandlers(FluidStacksResourceHandler... handlers) {
        try (Transaction tx = Transaction.open(null)) {
            int listIndex = 0;
            for (FluidStacksResourceHandler handler : handlers) {
                for (int slot = 0; slot < handler.size() && listIndex < fluids.size(); slot++) {
                    FluidStack stack = fluids.get(listIndex++);

                    FluidResource currentResource = handler.getResource(slot);

                    if (!currentResource.isEmpty()) {
                        handler.extract(slot, currentResource, handler.getAmountAsInt(slot), tx);
                    }

                    if (!stack.isEmpty()) {
                        handler.insert(slot, FluidResource.of(stack), stack.getAmount(), tx);
                    }
                }
            }
            tx.commit();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FluidListComponent other)) return false;
        if (this.fluids.size() != other.fluids.size()) return false;
        for (int i = 0; i < this.fluids.size(); i++) {
            FluidStack a = this.fluids.get(i);
            FluidStack b = other.fluids.get(i);
            if (!a.getFluid().equals(b.getFluid()) || a.getAmount() != b.getAmount()) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (FluidStack stack : this.fluids) {
            hash = hash * 31 + Objects.hash(stack.getFluid(), stack.getAmount());
        }
        return hash;
    }
}
