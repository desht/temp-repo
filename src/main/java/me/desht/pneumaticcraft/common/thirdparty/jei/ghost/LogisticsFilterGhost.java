/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.desht.pneumaticcraft.common.thirdparty.jei.ghost;

import com.google.common.collect.ImmutableList;
import me.desht.pneumaticcraft.client.gui.semiblock.AbstractLogisticsScreen;
import me.desht.pneumaticcraft.client.util.PointXY;
import me.desht.pneumaticcraft.common.entity.semiblock.EntityLogisticsFrame;
import me.desht.pneumaticcraft.common.inventory.slot.SlotPhantom;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class LogisticsFilterGhost<T extends EntityLogisticsFrame> implements IGhostIngredientHandler<AbstractLogisticsScreen<T>> {
    @Override
    public <I> List<Target<I>> getTargets(AbstractLogisticsScreen<T> gui, I ingredient, boolean doStart) {
        if (ingredient instanceof ItemStack) {
            ImmutableList.Builder<Target<I>> builder = ImmutableList.builder();
            for (Slot slot : gui.getMenu().slots) {
                if (slot instanceof SlotPhantom) {
                    //noinspection unchecked
                    builder.add((Target<I>) new ItemStackTarget((SlotPhantom) slot, gui));
                }
            }
            return builder.build();
        } else if (ingredient instanceof FluidStack) {
            ImmutableList.Builder<Target<I>> builder = ImmutableList.builder();
            for (int i = 0; i < EntityLogisticsFrame.FLUID_FILTER_SLOTS; i++) {
                //noinspection unchecked
                builder.add((Target<I>) new FluidStackTarget(i, gui));
            }
            return builder.build();
        }
        return Collections.emptyList();
    }

    @Override
    public void onComplete() {
    }

    private record ItemStackTarget(SlotPhantom slot, AbstractLogisticsScreen<?> gui) implements Target<ItemStack> {
        @Override
        public Rect2i getArea() {
            return new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);
        }

        @Override
        public void accept(ItemStack ingredient) {
            gui.updateItemFilter(slot.getSlotIndex(), ingredient.copy());
        }
    }

    private record FluidStackTarget(int slotNumber, AbstractLogisticsScreen<?> gui) implements Target<FluidStack> {
        @Override
        public Rect2i getArea() {
            PointXY p = gui.getFluidSlotPos(slotNumber);
            return new Rect2i(p.x(), p.y(), 16, 16);
        }

        @Override
        public void accept(FluidStack ingredient) {
            gui.updateFluidFilter(slotNumber, ingredient.copy());
        }
    }
}
