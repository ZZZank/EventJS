package zank.mods.eventjs.mixin;

import dev.latvian.kubejs.forge.BuiltinKubeJSForgePlugin;
import dev.latvian.kubejs.forge.KubeJSForgeEventHandlerWrapper;
import dev.latvian.kubejs.script.BindingsEvent;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.eventjs.wrapper.ClassConvertible;
import zank.mods.eventjs.SidedNativeEvents;

/**
 * @author ZZZank
 */
@Mixin(value = BuiltinKubeJSForgePlugin.class, remap = false)
public abstract class MixinKubeJSForgePlugin {

    @Inject(method = "onPlatformEvent", at = @At("HEAD"), cancellable = true)
    private static void ejs$replaceImpl(BindingsEvent event, Object[] args, CallbackInfoReturnable<Object> cir) {
        if (args.length < 2) {
            throw new RuntimeException("Invalid syntax! onPlatformEvent(string, function) required event class and handler");
        }

        try {
            val type = ClassConvertible.of(args[0]);
            val handler = (KubeJSForgeEventHandlerWrapper) args[1];
            SidedNativeEvents.STARTUP.onEvent(type, handler);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        cir.setReturnValue(null);
    }
}
