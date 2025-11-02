package class056;

import java.util.*;

/**
 * 交换字符串中的元素
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，
 * 其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 你可以任意多次交换在 pairs 中任意一对索引处的字符。
 * 返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。
 * 
 * 示例 1:
 * 输入: s = "dcab", pairs = [[0,3],[1,2]]
 * 输出: "bacd"
 * 解释: 
 * 交换 s[0] 和 s[3], s = "bcad"
 * 交换 s[1] 和 s[2], s = "bacd"
 * 
 * 示例 2:
 * 输入: s = "dcab", pairs = [[0,3],[1,2],[0,2]]
 * 输出: "abcd"
 * 解释: 
 * 交换 s[0] 和 s[3], s = "bcad"
 * 交换 s[0] 和 s[2], s = "acbd"
 * 交换 s[1] 和 s[2], s = "abcd"
 * 
 * 示例 3:
 * 输入: s = "cba", pairs = [[0,1],[1,2]]
 * 输出: "abc"
 * 解释: 
 * 交换 s[0] 和 s[1], s = "bca"
 * 交换 s[1] 和 s[2], s = "bac"
 * 交换 s[0] 和 s[1], s = "abc"
 * 
 * 约束条件：
 * 1 <= s.length <= 10^5
 * 0 <= pairs.length <= 10^5
 * 0 <= pairs[i][0], pairs[i][1] < s.length
 * s 中只含有小写英文字母
 * 
 * 测试链接: https://leetcode.cn/problems/smallest-string-with-swaps/
 * 相关平台: LeetCode 1202
 */
public class Code13_SmallestStringWithSwaps {
    
    /**
     * 使用并查集解决交换字符串中的元素问题
     * 
     * 解题思路：
     * 1. 使用并查集将可以相互交换的索引分组到同一个连通组件中
     * 2. 对于每个连通组件，将对应的字符收集起来并排序
     * 3. 将排序后的字符按顺序放回原位置，得到字典序最小的字符串
     * 
     * 时间复杂度：O(N * log N + M * α(N))，其中N是字符串长度，M是索引对数量，α是阿克曼函数的反函数
     * 空间复杂度：O(N)
     * 
     * @param s 输入字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    public static String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        // 边界条件检查
        if (s == null || s.length() <= 1) {
            return s;
        }
        
        int n = s.length();
        
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 处理所有索引对
        for (List<Integer> pair : pairs) {
            uf.union(pair.get(0), pair.get(1));
        }
        
        // 将同一连通组件的索引和字符分组
        Map<Integer, List<Integer>> components = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = uf.find(i);
            components.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
        }
        
        // 构造结果字符串
        char[] result = new char[n];
        
        // 对每个连通组件内的字符进行排序并放回对应位置
        for (List<Integer> indices : components.values()) {
            // 收集字符
            List<Character> chars = new ArrayList<>();
            for (int index : indices) {
                chars.add(s.charAt(index));
            }
            
            // 排序字符
            Collections.sort(chars);
            
            // 将排序后的字符放回对应位置
            for (int i = 0; i < indices.size(); i++) {
                result[indices.get(i)] = chars.get(i);
            }
        }
        
        return new String(result);
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            // 初始时每个节点都是自己的父节点
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                // 路径压缩：将路径上的所有节点直接连接到根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            // 如果已经在同一个集合中，则无需合并
            if (rootX != rootY) {
                parent[rootX] = rootY;
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "dcab";
        List<List<Integer>> pairs1 = new ArrayList<>();
        pairs1.add(Arrays.asList(0, 3));
        pairs1.add(Arrays.asList(1, 2));
        System.out.println("测试用例1结果: " + smallestStringWithSwaps(s1, pairs1)); // 预期输出: "bacd"
        
        // 测试用例2
        String s2 = "dcab";
        List<List<Integer>> pairs2 = new ArrayList<>();
        pairs2.add(Arrays.asList(0, 3));
        pairs2.add(Arrays.asList(1, 2));
        pairs2.add(Arrays.asList(0, 2));
        System.out.println("测试用例2结果: " + smallestStringWithSwaps(s2, pairs2)); // 预期输出: "abcd"
        
        // 测试用例3
        String s3 = "cba";
        List<List<Integer>> pairs3 = new ArrayList<>();
        pairs3.add(Arrays.asList(0, 1));
        pairs3.add(Arrays.asList(1, 2));
        System.out.println("测试用例3结果: " + smallestStringWithSwaps(s3, pairs3)); // 预期输出: "abc"
        
        // 测试用例4：空字符串
        String s4 = "";
        List<List<Integer>> pairs4 = new ArrayList<>();
        System.out.println("测试用例4结果: " + smallestStringWithSwaps(s4, pairs4)); // 预期输出: ""
        
        // 测试用例5：单字符
        String s5 = "a";
        List<List<Integer>> pairs5 = new ArrayList<>();
        System.out.println("测试用例5结果: " + smallestStringWithSwaps(s5, pairs5)); // 预期输出: "a"
    }
}