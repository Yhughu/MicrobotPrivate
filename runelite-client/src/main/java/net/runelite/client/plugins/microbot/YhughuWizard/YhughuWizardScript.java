package net.runelite.client.plugins.microbot.YhughuWizard;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.YhughuWizard.YhughuWizardConfig;
import net.runelite.client.plugins.microbot.YhughuWizard.enums.WizardStatus;
import net.runelite.client.plugins.microbot.aiofighter.skill.AttackStyleScript;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Rs2Random;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.concurrent.TimeUnit;

import static net.runelite.client.plugins.microbot.YhughuWizard.enums.EnemySelector.CUSTOM;


public class YhughuWizardScript extends Script {
    private YhughuWizardConfig config;
    public WizardStatus CurrentWizardStatus = WizardStatus.MOVE_TO_AREA;

    public static boolean test = false;

    AttackStyleScript attackStyleSCript = new AttackStyleScript();
    public boolean run(YhughuWizardConfig config) {
        this.config = config;
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                //CODE HERE
                boolean ate = Rs2Player.eatAt(config.FoodPercentage());
                if (ate) {
                    Microbot.log("Nom nom nom");
                }
                switch (CurrentWizardStatus) {
                    case PREPARE_INVENTORY:
                        //something
                        break;
                    case MOVE_TO_AREA:
                        MoveToStart();
                        break;
                    case FIGHT_AND_LOOT:
                        FightingAndLooting();
                        break;
                    case BANK:
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



    public void FightingAndLooting () {
        if (config.Enemy() == CUSTOM) {
            DoFightingAndLooting(config.CustomEnemy());
        } else DoFightingAndLooting(config.Enemy().getName());
    }

    private void DoFightingAndLooting(String EnemyName) {
        if (Rs2Combat.inCombat()) {
            if (Rs2Player.getHealthPercentage() < config.FoodPercentage()) {
                Rs2Player.eatAt(config.FoodPercentage());
            }
            Microbot.log("In combat");
        } else {
            Microbot.log("Not in combat");
            boolean isGetBones = config.IsBury()
                    ? Rs2GroundItem.loot(config.Enemy().getRemains(), 5)
                    : false;
            boolean isLooting = (Rs2GroundItem.lootAtGePrice(config.PriceToLoot())
                    || Rs2GroundItem.loot(config.AdditionalItem(), 5)
                    || isGetBones);
            if (isLooting) return;
            if (Rs2Inventory.isFull()) {
                if(config.IsBury() && Rs2Inventory.contains(config.Enemy().getRemains())) BuryRemains();
                else {
                    CurrentWizardStatus = WizardStatus.BANK;
                }
            }
            var npc = Rs2Npc.getNpcsForPlayer(EnemyName);
            //if npc is attacking us, then attack back
            if (!npc.isEmpty() && !Microbot.getClient().getLocalPlayer().isInteracting()) {
                Microbot.log("I am being attacked");
                Rs2Npc.attack(npc.get(0));
                return;
            }
            if (!Rs2Combat.inCombat() && !isLooting) {
                if (Rs2Npc.getAttackableNpcs(EnemyName) != null){
                    Rs2Npc.attack(EnemyName);
                }
            }
        }
    }

    public void Banking () {
        if(Rs2Bank.isNearBank(5)) {
            if(!Rs2Bank.isOpen()) {
                Rs2Bank.useBank();
            }
            Rs2Bank.depositAllExcept("Mind rune");
            CurrentWizardStatus = WizardStatus.MOVE_TO_AREA;
        } else MoveToBankArea();
    }

    public void MoveToStart () {
        WorldPoint PlaceToStart = config.Enemy().getEnemyarea().toWorldPointList().get(Rs2Random.between(0, config.Enemy().getEnemyarea().toWorldPointList().size() - 1));
        if (config.Enemy().getEnemyarea().contains(Microbot.getClient().getLocalPlayer().getWorldLocation()) || config.Enemy() == CUSTOM) {
            CurrentWizardStatus = WizardStatus.FIGHT_AND_LOOT;
        } else Rs2Walker.walkTo(PlaceToStart);
    }

    public void BuryRemains() {
        while (Rs2Inventory.contains(config.Enemy().getRemains())) {
            Rs2Inventory.interact(config.Enemy().getRemains(), config.Enemy().getRemainsuse());
            Rs2Player.waitForAnimation();
            Rs2Antiban.actionCooldown();
        }
    }

    public void MoveToBankArea() {
        Rs2Walker.walkTo(Rs2Bank.getNearestBank().getWorldPoint());
    }
    
    @Override
    public void shutdown() {
        super.shutdown();
    }
}