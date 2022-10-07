package eu.asangarin.garmor.gecko;

import eu.asangarin.garmor.CustomArmor;
import lombok.Getter;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GeckoArmorController implements IAnimatable {
	@Getter
	private final String id, modelPath, animationPath, texturePath, animationName;

	public GeckoArmorController(String id, CustomArmor armor) {
		this.id = id;
		this.modelPath = armor.getModel();
		this.animationPath = armor.getAnimation();
		this.texturePath = armor.getTexture();
		this.animationName = armor.getAdvanced().getAnimationName();
	}

	@Getter
	private final AnimationFactory factory = new AnimationFactory(this);

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
	}
}
