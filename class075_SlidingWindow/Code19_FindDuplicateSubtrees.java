import java.util.*;

/**
 * LeetCode 652. 寻找重复的子树 (Find Duplicate Subtrees)
 * 
 * 题目描述:
 * 给定一棵二叉树，返回所有重复的子树。
 * 对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。
 * 两棵树重复是指它们具有相同的结构以及相同的结点值。
 * 
 * 示例1:
 *         1
 *        / \
 *       2   3
 *      /   / \
 *     4   2   4
 *        /
 *       4
 * 
 * 输出:
 * [[2,4],[4]]
 * 
 * 解释:
 * 上面的二叉树有两个重复子树。
 * 第一个重复子树是4，如蓝色节点所示。
 * 第二个重复子树是2 -> 4，如橙色节点所示。
 * 
 * 题目链接: https://leetcode.com/problems/find-duplicate-subtrees/
 * 
 * 解题思路:
 * 这道题需要我们找出二叉树中所有重复的子树。解决这个问题的关键在于能够唯一地表示每个子树，并能够快速判断是否已经存在相同的子树。
 * 
 * 解法: 递归 + 哈希表
 * 1. 对于每个子树，我们需要生成一个唯一标识符，可以通过序列化的方式实现
 * 2. 使用哈希表来记录每个子树标识符出现的次数
 * 3. 当一个子树标识符出现次数为2时，将该子树的根节点加入结果列表
 * 
 * 时间复杂度: O(n^2)，其中 n 是树中的节点数。在最坏情况下，序列化每个节点需要O(n)时间，共有n个节点。
 * 空间复杂度: O(n^2)，存储所有子树的序列化表示。
 * 
 * 优化版本：使用ID来代替完整序列化字符串，可以将时间和空间复杂度优化到O(n)。
 */

// 定义二叉树节点
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class Code19_FindDuplicateSubtrees {
    
    /**
     * 解法一: 使用序列化 + 哈希表
     * 将每个子树序列化为字符串，然后使用哈希表记录每个子树出现的次数。
     */
    public static List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 哈希表用于记录每个子树序列化字符串出现的次数
        Map<String, Integer> subtreeCount = new HashMap<>();
        
        // 递归函数，用于序列化子树并检查重复
        serializeAndCheckDuplicates(root, subtreeCount, result);
        
        return result;
    }
    
    /**
     * 递归序列化子树并检查重复
     * 
     * @param node 当前节点
     * @param subtreeCount 子树计数哈希表
     * @param result 结果列表
     * @return 当前子树的序列化字符串
     */
    private static String serializeAndCheckDuplicates(TreeNode node, Map<String, Integer> subtreeCount, List<TreeNode> result) {
        if (node == null) {
            return "#"; // 用#表示空节点
        }
        
        // 序列化当前节点的左子树、当前节点的值、右子树
        String key = node.val + "," + 
                     serializeAndCheckDuplicates(node.left, subtreeCount, result) + "," + 
                     serializeAndCheckDuplicates(node.right, subtreeCount, result);
        
        // 获取当前子树出现的次数，如果是第二次出现，则添加到结果中
        subtreeCount.put(key, subtreeCount.getOrDefault(key, 0) + 1);
        if (subtreeCount.get(key) == 2) {
            result.add(node); // 只有当子树出现次数为2时添加，避免重复添加
        }
        
        return key;
    }
    
    /**
     * 解法二: 使用ID代替完整序列化字符串（优化版本）
     * 为每个不同的子树分配一个唯一ID，使用ID来标识子树而不是完整的序列化字符串。
     */
    public static List<TreeNode> findDuplicateSubtreesOptimized(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 哈希表用于将子树的序列化字符串映射到唯一ID
        Map<String, Integer> subtreeId = new HashMap<>();
        // 哈希表用于记录每个ID（子树）出现的次数
        Map<Integer, Integer> idCount = new HashMap<>();
        // 当前可用的下一个ID
        int[] nextId = {1};
        
        // 递归函数，使用ID检查重复子树
        findDuplicatesWithId(root, subtreeId, idCount, nextId, result);
        
        return result;
    }
    
    /**
     * 递归函数，使用ID检查重复子树
     * 
     * @param node 当前节点
     * @param subtreeId 子树到ID的映射
     * @param idCount ID出现次数的映射
     * @param nextId 下一个可用的ID
     * @param result 结果列表
     * @return 当前子树的ID
     */
    private static int findDuplicatesWithId(TreeNode node, Map<String, Integer> subtreeId, 
                                          Map<Integer, Integer> idCount, int[] nextId, 
                                          List<TreeNode> result) {
        if (node == null) {
            return 0; // 空节点的ID为0
        }
        
        // 构建当前子树的键
        String key = node.val + "," + 
                     findDuplicatesWithId(node.left, subtreeId, idCount, nextId, result) + "," + 
                     findDuplicatesWithId(node.right, subtreeId, idCount, nextId, result);
        
        // 如果当前子树还没有分配ID，则分配一个新ID
        int id = subtreeId.computeIfAbsent(key, k -> nextId[0]++);
        
        // 增加当前ID的计数，并在计数为2时添加到结果中
        idCount.put(id, idCount.getOrDefault(id, 0) + 1);
        if (idCount.get(id) == 2) {
            result.add(node);
        }
        
        return id;
    }
    
    /**
     * 将树转换为字符串表示（用于打印结果）
     */
    public static List<String> treesToString(List<TreeNode> trees) {
        List<String> result = new ArrayList<>();
        for (TreeNode root : trees) {
            StringBuilder sb = new StringBuilder();
            buildTreeString(root, sb);
            result.add(sb.toString());
        }
        return result;
    }
    
    private static void buildTreeString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null");
            return;
        }
        sb.append(node.val);
        if (node.left != null || node.right != null) {
            sb.append("[");
            buildTreeString(node.left, sb);
            sb.append(",");
            buildTreeString(node.right, sb);
            sb.append("]");
        }
    }
    
    /**
     * 构建测试用例中的树
     */
    public static TreeNode buildExampleTree() {
        // 构建示例中的树
        //       1
        //      / \
        //     2   3
        //    /   / \
        //   4   2   4
        //      /
        //     4
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(2);
        TreeNode node6 = new TreeNode(4);
        TreeNode node7 = new TreeNode(4);
        
        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node3.left = node5;
        node3.right = node6;
        node5.left = node7;
        
        return node1;
    }
    
    public static void main(String[] args) {
        // 测试用例1: 示例中的树
        TreeNode root1 = buildExampleTree();
        System.out.println("测试用例1:");
        System.out.println("解法一（序列化）结果: " + treesToString(findDuplicateSubtrees(root1))); // 预期输出类似: [2[4,null],4]
        
        // 重新构建树，因为解法一可能修改了树的状态（虽然这里不会，但为了保险起见）
        TreeNode root1Again = buildExampleTree();
        System.out.println("解法二（ID优化）结果: " + treesToString(findDuplicateSubtreesOptimized(root1Again))); // 预期输出类似: [2[4,null],4]
        System.out.println();
        
        // 测试用例2: 空树
        TreeNode root2 = null;
        System.out.println("测试用例2（空树）:");
        System.out.println("解法一（序列化）结果: " + treesToString(findDuplicateSubtrees(root2))); // 预期输出: []
        System.out.println("解法二（ID优化）结果: " + treesToString(findDuplicateSubtreesOptimized(root2))); // 预期输出: []
        System.out.println();
        
        // 测试用例3: 只有一个节点的树
        TreeNode root3 = new TreeNode(1);
        System.out.println("测试用例3（单节点树）:");
        System.out.println("解法一（序列化）结果: " + treesToString(findDuplicateSubtrees(root3))); // 预期输出: []
        System.out.println("解法二（ID优化）结果: " + treesToString(findDuplicateSubtreesOptimized(root3))); // 预期输出: []
        System.out.println();
        
        // 测试用例4: 所有节点都相同的树
        TreeNode root4 = new TreeNode(0);
        root4.left = new TreeNode(0);
        root4.right = new TreeNode(0);
        root4.left.left = new TreeNode(0);
        root4.right.right = new TreeNode(0);
        System.out.println("测试用例4（所有节点都相同）:");
        System.out.println("解法一（序列化）结果: " + treesToString(findDuplicateSubtrees(root4))); // 预期输出类似: [0,0]
        
        // 重新构建树
        TreeNode root4Again = new TreeNode(0);
        root4Again.left = new TreeNode(0);
        root4Again.right = new TreeNode(0);
        root4Again.left.left = new TreeNode(0);
        root4Again.right.right = new TreeNode(0);
        System.out.println("解法二（ID优化）结果: " + treesToString(findDuplicateSubtreesOptimized(root4Again))); // 预期输出类似: [0,0]
        System.out.println();
        
        // 性能测试 - 构建一个较大的树，其中有重复子树
        System.out.println("性能测试:");
        // 构建一个平衡树，其中有重复子树
        TreeNode balancedTree = buildBalancedTreeWithDuplicates(1, 7);
        
        long startTime = System.currentTimeMillis();
        List<TreeNode> result1 = findDuplicateSubtrees(balancedTree);
        long endTime = System.currentTimeMillis();
        System.out.println("解法一（序列化）- 找到的重复子树数量: " + result1.size());
        System.out.println("解法一（序列化）- 耗时: " + (endTime - startTime) + "ms");
        
        // 重新构建树
        TreeNode balancedTreeAgain = buildBalancedTreeWithDuplicates(1, 7);
        startTime = System.currentTimeMillis();
        List<TreeNode> result2 = findDuplicateSubtreesOptimized(balancedTreeAgain);
        endTime = System.currentTimeMillis();
        System.out.println("解法二（ID优化）- 找到的重复子树数量: " + result2.size());
        System.out.println("解法二（ID优化）- 耗时: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 构建一个带有重复子树的平衡二叉树
     * 
     * @param start 起始值
     * @param end 结束值
     * @return 构建的树的根节点
     */
    private static TreeNode buildBalancedTreeWithDuplicates(int start, int end) {
        if (start > end) {
            return null;
        }
        
        int mid = start + (end - start) / 2;
        TreeNode root = new TreeNode(mid);
        
        // 为了创建重复子树，我们可以使部分子树的值重复
        if (start <= end - 2) {
            root.left = buildBalancedTreeWithDuplicates(start, mid - 1);
            root.right = buildBalancedTreeWithDuplicates(start, mid - 1); // 重复左子树的结构
        } else {
            root.left = buildBalancedTreeWithDuplicates(start, mid - 1);
            root.right = buildBalancedTreeWithDuplicates(mid + 1, end);
        }
        
        return root;
    }
}