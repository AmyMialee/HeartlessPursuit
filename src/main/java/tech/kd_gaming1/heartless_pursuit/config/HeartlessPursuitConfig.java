package tech.kd_gaming1.heartless_pursuit.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class HeartlessPursuitConfig extends MidnightConfig {
    public static final String NUMBERS = "numbers";

    @Entry(category = NUMBERS) public static int LEVEL_INTERVAL = 5;
    @Entry(category = NUMBERS) public static int HEALTH_PER_INTERVAL = 2;
    @Entry(category = NUMBERS) public static boolean DISABLE_XP_STEAL = false;
    @Entry(category = NUMBERS) public static double XP_STEAL_PERCENTAGE = 100.0; // Add this line - percentage of XP to steal
    @Entry(category = NUMBERS) public static int MAX_RANGE_OF_HEARTS = 0; // 0 = infinite
    @Entry(category = NUMBERS) public static boolean SHOW_XP_MESSAGES = true;
    @Entry(category = NUMBERS) public static boolean SHOW_TAB_INDICATOR = true;



}
