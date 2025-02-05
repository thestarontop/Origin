package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MoveEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;

public class AutoScaffold extends Module {
        public AutoScaffold(){super("AutoScaffold","bzd",Category.MISC);}
        boolean AlreadyVoid;
        boolean Toggled = false;
        boolean NeedScaffold = false;
        double GroundPosY;
        int i;

        @Override
        public void onEnable(){
                super.onEnable();
                Toggled = false;
                NeedScaffold = false;
        }
        @EventTarget
        public void onMove(MoveEvent event){
                AlreadyVoid = true;
                if (mc.player.isOnGround()){
                        GroundPosY = mc.player.getY();
                }
                i = (int) Math.round(-(mc.player.getY()-1.01));
                while(i<=0) {
                        AlreadyVoid= !mc.level.getCollisions(mc.player, mc.player.getBoundingBox().move(0,i,0).expandTowards(0,0,0)).iterator().hasNext();
                        i+=1;
                        if(!AlreadyVoid) break;
                }
                if (AlreadyVoid){
                        NeedScaffold=true;
                }
                if (NeedScaffold){
                        if(((mc.player.getY()+mc.player.getDeltaMovement().y)<=(GroundPosY+0.625)) && mc.player.getY()>=(GroundPosY-3)) {
                                        if(!madebystarontopandfml.getInstance().getModuleManager().getModule("Scaffold").isEnabled()) {
                                                madebystarontopandfml.getInstance().getModuleManager().getModule("Scaffold").setEnable(true);
                                                Toggled=true;
                                        }
                        }else {
                                if(Toggled) {
                                        Toggled=false;
                                        madebystarontopandfml.getInstance().getModuleManager().getModule("Scaffold").setEnable(false);
                                }
                        }
                }else{
                        if (Toggled) {
                                Toggled = false;
                                madebystarontopandfml.getInstance().getModuleManager().getModule("Scaffold").setEnable(false);
                        }
                }
        }
}
