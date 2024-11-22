// TabListManager.java

package tech.kd_gaming1.heartless_pursuit;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import java.util.HashMap;
import java.util.UUID;

public class TabListManager {
    private static final HashMap<UUID, Integer> lastLevelCache = new HashMap<>();

    // Register this in your mod initializer
    public static void register() {
        // Update on player join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            updatePlayerTabInfo(player);
            lastLevelCache.put(player.getUuid(), player.experienceLevel);
        });

        // Clean up cache when player leaves
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            lastLevelCache.remove(handler.player.getUuid());
        });
    }

    // Method to be called from your existing mixin
    public static void onXpLevelChange(ServerPlayerEntity player) {
        int currentLevel = player.experienceLevel;
        UUID playerUUID = player.getUuid();

        // Only update if level has changed
        if (!lastLevelCache.containsKey(playerUUID) || lastLevelCache.get(playerUUID) != currentLevel) {
            lastLevelCache.put(playerUUID, currentLevel);
            updatePlayerTabInfo(player);
        }
    }

    private static void updatePlayerTabInfo(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerScoreboard scoreboard = server.getScoreboard();
        String teamName = "xp_" + player.getUuid().toString().substring(0, 8);
        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            team = scoreboard.addTeam(teamName);
        }

        // Set the suffix with XP level
        String xpInfo = " §a[Level: " + player.experienceLevel + "]";
        team.setSuffix(Text.literal(xpInfo));

        // Get player name
        String playerName = player.getName().getString();

        // Remove from any existing team first
        scoreboard.clearTeam(playerName);

        // Add to the new team
        scoreboard.addScoreHolderToTeam(playerName, team);
    }
}