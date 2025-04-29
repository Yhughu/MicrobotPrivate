package net.runelite.client.plugins.microbot.firstplugin;

import net.runelite.api.ObjectID;
import net.runelite.api.World;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.aiofighter.skill.AttackStyleScript;
import net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.concurrent.TimeUnit;


public class firstpluginScript extends Script {

    public static boolean test = false;

    public static String logsVar = "logs";

    public FiremakingStatus FiremakingStatus = net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FETCH_LOGS;

    WorldPoint[] startingPositions = new WorldPoint[] {new WorldPoint(3203,3429,0), new WorldPoint(3203,3428,0), new WorldPoint(3203,3430,0)};

    AttackStyleScript attackStyleSCript = new AttackStyleScript();
    public boolean run(firstpluginConfig config) {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE

                boolean hasTinderbox = Rs2Inventory.hasItem("tinderbox");
                FetchLogsAndTinderbox(hasTinderbox, logsVar);
                //Fetch logsVar + tinderbox
                FindEmptySpot();
                //Find spot to start firemaking
                BurnLogs();
                //Firemake until empty inventory
                //Return To a Bank

                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean FetchLogsAndTinderbox(boolean hasTinderbox, String logs) {
        if (FiremakingStatus == net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FETCH_LOGS) {
            if (Rs2Inventory.hasItem(logsVar)) {
                FiremakingStatus = net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIND_EMPTY_SPOT;
                return false;
            }
            if(!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
                if (!hasTinderbox) {
                    Rs2Bank.withdrawItem("tinderbox");
                }
                Rs2Bank.withdrawAll(true, logs);
            }
        }

        return true;

    }

    private boolean FindEmptySpot() {
        if(FiremakingStatus == net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIND_EMPTY_SPOT){
            Rs2Walker.walkTo(startingPositions[Rs2Random.between(0, startingPositions.length -1)]);
            boolean isCloseToFiremakingSpot = Microbot.getClient().getLocalPlayer().getWorldLocation().distanceTo(startingPositions[0]) < 6;
            if (!isCloseToFiremakingSpot)
                return false;
            WorldPoint currentFiremakingSpot = null;
            for (WorldPoint fireMakingSpot: startingPositions) {
                if (Rs2GameObject.findObject(26185, fireMakingSpot) == null) {
                    currentFiremakingSpot = fireMakingSpot;
                    break;
                }
            }

            if (currentFiremakingSpot != null) {
                Rs2Walker.walkFastCanvas(currentFiremakingSpot);
            }

            if (Microbot.getClient().getLocalPlayer().getWorldLocation().equals(currentFiremakingSpot)) {
                FiremakingStatus = net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIREMAKING;
            }
        }
        return true;
    }

    private boolean BurnLogs(){
        if (FiremakingStatus == net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIREMAKING) {
            if(!Rs2Inventory.hasItem(logsVar)) {
                FiremakingStatus = net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FETCH_LOGS;
                return false;
            }

            if (Rs2GameObject.findObject(26185, Microbot.getClient().getLocalPlayer().getWorldLocation()) != null) {
                FiremakingStatus = net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIND_EMPTY_SPOT;
                return false;
            }

            while(FiremakingStatus == net.runelite.client.plugins.microbot.firstplugin.enums.FiremakingStatus.FIREMAKING) {
                if (!Rs2Inventory.hasItem(logsVar))
                    break;
                Rs2Inventory.use("tinderbox");
                Rs2Inventory.use(logsVar);
                sleepUntilOnClientThread(() -> Microbot.getClient().getLocalPlayer().getPoseAnimation() == 822, 30000);

            }
        }
        return true;
    }


    @Override
    public void shutdown() {
        super.shutdown();
    }
}