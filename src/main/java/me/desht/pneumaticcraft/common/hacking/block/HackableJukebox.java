package me.desht.pneumaticcraft.common.hacking.block;

import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IHackableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.List;

import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.RL;

public class HackableJukebox implements IHackableBlock {
    @Override
    public ResourceLocation getHackableId() {
        return RL("jukebox");
    }

    @Override
    public boolean canHack(IBlockReader world, BlockPos pos, PlayerEntity player) {
        return world.getBlockState(pos).get(JukeboxBlock.HAS_RECORD);
    }

    @Override
    public void addInfo(IBlockReader world, BlockPos pos, List<String> curInfo, PlayerEntity player) {
        curInfo.add("pneumaticcraft.armor.hacking.result.silence");
    }

    @Override
    public void addPostHackInfo(IBlockReader world, BlockPos pos, List<String> curInfo, PlayerEntity player) {
        curInfo.add("pneumaticcraft.armor.hacking.finished.silenced");
    }

    @Override
    public int getHackTime(IBlockReader world, BlockPos pos, PlayerEntity player) {
        return 100;
    }

    @Override
    public void onHackComplete(World world, BlockPos pos, PlayerEntity player) {
        BlockState state = world.getBlockState(pos);
        fakeRayTrace(player, pos).ifPresent(rtr -> state.onBlockActivated(world, player, Hand.MAIN_HAND, rtr));
    }
}
