package br.laion.ranks.plugin.Object;

import com.google.gson.*;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfiguration {

	@Getter
	private String name;
	@Getter
	private File file;

	private FileReader fileReader;

	@Getter
	private JsonObject jsonObject;

	private Gson gson;

	public JsonConfiguration(String name, File file) throws IOException {
		this.name = name;
		this.file = file;

		this.gson = new GsonBuilder().setPrettyPrinting().create();

		if (!file.exists())
			if (!file.createNewFile())
				throw new IOException("Could not create file");

		fileReader = new FileReader(file);

		JsonElement jsonElement = new JsonParser().parse(fileReader);

		if (jsonElement instanceof JsonNull)
			jsonObject = new JsonObject();
		else
			jsonObject = jsonElement.getAsJsonObject();
	}

	public boolean contains(String key) {
		if (!key.contains("."))
			jsonObject.has(key);

		String[] path = key.split("\\.");
		JsonElement currentElement = jsonObject;

		for (String subPath : path) {
			if (!currentElement.isJsonObject())
				return false;
			if (!currentElement.getAsJsonObject().has(subPath))
				return false;

			currentElement = currentElement.getAsJsonObject().get(subPath);
		}

		return true;
	}

	public JsonElement get(String key) {
		if (!contains(key))
			return JsonNull.INSTANCE;
		if (!key.contains("."))
			jsonObject.get(key);

		String[] path = key.split("\\.");
		JsonElement currentElement = jsonObject;

		for (String subPath : path) {
			if (!currentElement.isJsonObject())
				return JsonNull.INSTANCE;
			if (!currentElement.getAsJsonObject().has(subPath))
				return JsonNull.INSTANCE;

			currentElement = currentElement.getAsJsonObject().get(subPath);
		}

		return currentElement;
	}

	public JsonConfiguration set(String key, Object value) {
		if (!key.contains(".")) {
			set(jsonObject, key, value);
			return this;
		}

		String[] path = key.split("\\.");
		JsonObject currentElement = jsonObject;

		for (int i = 0; i < path.length; i++) {
			String subPath = path[i];

			if (i == path.length - 1) {
				set(currentElement, subPath, value);
				break;
			}

			if (!currentElement.has(subPath)) {
				currentElement.add(subPath, new JsonObject());
				currentElement = currentElement.get(subPath).getAsJsonObject();
				continue;
			}

			currentElement = currentElement.get(subPath).getAsJsonObject();
		}

		return this;
	}

	private boolean set(JsonObject jsonObject, String key, Object value) {
		jsonObject.add(key, gson.toJsonTree(value));
		return true;
	}

	public void save() throws IOException {
		FileWriter fileWriter = new FileWriter(file);

		gson.toJson(jsonObject, fileWriter);

		fileWriter.flush();
		fileWriter.close();
	}

}
