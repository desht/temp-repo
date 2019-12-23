package me.desht.pneumaticcraft.common.capabilities;

import me.desht.pneumaticcraft.api.tileentity.IAirHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A reference implementation.  Subclass this or implement your own.
 */
public class BasicAirHandler implements IAirHandler, INBTSerializable<CompoundNBT> {
    private final int baseVolume;
    protected int airAmount;

    public BasicAirHandler(int volume) {
        this.baseVolume = volume;
        this.airAmount = 0;
    }

    @Override
    public float getPressure() {
        return (float) airAmount / baseVolume;
    }

    @Override
    public int getAir() {
        return airAmount;
    }

    @Override
    public void addAir(int amount) {
        // floor at -1 bar, which is a hard vacuum
        airAmount = Math.max(airAmount + amount, -baseVolume);
    }

    @Override
    public int getBaseVolume() {
        return baseVolume;
    }

    @Override
    public int getVolume() {
        return baseVolume;
    }

    @Override
    public float maxPressure() {
        return 10f;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Air", getAir());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        airAmount = nbt.getInt("Air");
    }
}
