package zank.mods.eventsjs.wrapper;

import dev.latvian.mods.rhino.NativeJavaClass;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public interface ClassConvertible extends Supplier<Class<?>> {

    static void youDontNeedToLoadThisClassSinceIProvidedTypeWrappersForYou() {}

    static ClassConvertible fromRaw(Class<?> raw) {
        return () -> raw;
    }

    static ClassConvertible fromJS(NativeJavaClass njc) {
        return njc::getClassObject;
    }

    static ClassConvertible fromName(String className) {
        return () -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static ClassConvertible of(Object o) {
        if (o instanceof ClassConvertible) {
            return (ClassConvertible) o;
        } else if (o instanceof CharSequence) {
            return fromName(o.toString());
        } else if (o instanceof Class) {
            return fromRaw((Class) o);
        } else if (o instanceof NativeJavaClass) {
            return fromJS((NativeJavaClass) o);
        }
        return null;
    }
}
