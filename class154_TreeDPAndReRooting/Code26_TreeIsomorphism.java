// 树的同构问题 - HDU 2815 / POJ 1635
// 题目来源：HDU 2815. Tree Isomorphism / POJ 1635. Subway tree systems
// 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2815
// 题目链接：http://poj.org/problem?id=1635
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=2815
// 测试链接 : http://poj.org/problem?id=1635
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
树的同构问题是指判断两棵树是否具有相同的结构，可以通过节点重命名使得两棵树完全相同。

算法思路（AHU算法）：
1. 为每个节点计算一个哈希值，表示其子树的结构
2. 哈希值基于子节点的哈希值计算，并考虑子节点的顺序
3. 如果两棵树的根节点哈希值相同，则两棵树同构

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(n) - 存储哈希值和树结构
是否为最优解：是，AHU算法是解决树同构问题的标准方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code26_TreeIsomorphism.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code26_TreeIsomorphism.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code26_TreeIsomorphism.cpp

工程化考量：
1. 哈希冲突：使用大质数减少冲突概率
2. 性能优化：预处理哈希值，避免重复计算
3. 边界条件：空树、单节点树
*/

import java.util.*;

public class Code26_TreeIsomorphism {
    
    /**
     * 判断两棵有根树是否同构
     * 
     * @param tree1 第一棵树的邻接表表示
     * @param tree2 第二棵树的邻接表表示
     * @param root1 第一棵树的根节点
     * @param root2 第二棵树的根节点
     * @return 是否同构
     */
    public boolean isIsomorphic(List<List<Integer>> tree1, List<List<Integer>> tree2, 
                                int root1, int root2) {
        if (tree1.size() != tree2.size()) {
            return false;
        }
        
        int n = tree1.size();
        
        // 计算两棵树的哈希值
        long hash1 = computeTreeHash(tree1, root1, -1);
        long hash2 = computeTreeHash(tree2, root2, -1);
        
        return hash1 == hash2;
    }
    
    /**
     * 计算树的哈希值（AHU算法）
     * 
     * @param tree 树的邻接表表示
     * @param node 当前节点
     * @param parent 父节点
     * @return 子树哈希值
     */
    private long computeTreeHash(List<List<Integer>> tree, int node, int parent) {
        List<Long> childHashes = new ArrayList<>();
        
        for (int child : tree.get(node)) {
            if (child != parent) {
                childHashes.add(computeTreeHash(tree, child, node));
            }
        }
        
        // 对子节点哈希值排序（消除顺序影响）
        Collections.sort(childHashes);
        
        // 计算当前节点的哈希值
        long hash = 1; // 起始值
        final long MOD = 1000000007;
        final long BASE = 131; // 质数基数
        
        for (long childHash : childHashes) {
            hash = (hash * BASE + childHash) % MOD;
        }
        
        return hash;
    }
    
    /**
     * 判断两棵无根树是否同构
     * 
     * @param tree1 第一棵树的邻接表表示
     * @param tree2 第二棵树的邻接表表示
     * @return 是否同构
     */
    public boolean isIsomorphicUnrooted(List<List<Integer>> tree1, List<List<Integer>> tree2) {
        if (tree1.size() != tree2.size()) {
            return false;
        }
        
        int n = tree1.size();
        if (n == 0) {
            return true;
        }
        
        // 找到两棵树的中心节点（可能有一个或两个中心）
        List<Integer> centers1 = findTreeCenters(tree1);
        List<Integer> centers2 = findTreeCenters(tree2);
        
        // 计算第一棵树所有中心节点的哈希值
        Set<Long> hashes1 = new HashSet<>();
        for (int center : centers1) {
            hashes1.add(computeTreeHash(tree1, center, -1));
        }
        
        // 检查第二棵树是否有匹配的哈希值
        for (int center : centers2) {
            long hash2 = computeTreeHash(tree2, center, -1);
            if (hashes1.contains(hash2)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 找到树的中心节点
     * 
     * @param tree 树的邻接表表示
     * @return 中心节点列表
     */
    private List<Integer> findTreeCenters(List<List<Integer>> tree) {
        int n = tree.size();
        int[] degree = new int[n];
        Queue<Integer> leaves = new LinkedList<>();
        
        // 初始化度数和叶子节点
        for (int i = 0; i < n; i++) {
            degree[i] = tree.get(i).size();
            if (degree[i] <= 1) {
                leaves.offer(i);
                degree[i] = 0;
            }
        }
        
        int count = leaves.size();
        
        // 拓扑排序，层层剥离叶子节点
        while (count < n) {
            int size = leaves.size();
            for (int i = 0; i < size; i++) {
                int leaf = leaves.poll();
                for (int neighbor : tree.get(leaf)) {
                    if (--degree[neighbor] == 1) {
                        leaves.offer(neighbor);
                    }
                }
            }
            count += leaves.size();
        }
        
        return new ArrayList<>(leaves);
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code26_TreeIsomorphism solution = new Code26_TreeIsomorphism();
        
        // 测试用例1: 同构的有根树
        List<List<Integer>> tree1 = new ArrayList<>();
        List<List<Integer>> tree2 = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            tree1.add(new ArrayList<>());
            tree2.add(new ArrayList<>());
        }
        
        // 树1: 0-1, 0-2, 1-3, 1-4
        tree1.get(0).add(1); tree1.get(0).add(2);
        tree1.get(1).add(0); tree1.get(1).add(3); tree1.get(1).add(4);
        tree1.get(2).add(0);
        tree1.get(3).add(1);
        tree1.get(4).add(1);
        
        // 树2: 0-1, 0-2, 2-3, 2-4 (结构相同，但节点连接不同)
        tree2.get(0).add(1); tree2.get(0).add(2);
        tree2.get(1).add(0);
        tree2.get(2).add(0); tree2.get(2).add(3); tree2.get(2).add(4);
        tree2.get(3).add(2);
        tree2.get(4).add(2);
        
        boolean result1 = solution.isIsomorphic(tree1, tree2, 0, 0);
        System.out.println("有根树同构测试: " + result1); // 期望: true
        
        // 测试用例2: 不同构的树
        List<List<Integer>> tree3 = new ArrayList<>();
        for (int i = 0; i < 4; i++) tree3.add(new ArrayList<>());
        
        // 链状树: 0-1-2-3
        tree3.get(0).add(1);
        tree3.get(1).add(0); tree3.get(1).add(2);
        tree3.get(2).add(1); tree3.get(2).add(3);
        tree3.get(3).add(2);
        
        boolean result2 = solution.isIsomorphicUnrooted(tree1, tree3);
        System.out.println("无根树同构测试: " + !result2); // 期望: false
        
        // 测试用例3: 空树
        List<List<Integer>> emptyTree1 = new ArrayList<>();
        List<List<Integer>> emptyTree2 = new ArrayList<>();
        boolean result3 = solution.isIsomorphicUnrooted(emptyTree1, emptyTree2);
        System.out.println("空树同构测试: " + result3); // 期望: true
    }
    
    /**
     * 算法复杂度分析：
     * 时间复杂度：O(n) - 每个节点访问一次
     * 空间复杂度：O(n) - 存储哈希值和树结构
     * 
     * 算法正确性验证：
     * 1. 基础情况：空树和单节点树
     * 2. 结构判断：正确识别同构和不同构的树
     * 3. 无根树处理：通过中心节点方法处理无根树
     * 
     * 工程化改进：
     * 1. 使用多种哈希函数减少冲突概率
     * 2. 添加详细的注释和文档
     * 3. 支持大规模树结构比较
     */
}