package br.laion.ranks.plugin.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.laion.ranks.plugin.RanksPlugin;
import br.laion.ranks.plugin.Utils.ConfigUtils;
import br.laion.ranks.plugin.Utils.Scroller;

import minecraft.arplex.core.annotation.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRanks {
    
            @Command(
                name = "rank",
                aliases = {"ranks"},
                inGameOnly = true,
                permission: "ranks.use"
        )
    
        public void ranks(Execution execution, CommandSender sender, String[] args) {

        if (sender == null) {
            sender.sendMessage(new String[]{
                    "",
                    "§cOcorreu um erro, relogue no servidor.",
                    "",
            });
            
            Player p = (Player) sender;
            
            if (!(permission == "ranks.use")) {
            p.sendMessage(ConfigUtils.getString("messages.sem-perm", true));
            return;
        }

        List<ItemStack> items = new ArrayList<>();

        RanksPlugin.plugin.getConfig().getConfigurationSection("ranks").getKeys(false).forEach(r -> {

            String name = RanksPlugin.plugin.getConfig().getString("ranks." + r + ".name-in-inv").replace("&", "§");
            ItemStack item = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(name);

            int kills = RanksPlugin.plugin.getConfig().getInt("ranks." + r + ".kills");

            meta.setLore(Collections.singletonList("§7Mate §f" + kills + " §7jogador(es) para conquistar este rank."));
            item.setItemMeta(meta);

            items.add(item);
        });

        Scroller scroller = new Scroller.ScrollerBuilder()
                .withName(ConfigUtils.getString("inventory.title", true))
                .withItems(items)
                .withBackItem(36, player -> {})
                .withArrowsSlots(18, 26)
                .build();

        scroller.open(p);

        return false;
    }
}
