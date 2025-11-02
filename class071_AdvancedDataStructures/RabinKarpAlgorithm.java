package class186;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rabin-Karp滚动哈希字符串匹配算法实现
 * <p>
 * Rabin-Karp算法是一种基于哈希的字符串搜索算法，由Richard M. Karp和Michael O. Rabin在1987年提出。
 * 该算法通过使用滚动哈希技术，使得可以在O(1)时间内更新滑动窗口的哈希值，从而实现高效的字符串匹配。
 * <p>
 * 时间复杂度：
 *   - 最好情况: O(n+m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)，当有很多哈希冲突时
 *   - 平均情况: O(n+m)
 * 空间复杂度：O(1) - 基本操作，O(n) - 存储所有匹配
 * <p>
 * 应用场景：
 * - 字符串搜索
 * - 重复子串检测
 * - 多模式串匹配（通过适当扩展）
 * - 生物信息学中的DNA序列匹配
 */
public class RabinKarpAlgorithm {
    private static final long BASE = 256;  // 字符集大小
    private static final long MOD = 1000000007;  // 大素数，防止溢出
    
    /**
     * Rabin-Karp字符串匹配算法
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    public static int search(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        int n = text.length();
        int m = pattern.length();
        
        // 边界条件检查
        if (m == 0) {
            return 0; // 空模式串匹配任何位置的开始
        }
        if (n < m) {
            return -1; // 文本串比模式串短，不可能匹配
        }
        
        // 计算pattern的哈希值和text前m个字符的哈希值
        long patternHash = 0;
        long textHash = 0;
        long highestPow = 1; // BASE^(m-1) % MOD
        
        // 预计算最高位权值和初始哈希值
        for (int i = 0; i < m - 1; i++) {
            highestPow = (highestPow * BASE) % MOD;
        }
        
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 如果哈希值相同，进行精确比较以避免哈希冲突
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i; // 找到匹配
                }
            }
            
            // 更新滑动窗口的哈希值
            if (i < n - m) {
                // 移除最左边的字符
                textHash = (textHash - highestPow * text.charAt(i) % MOD + MOD) % MOD;
                // 添加新的右边字符
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
            }
        }
        
        return -1; // 未找到匹配
    }
    
    /**
     * 查找模式串在文本串中所有出现的位置
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置的列表
     */
    public static List<Integer> searchAll(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        int n = text.length();
        int m = pattern.length();
        
        // 边界条件检查
        if (m == 0) {
            // 空模式串匹配每个位置的开始
            for (int i = 0; i <= n; i++) {
                matches.add(i);
            }
            return matches;
        }
        if (n < m) {
            return matches; // 无匹配
        }
        
        // 计算pattern的哈希值和text前m个字符的哈希值
        long patternHash = 0;
        long textHash = 0;
        long highestPow = 1; // BASE^(m-1) % MOD
        
        // 预计算最高位权值和初始哈希值
        for (int i = 0; i < m - 1; i++) {
            highestPow = (highestPow * BASE) % MOD;
        }
        
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 如果哈希值相同，进行精确比较以避免哈希冲突
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    matches.add(i); // 记录匹配位置
                }
            }
            
            // 更新滑动窗口的哈希值
            if (i < n - m) {
                // 移除最左边的字符
                textHash = (textHash - highestPow * text.charAt(i) % MOD + MOD) % MOD;
                // 添加新的右边字符
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
            }
        }
        
        return matches;
    }
    
    /**
     * 双哈希版本的Rabin-Karp算法，用于减少哈希冲突
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    public static int searchDoubleHash(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0) return 0;
        if (n < m) return -1;
        
        // 使用两个不同的哈希参数
        long BASE1 = 256, MOD1 = 1000000007;
        long BASE2 = 263, MOD2 = 1000000009;
        
        long patternHash1 = 0, textHash1 = 0;
        long patternHash2 = 0, textHash2 = 0;
        long highestPow1 = 1, highestPow2 = 1;
        
        // 预计算最高位权值
        for (int i = 0; i < m - 1; i++) {
            highestPow1 = (highestPow1 * BASE1) % MOD1;
            highestPow2 = (highestPow2 * BASE2) % MOD2;
        }
        
        // 计算初始哈希值
        for (int i = 0; i < m; i++) {
            patternHash1 = (patternHash1 * BASE1 + pattern.charAt(i)) % MOD1;
            textHash1 = (textHash1 * BASE1 + text.charAt(i)) % MOD1;
            
            patternHash2 = (patternHash2 * BASE2 + pattern.charAt(i)) % MOD2;
            textHash2 = (textHash2 * BASE2 + text.charAt(i)) % MOD2;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 双重哈希都相等时才进行精确比较
            if (patternHash1 == textHash1 && patternHash2 == textHash2) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }
            
            // 更新哈希值
            if (i < n - m) {
                // 哈希1更新
                textHash1 = (textHash1 - highestPow1 * text.charAt(i) % MOD1 + MOD1) % MOD1;
                textHash1 = (textHash1 * BASE1 + text.charAt(i + m)) % MOD1;
                
                // 哈希2更新
                textHash2 = (textHash2 - highestPow2 * text.charAt(i) % MOD2 + MOD2) % MOD2;
                textHash2 = (textHash2 * BASE2 + text.charAt(i + m)) % MOD2;
            }
        }
        
        return -1;
    }
    
    /**
     * 计算模式串在文本串中出现的次数
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中出现的次数
     */
    public static int countOccurrences(String text, String pattern) {
        return searchAll(text, pattern).size();
    }
    
    /**
     * 查找文本中所有长度为length的重复子串（出现至少minOccurrences次）
     * @param text 文本串
     * @param length 子串长度
     * @param minOccurrences 最小出现次数
     * @return 包含重复子串及其位置列表的映射
     */
    public static Map<String, List<Integer>> findRepeatedSubstrings(String text, int length, int minOccurrences) {
        if (text == null || length <= 0 || minOccurrences <= 1) {
            throw new IllegalArgumentException("参数无效");
        }
        
        int n = text.length();
        if (length > n) {
            return new HashMap<>();
        }
        
        Map<String, List<Integer>> result = new HashMap<>();
        Map<Long, List<Integer>> hashMap = new HashMap<>();
        
        // 计算初始哈希值和最高位权值
        long textHash = 0;
        long highestPow = 1;
        for (int i = 0; i < length - 1; i++) {
            highestPow = (highestPow * BASE) % MOD;
        }
        
        for (int i = 0; i < length; i++) {
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
        }
        
        // 滑动窗口遍历所有长度为length的子串
        for (int i = 0; i <= n - length; i++) {
            List<Integer> positions = hashMap.getOrDefault(textHash, new ArrayList<>());
            
            // 检查是否有哈希冲突
            boolean found = false;
            for (int pos : positions) {
                boolean match = true;
                for (int j = 0; j < length; j++) {
                    if (text.charAt(pos + j) != text.charAt(i + j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    // 找到重复子串
                    String substring = text.substring(i, i + length);
                    List<Integer> allPositions = result.getOrDefault(substring, new ArrayList<>());
                    
                    // 如果是第一次添加这个子串，先加入之前的位置
                    if (allPositions.isEmpty()) {
                        allPositions.add(pos);
                    }
                    allPositions.add(i);
                    result.put(substring, allPositions);
                    found = true;
                    break;
                }
            }
            
            // 如果没有冲突或冲突未匹配，添加当前位置
            if (!found) {
                positions.add(i);
                hashMap.put(textHash, positions);
            }
            
            // 更新哈希值
            if (i < n - length) {
                textHash = (textHash - highestPow * text.charAt(i) % MOD + MOD) % MOD;
                textHash = (textHash * BASE + text.charAt(i + length)) % MOD;
            }
        }
        
        // 过滤出现次数少于minOccurrences的子串
        result.entrySet().removeIf(entry -> entry.getValue().size() < minOccurrences);
        
        return result;
    }
    
    /**
     * 优化版本的Rabin-Karp搜索算法，使用预计算的幂值数组
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    public static int searchOptimized(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0) return 0;
        if (n < m) return -1;
        
        // 预计算所有幂值，避免重复计算
        long[] power = new long[m];
        power[0] = 1;
        for (int i = 1; i < m; i++) {
            power[i] = (power[i-1] * BASE) % MOD;
        }
        
        // 计算哈希值
        long patternHash = 0;
        long textHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash + pattern.charAt(i) * power[m-1-i]) % MOD;
            textHash = (textHash + text.charAt(i) * power[m-1-i]) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }
            
            // 更新哈希值
            if (i < n - m) {
                // 移除最左边的字符贡献
                textHash = (textHash - text.charAt(i) * power[m-1] % MOD + MOD) % MOD;
                // 整体左移一位（乘以BASE）
                textHash = (textHash * BASE) % MOD;
                // 添加新的右边字符
                textHash = (textHash + text.charAt(i + m)) % MOD;
            }
        }
        
        return -1;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例1：基本匹配
        String text1 = "hello world";
        String pattern1 = "world";
        System.out.println("测试1 - 查找'world'在'hello world'中的位置: " + search(text1, pattern1)); // 应该是6
        
        // 测试用例2：多次匹配
        String text2 = "abababa";
        String pattern2 = "aba";
        List<Integer> results2 = searchAll(text2, pattern2);
        System.out.print("测试2 - 查找所有'aba'在'abababa'中的位置: ");
        for (int pos : results2) {
            System.out.print(pos + " "); // 应该是0 2 4
        }
        System.out.println();
        
        // 测试用例3：无匹配
        String text3 = "hello";
        String pattern3 = "world";
        System.out.println("测试3 - 查找'world'在'hello'中的位置: " + search(text3, pattern3)); // 应该是-1
        
        // 测试用例4：边界情况
        String text4 = "test";
        String pattern4 = "";
        List<Integer> results4 = searchAll(text4, pattern4);
        System.out.print("测试4 - 查找空串在'test'中的位置: ");
        for (int pos : results4) {
            System.out.print(pos + " "); // 应该是0 1 2 3 4
        }
        System.out.println();
        
        // 测试用例5：双哈希版本
        String text5 = "a!b@c#d$e%";
        String pattern5 = "c#d";
        System.out.println("测试5 - 双哈希版本查找'c#d'的位置: " + searchDoubleHash(text5, pattern5)); // 应该是4
        
        // 测试用例6：计数功能
        String text6 = "abababa";
        String pattern6 = "aba";
        System.out.println("测试6 - 'aba'在'abababa'中出现的次数: " + countOccurrences(text6, pattern6)); // 应该是3
        
        // 测试用例7：查找重复子串
        String text7 = "abcabcabcdefdefxyz";
        Map<String, List<Integer>> repeatedSubstrings = findRepeatedSubstrings(text7, 3, 2);
        System.out.println("\n测试7 - 查找长度为3且至少出现2次的重复子串:");
        for (Map.Entry<String, List<Integer>> entry : repeatedSubstrings.entrySet()) {
            System.out.print("子串 '" + entry.getKey() + "' 出现位置: ");
            for (int pos : entry.getValue()) {
                System.out.print(pos + " ");
            }
            System.out.println();
        }
        
        // 测试用例8：优化版本
        String text8 = "this is an optimized version of the algorithm";
        String pattern8 = "optimized";
        System.out.println("\n测试8 - 优化版本查找: " + searchOptimized(text8, pattern8)); // 应该是10
        
        // 测试用例9：长文本测试
        StringBuilder longTextBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longTextBuilder.append("abcdefghijklmnopqrstuvwxyz");
        }
        String longText = longTextBuilder.toString() + "TARGET" + longTextBuilder.toString();
        String targetPattern = "TARGET";
        
        long startTime = System.currentTimeMillis();
        int pos = search(longText, targetPattern);
        long endTime = System.currentTimeMillis();
        System.out.println("\n测试9 - 长文本匹配:");
        System.out.println("目标位置: " + pos); // 应该是26000
        System.out.println("匹配耗时: " + (endTime - startTime) + "ms");
    }
}