package class156;

// Smallest String With Swaps (LeetCode 1202)
// 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，
// 其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
// 你可以任意多次交换在 pairs 中任意一对索引处的字符。
// 返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。
// 测试链接 : https://leetcode.com/problems/smallest-string-with-swaps/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.util.*;

/**
 * 带权并查集解决最小字符串交换问题
 * 
 * 问题分析：
 * 通过给定的索引对，将字符串中可以交换的字符分组，每组内字符可以任意交换位置，
 * 求字典序最小的字符串。
 * 
 * 核心思想：
 * 1. 使用并查集将可以交换的索引分组
 * 2. 对每组内的字符按字典序排序
 * 3. 将排序后的字符按索引顺序重新组合成字符串
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - 总体: O(n * log(n) + m * α(n))，其中n是字符串长度，m是索引对数量
 * 
 * 空间复杂度: O(n) 用于存储father数组和每组的字符列表
 * 
 * 应用场景：
 * - 字符串重排优化
 * - 连通分量排序
 * - 图的连通性应用
 * 
 * 题目来源：LeetCode 1202
 * 题目链接：https://leetcode.com/problems/smallest-string-with-swaps/
 * 题目名称：Smallest String With Swaps
 */
public class Code17_SmallestStringWithSwaps {

    // father[i] 表示索引i的父节点
    public static int[] father = new int[100001];

    /**
     * 初始化并查集
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param n 字符串长度
     */
    public static void prepare(int n) {
        // 初始化每个索引为自己所在集合的代表
        for (int i = 0; i < n; i++) {
            father[i] = i;
        }
    }

    /**
     * 查找索引i的根节点，并进行路径压缩
     * 时间复杂度: O(α(n)) 近似O(1)
     * 
     * @param i 要查找的索引
     * @return 索引i所在集合的根节点
     */
    public static int find(int i) {
        // 如果不是根节点
        if (i != father[i]) {
            // 递归查找根节点，同时进行路径压缩
            father[i] = find(father[i]);
        }
        return father[i];
    }

    /**
     * 合并两个索引所在的集合
     * 时间复杂度: O(α(n)) 近似O(1)
     * 
     * @param i 索引i
     * @param j 索引j
     */
    public static void union(int i, int j) {
        // 查找两个索引的根节点
        int fi = find(i), fj = find(j);
        // 如果不在同一集合中
        if (fi != fj) {
            // 合并两个集合
            father[fi] = fj;
        }
    }

    /**
     * 通过索引对交换得到字典序最小的字符串
     * 
     * @param s 输入字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        
        // 初始化并查集
        prepare(n);
        
        // 处理所有索引对，建立连通关系
        for (List<Integer> pair : pairs) {
            union(pair.get(0), pair.get(1));
        }
        
        // 将同一连通分量的字符分组
        Map<Integer, List<Character>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(s.charAt(i));
        }
        
        // 对每组内的字符按字典序排序（降序，为了后面能从尾部取最小的）
        for (List<Character> group : groups.values()) {
            Collections.sort(group, Collections.reverseOrder());
        }
        
        // 构造结果字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            List<Character> group = groups.get(root);
            // 取出当前组中字典序最小的字符
            result.append(group.remove(group.size() - 1));
        }
        
        return result.toString();
    }

    // 测试用例
    public static void main(String[] args) {
        Code17_SmallestStringWithSwaps solution = new Code17_SmallestStringWithSwaps();
        
        // 测试用例1
        String s1 = "dcab";
        List<List<Integer>> pairs1 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2)
        );
        System.out.println(solution.smallestStringWithSwaps(s1, pairs1)); // bacd
        
        // 测试用例2
        String s2 = "dcab";
        List<List<Integer>> pairs2 = Arrays.asList(
            Arrays.asList(0, 3),
            Arrays.asList(1, 2),
            Arrays.asList(0, 2)
        );
        System.out.println(solution.smallestStringWithSwaps(s2, pairs2)); // abcd
        
        // 测试用例3
        String s3 = "cba";
        List<List<Integer>> pairs3 = Arrays.asList(
            Arrays.asList(0, 1),
            Arrays.asList(1, 2)
        );
        System.out.println(solution.smallestStringWithSwaps(s3, pairs3)); // abc
    }
}