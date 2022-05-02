/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.CommandRegistry;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.misc.ClientSettings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    public Help() {
        super("Help", "Shows all commands", "help", "h", "?", "cmds", "commands");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("help", "floodlp");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        List<String> holyShitThisIsComplex = new ArrayList<>();
        for (Command command : CommandRegistry.getCommands()) {
            holyShitThisIsComplex.addAll(Arrays.asList(command.getAliases()));
        }
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, holyShitThisIsComplex.toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length == 0) {
            message("All commands and their description");
            for (Command command : CommandRegistry.getCommands()) {
                message(command.getName() + ": " + command.getDescription());
                message("  " + String.join(", ", command.getAliases()), Color.GRAY);
            }
        } else {
            String s = args[0];
            Command c = CommandRegistry.getByAlias(s);
            if (c == null) error("Command \"" + s + "\" was not found");
            else {
                message("Command " + c.getName());
                message(c.getDescription(), Color.GRAY);
                message("Aliases: " + String.join(", ", c.getAliases()), Color.GRAY);
                message("");
                ExamplesEntry e = c.getExampleArguments();
                if (e == null) {
                    message("No examples :(");
                } else {
                    message("Examples:");
                    String prefix = ModuleRegistry.getByClass(ClientSettings.class).getPrefix().getValue();
                    for (String example : e.examples()) {
                        String mEx = prefix + c.getAliases()[0] + " " + example;
                        message("  - " + mEx);
                    }
                }
            }
        }

    }
}
