package tech.kd_gaming1.heartless_pursuit.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class HeartlessPursuitConfig extends MidnightConfig {
    public static final String NUMBERS = "numbers";

    @Entry(category = NUMBERS) public static int LEVEL_INTERVAL = 5;
    @Entry(category = NUMBERS) public static int HEALTH_PER_INTERVAL = 2;
    @Entry(category = NUMBERS) public static double XP_STEAL_PERCENTAGE = 100.0; // Add this line - percentage of XP to steal
    @Entry(category = NUMBERS) public static boolean SHOW_XP_MESSAGES = true; // Add this line - toggle messages
}
