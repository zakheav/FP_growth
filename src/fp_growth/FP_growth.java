package fp_growth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dataInput.DBDataSource;
import dataInput.DataSource;
import dataInput.TreeDataSource;

public class FP_growth {
	private final FP_tree FPTree;
	private final int setSize;// 频繁集的大小上线
	public FP_growth() {
		this.FPTree = new FP_tree(new DBDataSource());
		this.setSize = 2;
	}

	@SuppressWarnings("unchecked")
	// 返回的set中每个元素是一个map，每个map包含了一个频繁集"data"以及这个频繁集出现的次数"counter"
	private Set<Map<String, Object>> find_freqSet(FP_tree fp_tree) {
		Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
		// 遍历FPTree中的rank数组，从后往前遍历
		for (int i = fp_tree.rank.length - 1; i >= 0; --i) {
			Integer end = fp_tree.rank[i];
			// 得到以end元素结尾的FPTree分支
			List<TreeNode> endList = new ArrayList<TreeNode>();
			int endCounter = 0;// end元素在fp_tree中出现的次数
			TreeNode now = fp_tree.headerList.get(end);
			while (now != null) {
				endList.add(now);
				endCounter += now.count;
				now = now.nodeLink;
			}
			DataSource dataSource = new TreeDataSource(endList);
			FP_tree child_fptree = new FP_tree(dataSource);
			dataSource.clear_cache();// 释放内存（必要）

			Set<Map<String, Object>> child_freqSetData = new HashSet<Map<String, Object>>();
			if (!child_fptree.root.children.isEmpty()) {// 不是空的树
				child_freqSetData = find_freqSet(child_fptree);
			}
			for(Map<String, Object> dataMap : child_freqSetData) {// 把每个频繁集加上end这个后缀
				((List<Integer>)dataMap.get("data")).add(end);
				if(((List<Integer>)dataMap.get("data")).size() <= setSize)// 只储存大小在setSize之内的频繁集
					result.add(dataMap);
			}
			// 把end这个后缀单独组成的频繁集加入到result中
			Map<String, Object> endFreqData = new HashMap<String, Object>();
			List<Integer> endFreqSet = new ArrayList<Integer>();
			endFreqSet.add(end);
			endFreqData.put("data", endFreqSet);
			endFreqData.put("counter", endCounter);
			result.add(endFreqData);
		}
		return result;
	}

	public Set<Map<String, Object>> start() {
		return find_freqSet(FPTree);
	}

	public static void main(String[] args) {
		FP_growth fpg = new FP_growth();
		Set<Map<String, Object>> result = fpg.start();
		for (Map<String, Object> freqData : result) {
			System.out.println();
			@SuppressWarnings("unchecked")
			List<Integer> freqSet = (List<Integer>) freqData.get("data");
			for (Integer e : freqSet) {
				System.out.print(e + ", ");
			}
			System.out.print(": " + freqData.get("counter"));
		}
	}
}
