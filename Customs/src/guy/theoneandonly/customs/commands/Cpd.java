package guy.theoneandonly.customs.commands;

import guy.theoneandonly.customs.Customs;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cpd implements CommandExecutor {

    Customs plugin;

    public Cpd(Customs instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        if (!(sender instanceof Player)) {
            if (commandLable.equalsIgnoreCase("cpd")) {
                if (args.length < 3) {
                    System.out.println("Not enough paramaters. Useage 'cpd <PlayerName> <Skill> <EXP>'");
                } else if (args.length == 3) {
                    if (plugin.sbh.professions.containsKey(args[1])) {
                        Player player = null;
                        if (Bukkit.getPlayer(args[0]).isOnline()) {
                            player = Bukkit.getPlayer(args[0]);
                        } else {
                            String playerName = args[0];
                            for (String e : plugin.ph.playerDB.keySet()) {
                                if (playerName.contains(e)) {
                                    player = Bukkit.getPlayer(e);
                                    continue;
                                }
                            }
                        }
                        if (player != null) {
                            int playerSkillLevel = plugin.sbh.getLevel(player.getName(), args[1]);
                            int skillMaxLevel = plugin.sbh.getMaxProfLvl(args[1]);
                            int xpNeededToLevel;
                            int currentExp = plugin.sbh.getCurrentExp(player.getName(), args[1]);
                            int expLost = Integer.parseInt(args[2]);
                            int remainingXP;
                            if (playerSkillLevel == 0 && ((currentExp - expLost) < 0)) {
                                remainingXP = 0;
                                xpNeededToLevel = plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                            } else if ((currentExp - expLost) < 0) {//If they loose a level.
                                playerSkillLevel--;
                                xpNeededToLevel = plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                                remainingXP = (xpNeededToLevel + (currentExp - expLost));
                                if ((playerSkillLevel == 0) && (remainingXP < 0)) {
                                    remainingXP = 0;
                                }
                                if (plugin.ph.showingExpGains.get(player.getName()) && plugin.getConfig().getConfigurationSection(args[1]).getBoolean("visible")) {
                                    player.sendMessage(ChatColor.AQUA + "You've lost " + ChatColor.GOLD + args[2] + ChatColor.AQUA + " in " + ChatColor.GREEN + args[1] + ChatColor.AQUA + ". You are now level " + ChatColor.YELLOW + playerSkillLevel + ChatColor.AQUA + ".");
                                }
                                List<String> commands = plugin.getConfig().getStringList(args[1] + ".custom.level " + playerSkillLevel + ".down_commands");
                                commandExecutor(player, commands);

                                while (remainingXP < 0) {//If they loose more then one level.
                                    playerSkillLevel--;
                                    xpNeededToLevel = plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                                    remainingXP = xpNeededToLevel + remainingXP;
                                    if ((playerSkillLevel == 0) && (remainingXP < 0)) {
                                        remainingXP = 0;
                                    }
                                    if (plugin.ph.showingExpGains.get(player.getName()) && plugin.getConfig().getConfigurationSection(args[1]).getBoolean("visible")) {
                                        player.sendMessage(ChatColor.AQUA + "You are now level " + ChatColor.YELLOW + playerSkillLevel + ChatColor.AQUA + ".");
                                    }
                                    List<String> commands2 = plugin.getConfig().getStringList(args[1] + ".custom.level " + playerSkillLevel + ".down_commands");
                                    commandExecutor(player, commands2);
                                }

                            } else {//If they don't loose a level.
                                remainingXP = (currentExp - expLost);
                                xpNeededToLevel = plugin.sbh.getExpNeeded(args[1], (playerSkillLevel + 1));
                                if (plugin.ph.showingExpGains.get(player.getName()) && plugin.getConfig().getConfigurationSection(args[1]).getBoolean("visible")) {
                                    player.sendMessage(ChatColor.AQUA + "You've lost " + ChatColor.GOLD + args[2] + ChatColor.AQUA + " EXP in " + ChatColor.GREEN + args[1] + ChatColor.AQUA + ".");
                                }
                            }
                            plugin.ph.setSkill(player.getName(), args[1], playerSkillLevel, remainingXP);
                            plugin.sbh.updateBoard(player.getName(), args[1], playerSkillLevel, remainingXP, xpNeededToLevel);
                            return true;
                        }
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
