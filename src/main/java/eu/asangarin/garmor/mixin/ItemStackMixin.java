package eu.asangarin.garmor.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean hasNbt();

	@Shadow
	@Nullable
	public abstract NbtCompound getNbt();

	@Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
	private void injected(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
		if (context.isAdvanced() && player.getAbilities().creativeMode) {
			//noinspection ConstantConditions
			if (this.hasNbt() && this.getNbt().contains("ariarmor")) {
				List<Text> list = cir.getReturnValue();
				String id = this.getNbt().getString("ariarmor");
				list.add((new LiteralText("Custom Armor ID: " + id).formatted(Formatting.DARK_GRAY)));
				cir.setReturnValue(list);
			}
		}
	}
}
