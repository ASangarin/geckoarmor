package eu.asangarin.garmor;

import eu.asangarin.garmor.gecko.GeckoArmorController;
import eu.asangarin.garmor.gecko.GeckoArmorRenderer;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;

import java.util.HashMap;
import java.util.Map;

public class ArmorDatabase {
	private final Map<String, ArmorMaterials> armorMaterials = new HashMap<>();
	private final Map<String, GeckoArmorRenderer> armorRenderers = new HashMap<>();
	private final Map<String, GeckoArmorController> armorControllers = new HashMap<>();

	public GeckoArmorRenderer getRenderer(String id) {
		return armorRenderers.get(id);
	}

	public GeckoArmorController getController(String id) {
		return armorControllers.get(id);
	}

	public void clear() {
		armorMaterials.clear();
		armorRenderers.clear();
		armorControllers.clear();
	}

	public void add(CustomArmor armor) {
		ArmorMaterials armorMat = get(armor.getVanillaType());
		if (armorMat == null) {
			System.out.println("ERROR, unknown armor material: " + armor.getVanillaType());
			return;
		}

		String id = armor.getId();
		System.out.println("Added Armor: " + id);
		armorMaterials.put(id, armorMat);
		armorRenderers.put(id, new GeckoArmorRenderer(armor));
		armorControllers.put(id, new GeckoArmorController(id, armor));
	}

	private ArmorMaterials get(String type) {
		return switch (type.toLowerCase()) {
			case "leather" -> ArmorMaterials.LEATHER;
			case "chain" -> ArmorMaterials.CHAIN;
			case "iron" -> ArmorMaterials.IRON;
			case "gold" -> ArmorMaterials.GOLD;
			case "diamond" -> ArmorMaterials.DIAMOND;
			case "netherite" -> ArmorMaterials.NETHERITE;
			default -> null;
		};
	}

	public boolean test(String key, ArmorMaterial material) {
		return armorMaterials.containsKey(key) && armorMaterials.get(key) == material;
	}
}
