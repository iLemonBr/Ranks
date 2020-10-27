package br.laion.ranks.plugin.Utils;

import br.laion.ranks.plugin.RanksPlugin;

public class ConfigUtils {

	public static String getString(String path, boolean color) {

		return (color) ? RanksPlugin.plugin.getConfig().getString(path).replace("&", "§")
				: RanksPlugin.plugin.getConfig().getString(path);
	}

}
