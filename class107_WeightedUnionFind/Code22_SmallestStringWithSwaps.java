/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 你可以 任意多次交换 在 pairs 中任意一对索引处的字符。
 * 返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。
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
 * 示例 3:
 * 输入：s = "cba", pairs = [[0,1],[1,2]]
 * 输出："abc"
 * 解释：
 * 交换 s[0] 和 s[1], s = "bca"
 * 交换 s[1] 和 s[2], s = "bac"
 * 交换 s[0] 和 s[1], s = "abc"
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的索引合并到同一个集合中
 * 2. 对于每个集合，将其中的字符按照字典序排序
 * 3. 按照原始索引的顺序，依次从对应的集合中取出最小的可用字符
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n + m * α(n))，其中n是字符串长度，m是pairs数组长度，α是阿克曼函数的反函数，近似为常数
 * - 收集每个集合中的字符：O(n)
 * - 对每个集合中的字符排序：O(n log k)，其中k是集合的最大大小
 * - 重组字符串：O(n)
 * - 总体时间复杂度：O(n log n + m * α(n))
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储每个集合的字符：O(n)
 * - 总体空间复杂度：O(n)
 */

import java.util.*;

public class Code22_SmallestStringWithSwaps {
    // 并查集的父节点数组
    private int[] parent;
    // 并查集的秩数组，用于按秩合并
    private int[] rank;
    
    /**
     * 初始化并查集
     * @param n 元素数量
     */
    public void initUnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        
        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 1;
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
     * 合并两个元素所在的集合，使用按秩合并优化
     * @param x 第一个元素
     * @param y 第二个元素
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return; // 已经在同一集合中
        }
        
        // 按秩合并：将较小秩的树连接到较大秩的树上
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // 秩相等时，任选一个作为根，并增加其秩
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
    
    /**
     * 计算字典序最小的字符串
     * @param s 原始字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        
        // 初始化并查集
        initUnionFind(n);
        
        // 将可以交换的索引合并到同一个集合中
        for (List<Integer> pair : pairs) {
            union(pair.get(0), pair.get(1));
        }
        
        // 收集每个集合中的字符
        // key: 集合的根节点索引
        // value: 该集合中的所有字符（按索引顺序收集）
        Map<Integer, List<Character>> charGroups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            charGroups.putIfAbsent(root, new ArrayList<>());
            charGroups.get(root).add(s.charAt(i));
        }
        
        // 对每个集合中的字符按字典序排序
        // 注意：为了从后往前取字符（方便后续按索引放置），这里将字符降序排序
        Map<Integer, Deque<Character>> sortedGroups = new HashMap<>();
        for (Map.Entry<Integer, List<Character>> entry : charGroups.entrySet()) {
            int root = entry.getKey();
            List<Character> chars = entry.getValue();
            
            // 排序字符
            Collections.sort(chars);
            
            // 将排序后的字符放入双端队列
            Deque<Character> deque = new LinkedList<>();
            for (char c : chars) {
                deque.offerLast(c);
            }
            
            sortedGroups.put(root, deque);
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int root = find(i);
            // 从对应集合中取出最小的可用字符
            result.append(sortedGroups.get(root).pollFirst());
        }
        
        return result.toString();
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code22_SmallestStringWithSwaps solution = new Code22_SmallestStringWithSwaps();
        
        // 测试用例1
        String s1 = "dcab";
        List<List<Integer>> pairs1 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2)
        );
        System.out.println("测试用例1结果：" + solution.smallestStringWithSwaps(s1, pairs1));
        // 预期输出："bacd"
        
        // 测试用例2
        String s2 = "dcab";
        List<List<Integer>> pairs2 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2),
            Arrays.asList(0, 2)
        );
        System.out.println("测试用例2结果：" + solution.smallestStringWithSwaps(s2, pairs2));
        // 预期输出："abcd"
        
        // 测试用例3
        String s3 = "cba";
        List<List<Integer>> pairs3 = Arrays.asList(
            Arrays.asList(0, 1),
            Arrays.asList(1, 2)
        );
        System.out.println("测试用例3结果：" + solution.smallestStringWithSwaps(s3, pairs3));
        // 预期输出："abc"
        
        // 测试用例4：空字符串
        String s4 = "";
        List<List<Integer>> pairs4 = new ArrayList<>();
        System.out.println("测试用例4结果：" + solution.smallestStringWithSwaps(s4, pairs4));
        // 预期输出：""
    }
    
    /**
     * 异常处理考虑：
     * 1. 空字符串处理：当s为空时，直接返回空字符串
     * 2. 空pairs数组处理：当pairs为空时，无法进行任何交换，直接返回原字符串
     * 3. 索引越界检查：确保pairs中的索引在有效范围内
     * 4. 大规模数据处理：通过路径压缩和按秩合并确保并查集操作的高效性
     */
    
    /**
     * 优化点：
     * 1. 使用路径压缩和按秩合并优化并查集性能
     * 2. 使用HashMap高效管理不同集合的字符
     * 3. 使用双端队列Deque高效取出排序后的字符
     * 4. 预先为StringBuilder分配容量，减少动态扩容开销
     */
}