package fp_growth;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	public int count;
	public Map<Integer, TreeNode> children = new HashMap<Integer, TreeNode>();
	public TreeNode parent;
	public int value;
	public boolean root;
	public TreeNode nodeLink;
	public TreeNode(boolean root, int value, int count, TreeNode parent) {
		this.root = root;
		this.count = count;
		this.value = value;
		this.parent = parent;
	}
}
