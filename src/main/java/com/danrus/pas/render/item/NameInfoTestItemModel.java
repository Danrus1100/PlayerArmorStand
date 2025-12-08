package com.danrus.pas.render.item;

import com.danrus.pas.api.info.NameInfo;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NameInfoTestItemModel implements ItemModel {

    private final ItemModel onEmpty;
    private final ItemModel onNotEmpty;

    public NameInfoTestItemModel(ItemModel onEmpty, ItemModel onNotEmpty) {
        this.onEmpty = onEmpty;
        this.onNotEmpty = onNotEmpty;
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level,
                       //? >=1.21.9
                       /*@Nullable net.minecraft.world.entity.ItemOwner owner,*/
                       //? <1.21.9
                       @Nullable LivingEntity owner,
                       int seed) {
        NameInfo info = NameInfo.parse(stack.getCustomName());
        ItemModel chosenModel = info.isEmpty() ? onEmpty : onNotEmpty;
        chosenModel.update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
    }

    public static record Unbaked(ItemModel.Unbaked onEmpty, ItemModel.Unbaked onNotEmpty) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        ItemModels.CODEC.fieldOf("on_empty").forGetter(Unbaked::onEmpty),
                        ItemModels.CODEC.fieldOf("on_not_empty").forGetter(Unbaked::onNotEmpty)
                ).apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext context) {
            ItemModel bakedOnEmpty = onEmpty.bake(context);
            ItemModel bakedOnNotEmpty = onNotEmpty.bake(context);
            return new NameInfoTestItemModel(bakedOnEmpty, bakedOnNotEmpty);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            onEmpty.resolveDependencies(resolver);
            onNotEmpty.resolveDependencies(resolver);
        }
    }
}
