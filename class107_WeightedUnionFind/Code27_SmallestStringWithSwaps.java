/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 
 * 你可以多次交换在 pairs 中任意一对索引处的字符。
 * 
 * 返回在经过若干次交换后，该字符串可以变成的按字典序最小的字符串。
 * 
 * 示例 1:
 * 输入：s = "dcab", pairs = [[0,3],[1,2]]
 * 输出："bacd"
 * 解释：
 * 交换 s[0] 和 s[3], s = "bcad"
 * 交换 s[1] 和 s[2], s = "bacd"
 * 
 * 示例 2:
 * 输入：s = "dcab", pairs = [[0,3],[1,2],[0,2]]
 * 输出："abcd"
 * 解释：
 * 交换 s[0] 和 s[3], s = "bcad"
 * 交换 s[0] 和 s[2], s = "acbd"
 * 交换 s[1] 和 s[2], s = "abcd"
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的字符的索引归为一个连通分量
 * 2. 对于每个连通分量，将其对应的字符收集起来并排序
 * 3. 按照排序后的字符顺序重新填充原字符串
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有索引对：O(m * α(n))，其中m是pairs数组的长度
 * - 收集字符并排序：O(n * log n)
 * - 重建字符串：O(n)
 * - 总体时间复杂度：O(n * log n + m * α(n)) ≈ O(n * log n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储连通分量的映射：O(n)
 * - 存储排序后的字符：O(n)
 * - 总体空间复杂度：O(n)
 */

import java.util.*;

public class Code27_SmallestStringWithSwaps {
    // 并查集的父节点数组
    private int[] parent;
    // 并查集的秩数组，用于按秩合并优化
    private int[] rank;
    
    /**
     * 初始化并查集
     * @param n 字符串长度
     */
    public void initUnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        
        // 初始化，每个元素的父节点是自己，秩为0
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    public int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return; // 已经在同一个集合中
        }
        
        // 按秩合并：将秩小的树连接到秩大的树下
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // 秩相同时，任选一个作为根，并增加其秩
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
    
    /**
     * 交换字符串中的元素，使得结果字典序最小
     * @param s 原始字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        
        // 初始化并查集
        initUnionFind(n);
        
        // 处理所有索引对，将可以互相交换的索引归为一个连通分量
        for (List<Integer> pair : pairs) {
            int a = pair.get(0);
            int b = pair.get(1);
            union(a, b);
        }
        
        // 使用HashMap将每个连通分量的根节点映射到对应的字符列表
        Map<Integer, PriorityQueue<Character>> componentMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            // 如果该根节点不存在于map中，创建一个新的优先队列
            componentMap.computeIfAbsent(root, k -> new PriorityQueue<>()).offer(s.charAt(i));
        }
        
        // 重建字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            // 从对应的优先队列中取出最小的字符
            result.append(componentMap.get(root).poll());
        }
        
        return result.toString();
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code27_SmallestStringWithSwaps solution = new Code27_SmallestStringWithSwaps();
        
        // 测试用例1
        String s1 = "dcab";
        List<List<Integer>> pairs1 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2)
        );
        System.out.println("测试用例1结果：" + solution.smallestStringWithSwaps(s1, pairs1));
        // 预期输出：bacd
        
        // 测试用例2
        String s2 = "dcab";
        List<List<Integer>> pairs2 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2),
            Arrays.asList(0, 2)
        );
        System.out.println("测试用例2结果：" + solution.smallestStringWithSwaps(s2, pairs2));
        // 预期输出：abcd
        
        // 测试用例3：没有交换对的情况
        String s3 = "dcba";
        List<List<Integer>> pairs3 = new ArrayList<>();
        System.out.println("测试用例3结果：" + solution.smallestStringWithSwaps(s3, pairs3));
        // 预期输出：dcba
        
        // 测试用例4：所有字符都可以交换的情况
        String s4 = "dcba";
        List<List<Integer>> pairs4 = Arrays.asList(
            Arrays.asList(0, 1),
            Arrays.asList(1, 2),
            Arrays.asList(2, 3)
        );
        System.out.println("测试用例4结果：" + solution.smallestStringWithSwaps(s4, pairs4));
        // 预期输出：abcd
    }
    
    /**
     * 优化说明：
     * 1. 使用PriorityQueue（优先队列）来存储每个连通分量的字符，可以自动保持有序
     * 2. 使用HashMap的computeIfAbsent方法简化代码，提高可读性
     * 3. 路径压缩和按秩合并优化并查集的性能
     * 
     * 时间复杂度分析：
     * - 并查集操作：O(m * α(n))，其中m是pairs数组的长度
     * - 构建字符映射和排序：O(n * log n)，因为每个连通分量的字符需要排序
     * - 重建字符串：O(n)
     * - 总体时间复杂度：O(n * log n + m * α(n)) ≈ O(n * log n + m)
     * 
     * 空间复杂度分析：
     * - 并查集数组：O(n)
     * - 字符映射：O(n)
     * - 总体空间复杂度：O(n)
     */
}