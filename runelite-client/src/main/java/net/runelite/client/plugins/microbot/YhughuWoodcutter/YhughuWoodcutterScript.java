package net.runelite.client.plugins.microbot.YhughuWoodcutter;

import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.YhughuWoodcutter.enums.WoodcuttingStatus;
import net.runelite.client.plugins.microbot.YhughuWoodcutter.enums.WoodcuttingTreeSelector;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.bank.enums.BankLocation;
import net.runelite.client.plugins.microbot.util.equipment.Rs2Equipment;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import java.util.concurrent.TimeUnit;


public class YhughuWoodcutterScript extends Script {

    public static boolean test = false;
    private static final WorldArea TreeArea = new WorldArea(3275, 3446, 6, 6, 0);
    private static final WorldArea OakArea = new WorldArea(3189, 3458, 6, 6, 0);
    private static final WorldArea WillowArea = new WorldArea(2965, 3194, 5, 5, 0);
    private static final WorldArea YewArea = new WorldArea(3085, 3468, 4, 14, 0);

    public WoodcuttingStatus CurrentWoodcuttingStatus = net.runelite.client.plugins.microbot.YhughuWoodcutter.enums.WoodcuttingStatus.PREPARE_INVENTORY;
    private YhughuWoodcutterConfig config;

    public boolean run(YhughuWoodcutterConfig config) {
        this.config = config;

        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE
                switch (CurrentWoodcuttingStatus) {
                    case PREPARE_INVENTORY:
                        System.out.println("Status: Prepare inventory.");
                        PrepareInventory();
                        break;
                    case MOVE_TO_AREA:
                        System.out.println("Status: Moving to trees.");
                        MoveToTreeArea();
                        break;
                    case CHOP_LOGS:
                        System.out.println("Status: Chopping logs.");
                        ChopLogs();
                        break;
                    case BANK:
                        System.out.println("Status: Banking.");
                        BankChoppedLogs ();
                        break;
                }


                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    public boolean HasItemsReady () {
        return Rs2Inventory.hasItem(config.AXE().getName()) || Rs2Equipment.isWearing(config.AXE().getName());
    }

    public boolean IsPlayerInRightArea () {
        switch(config.TREE()) {
            case TREE:
                if(TreeArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in tree area.");
                    return true;
                }
            case OAK:
                if(OakArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in oak area.");
                    return true;
                }
            case WILLOW:
                if(WillowArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in willow area.");
                    return true;
                }
            case YEW:
                if(YewArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in yew area.");
                    return true;
                }
        } return false;

    }

    private WorldPoint GetRandomAnchorInArea () {
        switch(config.TREE()) {
            case TREE:
                return TreeArea.toWorldPointList().get(Rs2Random.between(TreeArea.toWorldPointList().size()/2 - 3, TreeArea.toWorldPointList().size()/2 + 3));
            case OAK:
                return OakArea.toWorldPointList().get(Rs2Random.between(0, OakArea.toWorldPointList().size() - 1));
            case WILLOW:
                return WillowArea.toWorldPointList().get(Rs2Random.between(WillowArea.toWorldPointList().size()/2 - 3, WillowArea.toWorldPointList().size()/2 + 3));
            case YEW:
                return YewArea.toWorldPointList().get(Rs2Random.between(YewArea.toWorldPointList().size()/2 - 3, YewArea.toWorldPointList().size()/2 + 3));
        } return null;
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


    public void PrepareInventory() {
        if(Rs2Inventory.hasItem(config.AXE().getName()) || Rs2Equipment.isWearing(config.AXE().getName())) {
            System.out.println("Inventory is ready!");
            CurrentWoodcuttingStatus = WoodcuttingStatus.MOVE_TO_AREA;

        }
        else {
            if(Rs2Bank.isNearBank(5)) {
                if(!Rs2Bank.isOpen()) {
                    Rs2Bank.useBank();
                }
                Rs2Bank.withdrawItem(config.AXE().getName());
                CurrentWoodcuttingStatus = WoodcuttingStatus.MOVE_TO_AREA;

            } else MoveToBankArea();
        }
    }

    private static void MoveToBankArea() {

        BankLocation RandomBankPoint = Rs2Bank.getNearestBank(Microbot.getClient().getLocalPlayer().getWorldLocation());
        assert RandomBankPoint != null;
        Rs2Walker.walkTo(RandomBankPoint.getWorldPoint());

    }

    public void MoveToTreeArea () {
        if  (!HasItemsReady()) {
            System.out.println("No axe found!");
            CurrentWoodcuttingStatus = WoodcuttingStatus.PREPARE_INVENTORY;
        } else {
            if(IsPlayerInRightArea ()) {
                System.out.println("Player is in the right location.");
                CurrentWoodcuttingStatus = WoodcuttingStatus.CHOP_LOGS;
            } else {
                WorldPoint RandomTreePoint = GetRandomAnchorInArea ();
                Rs2Walker.walkTo(RandomTreePoint);
            }
        }

    }

    public void ChopLogs () {
        if  (!HasItemsReady()) {
            System.out.println("No axe found!");
            CurrentWoodcuttingStatus = WoodcuttingStatus.PREPARE_INVENTORY;
        } else if (Rs2Inventory.isFull()) {
            System.out.println("Inventory is full.");
            CurrentWoodcuttingStatus = WoodcuttingStatus.BANK;
        } else {
            WorldPoint TreeAnchor = GetRandomAnchorInArea ();
            GameObject CloseTree = Rs2GameObject.findReachableObject(config.TREE().getName(), true, 8, TreeAnchor);
            System.out.println("Close tree found: " + CloseTree);
            if (CloseTree != null) {
                if (Rs2GameObject.interact(CloseTree, "Chop down")) {
                    waitForAnimationToComplete(20000);
                    Rs2Antiban.actionCooldown();
                } else CurrentWoodcuttingStatus = WoodcuttingStatus.MOVE_TO_AREA;
            }
        } Rs2Player.waitForAnimation();

    }

    public void BankChoppedLogs () {
        if (config.TREE() == WoodcuttingTreeSelector.WILLOW){
            Rs2Inventory.dropAllExcept(config.AXE().getName());
            CurrentWoodcuttingStatus = WoodcuttingStatus.CHOP_LOGS;
        } else
        if(Rs2Bank.isNearBank(5)) {
            if(!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
            }
            Rs2Bank.depositAllExcept(config.AXE().getName());
            CurrentWoodcuttingStatus = WoodcuttingStatus.MOVE_TO_AREA;
        } else MoveToBankArea();
    }







    @Override
    public void shutdown() {
        super.shutdown();
    }
}