# EventJS

is a KubeJS addon that adds reloadable native events for KubeJS.

With EventJS, you can

*   refresh your native event listeners without restarting the whole game or store the handler within a `global['someId']`, which is both simpler and more performant.
*   add/remove native event listeners as you like without relaunching, which was impossible in KubeJS for 1.16-1.20
*   prevent your game from crashing if something goes wrong in your native event listener
*   add event listener only in server/client side

## Usage

By default, EventJS will replace the original native event listening method to make it reloadable. So you actually don't need to learn anything new in order to use EventJS, just install it and you Forge event listening is reloadable now.

But, if you're looking for some more dynamic and well-defined event listending, you can use `NativeEvents.onEvent(...)`, and `NativeEvents.onGenericEvent(...)` for generic event listening.

`NativeEvents` will be avaliable for all 3 script types (client/server/startup), so you can perform sided event listening, that is, for example, listen to client-only event in `client_scripts`, then this event listening will not happen on server side.

### Syntax (v1.0.0~1.3.0)

```js
NativeEvents.onEvent(eventType, handler)
NativeEvents.onEvent(priority, receiveCancelled, eventType, handler)
NativeEvents.onGenericEvent(genericClassFilter, eventType, handler)
NativeEvents.onGenericEvent(genericClassFilter, priority, receiveCancelled, eventType, handler)
```

The `handler` in JS side, is a callback that receives the event, for example: `(event) => {...}`

The `eventType` and `genericClassType` is something that can represent a class, like a string that holds the event class name, or the event class itself loaded by `java(...)` or `Java.loadClass(...)`.

```typescript
let handler: (event: AnyForgeEvent) => void
let eventType: string | Class
let genericClassFilter: string | Class
let priority: $EventPriority | "highest" | "high" | "normal" | "low" | "lowest"
let receiveCancelled: boolean
```

### Changes in 1.4.0

In EventJS 1.4.0, in order to support [ProbeJS Legacy](https://www.curseforge.com/minecraft/mc-mods/probejs-legacy) out of the box, the `eventType` and `genericClassFilter` will only actively support `Class` loaded via `java(...)` (1.16.5) or `Java.loadClass(...)` (1.20.1).

```typescript
let eventType: Class
let genericClassFilter: Class
```

## Supported Version

1.0.0: 1.16.5

1.1.0: 1.16.5, 1.20.1

1.2.0: all major version between 1.16 and 1.20

## Do You Know That

The reason why I make this mod is that KubeJS is absurdly slow in implementing similar features. They added native event listener reloading only after KubeJS updated to 1.21, leaving all versions between 1.16~1.20 behind.

KubeJS for 1.21+ also contains support for native event listener reloading, using the same name `NativeEvents`, but its implementation is not the same as EventJS, so please dont make assumptions when interacting EventJS internals via Java.
