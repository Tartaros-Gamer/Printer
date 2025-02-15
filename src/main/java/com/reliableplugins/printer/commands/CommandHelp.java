/*
 * Project: Printer
 * Copyright (C) 2020 Bilal Salha <bsalha1@gmail.com>
 * GNU GPLv3 <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package com.reliableplugins.printer.commands;

import com.reliableplugins.printer.config.Message;
import com.reliableplugins.printer.utils.BukkitUtil;
import org.bukkit.command.CommandSender;

public class CommandHelp extends Command
{
    private final CommandHandler baseCommand;

    public CommandHelp(CommandHandler baseCommand)
    {
        super("help", "", "The help command for Printer", false, new String[0]);
        this.baseCommand = baseCommand;
    }

    public void execute(CommandSender sender, String[] args)
    {
        int pageNum = 1;
        if (args.length > 0)
        {
            try
            {
                pageNum = Integer.parseInt(args[0]);
            }
            catch (Exception ignored)
            {
            }
        }
        Command[] commands = baseCommand.getSubCommands().toArray(new Command[0]);
        int commandPerPage = 5;

        int maxPage = (int) Math.ceil((double) commands.length / commandPerPage);
        if (pageNum > maxPage)
        {
            pageNum = maxPage;
        }

        String header = Message.HELP_PRINTER_HEADER.getColoredMessageWithoutHeader()
                .replace("{PAGE}", Integer.toString(pageNum))
                .replace("{NUM_PAGES}", Integer.toString(maxPage));
        Message.sendMultilineMessage(sender, BukkitUtil.color(header));
        int pageIndex = (pageNum - 1) * commandPerPage;

        for (int i = pageIndex; i < (pageIndex + 5) && i < commands.length; i++)
        {
            Command command = commands[i];
            if (!sender.hasPermission(command.getPermission()) && !sender.isOp())
            {
                continue;
            }
            Message.sendMultilineMessage(sender, command.getDescription());
        }
    }

}
