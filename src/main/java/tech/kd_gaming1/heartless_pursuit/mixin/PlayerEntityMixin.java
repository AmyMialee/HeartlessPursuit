package tech.kd_gaming1.heartless_pursuit.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.kd_gaming1.heartless_pursuit.HealthManager;
import tech.kd_gaming1.heartless_pursuit.TabListManager;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Unique
    private int lastKnownXpLevel = -1;

    @Unique
    private boolean dropOriginalXp = true;

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        // The player was killed by another player, so we don't want them to drop XP
        // Reset the flag to allow normal XP dropping in other cases
        dropOriginalXp = !(source.getAttacker() instanceof ServerPlayerEntity);
    }

    @ModifyReturnValue(method = "getExperienceToDrop", at = @At("TAIL"))
    private int changeXpToDrop(int original) {
        return dropOriginalXp ? original : 0;
    }

    // Monitor experience level in the tick method
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the player is on the server side and if their level has changed
        if (player instanceof ServerPlayerEntity) {
            int currentLevel = player.experienceLevel;

            if (currentLevel != lastKnownXpLevel) {
                // Call HealthManager to update health based on the new level
                HealthManager.updatePlayerHealth((ServerPlayerEntity) player);
                // Call TabListManager to update the tab list
                TabListManager.onXpLevelChange((ServerPlayerEntity) player);
                lastKnownXpLevel = currentLevel; // Update last known level
            }
        }
    }
}