package guy.theoneandonly.customs;

import guy.theoneandonly.customs.commands.Cpro;
import guy.theoneandonly.customs.commands.Cpa;
import guy.theoneandonly.customs.commands.Cpd;
import guy.theoneandonly.customs.commands.Cproreload;
import guy.theoneandonly.customs.handlers.PlayerHandler;
import guy.theoneandonly.customs.handlers.ScoreBoardHandler;
import guy.theoneandonly.customs.handlers.pluginListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Customs extends JavaPlugin implements Listener {

    public ScoreBoardHandler sbh;
    public PlayerHandler ph;
    public pluginListener pl = new pluginListener(this);

    @Override
    public void onEnable() {
        try {
            new Config(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ph = new PlayerHandler(this);
        sbh = new ScoreBoardHandler(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(pl, this);
        getCommand("cpro").setExecutor(new Cpro(this));
        getCommand("cpa").setExecutor(new Cpa(this));
        getCommand("cproreload").setExecutor(new Cproreload(this));
        getCommand("cpd").setExecutor(new Cpd(this));

        //Code for handling reloads.
        Player[] onlinePlayers = Bukkit.getOnlinePlayers();
        if (onlinePlayers.length != 0) {
            for (int i = 0; i < onlinePlayers.length; i++) {
                ph.addDB(onlinePlayers[i].getName());
                ph.setShowingExpGains(onlinePlayers[i].getName(), true);
                sbh.addScoreBoard(onlinePlayers[i]);
            }
        }
    }

    @Override
    public void onDisable() {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers();
        if (onlinePlayers.length != 0) {
            for (int i = 0; i < onlinePlayers.length; i++) {
                ph.removeDB(onlinePlayers[i].getName());
                sbh.removeScoreBoard(onlinePlayers[i]);
                sbh.removeBoardShowing(onlinePlayers[i].getName());
            }
        }
    }
}
