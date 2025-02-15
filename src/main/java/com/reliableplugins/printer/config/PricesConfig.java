/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.config;

import com.reliableplugins.printer.Printer;
import com.reliableplugins.printer.type.ColoredMaterial;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.logging.Level;

public class PricesConfig extends Config
{
    private HashMap<Material, Double> blockPrices = new HashMap<>();
    private HashMap<Material, Double> itemPrices = new HashMap<>();
    private HashMap<ColoredMaterial, Double> coloredPrices = new HashMap<>();

    public PricesConfig()
    {
        super("prices.yml");
    }

    @Override
    public void load()
    {
        blockPrices = new HashMap<>();
        itemPrices = new HashMap<>();
        coloredPrices = new HashMap<>();

        getConfig().options().header("For a complete list of material names go to: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html\n"
                + "For colored blocks or typed blocks such as oak, birch, spruce and jungle logs, add the name of \n"
                + "the material followed by a dash '-' and then the id. For example: black wool = WOOL-15\n"
                + "For an example config, go to example-prices.yml");

        // Load Prices
        for(String blockName : config.getKeys(true))
        {
            blockName = blockName.toUpperCase(); // Make uppercase for support in the Material enumeration

            double price = config.getDouble(blockName);
            if(price < 0)
            {
                Printer.INSTANCE.getLogger().log(Level.WARNING, blockName + " has an invalid price: " + price);
            }
            else
            {
                if(blockName.contains("-"))
                {
                    coloredPrices.put(getColored(blockName), price);
                }
                else
                {
                    Material material;
                    try
                    {
                        material = Material.valueOf(blockName);
                    }
                    catch (IllegalArgumentException e)
                    {
                        Printer.INSTANCE.getLogger().log(Level.WARNING, blockName + " is not a valid Material!");
                        continue;
                    }

                    if(BukkitUtil.isItemOfBlock(material))
                    {
                        itemPrices.put(material, price);

                        // If item has a corresponding block, add it
                        Material itemMaterialBlock = BukkitUtil.getBlockOfItem(material);
                        if(itemMaterialBlock != null)
                        {
                            blockPrices.put(itemMaterialBlock, price);
                        }
                    }
                    else if(BukkitUtil.isBlockOfItem(material))
                    {
                        blockPrices.put(material, price);

                        // If block has a corresponding item, add it
                        Material itemMaterial = BukkitUtil.getItemOfBlock(material);
                        if(itemMaterial != null)
                        {
                            itemPrices.put(itemMaterial, price);
                        }
                    }
                    else
                    {
                        blockPrices.put(material, price);
                    }
                }
            }
        }
        save();
    }

    private ColoredMaterial getColored(String blockName)
    {
        String materialName = blockName.substring(0, blockName.indexOf('-'));
        byte data = (byte) Short.parseShort(blockName.substring(blockName.indexOf('-') + 1));
        Material material = Material.valueOf(materialName);

        return ColoredMaterial.fromMaterialAndData(material, data);
    }

    public HashMap<ColoredMaterial, Double> getColoredPrices()
    {
        return coloredPrices;
    }

    public HashMap<Material, Double> getBlockPrices()
    {
        return blockPrices;
    }

    public HashMap<Material, Double> getItemPrices()
    {
        return itemPrices;
    }
}
