package dataInput;

import java.util.Map;

public interface DataSource {// 数据集接口，产生用于构造FPTree的原始数据
	public Map<String, Object> get_nextData();// 数据集的迭代器，迭代结束结束返回null(返回值key="data"对应的是事务，key="counter"对应事物出现的次数)
	public void refresh_dataSource();// 重置迭代器
	public void clear_cache();// 清空数据源中的缓存
}
