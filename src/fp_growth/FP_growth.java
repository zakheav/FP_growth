package fp_growth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataInput.DBDataSource;
import dataInput.DataSource;
import dataInput.TreeDataSource;

public class FP_growth {
	private FP_tree FPTree;

	public FP_growth() {
		FPTree = new FP_tree(new DBDataSource());
	}

	private Set<List<Integer>> find_freqSet(FP_tree fp_tree) {
		Set<List<Integer>> freqSetList = new HashSet<List<Integer>>();// 频繁集
		// 遍历FPTree中的rank数组，从后往前遍历
		for (int i = fp_tree.rank.length - 1; i >= 0; --i) {
			int end = fp_tree.rank[i];
			// 得到以end元素结尾的FPTree分支
			List<TreeNode> endList = new ArrayList<TreeNode>();
			TreeNode now = fp_tree.headerList.get(end);
			while (now != null) {
				endList.add(now);
				now = now.nodeLink;
			}
			DataSource dataSource = new TreeDataSource(endList);
			FP_tree child_fptree = new FP_tree(dataSource);
			dataSource.clear_cache();// 释放内存（必要）
			
			Set<List<Integer>> child_freqSetList = new HashSet<List<Integer>>();
			if (child_fptree.root.child.isEmpty()) {// 是空的树
				List<Integer> set = new ArrayList<Integer>();
				child_freqSetList.add(set);
			} else {
				child_freqSetList = find_freqSet(child_fptree);
			}
			// 获取以rank[i]结尾的频繁集，加入到freqSet中去
			for(List<Integer> set : child_freqSetList) {
				set.add(end);
				freqSetList.add(set);
			}
		}
		return freqSetList;
	}

	public Set<List<Integer>> start() {
		return find_freqSet(FPTree);
	}
	
	public static void main(String[] args) {
		FP_growth fpg = new FP_growth();
		Set<List<Integer>> result = fpg.start();
		for(List<Integer> freqSet : result) {
			System.out.println();
			for(Integer e : freqSet) {
				System.out.print(e+", ");
			}
		}
	}
}
