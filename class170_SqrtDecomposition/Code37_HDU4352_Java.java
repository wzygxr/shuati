/**
 * HDU 4352 XHXJ's LIS
 * 题目要求：计算区间内数位LIS长度等于k的数的个数
 * 核心技巧：数位DP + 分块状态压缩
 * 时间复杂度：O(len(digits) * 2^10 * 10)
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=4352
 * 
 * 算法思想详解：
 * 1. 数位DP处理大数范围查询
 * 2. 使用二进制状态压缩表示当前LIS状态
 * 3. 分块处理状态转移，优化计算
 * 4. 利用记忆化搜索避免重复计算
 */

import java.util.*;

public class Code37_HDU4352_Java {
    private int k; // LIS长度要求
    private long l, r; // 查询区间
    private int[] digits; // 当前处理的数字的各位
    private long[][][] dp; // dp[pos][status][limit]：位置pos，状态status，是否有限制
    private static final int MAX_DIGITS = 20; // 最大位数
    private static final int MAX_STATUS = 1 << 10; // 状态数（10个数字）
    
    /**
     * 构造函数，初始化问题参数
     */
    public Code37_HDU4352_Java(long l, long r, int k) {
        this.l = l;
        this.r = r;
        this.k = k;
        this.dp = new long[MAX_DIGITS][MAX_STATUS][2];
    }
    
    /**
     * 计算从0到n的满足条件的数的个数
     */
    public long solve() {
        // 计算[0, r] - [0, l-1]
        return calculate(r) - calculate(l - 1);
    }
    
    /**
     * 计算从0到x的满足条件的数的个数
     */
    private long calculate(long x) {
        if (x < 0) {
            return 0;
        }
        
        // 将x转换为数字数组
        List<Integer> digitList = new ArrayList<>();
        if (x == 0) {
            digitList.add(0);
        } else {
            while (x > 0) {
                digitList.add((int)(x % 10));
                x /= 10;
            }
        }
        
        // 反转以获得正确的顺序（高位到低位）
        Collections.reverse(digitList);
        digits = new int[digitList.size()];
        for (int i = 0; i < digitList.size(); i++) {
            digits[i] = digitList.get(i);
        }
        
        // 初始化dp数组为-1（表示未计算）
        for (int i = 0; i < MAX_DIGITS; i++) {
            for (int j = 0; j < MAX_STATUS; j++) {
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
            }
        }
        
        // 开始数位DP
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数位DP的DFS实现
     * 
     * @param pos 当前处理的位置
     * @param status 当前LIS的状态（二进制压缩）
     * @param leadingZero 是否前导零
     * @param limit 当前位是否受原数限制
     * @return 满足条件的数的个数
     */
    private long dfs(int pos, int status, boolean leadingZero, boolean limit) {
        // 已经处理完所有位
        if (pos == digits.length) {
            // 如果是前导零（即数字0），则其LIS长度为0
            // 否则计算状态中的二进制中1的个数
            if (leadingZero) {
                return k == 0 ? 1 : 0;
            }
            return getLISLength(status) == k ? 1 : 0;
        }
        
        // 如果有前导零，单独处理（此时状态为0）
        if (leadingZero) {
            // 计算选0的情况（前导零继续）
            long res = dfs(pos + 1, 0, true, limit && (digits[pos] == 0));
            
            // 计算选非0的情况（前导零结束）
            int maxDigit = limit ? digits[pos] : 9;
            for (int d = 1; d <= maxDigit; d++) {
                res += dfs(pos + 1, getNewStatus(0, d), false, limit && (d == maxDigit));
            }
            
            return res;
        }
        
        // 检查是否已经计算过这个状态
        int limitCode = limit ? 1 : 0;
        if (dp[pos][status][limitCode] != -1) {
            return dp[pos][status][limitCode];
        }
        
        long res = 0;
        int maxDigit = limit ? digits[pos] : 9;
        
        // 尝试每一个可能的数字
        for (int d = 0; d <= maxDigit; d++) {
            int newStatus = getNewStatus(status, d);
            res += dfs(pos + 1, newStatus, false, limit && (d == maxDigit));
        }
        
        // 记忆化存储结果
        dp[pos][status][limitCode] = res;
        return res;
    }
    
    /**
     * 根据当前状态和新数字，计算新的LIS状态
     * 
     * @param status 当前状态（二进制压缩，每一位表示是否存在该数字）
     * @param d 新数字
     * @return 新状态
     */
    private int getNewStatus(int status, int d) {
        // 状态为二进制位掩码，其中状态的二进制表示中1的位置代表当前LIS中的数字
        // 例如：状态0b1010表示当前LIS中的数字是1和3
        
        // 找到d应该插入的位置（在LIS中找第一个比d大的位置）
        int tmp = status;
        for (int i = d; i < 10; i++) {
            if ((tmp & (1 << i)) != 0) {
                // 找到了第一个比d大的数字，替换它
                tmp ^= (1 << i);
                break;
            }
        }
        // 将d添加到状态中
        tmp |= (1 << d);
        return tmp;
    }
    
    /**
     * 计算状态对应的LIS长度
     * 
     * @param status 状态
     * @return LIS长度（二进制中1的个数）
     */
    private int getLISLength(int status) {
        return Integer.bitCount(status);
    }
    
    /**
     * 运行标准测试
     */
    public static void runTest() {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt(); // 测试用例数量
        
        for (int caseNum = 1; caseNum <= t; caseNum++) {
            long l = scanner.nextLong();
            long r = scanner.nextLong();
            int k = scanner.nextInt();
            
            Code37_HDU4352_Java solution = new Code37_HDU4352_Java(l, r, k);
            long result = solution.solve();
            
            System.out.println("Case #" + caseNum + ": " + result);
        }
        
        scanner.close();
    }
    
    /**
     * 正确性测试
     */
    public static void correctnessTest() {
        System.out.println("=== 正确性测试 ===");
        
        // 测试用例
        long[][] testCases = {
            {1, 10, 1},     // 1-10中LIS长度为1的数：1,2,3,4,5,6,7,8,9,10 → 10个
            {1, 100, 2},    // 1-100中LIS长度为2的数：大部分两位数
            {10, 30, 2}     // 10-30中LIS长度为2的数：10,12,13,...,30
        };
        
        for (long[] test : testCases) {
            long l = test[0];
            long r = test[1];
            int k = (int)test[2];
            
            Code37_HDU4352_Java solution = new Code37_HDU4352_Java(l, r, k);
            long result = solution.solve();
            
            System.out.printf("区间[%d, %d]中LIS长度为%d的数的个数：%d\n", 
                l, r, k, result);
        }
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试不同规模的数据
        long[][] testCases = {
            {1, 1000},              // 小规模
            {1, 1000000},           // 中等规模
            {1, 10000000000L}       // 大规模
        };
        
        for (long[] test : testCases) {
            long l = test[0];
            long r = test[1];
            int k = 3; // 固定k=3
            
            Code37_HDU4352_Java solution = new Code37_HDU4352_Java(l, r, k);
            
            long startTime = System.currentTimeMillis();
            long result = solution.solve();
            long endTime = System.currentTimeMillis();
            
            System.out.printf("区间[%d, %d], k=%d => 结果=%d, 耗时=%d ms\n", 
                l, r, k, result, endTime - startTime);
        }
    }
    
    /**
     * 状态转移演示
     */
    public static void stateTransitionDemo() {
        System.out.println("=== 状态转移演示 ===");
        
        Code37_HDU4352_Java demo = new Code37_HDU4352_Java(0, 0, 0);
        
        // 演示几个状态转移的例子
        System.out.println("状态转移示例：");
        
        int[][] examples = {
            {0, 3},   // 初始状态0，添加数字3
            {0b1000, 2}, // 状态0b1000（表示有数字3），添加数字2
            {0b1100, 1}, // 状态0b1100（表示有数字3和2），添加数字1
            {0b1110, 4}  // 状态0b1110（表示有数字3、2和1），添加数字4
        };
        
        for (int[] example : examples) {
            int status = example[0];
            int digit = example[1];
            int newStatus = demo.getNewStatus(status, digit);
            
            System.out.printf("状态 %s (长度=%d), 添加数字 %d → 新状态 %s (长度=%d)\n", 
                Integer.toBinaryString(status), Integer.bitCount(status),
                digit, 
                Integer.toBinaryString(newStatus), Integer.bitCount(newStatus));
        }
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        System.out.println("HDU 4352 XHXJ's LIS 解决方案");
        System.out.println("1. 运行标准测试（按题目输入格式）");
        System.out.println("2. 运行正确性测试");
        System.out.println("3. 运行性能测试");
        System.out.println("4. 查看状态转移演示");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("请选择测试类型：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                runTest();
                break;
            case 2:
                correctnessTest();
                break;
            case 3:
                performanceTest();
                break;
            case 4:
                stateTransitionDemo();
                break;
            default:
                System.out.println("无效选择，运行正确性测试");
                correctnessTest();
                break;
        }
        
        scanner.close();
    }
    
    /**
     * 时间复杂度分析：
     * - 数位DP的状态数：O(len * 2^10 * 2)，其中len是数字的最大位数（约20位）
     * - 每个状态的转移次数：O(10)（每个数字有0-9共10种可能）
     * - 总时间复杂度：O(20 * 1024 * 2 * 10) = O(409600)，这是一个非常小的常数
     * 
     * 空间复杂度分析：
     * - dp数组：O(len * 2^10 * 2) = O(20 * 1024 * 2) = O(40960)，空间占用很小
     * - 其他辅助数组：O(len)
     * - 总空间复杂度：O(40960 + len)
     * 
     * 算法优化技巧：
     * 1. 状态压缩：使用二进制位掩码表示LIS状态
     * 2. 记忆化搜索：避免重复计算相同状态
     * 3. 分块处理：将复杂的状态转移分解为多个简单步骤
     * 4. 前导零处理：单独处理前导零情况，避免影响LIS计算
     * 5. 边界处理：仔细处理数位限制条件
     * 
     * 最优解分析：
     * 这是一个典型的数位DP问题，使用状态压缩和记忆化搜索是最优解法
     * 时间复杂度远低于暴力枚举法的O(r-l+1)
     * 对于大数范围的查询，这种方法是必要的
     */
}