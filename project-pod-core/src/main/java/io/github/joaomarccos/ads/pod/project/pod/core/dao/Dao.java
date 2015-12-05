package io.github.joaomarccos.ads.pod.project.pod.core.dao;

import java.util.List;

/**
 *
 * @author joaomarcos
 * @param <T>
 */
public interface Dao <T> {

    public boolean save(T professor);  

    public boolean update(T professor);

    public List<T> list();
}
