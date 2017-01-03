package fp_growth;

import java.util.HashMap;
import java.util.Map;
import dataInput.DataSource;
import dataInput.OfferData;

public class FP_tree {
	public Map<Integer, TreeNode> headerList = new HashMap<Integer, TreeNode>();// nodeList的头节点表
	public Integer[] rank;// 每个元素出现次数从大到小的排序（原因是上面的headerList是map结构的，没办法保证次序）
	public TreeNode root;

	public FP_tree(DataSource dataSource) {
		OfferData od = new OfferData(dataSource);
		rank = od.get_dataFreqRank();

		root = new TreeNode(true, -1, null);
		Integer[] path = od.get_nextData();
		while (path != null) {
			add_pathToTree(path);
			path = od.get_nextData();
		}
	}

	private void add_pathToTree(Integer[] path) {
		TreeNode now = root;
		for (Integer e : path) {
			// 看是否需要新建一个节点
			if (!now.child.containsKey(e)) {// 需要新建节点
				TreeNode newNode = new TreeNode(false, e, now);
				now.child.put(e, newNode);
				if (headerList.containsKey(e)) {// 构造nodeList
					newNode.nodeLink = headerList.get(e);
				}
				headerList.put(e, newNode);
				now = newNode;
			} else {// 不需要新建节点
				now = now.child.get(e);
				++now.count;
			}
		}
	}
}
