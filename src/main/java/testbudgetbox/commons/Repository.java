package testbudgetbox.commons;

import java.util.List;
import java.util.Map;


public interface Repository<A> {
	public A get(int id) throws Exception;
	public A add(A a) throws Exception;
	public boolean del(A a) throws Exception;
	public A update(A a) throws Exception;
	public List<A> getAll() throws Exception;
	public Map<Integer, A> getMap() throws Exception;
}
