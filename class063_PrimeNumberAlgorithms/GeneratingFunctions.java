// 生成函数和组合计数（Burnside引理/Polya定理）的Java实现
// 时间复杂度：根据具体问题而定
// 空间复杂度：根据具体问题而定

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratingFunctions {
    // 生成函数 - 多项式乘法（普通生成函数）
    // 时间复杂度：O(n²)
    public static List<Long> multiplyPolynomials(List<Long> a, List<Long> b) {
        List<Long> res = new ArrayList<>(a.size() + b.size() - 1);
        for (int i = 0; i < a.size() + b.size() - 1; i++) {
            res.add(0L);
        }
        
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                res.set(i + j, res.get(i + j) + a.get(i) * b.get(j));
            }
        }
        
        return res;
    }
    
    // 生成函数 - 计算组合数 C(n, k)
    // 使用动态规划方法，时间复杂度：O(nk)
    public static long[][] computeCombinations(int n, int k) {
        long[][] C = new long[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            C[i][0] = 1;
            for (int j = 1; j <= Math.min(i, k); j++) {
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }
        return C;
    }
    
    // 快速幂算法
    public static long power(long a, long b) {
        long res = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                res *= a;
            }
            a *= a;
            b /= 2;
        }
        return res;
    }
    
    // 快速幂算法（模运算）
    public static long powerMod(long a, long b, long mod) {
        long res = 1;
        a %= mod;
        while (b > 0) {
            if (b % 2 == 1) {
                res = (res * a) % mod;
            }
            a = (a * a) % mod;
            b /= 2;
        }
        return res;
    }
    
    // 计算欧拉函数 φ(n)
    public static long eulerPhi(long n) {
        long res = n;
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) {
                    n /= p;
                }
                res -= res / p;
            }
        }
        if (n > 1) {
            res -= res / n;
        }
        return res;
    }
    
    // 扩展欧几里得算法
    public static long extendedGcd(long a, long b, long[] xy) {
        if (b == 0) {
            xy[0] = 1;
            xy[1] = 0;
            return a;
        }
        long[] xy1 = new long[2];
        long g = extendedGcd(b, a % b, xy1);
        xy[0] = xy1[1];
        xy[1] = xy1[0] - (a / b) * xy1[1];
        return g;
    }
    
    // 模逆元
    public static long modInverse(long a, long mod) {
        long[] xy = new long[2];
        long g = extendedGcd(a, mod, xy);
        if (g != 1) {
            return -1; // 不存在逆元
        }
        return (xy[0] % mod + mod) % mod;
    }
    
    // Burnside引理：计算等价类的数量
    // 给定置换群的大小m和每个置换的不动点数目，计算等价类数目
    public static long burnside(long m, List<Long> fixedPoints) {
        long sum = 0;
        for (long fp : fixedPoints) {
            sum += fp;
        }
        return sum / m;
    }
    
    // Polya定理：计算涂色方案数
    // n: 物体数量
    // k: 颜色数量
    // rotations: 旋转置换的循环分解
    public static long polya(int n, int k, List<Integer> rotations) {
        long sum = 0;
        for (int cycles : rotations) {
            sum += power(k, cycles);
        }
        return sum / rotations.size();
    }
    
    // 项链问题：计算用k种颜色涂色n个珠子的项链的不同方案数
    // 考虑旋转等价
    public static long necklace(int n, int k) {
        long sum = 0;
        for (int d = 1; d <= n; d++) {
            if (n % d == 0) {
                sum += eulerPhi(d) * power(k, n / d);
            }
        }
        return sum / n;
    }
    
    // 手镯问题：计算用k种颜色涂色n个珠子的手镯的不同方案数
    // 考虑旋转和平移等价
    public static long bracelet(int n, int k) {
        long sum = 0;
        // 旋转等价部分
        for (int d = 1; d <= n; d++) {
            if (n % d == 0) {
                sum += eulerPhi(d) * power(k, n / d);
            }
        }
        
        // 翻转等价部分
        if (n % 2 == 0) {
            // 偶数情况：n/2个翻转经过两个珠子，n/2个翻转经过两个对中心点
            sum += (n / 2) * power(k, n / 2 + 1);
            sum += (n / 2) * power(k, n / 2);
        } else {
            // 奇数情况：n个翻转都经过一个珠子和一个对中心点
            sum += n * power(k, (n + 1) / 2);
        }
        
        return sum / (2 * n);
    }
    
    // 指数生成函数乘法
    public static List<Long> multiplyExponential(List<Long> a, List<Long> b) {
        List<Long> res = new ArrayList<>(a.size() + b.size() - 1);
        for (int i = 0; i < a.size() + b.size() - 1; i++) {
            res.add(0L);
        }
        
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                res.set(i + j, res.get(i + j) + a.get(i) * b.get(j));
            }
        }
        
        return res;
    }
    
    // 计算阶乘和阶乘的逆元
    public static void computeFactorials(int n, long[] fact, long[] invFact, long mod) {
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i - 1] * i) % mod;
        }
        invFact[n] = powerMod(fact[n], mod - 2, mod);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = (invFact[i + 1] * (i + 1)) % mod;
        }
    }
    
    // 组合数 C(n, k) 模运算
    public static long combMod(int n, int k, long[] fact, long[] invFact, long mod) {
        if (k < 0 || k > n) return 0;
        return fact[n] * invFact[k] % mod * invFact[n - k] % mod;
    }
    
    // 打印多项式
    public static void printPolynomial(List<Long> poly, String name) {
        System.out.print(name + ": ");
        boolean first = true;
        for (int i = 0; i < poly.size(); i++) {
            long coeff = poly.get(i);
            if (coeff != 0) {
                if (!first) {
                    if (coeff > 0) {
                        System.out.print(" + ");
                    } else {
                        System.out.print(" - ");
                        coeff = -coeff;
                    }
                } else {
                    if (coeff < 0) {
                        System.out.print("-");
                        coeff = -coeff;
                    }
                    first = false;
                }
                if (coeff != 1 || i == 0) {
                    System.out.print(coeff);
                }
                if (i > 0) {
                    System.out.print("x^" + i);
                }
            }
        }
        System.out.println();
    }
    
    // 力扣第1758题：生成交替二进制字符串的最少操作次数
    public static int minChanges(String s) {
        int changesStart0 = 0; // 以0开头的交替字符串需要的最少修改次数
        int changesStart1 = 0; // 以1开头的交替字符串需要的最少修改次数
        
        for (int i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {
                // 偶数位置
                if (s.charAt(i) == '1') changesStart0++;
                else changesStart1++;
            } else {
                // 奇数位置
                if (s.charAt(i) == '0') changesStart0++;
                else changesStart1++;
            }
        }
        
        return Math.min(changesStart0, changesStart1);
    }
    
    // 力扣第46题：全排列
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, new boolean[nums.length]);
        return result;
    }
    
    private static void backtrack(List<List<Integer>> result, List<Integer> current, int[] nums, boolean[] used) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(nums[i]);
            backtrack(result, current, nums, used);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
    
    // 力扣第77题：组合
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackCombine(result, new ArrayList<>(), 1, n, k);
        return result;
    }
    
    private static void backtrackCombine(List<List<Integer>> result, List<Integer> current, int start, int n, int k) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = start; i <= n; i++) {
            current.add(i);
            backtrackCombine(result, current, i + 1, n, k);
            current.remove(current.size() - 1);
        }
    }
    
    public static void main(String[] args) {
        // 测试多项式乘法（普通生成函数）
        System.out.println("=== 普通生成函数测试 ===");
        List<Long> a = Arrays.asList(1L, 2L, 3L); // 1 + 2x + 3x^2
        List<Long> b = Arrays.asList(4L, 5L, 6L); // 4 + 5x + 6x^2
        
        printPolynomial(a, "多项式A");
        printPolynomial(b, "多项式B");
        
        List<Long> product = multiplyPolynomials(a, b);
        printPolynomial(product, "乘积");
        
        // 测试组合数计算
        System.out.println("\n=== 组合数计算测试 ===");
        int n = 5, k = 3;
        long[][] C = computeCombinations(n, k);
        System.out.println("C(5, 3) = " + C[5][3]);
        
        // 测试Burnside引理
        System.out.println("\n=== Burnside引理测试 ===");
        List<Long> fixedPoints = Arrays.asList(4L, 0L, 0L, 0L); // 正方形的4个旋转置换的不动点数
        long equivalenceClasses = burnside(4, fixedPoints);
        System.out.println("等价类数目（正方形旋转）: " + equivalenceClasses);
        
        // 测试Polya定理
        System.out.println("\n=== Polya定理测试 ===");
        List<Integer> rotations = Arrays.asList(4, 1, 2, 1); // 正方形的4个旋转置换的循环数
        long colorings = polya(4, 2, rotations);
        System.out.println("用2种颜色给正方形顶点涂色的方案数: " + colorings);
        
        // 测试项链问题
        System.out.println("\n=== 项链问题测试 ===");
        int beads = 5; // 5个珠子
        int colors = 3; // 3种颜色
        long necklaceCount = necklace(beads, colors);
        long braceletCount = bracelet(beads, colors);
        System.out.println(beads + "个珠子，" + colors + "种颜色的项链方案数: " + necklaceCount);
        System.out.println(beads + "个珠子，" + colors + "种颜色的手镯方案数: " + braceletCount);
        
        // 测试力扣第1758题
        System.out.println("\n=== 力扣第1758题测试 ===");
        String test1 = "0100";
        String test2 = "10";
        String test3 = "1111";
        System.out.println("输入: \"" + test1 + "\"，最少操作次数: " + minChanges(test1));
        System.out.println("输入: \"" + test2 + "\"，最少操作次数: " + minChanges(test2));
        System.out.println("输入: \"" + test3 + "\"，最少操作次数: " + minChanges(test3));
        
        // 测试力扣第46题
        System.out.println("\n=== 力扣第46题测试 ===");
        int[] nums = {1, 2, 3};
        List<List<Integer>> permutations = permute(nums);
        System.out.println("全排列结果: " + permutations);
        
        // 测试力扣第77题
        System.out.println("\n=== 力扣第77题测试 ===");
        List<List<Integer>> combinations = combine(4, 2);
        System.out.println("组合结果: " + combinations);
        
        /*
         * 生成函数和组合计数算法总结：
         * 
         * 1. 普通生成函数：
         *    - 用于计数组合问题，如物品选择、整数分拆等
         *    - 多项式乘法对应组合的合并
         *    - 时间复杂度：多项式乘法O(n²)，可以使用FFT优化到O(n log n)
         * 
         * 2. 指数生成函数：
         *    - 用于排列问题，考虑顺序的组合
         *    - 乘法规则与普通生成函数不同
         * 
         * 3. Burnside引理：
         *    - 计算群作用下的等价类数目
         *    - 公式：等价类数目 = (1/|G|) * Σ(不动点数目)
         *    - 适用于解决对称性计数问题
         * 
         * 4. Polya定理：
         *    - Burnside引理的特例，针对置换群作用下的计数问题
         *    - 特别适用于涂色问题
         *    - 公式：方案数 = (1/|G|) * Σ(k^c(π))，其中c(π)是置换π的循环数
         * 
         * 应用场景：
         * 1. 组合数学中的计数问题
         * 2. 离散数学中的群论应用
         * 3. 密码学中的哈希函数设计
         * 4. 计算机图形学中的对称性检测
         * 5. 分子生物学中的序列分析
         * 
         * 相关题目：
         * 1. 力扣第77题：组合 - 组合问题
         * 2. 力扣第46题：全排列 - 排列问题
         * 3. Burnside引理/Polya定理相关问题 - 对称计数问题
         */
    }
}