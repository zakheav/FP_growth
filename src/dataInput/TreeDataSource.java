package dataInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fp_growth.TreeNode;

public class TreeDataSource implements DataSource {

	private List<Integer[]> cache = new ArrayList<Integer[]>();
	private List<Integer> counterList = new ArrayList<Integer>();// 记录每条记录的出现次数
	private int idx = 0;

	public TreeDataSource(List<TreeNode> endList) {// 根据FP_tree中的一部分构建数据集
		for (TreeNode endNode : endList) {// 遍历每个分支的最后一个节点
			int counter = endNode.count;// 表示这个分支的上面的部分需要在cache中出现的次数
			
			TreeNode nowNode = endNode.parent;
			List<Integer> temp = new ArrayList<Integer>();
			while (!nowNode.root) {// 沿着parent指针向上找，直到到达root
				temp.add(nowNode.value);
				nowNode = nowNode.parent;
			}
			if (!temp.isEmpty()) {
				Integer[] data = new Integer[temp.size()];
				int idx = 0;
				for (Integer e : temp) {
					data[idx++] = e;
				}
				cache.add(data);
				counterList.add(counter);
			}

		}
	}

	@Override
	public Map<String, Object> get_nextData() {
		if (idx == cache.size())
			return null;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", cache.get(idx));
		result.put("counter", counterList.get(idx));
		++idx;
		return result;
	}

	@Override
	public void refresh_dataSource() {
		idx = 0;
	}

	public void clear_cache() {// 在FPTree构建完后，应当调用这个函数
		this.cache = null;
	}
}
