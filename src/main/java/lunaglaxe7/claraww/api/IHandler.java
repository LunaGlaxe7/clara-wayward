package lunaglaxe7.claraww.api;

public interface IHandler<T extends ModEvent> {

    void handle(T event);
}
