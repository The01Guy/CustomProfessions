package guy.theoneandonly.customProfessions.commands;

import com.mini.Arguments;
import guy.theoneandonly.customProfessions.CustomProfessions;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cpa implements CommandExecutor {

    CustomProfessions plugin;

    public Cpa(CustomProfessions instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (!(sender instanceof Player)) {
            if (commandLabel.equalsIgnoreCase("cpa")) {
                if (args.length < 3) {
                    System.out.println("Not enough paramaters. Useage 'cpa <PlayerName> <Skill> <EXP>'");
                } else if (args.length == 3) {
                    if (plugin.sbh.professions.containsKey(args[1])) {
                        if (Bukkit.getPlayer(args[0]).isOnline()) {
                            Player player = Bukkit.getPlayer(args[0]);
                            System.out.println(player.getName());
                            int playerSkillLevel = plugin.sbh.getLevel(player.getName(), args[1]);
                            int skillMaxLevel = plugin.sbh.getMaxProfLvl(args[1]);
                            int xpNeededToLevel;
                            int currentExp = plugin.sbh.getCurrentExp(player.getName(), args[1]);
                            int expGained = Integer.parseInt(args[2]);
                            int remainingXP;
                            if (playerSkillLevel >= skillMaxLevel) {//if players level is the max level do nothing.
                                return true;
                            } else {
                                xpNeededToLevel = plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                            }
                            if ((expGained + currentExp) >= xpNeededToLevel) {
                                remainingXP = (expGained + currentExp) - xpNeededToLevel;
                                playerSkillLevel++;
                                if (Bukkit.getPlayer(args[0]).isOnline()) {
                                    player.sendMessage(ChatColor.AQUA + "You've received " + ChatColor.GOLD + args[2] + ChatColor.AQUA + " in " + ChatColor.GREEN + args[1] + ChatColor.AQUA + ". You are now level " + ChatColor.YELLOW + playerSkillLevel + ChatColor.AQUA + ".");
                                    List<String> commands = plugin.getConfig().getStringList("professions." + args[1] + ".level " + playerSkillLevel + ".commands");
                                    commandExecutor(player, commands);
                                }
                                while (remainingXP > plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1)) && playerSkillLevel < skillMaxLevel) {
                                    remainingXP = remainingXP - plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                                    playerSkillLevel++;
                                    if (Bukkit.getPlayer(args[0]).isOnline()) {
                                        player.sendMessage(ChatColor.AQUA + "You've received " + ChatColor.GOLD + remainingXP + ChatColor.AQUA + " in " + ChatColor.GREEN + args[1] + ChatColor.AQUA + ". You are now level " + ChatColor.YELLOW + playerSkillLevel + ChatColor.AQUA + ".");
                                        List<String> commands = plugin.getConfig().getStringList("professions." + args[1] + ".level " + playerSkillLevel + ".commands");
                                        commandExecutor(player, commands);
                                    }
                                }
                                Arguments playerEntry = new Arguments(args[0] + "_" + args[1]);
                                playerEntry.setValue("level", playerSkillLevel + "");
                                playerEntry.setValue("exp", remainingXP + "");
                                plugin.database.addIndex(playerEntry.getKey(), playerEntry);
                                plugin.database.update();
                                if (Bukkit.getPlayer(args[0]).isOnline()) {
                                    if (plugin.sbh.getMainShowing(args[0])) {
                                        plugin.sbh.setMainBoard(player);
                                    } else if (plugin.sbh.getProfShowing(args[0])) {
                                        plugin.sbh.setProfBoard(player, args[1]);
                                    } else if (plugin.sbh.getExpShowing(args[0])) {
                                        plugin.sbh.setExpBoard(player, args[1]);
                                    }
                                }
                            } else {
                                int temp = currentExp + expGained;
                                Arguments playerEntry = new Arguments(args[0] + "_" + args[1]);
                                playerEntry.setValue("level", playerSkillLevel + "");
                                playerEntry.setValue("exp", temp + "");
                                plugin.database.addIndex(playerEntry.getKey(), playerEntry);
                                plugin.database.update();
                                if (Bukkit.getPlayer(args[0]).isOnline()) {
                                    if (plugin.sbh.getExpShowing(args[0])) {
                                        plugin.sbh.setExpBoard(player, args[1]);
                                    }
                                    player.sendMessage(ChatColor.AQUA + "You've received " + ChatColor.GOLD + args[2] + ChatColor.AQUA + " EXP in " + ChatColor.GREEN + args[1] + ChatColor.AQUA + ".");
                                }
                            }
                        }
                    } else {
                        System.out.println("Not a valid skill");
                        return false;

                    }
                }
            }
        }
        return false;
    }

    public void commandExecutor(Player player, List<String> cmds) {
        for (String c : cmds) {
            String temp = c.replace("@p", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), temp);
        }
    }
}
