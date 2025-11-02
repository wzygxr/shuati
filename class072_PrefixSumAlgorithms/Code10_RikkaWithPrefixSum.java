package class046;

/**
 * Rikka with Prefix Sum (牛客网题目)
 * 
 * 题目描述:
 * 给定一个长度为n初始全为0的数列A。m次操作，要求支持以下三种操作：
 * 1. 区间加一个数v
 * 2. 全局修改，对于每一个i，把Ai改成原序列前i项的和（前缀和）
 * 3. 区间求和
 * 
 * 示例:
 * 输入:
 * 1
 * 100000 7
 * 1 1 3 1
 * 2
 * 3 2333 6666
 * 2
 * 3 2333 6666
 * 2
 * 3 2333 6666
 * 
 * 输出:
 * 13002
 * 58489497
 * 12043005
 * 
 * 提示:
 * 1 ≤ n,m ≤ 10^5
 * 1 ≤ L ≤ R ≤ n
 * 0 ≤ v ≤ 10^9
 * 查询操作不超过500次
 * 
 * 解题思路:
 * 使用差分数组和组合数学来优化操作。
 * 1. 对于操作1（区间加），使用差分数组记录
 * 2. 对于操作2（前缀和），记录前缀和操作的次数
 * 3. 对于操作3（区间求和），使用组合数学公式计算结果
 * 
 * 核心思想：如果在(i,j)点+w，那么(x,y)点的系数为C(x-i+y-j-1, x-i-1)
 * 
 * 时间复杂度: O(m * 查询次数)
 * 空间复杂度: O(m)
 * 
 * 这是一个高级的前缀和应用题目，结合了差分数组和组合数学。
 */
public class Code10_RikkaWithPrefixSum {
    private static final int MOD = 998244353;
    private static final int N = 200010;
    
    // 预计算阶乘和逆元
    private static long[] fac = new long[N];
    private static long[] inv = new long[N];
    
    static {
        // 预处理阶乘
        fac[0] = 1;
        for (int i = 1; i < N; i++) {
            fac[i] = fac[i - 1] * i % MOD;
        }
        
        // 预处理逆元
        inv[N - 1] = qmod(fac[N - 1], MOD - 2);
        for (int i = N - 2; i >= 0; i--) {
            inv[i] = inv[i + 1] * (i + 1) % MOD;
        }
    }
    
    /**
     * 快速幂运算
     * 
     * @param x 底数
     * @param y 指数
     * @return (x^y) % MOD
     */
    private static long qmod(long x, long y) {
        x %= MOD;
        long ans = 1;
        while (y > 0) {
            if ((y & 1) == 1) {
                ans = ans * x % MOD;
            }
            x = x * x % MOD;
            y >>= 1;
        }
        return ans;
    }
    
    /**
     * 计算组合数C(a,b)
     * 
     * @param a 组合数上标
     * @param b 组合数下标
     * @return C(a,b) % MOD
     */
    private static long C(int a, int b) {
        if (b > a || b < 0) {
            return 0;
        }
        return fac[a] * inv[b] % MOD * inv[a - b] % MOD;
    }
    
    /**
     * 主要解法函数
     * 
     * @param n 数组长度
     * @param operations 操作数组
     * @return 查询结果数组
     */
    public static long[] solve(int n, int[][] operations) {
        // 记录操作1的信息
        // 每个操作存储：[操作次数, 位置, 值]
        int[][] ops = new int[operations.length][3];
        int opCount = 0;
        
        // 当前前缀和操作次数
        int prefixSumCount = 1;
        
        // 结果数组
        long[] results = new long[operations.length];
        int resultCount = 0;
        
        for (int[] op : operations) {
            if (op[0] == 1) {
                // 操作1：区间加一个数
                int l = op[1];
                int r = op[2];
                int v = op[3];
                
                // 使用差分数组思想记录操作
                ops[opCount][0] = prefixSumCount - 1;  // 记录当前前缀和操作次数
                ops[opCount][1] = l;                   // 起始位置
                ops[opCount][2] = v % MOD;             // 值
                opCount++;
                
                ops[opCount][0] = prefixSumCount - 1;  // 记录当前前缀和操作次数
                ops[opCount][1] = r + 1;               // 结束位置+1
                ops[opCount][2] = -(v % MOD);          // 负值
                opCount++;
            } else if (op[0] == 2) {
                // 操作2：全局前缀和
                prefixSumCount++;
            } else {
                // 操作3：区间求和
                int l = op[1];
                int r = op[2];
                
                // 计算区间和
                long ans = (fun(prefixSumCount + 1, r, ops, opCount) - 
                           fun(prefixSumCount + 1, l - 1, ops, opCount)) % MOD;
                ans = (ans + MOD) % MOD;  // 确保结果为正
                
                results[resultCount++] = ans;
            }
        }
        
        // 返回实际结果数组
        long[] finalResults = new long[resultCount];
        System.arraycopy(results, 0, finalResults, 0, resultCount);
        return finalResults;
    }
    
    /**
     * 辅助函数，计算贡献值
     * 
     * @param x x坐标
     * @param y y坐标
     * @param ops 操作数组
     * @param opCount 操作数量
     * @return 贡献值
     */
    private static long fun(int x, int y, int[][] ops, int opCount) {
        long ans = 0;
        for (int i = 0; i < opCount; i++) {
            if (ops[i][0] < x && ops[i][1] <= y) {
                ans = (ans + C(x - ops[i][0] + y - ops[i][1] - 1, x - ops[i][0] - 1) * 
                      (long) ops[i][2]) % MOD;
            }
        }
        return ans;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 简化测试用例
        int n = 5;
        int[][] operations = {
            {1, 1, 3, 1},  // 区间[1,3]加1
            {2},           // 前缀和操作
            {3, 2, 4}      // 查询区间[2,4]的和
        };
        
        long[] results = solve(n, operations);
        
        System.out.println("测试结果:");
        for (long result : results) {
            System.out.println(result);
        }
    }
}