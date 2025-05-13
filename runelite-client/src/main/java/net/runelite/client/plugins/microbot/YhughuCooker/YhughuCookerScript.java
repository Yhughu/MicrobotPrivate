package net.runelite.client.plugins.microbot.YhughuCooker;

import net.runelite.api.AnimationID;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.YhughuCooker.enums.CookingStatus;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;


public class YhughuCookerScript extends Script {
    WorldPoint RangePoint = new WorldPoint(3237, 3409, 0);
    private YhughuCookerConfig config;
    public CookingStatus CookingStatus = net.runelite.client.plugins.microbot.YhughuCooker.enums.CookingStatus.BANKING;
    public WorldPoint OutsideDoor = new WorldPoint(3243,3412,0);
    public WorldPoint InsideDoor = new WorldPoint(3240,3412,0);


    public boolean run(YhughuCookerConfig config) {
        this.config = config;
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                switch (CookingStatus) {
                    case BANKING:
                        Microbot.log("Banking case called");
                        Banking();
                        break;
                    case COOKING:
                        Microbot.log("Cooking case called");
                        Cooking();
                        break;
                }
                //CODE HERE


                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    public static void waitForAnimationToComplete(int timeoutMillis) {

        if (Microbot.getClient().getLocalPlayer() == null) return;

        // Step 1: Wait until animation starts
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (Microbot.getClient().getLocalPlayer().getAnimation() != -1) {
                break;
            }
            sleep(50);
        }

        // Step 2: Wait until animation ends
        startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (Microbot.getClient().getLocalPlayer().getAnimation() == -1) {
                break;
            }
            sleep(50);
        }
    }

    public void GoInside() {
        GameObject Range = Rs2GameObject.getGameObject(RangePoint);
        if (! Rs2GameObject.isReachable(Range)) {
            Rs2Walker.walkTo(InsideDoor);
        } else CookingStatus = net.runelite.client.plugins.microbot.YhughuCooker.enums.CookingStatus.COOKING;
    }

    private void Banking() {
        if (Rs2Inventory.hasItem(config.FISH().getName(), true)) GoInside();
        else if (Rs2Bank.isNearBank(20)) {
            if (!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
            }
            if (Rs2Bank.isOpen()) {
                if (!Rs2Bank.hasItem(config.FISH().getName(), true)) {
                    Rs2Bank.closeBank();
                    Rs2Player.logout();
                }
                Rs2Bank.depositAll();
                Rs2Bank.withdrawAll(config.FISH().getName(), true);
            }
            if (Rs2Inventory.hasItem(config.FISH().getName(), true)) {
                GoInside();
            }
        }
    }

    private void Cooking() {
        GameObject Range = Rs2GameObject.getGameObject(RangePoint);
        if (Rs2Player.getAnimation() == 896) {
            Microbot.log("He is still cooking, why does the sleepuntil not work?!");
            waitForAnimationToComplete(40000);
        } else  if (! Rs2Inventory.hasItem(config.FISH().getName())) {
            Rs2Walker.walkTo(OutsideDoor);
            CookingStatus = net.runelite.client.plugins.microbot.YhughuCooker.enums.CookingStatus.BANKING;
        }
        else if (Rs2GameObject.isReachable(Range)) {
            if (Rs2Widget.findWidget("like to cook?", null, false) != null) {
                Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);
            } else {
                Rs2GameObject.interact(Range);
                sleep(Rs2Random.between(2500,6780));
                Rs2Antiban.actionCooldown();
            }


        } else {
            Microbot.log("Can't find range");
            GoInside();
        }
    }
    @Override
    public void shutdown() {
        super.shutdown();
    }
}