/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class SpigotUtil
{
    static int getPlayerLoadDistance(World world)
    {
        // Get range by world name, if world isn't configured, get the default world
        return Bukkit.spigot().getConfig().getInt("world-settings." + world.getName() + ".entity-tracking-range.players",
                Bukkit.spigot().getConfig().getInt("world-settings.default.entity-tracking-range.players", 48));
    }
}
