package br.laion.ranks.plugin.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import br.laion.ranks.plugin.RanksPlugin;

import java.io.IOException;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void on(PlayerJoinEvent e) throws IOException {

		Player p = e.getPlayer();
		String name = p.getName();

		if (RanksPlugin.data.get(name).isJsonNull()) {

			RanksPlugin.data.set(name + ".kills", 0);
			RanksPlugin.data.set(name + ".rank_prefix", "");
			RanksPlugin.data.set(name + ".rank", "§7Unranked");
			RanksPlugin.data.save();
		}
	}
}
