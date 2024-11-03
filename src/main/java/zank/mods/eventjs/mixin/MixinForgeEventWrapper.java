package zank.mods.eventjs.mixin;

import dev.latvian.mods.kubejs.forge.ForgeEventConsumer;
import dev.latvian.mods.kubejs.forge.ForgeEventWrapper;
import dev.latvian.mods.kubejs.forge.GenericForgeEventConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.eventjs.SidedNativeEvents;
import zank.mods.eventjs.wrapper.ClassConvertible;

/**
 * @author ZZZank
 */
@Mixin(value = ForgeEventWrapper.class, remap = false)
public abstract class MixinForgeEventWrapper {

    @Inject(method = "onEvent", at = @At("HEAD"), cancellable = true)
    public void ejs$replaceImpl(Object eventClass, ForgeEventConsumer consumer, CallbackInfoReturnable<Object> cir) {
        try {
            SidedNativeEvents.STARTUP.onEvent(ClassConvertible.of(eventClass), consumer::accept);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        cir.setReturnValue(null);
    }

    @Inject(method = "onGenericEvent", at = @At("HEAD"), cancellable = true)
    public void ejs$replaceGenericImpl(
        Object eventClass,
        Object genericClass,
        GenericForgeEventConsumer consumer,
        CallbackInfoReturnable<Object> cir
    ) {
        try {
            SidedNativeEvents.STARTUP.onGenericEvent(
                ClassConvertible.of(eventClass),
                ClassConvertible.of(genericClass),
                consumer::accept
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        cir.setReturnValue(null);
    }
}
