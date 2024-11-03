package zank.mods.eventjs.mixin;

import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.eventjs.SidedNativeEvents;

/**
 * @author ZZZank
 */
@Mixin(value = ScriptManager.class, remap = false)
public abstract class MixinScriptManager {

    @Shadow
    @Final
    public ScriptType scriptType;

    @Inject(method = "unload", at = @At("TAIL"))
    public void ejs$onUnload(CallbackInfo ci) {
        SidedNativeEvents.byType(this.scriptType).unload();
    }
}
