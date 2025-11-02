package class129;

/**
 * LeetCode 686. Repeated String Match
 * 
 * 题目描述：
 * 给定两个字符串 a 和 b，寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串。
 * 如果不存在这样的次数，则返回 -1。
 * 
 * 解题思路：
 * 这是一个字符串匹配问题，需要找到最小的重复次数使得 b 成为重复 a 后的字符串的子串。
 * 
 * 算法步骤：
 * 1. 计算理论最小重复次数：ceil(len(b) / len(a))
 * 2. 从理论最小次数开始尝试，最多尝试 3 次额外的重复
 * 3. 对于每次重复，检查 b 是否为重复字符串的子串
 * 4. 如果找到则返回重复次数，否则返回 -1
 * 
 * 为什么最多尝试 3 次额外重复：
 * - 理论最小次数确保重复字符串长度 >= b 的长度
 * - 额外的 1-2 次重复处理边界情况：
 *   - b 可能从一个 a 的末尾开始
 *   - b 可能到下一个 a 的开头结束
 * - 如果 3 次尝试后仍未找到，则说明不可能匹配
 * 
 * 时间复杂度分析：
 * - 假设重复次数为 m，则构建重复字符串需要 O(m*len(a)) 时间
 * - 检查子串需要 O(m*len(a)*len(b)) 时间
 * - 由于 m 是有界的（最多 3 次额外尝试），所以总体时间复杂度可视为 O(len(a)*len(b))
 * 空间复杂度：O((m+2)*len(a))
 * 
 * 字符串匹配算法总结：
 * 1. 字符串匹配是计算机科学中的基础问题
 * 2. 常见算法：
 *    - 暴力匹配（Brute Force）
 *    - KMP算法（Knuth-Morris-Pratt）
 *    - Rabin-Karp算法（基于哈希）
 *    - Boyer-Moore算法
 *    - Z-algorithm
 * 3. 重复字符串相关问题的技巧：
 *    - 计算最小重复单元
 *    - 利用数学性质减少搜索空间
 *    - 利用周期性分析
 * 4. 优化方向：
 *    - 使用高效的字符串匹配算法（如KMP）代替内置的contains方法
 *    - 预先计算滚动哈希以加速匹配
 *    - 利用字符串的周期性性质
 * 
 * 补充题目汇总：
 * 1. LeetCode 466. 统计重复个数（字符串匹配与循环节）
 * 2. LeetCode 459. 重复的子字符串
 * 3. LeetCode 28. 实现 strStr()
 * 4. LeetCode 1392. 最长快乐前缀
 * 5. LeetCode 686. 重复叠加字符串匹配
 * 6. LintCode 1244. 重复的子串模式
 * 7. HackerRank - String Construction
 * 8. Codeforces 1326B. Maximums
 * 9. AtCoder ABC141E. Who Says a Pun?
 * 10. 洛谷 P4391. [BOI2009]Radio Transmission 无线传输
 * 11. 牛客网 NC15328. 最大重复子串
 * 12. 杭电OJ 4300. Clairewd's message
 * 13. POJ 2406. Power Strings
 * 14. UVa 1328. Period
 * 15. CodeChef - COMPRESS_STRING
 * 16. SPOJ - REPEATS
 * 17. Project Euler 415. Prime substrings
 * 18. HackerEarth - String Search
 * 19. 计蒜客 - 重复字符串
 * 20. ZOJ 3946. Highway Project
 * 
 * 工程化考量：
 * 1. 在实际应用中，字符串匹配常用于：
 *    - 文本搜索引擎
 *    - DNA序列分析
 *    - 网络入侵检测
 *    - 自然语言处理
 * 2. 实现优化：
 *    - 对于大规模文本，应使用KMP、Boyer-Moore等高效算法
 *    - 考虑使用SIMD指令集加速字符串处理
 *    - 对于重复查询，使用缓存机制
 * 3. 内存管理：
 *    - 处理大字符串时注意内存使用，避免不必要的字符串复制
 *    - 考虑使用StringBuilder或StringBuffer进行字符串拼接
 * 4. 多线程考虑：
 *    - 字符串匹配操作通常是无状态的，可以并行处理
 *    - 注意线程安全问题，尤其是在使用缓存时
 * 5. 边界情况处理：
 *    - 空字符串
 *    - 单字符字符串
 *    - 极长字符串
 *    - 高频重复模式
public class LeetCode686_RepeatedStringMatch {
    
    /**
     * 寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串
     * 
     * @param a 字符串 a
     * @param b 字符串 b
     * @return 最小重复次数，如果不存在则返回 -1
     */
    public static int repeatedStringMatch(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        
        // 边界情况处理
        if (lenA == 0) {
            return -1;
        }
        
        if (lenB == 0) {
            return 0;
        }
        
        // 计算理论最小重复次数
        // 确保重复后的字符串长度至少等于 b 的长度
        int minRepetitions = (lenB + lenA - 1) / lenA;
        
        // 构建初始重复字符串
        StringBuilder repeatedStr = new StringBuilder();
        for (int i = 0; i < minRepetitions; i++) {
            repeatedStr.append(a);
        }
        
        // 尝试最多 3 次额外重复
        // 处理边界情况：b 可能跨越多个 a 的边界
        for (int i = 0; i < 3; i++) {
            // 检查 b 是否为当前重复字符串的子串
            if (repeatedStr.toString().contains(b)) {
                return minRepetitions;
            }
            
            // 添加一次额外重复
            minRepetitions++;
            repeatedStr.append(a);
        }
        
        // 如果尝试了足够的重复次数仍未找到，则不可能匹配
        return -1;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        String a1 = "abcd";
        String b1 = "cdabcdab";
        System.out.println("测试用例1:");
        System.out.println("输入: a = \"" + a1 + "\", b = \"" + b1 + "\"");
        System.out.println("输出: " + repeatedStringMatch(a1, b1)); // 期望输出: 3
        
        // 测试用例2
        String a2 = "a";
        String b2 = "aa";
        System.out.println("\n测试用例2:");
        System.out.println("输入: a = \"" + a2 + "\", b = \"" + b2 + "\"");
        System.out.println("输出: " + repeatedStringMatch(a2, b2)); // 期望输出: 2
        
        // 测试用例3
        String a3 = "a";
        String b3 = "a";
        System.out.println("\n测试用例3:");
        System.out.println("输入: a = \"" + a3 + "\", b = \"" + b3 + "\"");
        System.out.println("输出: " + repeatedStringMatch(a3, b3)); // 期望输出: 1
        
        // 测试用例4
        String a4 = "abc";
        String b4 = "wxyz";
        System.out.println("\n测试用例4:");
        System.out.println("输入: a = \"" + a4 + "\", b = \"" + b4 + "\"");
        System.out.println("输出: " + repeatedStringMatch(a4, b4)); // 期望输出: -1
        
        // 测试用例5
        String a5 = "abc";
        String b5 = "cabcabca";
        System.out.println("\n测试用例5:");
        System.out.println("输入: a = \"" + a5 + "\", b = \"" + b5 + "\"");
        System.out.println("输出: " + repeatedStringMatch(a5, b5)); // 期望输出: 4
    }
}