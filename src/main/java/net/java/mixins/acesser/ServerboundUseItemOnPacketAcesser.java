package net.java.mixins.acesser;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundUseItemOnPacket.class)
public interface ServerboundUseItemOnPacketAcesser {

    @Mutable
    @Accessor("blockHit")
    void setBlockHit(BlockHitResult blockHit);

}
