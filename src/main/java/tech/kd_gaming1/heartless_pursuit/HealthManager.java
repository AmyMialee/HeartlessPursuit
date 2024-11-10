package tech.kd_gaming1.heartless_pursuit;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

import static tech.kd_gaming1.heartless_pursuit.HeartlessPursuit.LOGGER;
import static tech.kd_gaming1.heartless_pursuit.config.HeartlessPursuitConfig.HEALTH_PER_INTERVAL;
import static tech.kd_gaming1.heartless_pursuit.config.HeartlessPursuitConfig.LEVEL_INTERVAL;

public class HealthManager {
    // Base health is 20 (10 hearts)
    private static final double BASE_HEALTH = 20.0;
    // Add 2 health points (1 heart) per 5 levels

    public static void updatePlayerHealth(ServerPlayerEntity player) {
        if (player == null) {
            LOGGER.warn("[HeartlessPursuit] Attempted to update health for null player");
            return;
        }

        int playerLevel = player.experienceLevel;
        // Calculate how many level intervals the player has reached
        int intervals = playerLevel / LEVEL_INTERVAL;
        // Calculate total health (base + bonus)
        double totalHealth = BASE_HEALTH + (intervals * HEALTH_PER_INTERVAL);

        // Get the player's health attribute
        EntityAttributeInstance healthAttribute = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);

        if (healthAttribute != null) {
            // Only update if the health value would be different
            if (healthAttribute.getBaseValue() != totalHealth) {
                healthAttribute.setBaseValue(totalHealth);
            } else {
                LOGGER.error("[HeartlessPursuit] Failed to get health attribute for player {}", player.getName().getString());
            }
        }
    }
}