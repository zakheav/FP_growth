package dataInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class DataStruct {
	public int value;
	public int counter;

	public DataStruct(int value, int counter) {
		this.value = value;
		this.counter = counter;
	}
}

public class OfferData {// 从DataSource中提取数据，并且清洗，然后通过get_nextData提供给FPTree用于构造
	private int freqThreshold = 2;// 设置频繁集的出现次数下限
	private Map<Integer, Integer> dataFreq = new HashMap<Integer, Integer>();// 存储元素出现的次数。key是元素，value是元素出现的次数
	private DataSource dataSource;

	public void set_dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public OfferData(DataSource dataSource) {
		set_dataSource(dataSource);
		countFreq();// 生成每个元素的频数统计
		dataSource.refresh_dataSource();
	}

	public Integer[] get_dataFreqRank() {// 把dataFreq数据按照出现次数从大到小排序
		List<DataStruct> temp = new ArrayList<DataStruct>();
		for (Integer e : dataFreq.keySet()) {
			temp.add(new DataStruct(e, dataFreq.get(e)));
		}
		Collections.sort(temp, new MyComparator());
		Integer[] result = new Integer[temp.size()];
		int idx = 0;
		for (DataStruct d : temp) {
			result[idx] = d.value;
			++idx;
		}
		return result;
	}

	private void countFreq() {
		Map<String, Object> raw = dataSource.get_nextData();
		
		while (raw != null) {
			Integer[] rawData = (Integer[]) raw.get("data");
			Integer dataCounter = (Integer) raw.get("counter");
			
			for (Integer e : rawData) {
				Integer old = dataFreq.get(e) == null ? 0 : dataFreq.get(e);
				dataFreq.put(e, old + dataCounter);
			}
			
			raw = dataSource.get_nextData();
		}
		Iterator<Entry<Integer, Integer>> it = dataFreq.entrySet().iterator();
		while (it.hasNext()) {// 清除掉出现频率小于阈值的元素
			Map.Entry<Integer, Integer> entry = it.next();
			if (entry.getValue() <= this.freqThreshold) {
				it.remove();
			}
		}
	}

	class MyComparator implements Comparator<DataStruct> {
		public int compare(DataStruct a, DataStruct b) {
			if (a.counter > b.counter) {
				return -1;
			} else if (a.counter < b.counter) {
				return 1;
			} else {
				return a.value - b.value;
			}
		}
	}

	private Integer[] clean_data(Integer[] rawData) {// 对原始数据进行清洗
		List<DataStruct> temp = new ArrayList<DataStruct>();
		for (int e : rawData) {
			if (dataFreq.containsKey(e)) {
				temp.add(new DataStruct(e, dataFreq.get(e)));
			}
		}
		Collections.sort(temp, new MyComparator());
		Integer[] data = new Integer[temp.size()];
		int idx = 0;
		for (DataStruct d : temp) {
			data[idx++] = d.value;
		}
		return data;
	}

	public Map<String, Object> get_nextData() {// 返回值key="data"对应的是清洗后的事务，key="counter"对应事物出现的次数
		Map<String, Object> raw = dataSource.get_nextData();
		if (raw == null)
			return null;
		Integer[] rawData = (Integer[]) raw.get("data");
		Integer counter = (Integer) raw.get("counter");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", clean_data(rawData));
		result.put("counter", counter);
		return result;
	}
}
