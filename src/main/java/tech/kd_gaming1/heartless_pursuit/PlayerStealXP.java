package tech.kd_gaming1.heartless_pursuit;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tech.kd_gaming1.heartless_pursuit.config.HeartlessPursuitConfig;
import tech.kd_gaming1.heartless_pursuit.event.PlayerKillPlayerCallback;

public class PlayerStealXP {
    public static void init() {
        PlayerKillPlayerCallback.EVENT.register((killer, victim) -> {
            // Get the victim's XP level and progress
            int victimLevel = victim.experienceLevel;
            int totalXP = getTotalXP(victim, victimLevel);

            // Add XP to killer
            killer.addExperience(totalXP);

            // Reset victim's XP to 0
            victim.setExperienceLevel(0);
            victim.setExperiencePoints(0);

            // Send messages if enabled in config
            if (HeartlessPursuitConfig.SHOW_XP_MESSAGES) {
                killer.sendMessage(Text.literal("You stole " + victimLevel + " levels from " + victim.getName().getString()));
                victim.sendMessage(Text.literal("Your levels were stolen by " + killer.getName().getString()));
            }
        });
    }

    private static int getTotalXP(ServerPlayerEntity victim, int victimLevel) {
        int victimProgress = (int) (victim.experienceProgress * victim.getNextLevelExperience());

        // Calculate total XP to transfer
        int totalXP = calculateTotalXP(victimLevel) + victimProgress;

        // Apply percentage modifier from config
        double stealPercentage = HeartlessPursuitConfig.XP_STEAL_PERCENTAGE;

        // Ensure the steal percentage is within a valid range
        if (stealPercentage < 0 || stealPercentage > 100) {
            stealPercentage = 50; // Default to 50% if out of range
        }

        totalXP = (int) (totalXP * (stealPercentage / 100.0));

        // Ensure totalXP is non-negative
        totalXP = Math.max(totalXP, 0);
        return totalXP;
    }

    private static int calculateTotalXP(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }

}