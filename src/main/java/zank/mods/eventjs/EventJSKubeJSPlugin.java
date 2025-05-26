package zank.mods.eventjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import zank.mods.eventjs.wrapper.ClassConvertible;

/**
 * @author ZZZank
 */
public class EventJSKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("NativeEvents", SidedNativeEvents.byType(event.manager.scriptType));
    }
}
