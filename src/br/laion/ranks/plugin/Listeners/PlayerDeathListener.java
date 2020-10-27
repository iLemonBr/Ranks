package br.laion.ranks.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.laion.ranks.plugin.RanksPlugin;
import br.laion.ranks.plugin.Utils.ConfigUtils;

import java.io.IOException;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void on(EntityDeathEvent e) throws IOException {

		if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {

			@SuppressWarnings("unused")
			Player damaged = (Player) e.getEntity();
			Player damager = e.getEntity().getKiller();
			int damagerKills = RanksPlugin.data.get(damager.getName() + ".kills").getAsInt();

			damagerKills += 1;

			for (String r : RanksPlugin.plugin.getConfig().getConfigurationSection("ranks").getKeys(false)) {

				if (damagerKills >= RanksPlugin.plugin.getConfig().getInt("ranks." + r + ".kills")) {

					RanksPlugin.data.set(damager.getName() + ".kills", damagerKills);
					RanksPlugin.data.set(damager.getName() + ".rank_prefix",
							RanksPlugin.plugin.getConfig().getString("ranks." + r + ".prefix").replace("&", "§"));
					RanksPlugin.data.set(damager.getName() + ".rank",
							RanksPlugin.plugin.getConfig().getString("ranks." + r + ".name").replace("&", "§"));

					RanksPlugin.data.save();

					RanksPlugin.plugin.getConfig().getStringList("ranks." + r + ".commands").forEach(c -> {

						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("{player}", damager.getName()));
					});

					damager.sendMessage(ConfigUtils.getString("messages.rank-conquistado", true).replace("{rank}",
							ConfigUtils.getString("ranks." + r + ".name", true)));

					if (RanksPlugin.plugin.getConfig().getBoolean("ranks." + r + ".broadcast")) {
						Bukkit.getOnlinePlayers().forEach(o -> {

							RanksPlugin.plugin.getConfig().getStringList("ranks." + r + ".broadcast-message")
									.forEach(m -> {

										o.sendMessage(m.replace("{player}", damager.getName()).replace("&", "§"));
									});
						});
					}

					if (RanksPlugin.plugin.getConfig().getBoolean("ranks." + r + ".lighting")) {

						PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1);
						Location loc = damager.getLocation();

						damager.addPotionEffect(resistance);
						damager.getWorld().strikeLightningEffect(loc);
					}
				}
			}
		}
	}
}
