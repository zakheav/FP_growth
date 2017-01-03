package fp_growth;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	public int count;
	public Map<Integer, TreeNode> child = new HashMap<Integer, TreeNode>();
	public TreeNode parent;
	public int value;
	public boolean root;
	public TreeNode nodeLink;
	public TreeNode(boolean root, int value, TreeNode parent) {
		this.root = root;
		this.count = 1;
		this.value = value;
		this.parent = parent;
	}
}
