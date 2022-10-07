package eu.asangarin.garmor.gecko;

import eu.asangarin.garmor.GeckoArmor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeckoArmorModel extends AnimatedGeoModel<GeckoArmorController> implements IAnimatableModel<GeckoArmorController> {
	@Override
	public Identifier getModelLocation(GeckoArmorController controller) {
		return GeckoArmor.i(controller.getModelPath());
	}

	@Override
	public Identifier getAnimationFileLocation(GeckoArmorController controller) {
		return GeckoArmor.i(controller.getAnimationPath());
	}

	@Override
	public Identifier getTextureLocation(GeckoArmorController controller) {
		return GeckoArmor.i(controller.getTexturePath());
	}
}
