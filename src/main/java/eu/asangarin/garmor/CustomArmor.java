package eu.asangarin.garmor;

import lombok.Getter;

@SuppressWarnings("unused")
public class CustomArmor {
	@Getter
	private String id, vanillaType, model, animation, texture;
	private AdvancedArmorSettings advanced;

	public AdvancedArmorSettings getAdvanced() {
		if(advanced == null) return advanced = new AdvancedArmorSettings();
		return advanced;
	}

	@SuppressWarnings("FieldMayBeFinal")
	@Getter
	public static class AdvancedArmorSettings {
		private String animationName = "animation.armor.idle", headBone = "helmet", bodyBone = "chestplate", rightArmBone = "rightArm", leftArmBone = "leftArm", rightLegBone = "rightLeg", leftLegBone = "leftLeg", rightBootBone = "rightBoot", leftBootBone = "leftBoot";
	}
}
