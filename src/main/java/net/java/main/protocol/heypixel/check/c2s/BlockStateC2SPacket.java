package net.java.main.protocol.heypixel.check.c2s;


import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.protocol.heypixel.msgpack.core.MessageBufferPacker;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;


public class BlockStateC2SPacket extends HeypixelCheckPacket {


    public BlockState state;
    public String str;


    public BlockStateC2SPacket(BlockPos blockPos) {
        this.state = Minecraft.getInstance().level.getBlockState(blockPos);
//        if(state.getBlock() == Blocks.PLAYER_HEAD || state.getBlock() == Blocks.PLAYER_HEAD) {
//            this.str = ((SkullBlock)state.getBlock()).
//        }

        BlockEntity entity = mc.level.getBlockEntity(blockPos);
        /*if (entity instanceof SkullBlockEntity sb) {
          /  int rot = sb.hashCode().get(SkullBlock.ROTATION);
            if (rot == 15) {
                rot = 0;
            } else {
                rot++;
            }
            rot = (rot / 4 + 2) % 4;
            Direction fac = switch (rot) {
                case 0 -> Direction.SOUTH;
                case 1 -> Direction.NORTH;
                case 2 -> Direction.WEST;
                case 3 -> Direction.EAST;
                default -> null;
            };

            str = "Block{" + sb.getOwnerProfile().getName() + "}[facing=" +
                (
                    state.getBlock() instanceof SkullBlock
                        ? state.getBlock().defaultBlockState().toString()
                        : fac.toString()
                )
                + ",type=single,waterlogged=false]";
        } else {
            str = state.toString();
        }*/
    }


    public BlockStateC2SPacket(ByteBuf friendlyByteBuf) {
        this.state = null;
    }

    public static void asyncCheck(HeypixelSessionManager manager, BlockPos blockPos) {
        check(manager, blockPos);
    }

    public static void check(HeypixelSessionManager manager, BlockPos blockPos) {
        new BlockStateC2SPacket(blockPos).m(manager).sendCheckPacket();
    }

    @Override
    public void processBuffer(FriendlyByteBuf friendlyByteBuf, BufferHelper bufferHelper) {
        bufferHelper.writeString(friendlyByteBuf, str);
//        System.out.println("state: " + str);
    }

    @Override
    public void writeData(MessageBufferPacker packer) throws IOException {
        packer.packValue(new Variable().setStringValue(str));
//        System.out.println("state: " + str);
    }
}
