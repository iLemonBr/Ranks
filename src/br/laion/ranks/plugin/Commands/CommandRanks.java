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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRanks implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if(!(p.hasPermission("ranks.use"))) {

            p.sendMessage(ConfigUtils.getString("messages.sem-perm", true));
            return false;
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
