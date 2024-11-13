package tech.kd_gaming1.heartless_pursuit.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class ListPlayerLevelsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("listplayerlevels")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
                        int level = player.experienceLevel;
                        Supplier<Text> message = () -> Text.literal(player.getName().getString() + " - Level: " + level);
                        source.sendFeedback(message, false);
                    }
                    return 1;
                }));
    }
}