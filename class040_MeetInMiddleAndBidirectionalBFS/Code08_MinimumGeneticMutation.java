package class063;

import java.util.*;

// 最小基因变化
// 题目来源：LeetCode 433
// 题目描述：
// 基因序列可以表示为一条由 8 个字符组成的字符串，其中每个字符都是 'A'、'C'、'G' 和 'T' 之一。
// 假设我们需要研究从基因序列 start 变为 end 所发生的基因变化。
// 一次基因变化意味着这个基因序列中的一个字符发生了变化。
// 同时，每次基因变化的结果，必须是在基因库 bank 中存在的有效基因序列。
// 假设 start 和 end 都是有效的基因序列，start 不等于 end。
// 请找出并返回能够使 start 变为 end 的最少变化次数。
// 如果无法完成此任务，则返回 -1。
// 测试链接 : https://leetcode.cn/problems/minimum-genetic-mutation/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，直到两者相遇
// 这样可以大大减少搜索空间，提高算法效率
// 时间复杂度：O(B)，其中B是基因库的大小
// 空间复杂度：O(B)
// 
// 工程化考量：
// 1. 异常处理：检查输入是否合法
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用HashSet进行快速查找，使用Queue进行BFS

public class Code08_MinimumGeneticMutation {
    
    /**
     * 计算从start基因序列到end基因序列的最小变化次数
     * @param start 起始基因序列
     * @param end 目标基因序列
     * @param bank 基因库
     * @return 最小变化次数，如果无法完成则返回-1
     */
    public static int minMutation(String start, String end, String[] bank) {
        // 边界条件检查
        if (start == null || end == null || bank == null) {
            return -1;
        }
        
        // 将基因库转换为Set，提高查找效率
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        
        // 如果目标基因不在基因库中，直接返回-1
        if (!bankSet.contains(end)) {
            return -1;
        }
        
        // 基因字符集
        char[] genes = {'A', 'C', 'G', 'T'};
        
        // 使用双向BFS
        // 从起点开始的搜索队列和已访问集合
        Set<String> startSet = new HashSet<>();
        // 从终点开始的搜索队列和已访问集合
        Set<String> endSet = new HashSet<>();
        // 全局已访问集合，避免重复搜索
        Set<String> visited = new HashSet<>();
        
        startSet.add(start);
        endSet.add(end);
        visited.add(start);
        visited.add(end);
        
        int steps = 0;
        
        // 当两个集合都不为空时继续搜索
        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            // 优化：总是从较小的集合开始搜索，减少搜索空间
            if (startSet.size() > endSet.size()) {
                Set<String> temp = startSet;
                startSet = endSet;
                endSet = temp;
            }
            
            Set<String> nextLevel = new HashSet<>();
            
            // 遍历当前层的所有基因序列
            for (String current : startSet) {
                // 尝试改变每个位置的字符
                char[] chars = current.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char original = chars[i];
                    
                    // 尝试所有可能的字符替换
                    for (char gene : genes) {
                        if (gene == original) {
                            continue; // 跳过相同的字符
                        }
                        
                        chars[i] = gene;
                        String next = new String(chars);
                        
                        // 检查是否与另一端的搜索集合相遇
                        if (endSet.contains(next)) {
                            return steps + 1;
                        }
                        
                        // 如果是有效的基因序列且未访问过，则加入下一层
                        if (bankSet.contains(next) && !visited.contains(next)) {
                            nextLevel.add(next);
                            visited.add(next);
                        }
                    }
                    
                    // 恢复原字符
                    chars[i] = original;
                }
            }
            
            // 进入下一层
            startSet = nextLevel;
            steps++;
        }
        
        // 无法找到路径
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        System.out.println("测试用例1：");
        String start1 = "AACCGGTT";
        String end1 = "AACCGGTA";
        String[] bank1 = {"AACCGGTA"};
        System.out.println("start: \"AACCGGTT\"");
        System.out.println("end: \"AACCGGTA\"");
        System.out.println("bank: [\"AACCGGTA\"]");
        System.out.println("期望输出: 1");
        System.out.println("实际输出: " + minMutation(start1, end1, bank1));
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2：");
        String start2 = "AACCGGTT";
        String end2 = "AAACGGTA";
        String[] bank2 = {"AACCGGTA", "AACCGCTA", "AAACGGTA"};
        System.out.println("start: \"AACCGGTT\"");
        System.out.println("end: \"AAACGGTA\"");
        System.out.println("bank: [\"AACCGGTA\", \"AACCGCTA\", \"AAACGGTA\"]");
        System.out.println("期望输出: 2");
        System.out.println("实际输出: " + minMutation(start2, end2, bank2));
        System.out.println();
        
        // 测试用例3
        System.out.println("测试用例3：");
        String start3 = "AAAAACCC";
        String end3 = "AACCCCCC";
        String[] bank3 = {"AAAACCCC", "AAACCCCC", "AACCCCCC"};
        System.out.println("start: \"AAAAACCC\"");
        System.out.println("end: \"AACCCCCC\"");
        System.out.println("bank: [\"AAAACCCC\", \"AAACCCCC\", \"AACCCCCC\"]");
        System.out.println("期望输出: 3");
        System.out.println("实际输出: " + minMutation(start3, end3, bank3));
    }
}