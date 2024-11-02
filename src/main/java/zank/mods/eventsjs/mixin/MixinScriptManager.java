package zank.mods.eventsjs.mixin;

import dev.latvian.kubejs.script.ScriptManager;
import dev.latvian.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.eventsjs.SidedNativeEvents;

/**
 * @author ZZZank
 */
@Mixin(value = ScriptManager.class, remap = false)
public class MixinScriptManager {

    @Shadow
    @Final
    public ScriptType type;

    @Inject(method = "unload", at = @At("TAIL"))
    public void ejs$onUnload(CallbackInfo ci) {
        SidedNativeEvents.byType(this.type).unload();
    }
}
