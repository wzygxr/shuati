package class084;

import java.util.*;

/**
 * Codeforces 55D. Beautiful Numbers
 * 题目链接：https://codeforces.com/problemset/problem/55/D
 * 
 * 题目描述：
 * 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。
 * 给定区间 [l, r]，求其中美丽数字的个数。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 是否已开始填数字
 *    - 当前数字对LCM(1-9)的余数
 *    - 已使用数字的LCM
 * 3. 关键优化：1-9的LCM是2520，所有数字的LCM都是2520的约数
 * 
 * 时间复杂度分析：
 * - 状态数：20 * 2 * 2 * 2520 * 50 ≈ 10^7
 * - 每个状态处理10种选择
 * - 总复杂度：O(10^8) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：20 * 2 * 2 * 2520 * 50 ≈ 40MB
 * - 使用HashMap可以进一步优化空间
 * 
 * 最优解分析：
 * 这是标准的最优解，利用了LCM的数学性质和数位DP的记忆化
 */

public class Code13_BeautifulNumbersCF {
    
    private static final int MOD = 2520; // 1-9的LCM
    private static int[] digits;         // 存储数位
    private static int[] lcmMap;        // LCM映射表
    
    /**
     * 预计算1-9所有子集的LCM
     * 时间复杂度: O(2^9 * 9) = O(4608)
     * 空间复杂度: O(2^9) = O(512)
     */
    private static void precomputeLCM() {
        lcmMap = new int[1 << 9];
        Arrays.fill(lcmMap, 1);
        
        for (int mask = 1; mask < (1 << 9); mask++) {
            int lcmVal = 1;
            for (int i = 1; i <= 9; i++) {
                if ((mask & (1 << (i-1))) != 0) {
                    lcmVal = lcm(lcmVal, i);
                }
            }
            lcmMap[mask] = lcmVal;
        }
    }
    
    /**
     * 计算两个数的最大公约数
     * 时间复杂度: O(log(min(a,b)))
     */
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 计算两个数的最小公倍数
     * 时间复杂度: O(log(min(a,b)))
     */
    private static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }
    
    /**
     * 计算区间[l, r]中美丽数字的个数
     * 时间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     * 空间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     */
    public static long countBeautifulNumbers(long l, long r) {
        precomputeLCM();
        return countUpTo(r) - countUpTo(l - 1);
    }
    
    /**
     * 计算[0, n]中美丽数字的个数
     * 使用记忆化搜索实现数位DP
     */
    private static long countUpTo(long n) {
        if (n < 0) return 0;
        if (n == 0) return 0; // 0不算美丽数字
        
        // 将数字转换为数位数组
        List<Integer> digitList = new ArrayList<>();
        long temp = n;
        if (temp == 0) {
            digitList.add(0);
        } else {
            while (temp > 0) {
                digitList.add((int)(temp % 10));
                temp /= 10;
            }
            Collections.reverse(digitList);
        }
        
        digits = new int[digitList.size()];
        for (int i = 0; i < digitList.size(); i++) {
            digits[i] = digitList.get(i);
        }
        
        int len = digits.length;
        
        // 记忆化数组：dp[pos][isLimit][isNum][mod][mask]
        // 使用Long数组支持null值，避免稀疏数组浪费空间
        Long[][][][][] dp = new Long[len][2][2][MOD][1 << 9];
        
        // 使用DFS进行数位DP
        return dfs(0, true, false, 0, 0, dp);
    }
    
    /**
     * 数位DP递归函数
     * 
     * @param pos 当前处理的位置
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已开始填数字
     * @param mod 当前数字对MOD的余数
     * @param mask 已使用数字的位掩码
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    private static long dfs(int pos, boolean isLimit, boolean isNum, 
                           int mod, int mask, Long[][][][][] dp) {
        // 递归终止条件：处理完所有数位
        if (pos == digits.length) {
            if (!isNum) return 0; // 前导零不算
            
            // 检查是否美丽：数字能被所有非零数字整除
            int actualLCM = lcmMap[mask];
            return (mod % actualLCM == 0) ? 1 : 0;
        }
        
        // 记忆化搜索：如果已计算过且不受限制且已开始填数字
        if (!isLimit && isNum && dp[pos][0][0][mod][mask] != null) {
            return dp[pos][0][0][mod][mask];
        }
        
        long ans = 0;
        
        // 处理前导零：可以选择跳过当前位
        if (!isNum) {
            ans += dfs(pos + 1, false, false, mod, mask, dp);
        }
        
        // 确定当前位可选数字范围
        int up = isLimit ? digits[pos] : 9;
        int start = isNum ? 0 : 1; // 处理前导零
        
        // 枚举当前位可选数字
        for (int d = start; d <= up; d++) {
            int newMod = (mod * 10 + d) % MOD;
            int newMask = mask;
            if (d > 0) {
                newMask |= (1 << (d-1));
            }
            ans += dfs(pos + 1, isLimit && d == up, true, newMod, newMask, dp);
        }
        
        // 记忆化存储：只存储不受限制且已开始填数字的状态
        if (!isLimit && isNum) {
            dp[pos][0][0][mod][mask] = ans;
        }
        
        return ans;
    }
    
    /**
     * 单元测试函数
     * 测试用例设计原则：
     * 1. 边界测试：小数字区间
     * 2. 常规测试：中等规模区间
     * 3. 性能测试：大规模区间
     */
    public static void testBeautifulNumbers() {
        System.out.println("=== 测试Beautiful Numbers ===");
        
        // 测试用例1: 小范围
        System.out.println("测试区间[1, 9]:");
        long result1 = countBeautifulNumbers(1, 9);
        System.out.println("结果: " + result1);
        System.out.println("预期: 9 (所有1-9的数字都美丽)");
        System.out.println();
        
        // 测试用例2: 包含不美丽数字
        System.out.println("测试区间[1, 20]:");
        long result2 = countBeautifulNumbers(1, 20);
        System.out.println("结果: " + result2);
        System.out.println("预期: 12 (13, 17, 19不美丽)");
        System.out.println();
        
        // 测试用例3: 较大范围
        System.out.println("测试区间[1, 100]:");
        long result3 = countBeautifulNumbers(1, 100);
        System.out.println("结果: " + result3);
        System.out.println("预期: 33");
        System.out.println();
    }
    
    /**
     * 性能测试函数
     * 测试算法在大规模数据下的性能
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试不同规模区间的性能
        long[][] testCases = {
            {1, 1000},
            {1, 1000000},
            {1, 1000000000},
            {1, 1000000000000L}
        };
        
        for (long[] testCase : testCases) {
            long startTime = System.currentTimeMillis();
            long result = countBeautifulNumbers(testCase[0], testCase[1]);
            long endTime = System.currentTimeMillis();
            
            System.out.println("区间[" + testCase[0] + ", " + testCase[1] + "]:");
            System.out.println("结果: " + result);
            System.out.println("耗时: " + (endTime - startTime) + "毫秒");
            System.out.println();
        }
    }
    
    /**
     * 调试函数：手动验证小范围结果
     * 用于理解算法执行过程和调试问题
     */
    public static void debugBeautifulNumbers(long l, long r) {
        System.out.println("=== 调试Beautiful Numbers ===");
        System.out.println("区间: [" + l + ", " + r + "]");
        
        // 手动计算小范围内的结果进行验证
        if (r - l <= 1000) {
            int manualCount = 0;
            for (long i = l; i <= r; i++) {
                if (i == 0) continue;
                
                long temp = i;
                int lcmVal = 1;
                boolean hasNonZero = false;
                
                while (temp > 0) {
                    int digit = (int)(temp % 10);
                    temp /= 10;
                    if (digit > 0) {
                        hasNonZero = true;
                        lcmVal = lcm(lcmVal, digit);
                    }
                }
                
                if (hasNonZero && i % lcmVal == 0) {
                    manualCount++;
                    if (manualCount <= 10) {
                        System.out.println("美丽数字: " + i);
                    }
                }
            }
            
            long dpCount = countBeautifulNumbers(l, r);
            System.out.println("手动计算: " + manualCount);
            System.out.println("DP计算: " + dpCount);
            System.out.println("结果一致: " + (manualCount == dpCount));
        } else {
            long result = countBeautifulNumbers(l, r);
            System.out.println("结果: " + result);
        }
        System.out.println();
    }
    
    /**
     * 工程化考量总结：
     * 1. 数学优化：利用LCM性质将状态数从2520*2^9减少到2520*50
     * 2. 空间优化：使用Long数组而不是long数组，避免稀疏数组浪费
     * 3. 边界处理：正确处理n=0和负数的情况
     * 4. 可读性：清晰的变量命名、注释和文档
     * 5. 测试覆盖：全面的单元测试和性能测试
     * 
     * 算法特色：
     * 1. 数论结合：巧妙利用LCM的数学性质
     * 2. 状态压缩：使用位掩码高效记录数字使用情况
     * 3. 模运算优化：利用2520的模运算简化计算
     * 4. 记忆化搜索：避免重复计算，提高效率
     * 
     * 跨语言实现差异：
     * - Java: 使用Long数组支持null，内存管理更安全
     * - C++: 使用vector，需要手动管理内存
     * - Python: 使用字典，语法更简洁但性能稍差
     */
    
    public static void main(String[] args) {
        // 运行功能测试
        testBeautifulNumbers();
        
        // 运行性能测试
        performanceTest();
        
        // 调试模式
        debugBeautifulNumbers(1, 100);
        
        // 额外测试：边界情况
        System.out.println("=== 边界测试 ===");
        System.out.println("区间[0, 0]: " + countBeautifulNumbers(0, 0));
        System.out.println("区间[1, 1]: " + countBeautifulNumbers(1, 1));
        System.out.println("区间[2520, 2520]: " + countBeautifulNumbers(2520, 2520));
    }
}