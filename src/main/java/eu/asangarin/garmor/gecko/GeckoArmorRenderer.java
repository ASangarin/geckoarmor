package eu.asangarin.garmor.gecko;

import eu.asangarin.garmor.CustomArmor;
import eu.asangarin.garmor.GeckoArmor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.compat.PatchouliCompat;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.GeoUtils;

import java.util.Arrays;
import java.util.Objects;

public class GeckoArmorRenderer implements IGeoRenderer<ArmorItem> {
	static {
		AnimationController.addModelFetcher((AnimationController.ModelFetcher<GeckoArmorController>) ((IAnimatable object) -> {
			if (object instanceof GeckoArmorController controller)
				return GeckoArmor.getDatabase().getRenderer(controller.getId()).getGeoModelProvider();
			return null;
		}));
	}

	public GeckoArmorRenderer(CustomArmor armor) {
		CustomArmor.AdvancedArmorSettings advanced = armor.getAdvanced();

		headBone = advanced.getHeadBone();
		bodyBone = advanced.getBodyBone();
		rightArmBone = advanced.getRightArmBone();
		leftArmBone = advanced.getLeftArmBone();
		rightLegBone = advanced.getRightLegBone();
		leftLegBone = advanced.getLeftLegBone();
		rightBootBone = advanced.getRightBootBone();
		leftBootBone = advanced.getLeftBootBone();

		modelProvider = new GeckoArmorModel();
	}

	private final GeckoArmorModel modelProvider;

	// Set these to the names of your armor's bones, or null if you aren't using
	// them
	public String headBone;
	public String bodyBone;
	public String rightArmBone;
	public String leftArmBone;
	public String rightLegBone;
	public String leftLegBone;
	public String rightBootBone;
	public String leftBootBone;

	protected ArmorItem currentArmorItem;
	protected LivingEntity entityLiving;
	protected ItemStack itemStack;
	protected EquipmentSlot armorSlot;
	protected BipedEntityModel<LivingEntity> baseModel;

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		setCurrentItem(entity, stack, slot, contextModel);
		this.render(matrices, vertexConsumers, light);
	}

	public void setCurrentItem(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<LivingEntity> model) {
		this.entityLiving = entityLiving;
		this.itemStack = itemStack;
		this.armorSlot = armorSlot;
		this.currentArmorItem = (ArmorItem) itemStack.getItem();
		this.baseModel = model;
	}

	/*public void render(float partialTicks, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn) {
		stack.translate(0.0D, 1.497F, 0.0D);
		stack.scale(-1.005F, -1.0F, 1.005F);

		IAnimatable animatable = GeckoArmor.GARMOR_ITEM.find(itemStack, null);

		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));

		AnimationEvent<IAnimatable> itemEvent = new AnimationEvent<>(animatable, 0, 0, MinecraftClient.getInstance().getTickDelta(), false,
				Arrays.asList(this.itemStack, this.entityLiving, this.armorSlot));
		modelProvider.setLivingAnimations(animatable, this.getUniqueID(this.currentArmorItem), itemEvent);

		this.fitToBiped();
		this.applySlot(armorSlot);
		stack.push();
		MinecraftClient.getInstance().getTextureManager().bindTexture(getTextureLocation(currentArmorItem));
		Color renderColor = getRenderColor(currentArmorItem, partialTicks, stack, null, bufferIn, packedLightIn);
		RenderLayer renderType = getRenderType(currentArmorItem, partialTicks, stack, null, bufferIn, packedLightIn,
				getTextureLocation(currentArmorItem));
		render(model, currentArmorItem, partialTicks, renderType, stack, null, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV,
				(float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f, (float) renderColor.getBlue() / 255f,
				(float) renderColor.getAlpha() / 255);

		if (FabricLoader.getInstance().isModLoaded("patchouli")) {
			PatchouliCompat.patchouliLoaded(stack);
		}
		stack.pop();
		stack.scale(-1.005F, -1.0F, 1.005F);
		stack.translate(0.0D, -1.497F, 0.0D);
	}*/

	public void applySlot(EquipmentSlot boneSlot) {
		getModel(getController(itemStack));

		IBone headBone = this.getAndHideBone(this.headBone);
		IBone bodyBone = this.getAndHideBone(this.bodyBone);
		IBone rightArmBone = this.getAndHideBone(this.rightArmBone);
		IBone leftArmBone = this.getAndHideBone(this.leftArmBone);
		IBone rightLegBone = this.getAndHideBone(this.rightLegBone);
		IBone leftLegBone = this.getAndHideBone(this.leftLegBone);
		IBone rightBootBone = this.getAndHideBone(this.rightBootBone);
		IBone leftBootBone = this.getAndHideBone(this.leftBootBone);

		switch (boneSlot) {
			case HEAD:
				if (headBone != null) headBone.setHidden(false);
				break;
			case CHEST:
				if (bodyBone != null) bodyBone.setHidden(false);
				if (rightArmBone != null) rightArmBone.setHidden(false);
				if (leftArmBone != null) leftArmBone.setHidden(false);
				break;
			case LEGS:
				if (rightLegBone != null) rightLegBone.setHidden(false);
				if (leftLegBone != null) leftLegBone.setHidden(false);
				break;
			case FEET:
				if (rightBootBone != null) rightBootBone.setHidden(false);
				if (leftBootBone != null) leftBootBone.setHidden(false);
				break;
		}
	}

	protected IBone getAndHideBone(String boneName) {
		if (boneName != null) {
			final IBone bone = this.modelProvider.getBone(boneName);
			bone.setHidden(true);
			return bone;
		}
		return null;
	}

	@Override
	public Integer getUniqueID(ArmorItem animatable) {
		//noinspection ConstantConditions
		return Objects.hash(this.armorSlot, itemStack.getItem(), itemStack.getCount(), itemStack.hasNbt() ? itemStack.getNbt().toString() : 1,
				this.entityLiving.getUuid().toString());
	}

	public void render(MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
		stack.translate(0.0D, 1.497F, 0.0D);
		stack.scale(-1.005F, -1.0F, 1.005F);

		GeckoArmorController animatable = getController(itemStack);
		GeoModel model = getModel(animatable);

		AnimationEvent<IAnimatable> itemEvent = new AnimationEvent<>(animatable, 0, 0, MinecraftClient.getInstance().getTickDelta(), false,
				Arrays.asList(this.itemStack, this.entityLiving, this.armorSlot));
		modelProvider.setLivingAnimations(animatable, this.getUniqueID(this.currentArmorItem), itemEvent);

		this.fitToBiped();
		this.applySlot(armorSlot);
		stack.push();
		MinecraftClient.getInstance().getTextureManager().bindTexture(getTextureLocation(currentArmorItem));
		Color renderColor = getRenderColor(currentArmorItem, 0, stack, bufferIn, null, packedLightIn);
		RenderLayer renderType = getRenderType(currentArmorItem, 0, stack, bufferIn, null, packedLightIn, getTextureLocation(currentArmorItem));
		render(model, currentArmorItem, 0, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.DEFAULT_UV,
				(float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f, (float) renderColor.getBlue() / 255f,
				(float) renderColor.getAlpha() / 255);

		if (FabricLoader.getInstance().isModLoaded("patchouli")) {
			PatchouliCompat.patchouliLoaded(stack);
		}
		stack.pop();
		stack.scale(-1.005F, -1.0F, 1.005F);
		stack.translate(0.0D, -1.497F, 0.0D);
	}

	private GeoModel getModel(GeckoArmorController controller) {
		return modelProvider.getModel(modelProvider.getModelLocation(controller));
	}

	private void fitToBiped() {
		if (!(this.entityLiving instanceof ArmorStandEntity)) {
			if (this.headBone != null) {
				IBone headBone = this.modelProvider.getBone(this.headBone);
				GeoUtils.copyRotations(baseModel.head, headBone);
				headBone.setPositionX(baseModel.head.pivotX);
				headBone.setPositionY(-baseModel.head.pivotY);
				headBone.setPositionZ(baseModel.head.pivotZ);
			}

			if (this.bodyBone != null) {
				IBone bodyBone = this.modelProvider.getBone(this.bodyBone);
				GeoUtils.copyRotations(baseModel.body, bodyBone);
				bodyBone.setPositionX(baseModel.body.pivotX);
				bodyBone.setPositionY(-baseModel.body.pivotY);
				bodyBone.setPositionZ(baseModel.body.pivotZ);
			}
			if (this.rightArmBone != null) {
				IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);
				GeoUtils.copyRotations(baseModel.rightArm, rightArmBone);
				rightArmBone.setPositionX(baseModel.rightArm.pivotX + 5);
				rightArmBone.setPositionY(2 - baseModel.rightArm.pivotY);
				rightArmBone.setPositionZ(baseModel.rightArm.pivotZ);
			}

			if (this.leftArmBone != null) {
				IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);
				GeoUtils.copyRotations(baseModel.leftArm, leftArmBone);
				leftArmBone.setPositionX(baseModel.leftArm.pivotX - 5);
				leftArmBone.setPositionY(2 - baseModel.leftArm.pivotY);
				leftArmBone.setPositionZ(baseModel.leftArm.pivotZ);
			}
			if (this.rightLegBone != null) {
				IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);
				GeoUtils.copyRotations(baseModel.rightLeg, rightLegBone);
				rightLegBone.setPositionX(baseModel.rightLeg.pivotX + 2);
				rightLegBone.setPositionY(12 - baseModel.rightLeg.pivotY);
				rightLegBone.setPositionZ(baseModel.rightLeg.pivotZ);
				if (this.rightBootBone != null) {
					IBone rightBootBone = this.modelProvider.getBone(this.rightBootBone);
					GeoUtils.copyRotations(baseModel.rightLeg, rightBootBone);
					rightBootBone.setPositionX(baseModel.rightLeg.pivotX + 2);
					rightBootBone.setPositionY(12 - baseModel.rightLeg.pivotY);
					rightBootBone.setPositionZ(baseModel.rightLeg.pivotZ);
				}
			}
			if (this.leftLegBone != null) {
				IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);
				GeoUtils.copyRotations(baseModel.leftLeg, leftLegBone);
				leftLegBone.setPositionX(baseModel.leftLeg.pivotX - 2);
				leftLegBone.setPositionY(12 - baseModel.leftLeg.pivotY);
				leftLegBone.setPositionZ(baseModel.leftLeg.pivotZ);
				if (this.leftBootBone != null) {
					IBone leftBootBone = this.modelProvider.getBone(this.leftBootBone);
					GeoUtils.copyRotations(baseModel.leftLeg, leftBootBone);
					leftBootBone.setPositionX(baseModel.leftLeg.pivotX - 2);
					leftBootBone.setPositionY(12 - baseModel.leftLeg.pivotY);
					leftBootBone.setPositionZ(baseModel.leftLeg.pivotZ);
				}
			}
		}
	}

	@Override
	public void render(GeoModel model, ArmorItem animatable, float partialTicks, RenderLayer type, MatrixStack matrixStackIn, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		IGeoRenderer.super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn,
				packedOverlayIn, red, green, blue, alpha);
	}

	protected VertexConsumerProvider rtb = null;

	@Override
	public void setCurrentRTB(VertexConsumerProvider rtb) {
		this.rtb = rtb;
	}

	@Override
	public VertexConsumerProvider getCurrentRTB() {
		return this.rtb;
	}

	@Override
	public GeckoArmorModel getGeoModelProvider() {
		return modelProvider;
	}

	@Override
	public Identifier getTextureLocation(ArmorItem instance) {
		return this.modelProvider.getTextureLocation(getController(itemStack));
	}

	private GeckoArmorController getController(ItemStack stack) {
		return GeckoArmor.getDatabase().getController(stack.getOrCreateNbt().getString("ariarmor"));
	}
}
