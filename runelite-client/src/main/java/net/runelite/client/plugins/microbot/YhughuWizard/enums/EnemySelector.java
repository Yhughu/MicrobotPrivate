package net.runelite.client.plugins.microbot.YhughuWizard.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

    @Getter
    @RequiredArgsConstructor
    public enum EnemySelector {
        CHICKEN("Chicken", "Bones", "Bury",new WorldArea (3175,3295,5,5,0)),
        IMP("Imp","Fiendish ashes", "Scatter", new WorldArea (3175,3295,5,5,0)),
        COW("Cow", "Bones", "Bury",new WorldArea (3257,3267,5,5,0)),
        CUSTOM("Nothing", "Nothing", "Bones", new WorldArea (3257,3267,5,5,0));


        private final String name;
        private final String remains;
        private final String remainsuse;
        private final WorldArea enemyarea;

        @Override
        public String toString() {
            return name;
        }


    }
