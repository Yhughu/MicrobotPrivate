package net.runelite.client.plugins.microbot.YhughuWizard;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.plugins.microbot.YhughuWizard.enums.WizardStatus;

import javax.inject.Inject;
import java.awt.*;

public class YhughuWizardOverlay extends OverlayPanel {

    @Inject
    YhughuWizardOverlay(YhughuWizardPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setNaughty();
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        try {
            WizardStatus WizardStatus = net.runelite.client.plugins.microbot.YhughuWizard.enums.WizardStatus.FIGHT_AND_LOOT;
            panelComponent.setPreferredSize(new Dimension(200, 300));
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Yhughu's Wizard")
                    .color(Color.CYAN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder().build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left(Microbot.status)
                            .right(String.valueOf(WizardStatus))
                    .build());


        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return super.render(graphics);
    }
}
