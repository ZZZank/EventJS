package zank.mods.eventjs;

import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.script.BindingsEvent;

/**
 * @author ZZZank
 */
public class EventJSKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("NativeEvents", SidedNativeEvents.byType(event.type));
    }
}
