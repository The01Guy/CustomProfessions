package guy.theoneandonly.customProfessions;

import com.mini.Arguments;
import guy.theoneandonly.customProfessions.commands.Cpro;
import com.mini.Mini;
import guy.theoneandonly.customProfessions.commands.Cpa;
import guy.theoneandonly.customProfessions.scoreBoard.ScoreBoardHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomProfessions extends JavaPlugin implements Listener {

    public Mini database;
    public ScoreBoardHandler sbh;

    @Override
    public void onEnable() {
        try {
            new Config(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        database = new Mini(this.getDataFolder().getPath(), "skills.mini");
        sbh = new ScoreBoardHandler(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        getCommand("cpro").setExecutor(new Cpro(this));
        getCommand("cpa").setExecutor(new Cpa(this));
        

    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        for(String prof : sbh.professions.keySet()){
            dbAdd(event.getPlayer().getName(), prof);
        }
        sbh.addScoreBoard(event.getPlayer());
        sbh.setMainShowing(event.getPlayer().getName(), false);
        sbh.setProfShowing(event.getPlayer().getName(), false);
        sbh.setExpShowing(event.getPlayer().getName(), false);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        sbh.showingRemove(event.getPlayer().getName());
        sbh.removeScoreBoard(event.getPlayer());
    }
    
    public void dbAdd(String playerName, String profession){
        if(database.hasIndex(playerName + "_" + profession)){
            return;
        }else{
            Arguments playerEntry = new Arguments(playerName + "_" + profession);
            playerEntry.setValue("level", "0");
            playerEntry.setValue("exp", "0");
            database.addIndex(playerEntry.getKey(), playerEntry);
            database.update();
        }
    }
}
