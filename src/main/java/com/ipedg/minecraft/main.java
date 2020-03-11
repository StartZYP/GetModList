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
    private List<ModData> modDataList = new ArrayList<>();
    private Boolean OpenSystem = false;
    private Boolean Debug = false;
    private String PlayerMsg;



    @Override
    public void onEnable() {
        try
        {
            File dataFolder = getDataFolder();
            File file = new File(dataFolder, "config.yml");
            if (!dataFolder.exists())
            {
                dataFolder.mkdir();
                Files.copy(getResourceAsStream("config.yml"), file.toPath(), new CopyOption[0]);
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            loadConfig(config);
            this.getProxy().getPluginManager().registerListener(this,this);
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
        List<String> main = config.getStringList("ModList");
        for (String tmp:main){
            String[] split = tmp.split("\\|");
            System.out.print(split[0]+"|"+split[1]);
            modDataList.add(new ModData(split[0],split[1]));
        }
        OpenSystem = config.getBoolean("OpenSystem");
        Debug = config.getBoolean("Debug");
        PlayerMsg = config.getString("Msg");
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
            if (Debug){
                System.out.print(mod.getName()+"|"+mod.getVersion());
            }
            if (OpenSystem){
                if (!modDataList.contains(mod)){
                    event.getPlayer().disconnect(new TextComponent(PlayerMsg));
                }
            }

        }
    }
}