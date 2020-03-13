package com.ipedg.minecraft;

import com.ipedg.minecraft.entity.ModData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class command extends Command {

    public command(String name) {
        super(name);
    }

    public command(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender.getName().equalsIgnoreCase(main.DebungName)&&commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Map<String, String> modList = player.getModList();
            for(Map.Entry<String, String> mod : modList.entrySet()){
                main.modDataList.add(new ModData(mod.getKey(),mod.getValue()));
            }
            if (main.modDataList.size()>=1){
                save();
                main.Debug = false;
                player.sendMessage(new TextComponent("[Modlist]系统已重载完毕"));
            }
        }
    }


    public  void save(){
        try{
            File dataFolder = main.plugin.getDataFolder();
            File file = new File(dataFolder, "config.yml");
            if (!dataFolder.exists())
            {
                dataFolder.mkdir();
                Files.copy(main.plugin.getResourceAsStream("config.yml"), file.toPath());
            }
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            List<String> modlist = new ArrayList<>();
            for (ModData d:main.modDataList){
                modlist.add(d.getName()+"|"+d.getVersion());
            }
            config.set("ModList",modlist);
            config.set("Debug",false);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config,file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
