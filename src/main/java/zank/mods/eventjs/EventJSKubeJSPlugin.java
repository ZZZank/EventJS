package zank.mods.eventjs;

import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import zank.mods.eventjs.wrapper.ClassConvertible;

/**
 * @author ZZZank
 */
public class EventJSKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("NativeEvents", SidedNativeEvents.byType(event.type));
    }
}
