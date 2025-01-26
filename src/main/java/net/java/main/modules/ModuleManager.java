package net.java.main.modules;

import net.java.main.modules.client.ClickGui;
import net.java.main.modules.client.HUD;
import net.java.main.modules.client.IRC;
import net.java.main.modules.combat.*;
import net.java.main.modules.combat.AutoRod;
import net.java.main.modules.exploit.*;
import net.java.main.modules.misc.*;
import net.java.main.modules.movement.*;
import net.java.main.modules.player.*;
import net.java.main.modules.render.*;
import net.java.main.modules.world.*;

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
        addHack(new GrimNoXZVelocity());
        addHack(new Xray());
        addHack(new HUD());
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
        //addHack(new FastLadder());
        addHack(new FastWeb());
        addHack(new Test());
        addHack(new InvMove());
        addHack(new ChestESP());
        addHack(new ChestAura());
        addHack(new FastBuilder());
        addHack(new KeepContainer());
        addHack(new Breaker());
        addHack(new HandDerp());
        //addHack(new GApple());
        addHack(new Animation());
        addHack(new KeepTab());
        //addHack(new Test1());
        //addHack(new net.java.main.modules.client.hud.ArrayList());
        //addHack(new TargetHud());
        //addHack(new Notification());
        //addHack(new WaterMark());
        //addHack(new Tower());
        addHack(new DelayVelocity());
        addHack(new LagFly());
        addHack(new NameProtect());
        addHack(new GhostBlock());
        addHack(new CivBreak());
        addHack(new FalseHand());
        addHack(new AutoShield());
        addHack(new ArmorBreaker());
        addHack(new Stealer());
        addHack(new AutoPartyGame());
        addHack(new AutoRod());
        addHack(new AutoFish());
        addHack(new LegitAura());
        addHack(new SelfDestruct());
        addHack(new Speed());
        addHack(new Projectiles());
        addHack(new Protocol());
        addHack(new IRC());
        addHack(new Crasher());
        addHack(new ColorSigns());
        addHack(new AntiAFK());
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
