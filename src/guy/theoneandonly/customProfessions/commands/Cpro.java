package guy.theoneandonly.customProfessions.commands;

import guy.theoneandonly.customProfessions.CustomProfessions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cpro implements CommandExecutor {

    CustomProfessions plugin;

    public Cpro(CustomProfessions instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (commandLabel.equalsIgnoreCase("cpro")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    if (plugin.sbh.isBoardShowing(player.getName())) {
                        if (plugin.sbh.getMainShowing(player.getName())) {
                            plugin.sbh.reloadBoard(player);
                            plugin.sbh.setMainShowing(player.getName(), false);
                            plugin.sbh.setProfShowing(player.getName(), false);
                            plugin.sbh.setExpShowing(player.getName(), false);
                            return true;
                        } else {
                            plugin.sbh.setMainBoard(player);
                            plugin.sbh.setMainShowing(player.getName(), true);
                            plugin.sbh.setProfShowing(player.getName(), false);
                            plugin.sbh.setExpShowing(player.getName(), false);
                            return true;
                        }
                    } else {
                        plugin.sbh.setMainBoard(player);
                        plugin.sbh.setMainShowing(player.getName(), true);
                        plugin.sbh.setProfShowing(player.getName(), false);
                        plugin.sbh.setExpShowing(player.getName(), false);
                        return true;
                    }
                } else if (args.length == 1) {
                    if (plugin.sbh.professions.containsKey(args[0])) {
                        if (plugin.sbh.isBoardShowing(player.getName())) {
                            if (plugin.sbh.getProfShowing(player.getName())) {
                                plugin.sbh.reloadBoard(player);
                                plugin.sbh.setMainShowing(player.getName(), false);
                                plugin.sbh.setProfShowing(player.getName(), false);
                                plugin.sbh.setExpShowing(player.getName(), false);
                                return true;
                            } else {
                                plugin.sbh.setProfBoard(player, args[0]);
                                plugin.sbh.setMainShowing(player.getName(), false);
                                plugin.sbh.setProfShowing(player.getName(), true);
                                plugin.sbh.setExpShowing(player.getName(), false);
                                return true;
                            }
                        } else {
                            plugin.sbh.setProfBoard(player, args[0]);
                            plugin.sbh.setMainShowing(player.getName(), false);
                            plugin.sbh.setProfShowing(player.getName(), true);
                            plugin.sbh.setExpShowing(player.getName(), false);
                            return true;
                        }
                    }
                } else if (args.length == 2) {
                    if (plugin.sbh.professions.containsKey(args[0])) {
                        if (args[1].equalsIgnoreCase("exp")) {
                            if (plugin.sbh.isBoardShowing(player.getName())) {
                                if (plugin.sbh.getExpShowing(player.getName())) {
                                    plugin.sbh.reloadBoard(player);
                                    plugin.sbh.setMainShowing(player.getName(), false);
                                    plugin.sbh.setProfShowing(player.getName(), false);
                                    plugin.sbh.setExpShowing(player.getName(), false);
                                    return true;
                                } else {
                                    plugin.sbh.setExpBoard(player, args[0]);
                                    plugin.sbh.setMainShowing(player.getName(), false);
                                    plugin.sbh.setProfShowing(player.getName(), false);
                                    plugin.sbh.setExpShowing(player.getName(), true);
                                    return true;
                                }
                            } else {
                                plugin.sbh.setExpBoard(player, args[0]);
                                plugin.sbh.setMainShowing(player.getName(), false);
                                plugin.sbh.setProfShowing(player.getName(), false);
                                plugin.sbh.setExpShowing(player.getName(), true);
                                return true;
                            }
                        }
                    }
                }else{
                    player.sendMessage("Useage: /cpro [profession] [exp]");
                    return false;
                }
            }else{
                System.out.println("This command can only be used by players");
                return false;
            }
        } else {
            return false;
        }
        return false;
    }
}
