package class121;

// LeetCode 1522. N叉树的直径
// 题目：给定一棵N叉树，你需要计算它的直径长度。
// 一棵N叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 两结点之间的路径长度是以它们之间边的数目表示。
// 来源：LeetCode
// 链接：https://leetcode.cn/problems/diameter-of-n-ary-tree/

import java.util.*;

// N叉树节点定义
class Node {
    public int val;
    public List<Node> children;
    
    public Node() {
        children = new ArrayList<Node>();
    }
    
    public Node(int _val) {
        val = _val;
        children = new ArrayList<Node>();
    }
    
    public Node(int _val, ArrayList<Node> _children) {
        val = _val;
        children = _children;
    }
}

public class LeetCode1522_DiameterOfNAryTree {
    
    // 全局变量，用于记录最大直径
    private int maxDiameter = 0;
    
    /**
     * 计算N叉树的直径
     * @param root N叉树根节点
     * @return 树的直径（边数）
     * 
     * 时间复杂度：O(n)，其中n是N叉树的节点数，每个节点只访问一次
     * 空间复杂度：O(h)，其中h是N叉树的高度，递归调用栈的深度
     */
    public int diameter(Node root) {
        maxDiameter = 0;  // 重置全局变量
        depth(root);      // 计算每个节点的深度并更新最大直径
        return maxDiameter;
    }
    
    /**
     * 计算以当前节点为根的子树深度，并更新最大直径
     * @param node 当前节点
     * @return 当前节点为根的子树的最大深度
     */
    private int depth(Node node) {
        // 基本情况：空节点深度为0
        if (node == null) {
            return 0;
        }
        
        // 特殊情况：没有子节点，深度为0
        if (node.children == null || node.children.isEmpty()) {
            return 0;
        }
        
        // 存储所有子节点的深度
        List<Integer> depths = new ArrayList<>();
        
        // 递归计算所有子节点的最大深度
        for (Node child : node.children) {
            int childDepth = depth(child);
            depths.add(childDepth);
        }
        
        // 对深度进行排序，找到最大的两个深度
        Collections.sort(depths, Collections.reverseOrder());
        
        // 计算经过当前节点的最长路径
        int pathThroughNode = 0;
        if (depths.size() >= 1) {
            pathThroughNode += depths.get(0);
        }
        if (depths.size() >= 2) {
            pathThroughNode += depths.get(1);
        }
        
        // 更新全局最大直径
        maxDiameter = Math.max(maxDiameter, pathThroughNode);
        
        // 返回以当前节点为根的子树的最大深度
        // 如果当前节点有子节点，最大深度是最大子节点深度 + 1
        // 如果当前节点没有子节点，深度为0
        return depths.isEmpty() ? 0 : depths.get(0) + 1;
    }
    
    /**
     * 使用优先队列优化的深度计算方法
     * @param node 当前节点
     * @return 当前节点为根的子树的最大深度
     */
    private int depthOptimized(Node node) {
        if (node == null) {
            return 0;
        }
        
        if (node.children == null || node.children.isEmpty()) {
            return 0;
        }
        
        // 使用优先队列（最大堆）来维护最大的两个深度
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        
        for (Node child : node.children) {
            int childDepth = depthOptimized(child);
            maxHeap.offer(childDepth);
        }
        
        // 取出最大的两个深度
        int max1 = maxHeap.isEmpty() ? 0 : maxHeap.poll();
        int max2 = maxHeap.isEmpty() ? 0 : maxHeap.poll();
        
        // 更新最大直径
        maxDiameter = Math.max(maxDiameter, max1 + max2);
        
        // 返回最大深度
        return max1 + 1;
    }
    
    /**
     * 迭代实现（避免递归深度过大）
     * @param root N叉树根节点
     * @return 树的直径
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int diameterIterative(Node root) {
        if (root == null) {
            return 0;
        }
        
        // 使用后序遍历计算每个节点的深度
        Map<Node, Integer> depthMap = new HashMap<>();
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        
        int maxDiameter = 0;
        
        while (!stack.isEmpty()) {
            Node node = stack.peek();
            
            // 如果所有子节点都已经处理过
            boolean allChildrenProcessed = true;
            
            for (Node child : node.children) {
                if (!depthMap.containsKey(child)) {
                    stack.push(child);
                    allChildrenProcessed = false;
                    break;
                }
            }
            
            if (allChildrenProcessed) {
                stack.pop();
                
                // 计算当前节点的深度
                List<Integer> childDepths = new ArrayList<>();
                for (Node child : node.children) {
                    childDepths.add(depthMap.get(child));
                }
                
                // 排序找到最大的两个深度
                Collections.sort(childDepths, Collections.reverseOrder());
                
                int max1 = childDepths.size() >= 1 ? childDepths.get(0) : 0;
                int max2 = childDepths.size() >= 2 ? childDepths.get(1) : 0;
                
                // 更新最大直径
                maxDiameter = Math.max(maxDiameter, max1 + max2);
                
                // 当前节点的深度 = 最大子节点深度 + 1
                depthMap.put(node, max1 + 1);
            }
        }
        
        return maxDiameter;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode1522_DiameterOfNAryTree solution = new LeetCode1522_DiameterOfNAryTree();
        
        // 测试用例1: 简单的三叉树
        //     1
        //   / | \
        //  2  3  4
        // 预期输出：2（路径 2-1-3 或 2-1-4 或 3-1-4）
        Node root1 = new Node(1);
        root1.children.add(new Node(2));
        root1.children.add(new Node(3));
        root1.children.add(new Node(4));
        
        System.out.println("测试用例1结果: " + solution.diameter(root1)); // 应该输出2
        System.out.println("测试用例1(优化)结果: " + solution.diameterOptimized(root1)); // 应该输出2
        System.out.println("测试用例1(迭代)结果: " + solution.diameterIterative(root1)); // 应该输出2
        
        // 测试用例2: 更复杂的N叉树
        //       1
        //     / | \
        //    2  3  4
        //   /|     |
        //  5 6     7
        // 预期输出：4（路径 5-2-1-4-7）
        Node root2 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        
        root2.children.add(node2);
        root2.children.add(node3);
        root2.children.add(node4);
        
        node2.children.add(new Node(5));
        node2.children.add(new Node(6));
        node4.children.add(new Node(7));
        
        System.out.println("测试用例2结果: " + solution.diameter(root2)); // 应该输出4
        System.out.println("测试用例2(优化)结果: " + solution.diameterOptimized(root2)); // 应该输出4
        System.out.println("测试用例2(迭代)结果: " + solution.diameterIterative(root2)); // 应该输出4
        
        // 测试用例3: 单节点树
        Node root3 = new Node(1);
        System.out.println("测试用例3结果: " + solution.diameter(root3)); // 应该输出0
        System.out.println("测试用例3(优化)结果: " + solution.diameterOptimized(root3)); // 应该输出0
        System.out.println("测试用例3(迭代)结果: " + solution.diameterIterative(root3)); // 应该输出0
        
        // 测试用例4: 空树
        System.out.println("测试用例4结果: " + solution.diameter(null)); // 应该输出0
        System.out.println("测试用例4(优化)结果: " + solution.diameterOptimized(null)); // 应该输出0
        System.out.println("测试用例4(迭代)结果: " + solution.diameterIterative(null)); // 应该输出0
    }
}