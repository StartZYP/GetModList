package com.ipedg.minecraft;

import com.ipedg.minecraft.entity.ModData;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class main extends Plugin implements Listener {
    public static List<ModData> modDataList = new ArrayList<>();
    public static String DebungName = "";
    public static Boolean Debug = false;
    public static String PlayerMsg;
    public static Plugin plugin;




    @Override
    public void onEnable() {
        plugin = this;
        try
        {
            File dataFolder = getDataFolder();
            File file = new File(dataFolder, "config.yml");
            if (!dataFolder.exists())
            {
                dataFolder.mkdir();
                Files.copy(getResourceAsStream("config.yml"), file.toPath());
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            loadConfig(config);
            this.getProxy().getPluginManager().registerListener(this,this);
            this.getProxy().getPluginManager().registerCommand(this, new command("gml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            getProxy().getLogger().log(Level.WARNING, "配置读取错误。");
        }
        super.onEnable();
    }

    public void loadConfig(Configuration config){
        //saveConfig();
        DebungName = config.getString("DebungName");
        Debug = config.getBoolean("Debug");
        PlayerMsg = config.getString("Msg");
        if (Debug){
            return;
        }
        List<String> main = config.getStringList("ModList");
        for (String tmp:main){
            String[] split = tmp.split("\\|");
            System.out.print(split[0]+"|"+split[1]);
            modDataList.add(new ModData(split[0],split[1]));
        }

    }

    @EventHandler
    public void PlayerJoinGame(PostLoginEvent event){
        Map<String, String> playerMods = event.getPlayer().getModList();
        List<ModData> data = new ArrayList<>();
        for(Map.Entry<String, String> mod : playerMods.entrySet()){
            data.add(new ModData(mod.getKey(),mod.getValue()));
        }
//        EntityPlayerMP handle = player.getHandle();
//        List<ModData> playerMods = getPlayerMods(handle);
        for (ModData mod:data){
            if (Debug&&event.getPlayer().getName().equalsIgnoreCase(DebungName)){
                getLogger().info("DebugModList:"+mod.getName()+"|"+mod.getVersion());
                return;
            }else {
                if (!modDataList.contains(mod)){
                    event.getPlayer().disconnect(new TextComponent(PlayerMsg));
                }
            }
        }
    }
}