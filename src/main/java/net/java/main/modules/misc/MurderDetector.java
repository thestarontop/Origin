package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.event.events.WorldEvent;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MurderDetector extends Module {
    public MurderDetector(){super("MurderDetector","bzd",Category.MISC);}
    public static Player murder1 = null;
    public static Player murder2 = null;
    public List<Item> murderItems = Arrays.asList(Items.IRON_SWORD,  // Items.iron_sword,
            Items.STONE_SWORD,  // Items.stone_sword,
            Items.IRON_SHOVEL,  // Items.iron_shovel,
            Items.STICK,  // Items.stick,
            Items.WOODEN_AXE,  // Items.wooden_axe,
            Items.WOODEN_SWORD,  // Items.wooden_sword,
            Items.STONE_SHOVEL,  // Items.stone_shovel,
            Items.BLAZE_ROD,  // Items.blaze_rod,
            Items.DIAMOND_SHOVEL,  // Items.diamond_shovel,
            Items.SHEARS,  // Items.shears,
            Items.PUMPKIN_PIE,  // Items.pumpkin_pie,
            Items.GOLDEN_PICKAXE,  // Items.golden_pickaxe,
            Items.CARROT_ON_A_STICK,  // Items.carrot_on_a_stick,
            Items.COOKIE,  // Items.cookie,
            Items.DIAMOND_AXE,  // Items.diamond_axe,
            Items.GOLDEN_SWORD,  // Items.golden_sword,
            Items.DIAMOND_SWORD,  // Items.diamond_sword,
            Items.DIAMOND_HOE,  // Items.diamond_hoe,
            Items.NAME_TAG,  // Items.name_tag,
            Items.SPRUCE_BOAT,
            Items.OAK_BOAT,
            Items.BIRCH_BOAT,
            Items.JUNGLE_BOAT,
            Items.ACACIA_BOAT,
            Items.DARK_OAK_BOAT,// Items.boat,
            Items.PRISMARINE_SHARD,  // Items.prismarine_shard,
            Items.COD,
            Items.SALMON,
            Items.TROPICAL_FISH,
            Items.PUFFERFISH,  // Items.fish,
            Items.COOKED_BEEF,  // Items.cooked_beef,
            Items.MELON_SLICE,  // Items.speckled_melon,
            Items.RED_DYE,
            Items.GREEN_DYE,
            Items.BLUE_DYE,
            Items.YELLOW_DYE,
            Items.PINK_DYE,
            Items.LIGHT_BLUE_DYE,
            Items.MAGENTA_DYE,
            Items.LIME_DYE,
            Items.CYAN_DYE,
            Items.PURPLE_DYE,
            Items.BROWN_DYE,
            Items.BLACK_DYE,
            Items.WHITE_DYE,
            Items.GRAY_DYE,
            Items.LIGHT_GRAY_DYE,  // Items.dye,
            Items.BOOK,  // Items.book,
            Items.QUARTZ,  // Items.quartz,
            Items.GOLDEN_CARROT,  // Items.golden_carrot,
            Items.APPLE,  // Items.apple,
            Items.MUSIC_DISC_13,
            Items.MUSIC_DISC_CAT,
            Items.MUSIC_DISC_BLOCKS,
            Items.MUSIC_DISC_CHIRP,
            Items.MUSIC_DISC_FAR,
            Items.MUSIC_DISC_MALL,
            Items.MUSIC_DISC_MELLOHI,
            Items.MUSIC_DISC_STAL,
            Items.MUSIC_DISC_STRAD,
            Items.MUSIC_DISC_WARD,
            Items.MUSIC_DISC_11,
            Items.MUSIC_DISC_WAIT, // Items.record_blocks
            Items.REDSTONE_TORCH,   // Blocks.redstone_torch,
            Items.DEAD_BUSH,   // Blocks.deadbush,
            Items.SPONGE,   // Blocks.sponge,
            Items.DRAGON_EGG
             );
        @EventTarget
        public void onUpdate(UpdateEvent event){
            for (Entity entity : mc.level.entitiesForRendering()){
                if (entity instanceof Player player){
                    if (murderItems.contains(player.getMainHandItem().getItem())){
                        if (murder1 == null) {
                            murder1 = player;
                            return;
                        }
                        if (murder2 == null){
                            murder2 = player;
                            return;
                        }
                    }
                }
            }
        }
        @EventTarget
        public void onPacket(PacketEvent event){
            if (event.getPacket() instanceof ClientboundLoginPacket){
                murder1 = null;
                murder2 = null;
            }
        }
        @Override
        public void onDisable(){
            super.onDisable();
            murder1 = null;
            murder2 = null;
        }
}
