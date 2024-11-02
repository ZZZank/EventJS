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
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(ClassConvertible.class, ClassConvertible::of);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("NativeEvent", SidedNativeEvents.byType(event.type));
    }
}
