package zank.mods.eventjs.mixin;

import dev.latvian.kubejs.command.KubeJSCommands;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.eventjs.SidedNativeEvents;

/**
 * @author ZZZank
 */
@Mixin(value = KubeJSCommands.class, remap = false)
public abstract class MixinKubeJSCommands {

    @Inject(method = "reloadStartup", at = @At("RETURN"))
    private static void ejs$startupReloadMessage(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        ejs$sendByType(source, SidedNativeEvents.STARTUP);
    }

    @Inject(method = "reloadServer", at = @At("RETURN"))
    private static void ejs$serverReloadMessage(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        ejs$sendByType(source, SidedNativeEvents.SERVER);
    }

    @Inject(method = "reloadClient", at = @At("RETURN"))
    private static void ejs$clientReloadMessage(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        ejs$sendByType(source, SidedNativeEvents.CLIENT);
    }

    @Unique
    private static void ejs$sendByType(CommandSourceStack source, SidedNativeEvents events) {
        source.sendSuccess(
            new TranslatableComponent(
                "EventJS refreshed native event listening for %s, %s handler(s) in total",
                events.type,
                events.getHandlerCount()
            )
                .withStyle(ChatFormatting.GREEN),
            false
        );
    }
}
