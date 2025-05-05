package net.runelite.client.plugins.microbot.YhughuFiremaking;

import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.YhughuWoodcutter.YhughuWoodcutterConfig;
import net.runelite.client.plugins.microbot.aiofighter.skill.AttackStyleScript;
import net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.bank.enums.BankLocation;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.tile.Rs2Tile;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.concurrent.TimeUnit;


public class YhughuFiremakingScript extends Script {
    public static boolean test = false;
    private YhughuFiremakingConfig config;
    public FiremakingStatus FiremakingStatus = net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus.BANKING;
    private static final WorldArea StartArea = new WorldArea(3201, 3428, 3, 3, 0);

    public boolean run(YhughuFiremakingConfig config) {
        this.config = config;
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE
                switch (FiremakingStatus) {
                    case BANKING:
                        Microbot.log("Banking case called");
                        Banking();
                        break;
                    case GOSTART:
                        Microbot.log("Moving to starting area");
                        GoToStart();
                        break;
                    case BURNANDMOVE:
                        Microbot.log("Finding a spot and firemaking");
                        BurnAndMove();
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

    private static void MoveToBankArea() {
        Rs2Walker.walkTo(Rs2Bank.getNearestBank().getWorldPoint());
    }

    private void Banking() {
        Microbot.log("Inside Banking void start");
        if(Rs2Bank.isNearBank(8)) {
            if(!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
                if (! Rs2Inventory.hasItem("tinderbox")) {
                    Rs2Bank.withdrawItem("tinderbox");
                }

            }
            Rs2Bank.withdrawAll(config.LOG().getName(), true);
            if (Rs2Inventory.hasItem(config.LOG().getName())) {
                FiremakingStatus = net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus.GOSTART;
            }
        } else {
            MoveToBankArea();
        }
    }

    private void GoToStart() {
        if(StartArea.contains(Rs2Player.getLocalPlayer().getWorldLocation())) {
            FiremakingStatus = net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus.BURNANDMOVE;
        } else Rs2Walker.walkTo(StartArea.toWorldPointList().get(Rs2Random.between(0, StartArea.toWorldPointList().size()-1)));
    }

    private boolean IsFireHere (WorldPoint TileToCheck) {
        if((Rs2GameObject.findGameObjectByLocation(TileToCheck) == Rs2GameObject.getTileObject(26185))) return false;
        else return true;
    }

    private WorldPoint TileWithoutFire () {
        WorldPoint WestTile = new WorldPoint(Rs2Player.getLocalPlayer().getWorldLocation().getX() - 1, Rs2Player.getLocalPlayer().getWorldLocation().getY(), Rs2Player.getLocalPlayer().getWorldLocation().getPlane());
        if (!IsFireHere(WestTile) && Rs2Tile.isWalkable(WestTile)) {
            return WestTile;
        } else {
            WorldPoint NorthTile = new WorldPoint(Rs2Player.getLocalPlayer().getWorldLocation().getX(), Rs2Player.getLocalPlayer().getWorldLocation().getY() + 1, Rs2Player.getLocalPlayer().getWorldLocation().getPlane());
            if (!IsFireHere(NorthTile) && Rs2Tile.isWalkable(NorthTile)) {
                return NorthTile;
            } else {
                WorldPoint SouthTile = new WorldPoint(Rs2Player.getLocalPlayer().getWorldLocation().getX(), Rs2Player.getLocalPlayer().getWorldLocation().getY() + 1, Rs2Player.getLocalPlayer().getWorldLocation().getPlane());
                if (!IsFireHere(SouthTile) && Rs2Tile.isWalkable(SouthTile)) {
                    return SouthTile;
                } else
                    FiremakingStatus = net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus.GOSTART;
                return SouthTile;
            }
        }
    }

    private void BurnAndMove() {
        if(! IsFireHere(Rs2Player.getLocalPlayer().getWorldLocation())) {
            if (Rs2Inventory.hasItem(config.LOG().getName())) {
                Rs2Inventory.use("tinderbox");
                Rs2Inventory.use(config.LOG().getName());
                sleepUntilOnClientThread(() -> Microbot.getClient().getLocalPlayer().getPoseAnimation() == 822, 30000);
            } else if (!Rs2Inventory.hasItem(config.LOG().getName())) FiremakingStatus = net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingStatus.BANKING;
        } else {
            Rs2Walker.walkTo(TileWithoutFire());
            System.out.println("Finding a new tile.");
        }
    }



    @Override
    public void shutdown() {
        super.shutdown();
    }
}