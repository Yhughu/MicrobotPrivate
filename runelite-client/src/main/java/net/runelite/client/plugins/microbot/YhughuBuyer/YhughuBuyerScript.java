package net.runelite.client.plugins.microbot.YhughuBuyer;

import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.YhughuBuyer.enums.BuyingStatus;
import net.runelite.client.plugins.microbot.globval.WidgetIndices;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.bank.enums.BankLocation;
import net.runelite.client.plugins.microbot.util.depositbox.Rs2DepositBox;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.settings.Rs2Settings;
import net.runelite.client.plugins.microbot.util.shop.Rs2Shop;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.worldhopper.WorldHopperPlugin;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class YhughuBuyerScript extends Script {

    public static boolean test = false;
    private static final WorldArea VarrockGeneralArea = new WorldArea(3215, 3414, 3, 5, 0);
    private static final WorldArea BettysArea = new WorldArea(3012, 3257, 4, 4, 0);
    private static final WorldArea GerryArea = new WorldArea(3013, 3222, 4, 5, 0);
    private static final WorldArea YewArea = new WorldArea(3085, 3468, 4, 14, 0);

    public BuyingStatus currentBuyingStatus = BuyingStatus.MOVE_TO_SHOP;
    private YhughuBuyerConfig config;

    public boolean run(YhughuBuyerConfig config) {
        this.config = config;

        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE
                switch (currentBuyingStatus) {
                    case MOVE_TO_SHOP:
                        System.out.println("Status: Moving to Shop.");
                        MoveToShopArea();
                        break;
                    case SHOP_AND_HOP:
                        System.out.println("Status: Start shopping and hopping.");
                        ShoppingAndHopping();
                        break;
                    case BANK:
                        System.out.println("Status: Banking.");
                        Banking();
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
        return Rs2Inventory.hasItemAmount("coins", 10000 );
    }

    private WorldPoint GetClosestBankOrDepositBox () {
        WorldPoint RandomDepositPoint = Rs2DepositBox.getNearestDepositBox().getWorldPoint();
        WorldPoint RandomBankPoint = Rs2Bank.getNearestBank().getWorldPoint();
        int DepositDistance = RandomDepositPoint.distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation());
        int BankDistance = RandomBankPoint.distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation());
        if (BankDistance > DepositDistance) {
            return RandomDepositPoint;
        } else return RandomBankPoint;
    }

    public boolean IsPlayerInRightArea () {
        switch(config.ITEM().getLocation()) {
            case "varrockgeneral":
                if (VarrockGeneralArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in correct area.");
                    return true;
                }
            case "betty":
                if (BettysArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in correct area.");
                    return true;
                }
            case "gerrant":
                if (GerryArea.contains(Microbot.getClient().getLocalPlayer().getWorldLocation())) {
                    System.out.println("Is in correct area.");
                    return true;
                }
        }
        return false;
    }

    private WorldPoint GetShopLocation () {
        switch (config.ITEM().getLocation()) {
            case ("varrockgeneral"):
                return VarrockGeneralArea.toWorldPointList().get(Rs2Random.between(0, VarrockGeneralArea.toWorldPointList().size() - 1));
            case ("betty"):
                return BettysArea.toWorldPointList().get(Rs2Random.between(0, BettysArea.toWorldPointList().size() - 1));
            case ("gerrant"):
                return GerryArea.toWorldPointList().get(Rs2Random.between(0, GerryArea.toWorldPointList().size() - 1));
        }
        return null;
    }


    private void MoveToBankArea() {
        Rs2Walker.walkTo(GetClosestBankOrDepositBox());
        System.out.println("MoveToBankArea called, moving to: " + GetClosestBankOrDepositBox());
    }

    public void MoveToShopArea () {
        if  (!HasItemsReady() || Rs2Inventory.isFull()) {
            System.out.println("Inventory full or out of money!");
            currentBuyingStatus = BuyingStatus.BANK;
        } else {
            if(IsPlayerInRightArea ()) {
                System.out.println("Player is in the right location.");
                currentBuyingStatus = BuyingStatus.SHOP_AND_HOP;
            } else {
                Rs2Walker.walkTo(GetShopLocation());
            }
        }

    }

    public void ShoppingAndHopping () {
        if  (!HasItemsReady() || Rs2Inventory.isFull()) {
            System.out.println("Inventory full or out of money!");
            currentBuyingStatus = BuyingStatus.BANK;
        }
        if (!Rs2Shop.isOpen()) {
            Rs2Shop.openShop(config.ITEM().getNpcname());
        } else {
            if (!Rs2Shop.hasMinimumStock(config.ITEM().getName(), config.ITEM().getThreshold() + 1)) {
                Rs2Shop.closeShop();
                if (Rs2Inventory.isFull()) currentBuyingStatus = BuyingStatus.BANK;
                else {
                    Rs2Random.waitEx(3200, 800);
                    int WorldToHopTo = Login.getNextWorld(false);
                    Microbot.hopToWorld(WorldToHopTo);
                    Rs2Antiban.actionCooldown();
                    }
            } else {
                Rs2Shop.buyItem(config.ITEM().getName(), config.AMOUNT().getAmount());
                if (config.ITEM().getNameseconditem() != null) Rs2Shop.buyItem(config.ITEM().getNameseconditem(), config.AMOUNT().getAmount());
        }

        }

    }

    public void Banking () {
        int distance = (Rs2DepositBox.getNearestDepositBox().getWorldPoint().distanceTo(Microbot.getClient().getLocalPlayer().getWorldLocation()));
        if (distance < 5) {
            if (!Rs2DepositBox.isOpen()) {
                Rs2DepositBox.openDepositBox();
            }
            Rs2DepositBox.depositAllExcept("coins");
            currentBuyingStatus = BuyingStatus.MOVE_TO_SHOP;
        } else if (Rs2Bank.isNearBank(5)) {
            if (!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
            }
            Rs2Bank.depositAllExcept("coins");
            if (Rs2Bank.hasItem(Collections.singletonList(config.ITEM().getName()), true, config.AmountToBuy())) {
                Rs2Bank.closeBank();
                Rs2Player.logout();
            }
            currentBuyingStatus = BuyingStatus.MOVE_TO_SHOP;
            if (!HasItemsReady()) Rs2Bank.withdrawDeficit("coins", Rs2Random.between(10000, 20000));
        } else MoveToBankArea();
    }


    @Override
    public void shutdown() {
        super.shutdown();
    }
}