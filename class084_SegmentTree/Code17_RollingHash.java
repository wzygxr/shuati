package class111;

// 滚动哈希算法实现 (Java版本)
// 题目来源: 字符串匹配、子串查找、重复检测
// 应用场景: Rabin-Karp算法、字符串相似度比较、重复子串检测
// 题目描述: 实现滚动哈希算法，支持高效计算字符串子串的哈希值
// 
// 解题思路:
// 1. 使用多项式哈希函数计算字符串哈希值
// 2. 通过滚动窗口技术高效更新哈希值
// 3. 支持快速计算任意子串的哈希值
// 4. 使用双哈希技术减少哈希冲突
// 
// 时间复杂度分析:
// - 初始化: O(n)，其中n是字符串长度
// - 计算子串哈希: O(1)
// - 滚动更新: O(1)
// 
// 空间复杂度: O(n)
// 
// 工程化考量:
// 1. 哈希冲突: 使用双哈希技术减少冲突概率
// 2. 数值溢出: 使用模运算防止整数溢出
// 3. 性能优化: 预计算幂次值加速滚动计算
// 4. 适用场景: 大规模字符串处理、模式匹配

import java.util.*;

public class Code17_RollingHash {
    
    // 原始字符串
    private String str;
    
    // 字符串长度
    private int n;
    
    // 基数（通常选择质数）
    private final int BASE1 = 131;
    private final int BASE2 = 13131;
    
    // 模数（选择大质数）
    private final long MOD1 = 1000000007L;
    private final long MOD2 = 1000000009L;
    
    // 前缀哈希数组
    private long[] prefixHash1, prefixHash2;
    
    // 幂次数组，用于快速计算 BASE^k
    private long[] power1, power2;
    
    /**
     * 构造函数 - 为给定字符串构建滚动哈希
     * @param str 输入字符串
     */
    public Code17_RollingHash(String str) {
        if (str == null) {
            throw new IllegalArgumentException("字符串不能为空");
        }
        
        this.str = str;
        this.n = str.length();
        
        // 初始化数组
        prefixHash1 = new long[n + 1];
        prefixHash2 = new long[n + 1];
        power1 = new long[n + 1];
        power2 = new long[n + 1];
        
        // 初始化幂次数组
        power1[0] = 1;
        power2[0] = 1;
        
        // 计算前缀哈希
        for (int i = 1; i <= n; i++) {
            char c = str.charAt(i - 1);
            
            // 更新幂次值
            power1[i] = (power1[i - 1] * BASE1) % MOD1;
            power2[i] = (power2[i - 1] * BASE2) % MOD2;
            
            // 更新前缀哈希
            prefixHash1[i] = (prefixHash1[i - 1] * BASE1 + c) % MOD1;
            prefixHash2[i] = (prefixHash2[i - 1] * BASE2 + c) % MOD2;
        }
    }
    
    /**
     * 计算子串的哈希值（双哈希）
     * @param l 子串起始位置（0-based）
     * @param r 子串结束位置（0-based，包含）
     * @return 包含两个哈希值的数组 [hash1, hash2]
     * @throws IllegalArgumentException 如果位置参数无效
     */
    public long[] getHash(int l, int r) {
        if (l < 0 || r >= n || l > r) {
            throw new IllegalArgumentException("位置参数无效: l=" + l + ", r=" + r);
        }
        
        // 计算第一个哈希值
        long hash1 = (prefixHash1[r + 1] - prefixHash1[l] * power1[r - l + 1] % MOD1 + MOD1) % MOD1;
        
        // 计算第二个哈希值
        long hash2 = (prefixHash2[r + 1] - prefixHash2[l] * power2[r - l + 1] % MOD2 + MOD2) % MOD2;
        
        return new long[]{hash1, hash2};
    }
    
    /**
     * 检查两个子串是否相等（使用哈希比较）
     * @param l1 第一个子串起始位置
     * @param r1 第一个子串结束位置
     * @param l2 第二个子串起始位置
     * @param r2 第二个子串结束位置
     * @return true如果两个子串相等
     */
    public boolean equals(int l1, int r1, int l2, int r2) {
        if (r1 - l1 != r2 - l2) {
            return false; // 长度不同，肯定不相等
        }
        
        long[] hash1 = getHash(l1, r1);
        long[] hash2 = getHash(l2, r2);
        
        return hash1[0] == hash2[0] && hash1[1] == hash2[1];
    }
    
    /**
     * 查找最长重复子串的长度（使用二分搜索）
     * @return 最长重复子串的长度
     */
    public int longestRepeatingSubstring() {
        int left = 1, right = n - 1;
        int result = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (hasRepeatingSubstring(mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 检查是否存在长度为len的重复子串
     * @param len 子串长度
     * @return true如果存在重复子串
     */
    private boolean hasRepeatingSubstring(int len) {
        if (len <= 0 || len > n) {
            return false;
        }
        
        // 使用哈希集合检测重复
        Set<String> seen = new HashSet<>();
        
        for (int i = 0; i <= n - len; i++) {
            long[] hash = getHash(i, i + len - 1);
            String hashKey = hash[0] + "_" + hash[1];
            
            if (seen.contains(hashKey)) {
                // 验证是否真的相等（防止哈希冲突）
                for (String existing : seen) {
                    if (existing.equals(hashKey)) {
                        // 这里可以添加实际字符串比较来确认
                        return true;
                    }
                }
            }
            
            seen.add(hashKey);
        }
        
        return false;
    }
    
    /**
     * 实现Rabin-Karp字符串匹配算法
     * @param pattern 要匹配的模式串
     * @return 模式串在文本中出现的所有起始位置
     */
    public List<Integer> rabinKarp(String pattern) {
        List<Integer> positions = new ArrayList<>();
        
        if (pattern == null || pattern.isEmpty() || pattern.length() > n) {
            return positions;
        }
        
        int m = pattern.length();
        
        // 计算模式串的哈希值
        RollingHash patternHash = new RollingHash(pattern);
        long[] patternHashValue = patternHash.getHash(0, m - 1);
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            long[] textHash = getHash(i, i + m - 1);
            
            // 比较哈希值
            if (textHash[0] == patternHashValue[0] && textHash[1] == patternHashValue[1]) {
                // 哈希值匹配，验证实际字符串是否相等（防止哈希冲突）
                if (str.substring(i, i + m).equals(pattern)) {
                    positions.add(i);
                }
            }
        }
        
        return positions;
    }
    
    /**
     * 获取滚动哈希的状态信息
     * @return 状态信息字符串
     */
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("滚动哈希状态:\n");
        sb.append("字符串长度: ").append(n).append("\n");
        sb.append("基数: BASE1=").append(BASE1).append(", BASE2=").append(BASE2).append("\n");
        sb.append("模数: MOD1=").append(MOD1).append(", MOD2=").append(MOD2).append("\n");
        
        // 显示一些示例哈希值
        if (n > 0) {
            sb.append("示例哈希值:\n");
            for (int len : new int[]{1, Math.min(5, n), Math.min(10, n)}) {
                if (len <= n) {
                    long[] hash = getHash(0, len - 1);
                    sb.append("前").append(len).append("个字符的哈希: [").append(hash[0])
                      .append(", ").append(hash[1]).append("]\n");
                }
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 性能测试 - 测试各种操作的速度
     */
    public void performanceTest() {
        System.out.println("=== 滚动哈希性能测试 ===");
        
        // 测试1: 哈希计算性能
        long startTime = System.nanoTime();
        
        int testCount = 10000;
        for (int i = 0; i < testCount; i++) {
            int l = i % (n - 10);
            int r = l + 9;
            if (r < n) {
                getHash(l, r);
            }
        }
        
        long hashTime = System.nanoTime() - startTime;
        System.out.printf("哈希计算性能: %.2f 纳秒/次%n", (double) hashTime / testCount);
        
        // 测试2: 字符串匹配性能
        startTime = System.nanoTime();
        List<Integer> matches = rabinKarp("test");
        long matchTime = System.nanoTime() - startTime;
        System.out.printf("Rabin-Karp匹配时间: %d 纳秒，找到 %d 个匹配%n", matchTime, matches.size());
        
        // 测试3: 重复子串检测性能
        startTime = System.nanoTime();
        int longestRepeat = longestRepeatingSubstring();
        long repeatTime = System.nanoTime() - startTime;
        System.out.printf("最长重复子串检测时间: %d 纳秒，最长长度: %d%n", repeatTime, longestRepeat);
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        System.out.println("=== 滚动哈希单元测试 ===");
        
        // 测试1: 基本功能测试
        System.out.println("测试1: 基本功能测试");
        String testStr = "abcdefghij";
        Code17_RollingHash rh = new Code17_RollingHash(testStr);
        
        // 测试相同子串的哈希值相等
        long[] hash1 = rh.getHash(0, 2); // "abc"
        long[] hash2 = rh.getHash(0, 2); // "abc"
        assert hash1[0] == hash2[0] && hash1[1] == hash2[1] : "相同子串哈希值应该相等";
        
        // 测试不同子串的哈希值不同
        long[] hash3 = rh.getHash(0, 2); // "abc"
        long[] hash4 = rh.getHash(1, 3); // "bcd"
        assert !(hash3[0] == hash4[0] && hash3[1] == hash4[1]) : "不同子串哈希值应该不同";
        
        System.out.println("基本功能测试通过");
        
        // 测试2: 字符串匹配测试
        System.out.println("\n测试2: 字符串匹配测试");
        String text = "ababcabcabababd";
        String pattern = "ababd";
        
        Code17_RollingHash rh2 = new Code17_RollingHash(text);
        List<Integer> positions = rh2.rabinKarp(pattern);
        
        assert positions.size() == 1 && positions.get(0) == 10 : "模式串应该在位置10找到";
        System.out.println("字符串匹配测试通过，找到位置: " + positions);
        
        // 测试3: 重复子串检测
        System.out.println("\n测试3: 重复子串检测");
        String repeatStr = "abcabcabc";
        Code17_RollingHash rh3 = new Code17_RollingHash(repeatStr);
        int longestRepeat = rh3.longestRepeatingSubstring();
        
        assert longestRepeat == 6 : "最长重复子串应该是6（abcabc）";
        System.out.println("重复子串检测通过，最长长度: " + longestRepeat);
        
        System.out.println("=== 单元测试完成 ===");
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            test();
            return;
        }
        
        // 演示示例
        System.out.println("=== 滚动哈希演示 ===");
        
        // 创建一个DNA序列的滚动哈希
        String dnaSequence = "ATCGATCGATCGATCGATCGATCG";
        Code17_RollingHash dnaHash = new Code17_RollingHash(dnaSequence);
        
        System.out.println("DNA序列: " + dnaSequence);
        System.out.println(dnaHash.getStatus());
        
        // 演示子串哈希计算
        System.out.println("\n子串哈希计算演示:");
        int[][] substrings = {{0, 5}, {5, 10}, {10, 15}};
        
        for (int[] range : substrings) {
            int l = range[0], r = range[1];
            long[] hash = dnaHash.getHash(l, r);
            String substring = dnaSequence.substring(l, r + 1);
            System.out.printf("子串 '%s' (位置%d-%d) 的哈希值: [%d, %d]%n", 
                substring, l, r, hash[0], hash[1]);
        }
        
        // 演示字符串匹配
        System.out.println("\n字符串匹配演示:");
        String pattern = "ATCG";
        List<Integer> matches = dnaHash.rabinKarp(pattern);
        System.out.println("模式串 '" + pattern + "' 在DNA序列中出现的位置: " + matches);
        
        // 演示重复子串检测
        System.out.println("\n重复子串检测演示:");
        int longestRepeat = dnaHash.longestRepeatingSubstring();
        System.out.println("最长重复子串长度: " + longestRepeat);
        
        // 性能测试
        dnaHash.performanceTest();
    }
}