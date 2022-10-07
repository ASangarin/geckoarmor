package eu.asangarin.garmor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class GeckoArmor implements ClientModInitializer {
	private static final ArmorDatabase DATABASE = new ArmorDatabase();
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return GeckoArmor.i("armor");
			}

			@Override
			public void reload(ResourceManager manager) {
				DATABASE.clear();

				manager.findResources("armor", path -> path.endsWith(".json")).forEach(id -> {
					try (InputStream stream = manager.getResource(id).getInputStream()) {
						Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
						DATABASE.add(GSON.fromJson(reader, CustomArmor.class));
						reader.close();
					} catch (Exception e) {
						System.out.println("Error occurred while loading wallet icons (" + e.getClass().getName() + ")");
						e.printStackTrace();
					}
				});
			}
		});
	}

	public static Identifier i(String path) {
		return new Identifier("ariarmor", path);
	}

	public static ArmorDatabase getDatabase() {
		return DATABASE;
	}
}
