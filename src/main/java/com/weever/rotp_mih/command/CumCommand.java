package com.weever.rotp_mih.command;

import com.github.standobyte.jojo.command.JojoCommandsCommand;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Collection;

public class CumCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("cum").requires(ctx -> ctx.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> spawnCum(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets")))
                )
        );
        JojoCommandsCommand.addCommand("cum");
    }

    private static int spawnCum(CommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        targets.forEach(target -> {
            if (!target.level.isClientSide) {
                if (target != null) {
                    double x = target.getX();
                    double y = target.getY() + 0.5;
                    double z = target.getZ();
                    MCUtil.runCommand(target, "particle rotp_mih:cum " + x + " " + y + " " + z + " 0.1 0.1 0.1 5 15");
                }
            }
        });
        return 1;
    }
}