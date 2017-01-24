package finalProjectCore;


public interface DAO <T> {
    boolean add(T t);
    boolean edit(T t);
    boolean remove(T t);

}