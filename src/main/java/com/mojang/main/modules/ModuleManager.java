package com.mojang.main.modules;

import com.mojang.main.modules.client.ClickGui;
import com.mojang.main.modules.client.HUD;
import com.mojang.main.modules.combat.*;
import com.mojang.main.modules.exploit.Disabler;
import com.mojang.main.modules.exploit.NoInvClose;
import com.mojang.main.modules.exploit.PortalGodMode;
import com.mojang.main.modules.misc.*;
import com.mojang.main.modules.movement.*;
import com.mojang.main.modules.player.AutoTool;
import com.mojang.main.modules.player.FastPlace;
import com.mojang.main.modules.render.*;
import com.mojang.main.modules.world.Eagle;
import com.mojang.main.modules.world.Scaffold;
import com.mojang.main.modules.client.*;
import com.mojang.main.modules.exploit.*;
import com.mojang.main.modules.misc.*;
import com.mojang.main.modules.movement.*;
import com.mojang.main.modules.exploit.Ghost;
import com.mojang.main.modules.world.*;
import com.mojang.main.modules.combat.*;
import com.mojang.main.modules.misc.AutoReport;
import com.mojang.main.modules.render.*;

import java.util.ArrayList;

public class ModuleManager {

    public ModuleManager() {
    intModules();
    categoryArrayList = new ArrayList<Module.Category>();
    categoryArrayList.add(Module.Category.COMBAT);
    categoryArrayList.add(Module.Category.CLIENT);
    categoryArrayList.add(Module.Category.MISC);
    categoryArrayList.add(Module.Category.RENDER);
    categoryArrayList.add(Module.Category.WORLD);
    categoryArrayList.add(Module.Category.MOVEMENT);
    categoryArrayList.add(Module.Category.EXPLOIT);
    categoryArrayList.add(Module.Category.PLAYER);
    }

    public static ArrayList<Module> modules;
    public static ArrayList<Module.Category> categoryArrayList;

    public static void addHack(Module m){
        modules.add(m);
    }

    public void intModules() {
        modules = new ArrayList<Module>();
        addHack(new ThunderDeath());
        addHack(new POC());
        addHack(new ClickGui());
        addHack(new AutoTotem());
        addHack(new AutoSoup());
        addHack(new AntiFireBall());
        addHack(new NoInvisible());
        addHack(new SuperKnockBack());
        addHack(new Eagle());
        addHack(new ClayRageBot());
        addHack(new Sprint());
        addHack(new KillAura());
        addHack(new Disabler());
        addHack(new Velocity());
        addHack(new Xray());
        addHack(new HUD());
        addHack(new MobAura());
        addHack(new Teams());
        addHack(new Rotation());
        addHack(new StrafeFix());
        addHack(new NoSlow());
        addHack(new NoPush());
        addHack(new Blink());
        addHack(new NoInvClose());
        addHack(new ChestStealer());
        addHack(new InvManager());
        addHack(new FullBright());
        addHack(new ESP());
        addHack(new NameTags());
        addHack(new Scaffold());
        addHack(new SilentStrafeFix());
        addHack(new Chams());
        addHack(new BoatJump());
        addHack(new CrystalAura());
        addHack(new AutoTool());
        addHack(new AntiBot());
        addHack(new Stuck());
        addHack(new Sneak());
        addHack(new EntitySpeed());
        addHack(new SafeWalk());
        addHack(new MidClick());
        addHack(new ThrowableAura());
        addHack(new FastPlace());
        addHack(new AutoReport());
        addHack(new AntiBlind());
        addHack(new LowFire());
        addHack(new Ghost());
        addHack(new BackTrack());
        addHack(new PortalGodMode());
        addHack(new FastLadder());
        addHack(new FastWeb());
        addHack(new Test());
    }
    public Module getModule(String name){
        for (Module m : ModuleManager.modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

}
