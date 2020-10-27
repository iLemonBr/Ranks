package br.laion.ranks.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import br.laion.ranks.plugin.Commands.CommandRanks;
import br.laion.ranks.plugin.Listeners.PlayerDeathListener;
import br.laion.ranks.plugin.Listeners.PlayerJoinListener;
import br.laion.ranks.plugin.Object.JsonConfiguration;

import java.io.File;
import java.io.IOException;

public class RanksPlugin extends JavaPlugin {

	public static RanksPlugin plugin;
	public static JsonConfiguration data;
	private PluginManager pm = Bukkit.getPluginManager();

	@Override
	public void onEnable() {

		plugin = this;

		saveDefaultConfig();

		try {
			data = new JsonConfiguration("data.json", new File(getDataFolder(), "/data.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new PlayerJoinListener(), this);

		getCommand("ranks").setExecutor(new CommandRanks());

		Bukkit.getConsoleSender().sendMessage("§e[Ranks] §aHabilitado com sucesso.");

		super.onEnable();
	}

	@Override
	public void onDisable() {

		HandlerList.unregisterAll();

		Bukkit.getConsoleSender().sendMessage("§e[Ranks] §cDesabilitado com sucesso.");
	}
}
