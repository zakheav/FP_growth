package dataInput;

import java.util.ArrayList;
import java.util.List;

public class DBDataSource implements DataSource {
	private List<Integer[]> cache = new ArrayList<Integer[]>();
	private int idx = 0;
	public DBDataSource() {
		Integer[] a1 = { 6, 1, 3, 4, 7, 9, 13, 16 };
		cache.add(a1);
		Integer[] a2 = { 1, 2, 3, 6, 12, 13, 15 };
		cache.add(a2);
		Integer[] a3 = { 2, 6, 8, 10, 15 };
		cache.add(a3);
		Integer[] a4 = { 2, 3, 11, 19, 16 };
		cache.add(a4);
		Integer[] a5 = { 1, 6, 3, 5, 12, 16, 13, 14 };
		cache.add(a5);
	}

	public Integer[] get_nextData() {
		if(idx == cache.size()) return null;
		return cache.get(idx++);
	}
	
	public void refresh_dataSource() {
		idx = 0;
	}
	
	public void clear_cache(){}
}
