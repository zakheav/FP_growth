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
	private int freqThreshold = 2;
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
		Integer[] rawData = dataSource.get_nextData();
		while (rawData != null) {
			for (Integer e : rawData) {
				Integer old = dataFreq.get(e) == null ? 0 : dataFreq.get(e);
				dataFreq.put(e, old + 1);
			}
			rawData = dataSource.get_nextData();
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

	public Integer[] get_nextData() {
		Integer[] rawData = dataSource.get_nextData();
		if (rawData == null)
			return null;
		return clean_data(rawData);
	}
}
