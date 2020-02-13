package top.phosky.mask.interfaces;

public interface CRUDInterface<T> {
    public int insert(T obj);

    public <K> int delete(K key);

    public <K> int update(K key, T obj);

    public <K> T selete(K key);
}
