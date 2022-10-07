package eu.asangarin.garmor.mixin;

import eu.asangarin.garmor.GeckoArmor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class GeckoArmorMixin extends FeatureRenderer<LivingEntity, BipedEntityModel<LivingEntity>> {
	private GeckoArmorMixin(FeatureRendererContext<LivingEntity, BipedEntityModel<LivingEntity>> context) {
		super(context);
	}

	@Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
	private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<LivingEntity> model, CallbackInfo ci) {
		ItemStack stack = entity.getEquippedStack(armorSlot);
		NbtCompound nbt = stack.getOrCreateNbt();
		if (stack.getItem() instanceof ArmorItem armorItem && nbt.contains("ariarmor")) {
			String id = nbt.getString("ariarmor");

			if (GeckoArmor.getDatabase().test(id, armorItem.getMaterial())) {
				try {
					GeckoArmor.getDatabase().getRenderer(id).render(matrices, vertexConsumers, stack, entity, armorSlot, light, getContextModel());
					ci.cancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}