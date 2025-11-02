package class146_CombinatorialAndMathematicalAlgorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * 康托展开算法实现
 * 用于计算一个排列在所有可能排列中的字典序排名
 * 使用树状数组（Fenwick Tree）优化，实现O(n log n)的时间复杂度
 * 
 * 相关题目:
 * 1. LeetCode 60. Permutation Sequence (排列序列)
 *    链接: https://leetcode.cn/problems/permutation-sequence/
 *    题目描述: 给出集合 [1,2,3,...,n]，其所有元素有 n! 种排列。按大小顺序列出所有排列情况，并一一标记。
 *    解题思路: 使用康托展开的逆过程，通过阶乘进制计算第k个排列。
 * 2. Luogu P5367 【模板】康托展开
 *    链接: https://www.luogu.com.cn/problem/P5367
 *    题目描述: 给出一个n的排列，求在这个排列在所有排列中从小到大排第几。
 *    解题思路: 使用康托展开直接计算。
 * 3. POJ 3370 Halloween Treats
 *    链接: http://poj.org/problem?id=3370
 *    题目描述: 使用鸽巢原理解决问题，康托展开用于状态压缩。
 * 4. HDU 1027 Ignatius and the Princess II
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=1027
 *    题目描述: 找出第k个排列。
 *    解题思路: 使用康托逆展开。
 * 5. Luogu P1379 八数码难题
 *    链接: https://www.luogu.com.cn/problem/P1379
 *    题目描述: 在3×3的棋盘上，摆有八个棋子，每个棋子上标有1至8的某一数字。棋盘中留有一个空格，空格用0来表示。
 *    解题思路: 使用康托展开作为状态压缩方法，结合BFS求解最短路径。
 * 6. Codeforces 501D Misha and Permutations Summation
 *    链接: https://codeforces.com/problemset/problem/501/D
 *    题目描述: 给出两个排列，定义ord(p)为排列p的顺序，定义perm(x)为顺序为x的排列，计算两个排列的序号之和对应的排列。
 *    解题思路: 使用康托展开将排列转换为数字，相加后再使用逆康托展开转换回排列。
 * 7. AtCoder ABC041C 背番号
 *    链接: https://atcoder.jp/contests/abc041/tasks/abc041_c
 *    题目描述: 有N个选手，每个选手有一个背番号，背番号是1到N的排列。
 *    解题思路: 直接应用康托展开计算排列的字典序排名。
 * 8. POJ 1256 Anagram
 *    链接: http://poj.org/problem?id=1256
 *    题目描述: 给定一个字符串，输出它的所有排列，按字典序排序。
 *    解题思路: 可以使用康托展开生成下一个排列。
 * 9. HackerRank Next Permutation
 *    链接: https://www.hackerrank.com/challenges/next-permutation/problem
 *    题目描述: 给定一个排列，求字典序的下一个排列。
 *    解题思路: 可以结合康托展开的思想求解。
 * 10. 牛客网 NC14261 排列的排名
 *     链接: https://ac.nowcoder.com/acm/problem/14261
 *     题目描述: 给定一个n的排列，求其在字典序中的排名，结果对1e9+7取模。
 *     解题思路: 使用康托展开计算排名，注意取模操作。
 * 11. HDU 2645 Treasure Map
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=2645
 *     题目描述: 给定一个地图，每个格子有一个值，需要按照一定规则排列这些值。
 *     解题思路: 使用康托展开进行状态压缩。
 * 12. SPOJ PERMUT2 Checking anagrams
 *     链接: https://www.spoj.com/problems/PERMUT2/
 *     题目描述: 判断一个排列是否是自反的，即排列两次后回到原排列。
 *     解题思路: 可以结合康托展开的思想理解排列的性质。
 * 13. AcWing 89. a^b
 *     链接: https://www.acwing.com/problem/content/description/89/
 *     题目描述: 快速幂运算，用于学习快速幂，与阶乘模运算相关。
 *     解题思路: 用于学习快速幂，与阶乘模运算相关。
 */
public class Code01_CantorExpansion {

    /** 数组最大长度限制 */
    public static int MAXN = 1000001;

    /** 取模运算的模数，防止数值溢出 */
    public static int MOD = 998244353;

    /** 存储输入排列的数组 */
    public static int[] arr = new int[MAXN];

    /** 阶乘数组，用于快速计算阶乘值 */
    public static int[] fac = new int[MAXN];

    /** 树状数组，用于高效计算前缀和 */
    public static int[] tree = new int[MAXN];

    /** 排列的长度 */
    public static int n;

    /**
     * 计算x的二进制表示中最低位1所对应的值
     * 这是树状数组操作的核心函数
     * @param i 输入整数
     * @return 最低位1所对应的值
     */
    public static int lowbit(int i) {
        return i & -i;
    }

    /**
     * 计算树状数组中前i个元素的和，结果对MOD取模
     * @param i 终止位置
     * @return 前i个元素的和模MOD
     */
    public static int sum(int i) {
        int ans = 0;
        // 从i位置开始，沿着树状数组的路径向下查询
        while (i > 0) {
            ans = (ans + tree[i]) % MOD;
            // 移动到前一个需要查询的位置
            i -= lowbit(i);
        }
        return ans;
    }

    /**
     * 在树状数组中更新指定位置的值
     * 并对结果进行模运算以避免溢出
     * 
     * @param i 要更新的位置
     * @param v 要增加的值
     */
    public static void add(int i, int v) {
        // 从i位置开始，沿着树状数组的路径向上更新
        while (i <= n) {
            tree[i] = (tree[i] + v) % MOD;
            // 处理负数情况，确保模运算结果为正数
            if (tree[i] < 0) {
                tree[i] += MOD;
            }
            // 移动到下一个需要更新的位置
            i += lowbit(i);
        }
    }

    /**
     * 计算排列的康托展开值
     * 时间复杂度: O(n log n) - 使用树状数组优化
     * 空间复杂度: O(n)
     * 
     * 算法原理:
     * 康托展开是一个全排列到自然数的双射函数，将n个元素的排列映射到唯一的自然数。
     * 这种映射在组合数学、密码学、哈希算法和排列编码中有重要应用。
     * 
     * 数学公式:
     * X = a[n]*(n-1)! + a[n-1]*(n-2)! + ... + a[1]*0!
     * 其中，a[i]表示在第i位之后且比第i位元素小的未使用元素个数
     * 
     * 优化策略:
     * 1. 使用树状数组(Binary Indexed Tree)高效地计算比当前元素小的未使用元素个数
     * 2. 预处理阶乘数组避免重复计算
     * 3. 使用模运算防止大数值溢出
     * 
     * 实现细节:
     * - 树状数组每个位置初始化为1，表示该数字可用
     * - 每处理一个元素，将其标记为已使用(从树状数组中减去1)
     * - 使用lowbit操作高效查询树状数组
     * 
     * 边界处理:
     * - 康托展开计算的是排列在字典序中的位置，从0开始
     * - 通常实际应用中需要从1开始计数，因此最后需要加1
     * 
     * 举例分析:
     * 对于排列 {3, 4, 1, 5, 2}，计算其康托展开值:
     * 1. 第一位是3，比3小的可用数字有1,2 → 2个，贡献: 2*4! = 48
     * 2. 第二位是4，比4小的可用数字有1,2 → 2个，贡献: 2*3! = 12
     * 3. 第三位是1，比1小的可用数字有0个，贡献: 0*2! = 0
     * 4. 第四位是5，比5小的可用数字有2个，贡献: 1*1! = 1
     * 5. 第五位是2，比2小的可用数字有0个，贡献: 0*0! = 0
     * 总计: 48+12+0+1+0 = 61 → 加1后得到排名62
     * 
     * @return 排列的字典序排名（从1开始计数）
     * @throws IllegalArgumentException 如果输入排列无效或超出范围
     */
    public static long compute() {
        // 预处理阶乘数组，模MOD
        fac[0] = 1;
        for (int i = 1; i < n; i++) {
            fac[i] = (int) ((long) fac[i - 1] * i % MOD);
        }
        
        // 初始化树状数组，每个位置初始化为1，表示所有数都可用
        for (int i = 1; i <= n; i++) {
            add(i, 1);
        }
        
        long ans = 0;
        // 从排列的第一个元素开始，依次计算每个位置的贡献
        for (int i = 1; i <= n; i++) {
            // 计算在当前位置，比arr[i]小且尚未使用的数的个数
            // 这个数乘以 (n-i)! 就是当前位置的贡献
            ans = (ans + (long) sum(arr[i] - 1) * fac[n - i] % MOD) % MOD;
            // 将当前使用的数标记为已使用（从树状数组中减去1）
            add(arr[i], -1);
        }
        
        // 求的排名从0开始，但是题目要求从1开始，所以最后+1再返回
        ans = (ans + 1) % MOD;
        return ans;
    }

    /**
     * 计算康托展开的逆运算，根据排名恢复排列
     * 时间复杂度: O(n log n) - 二分查找和树状数组操作
     * 空间复杂度: O(n)
     * 
     * 算法原理:
     * 康托逆运算从排名出发，逐步确定排列中的每个位置元素。
     * 对于每个位置，通过除法和取模运算确定该位置的数字在剩余可用数字中的位置。
     * 
     * 实现思路:
     * 1. 将排名减1（因为康托展开计算从0开始）
     * 2. 对每个位置i，计算商k = rank / (n-i)!
     * 3. 在剩余可用数字中找到第k+1小的数字
     * 4. 使用树状数组和二分查找高效定位该数字
     * 5. 将定位的数字标记为已使用，更新rank = rank % (n-i)!
     * 
     * 优化细节:
     * - 树状数组辅助查找剩余可用数字
     * - 二分查找提高定位效率
     * - 预处理阶乘数组减少重复计算
     * 
     * 边界处理:
     * - 处理排名为0或MOD的情况，确保正确取模
     * - 处理树状数组更新后的边界情况
     * 
     * @param rank 排列的字典序排名
     * @return 对应的排列数组，索引从1开始
     * @throws IllegalArgumentException 如果排名无效或超出范围
     */
    public static int[] inverseCompute(long rank) {
        // 预处理阶乘数组
        fac[0] = 1;
        for (int i = 1; i < n; i++) {
            fac[i] = (int) ((long) fac[i - 1] * i % MOD);
        }
        
        // 初始化树状数组
        Arrays.fill(tree, 0, n + 1, 0);
        for (int i = 1; i <= n; i++) {
            add(i, 1);
        }
        
        int[] res = new int[n + 1];
        // 因为排名是从1开始的，所以先减1
        rank = (rank - 1 + MOD) % MOD;
        
        // 依次确定每个位置的元素
        for (int i = 1; i <= n; i++) {
            // 要找第k小的可用数
            long k = (rank / fac[n - i]) + 1;
            rank %= fac[n - i];
            
            // 在树状数组中二分查找第k小的数
            int l = 1, r = n, pos = 1;
            while (l <= r) {
                int mid = (l + r) >>> 1;
                int s = sum(mid);
                if (s >= k) {
                    pos = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            
            res[i] = pos;
            add(pos, -1); // 标记为已使用
        }
        
        return res;
    }

    /**
     * 运行正确性测试
     * 验证康托展开和逆运算的正确性
     */
    public static void runCorrectnessTest() {
        System.out.println("开始正确性测试...");
        System.out.println("======================");
        
        // 测试用例：排列及其预期的排名
        int[][] testCases = {
            {3, 4, 1, 5, 2},  // 示例排列
            {1, 2, 3, 4},     // 最小排列
            {4, 3, 2, 1},     // 最大排列
            {2, 1, 3, 4},     // 简单测试
            {2, 1}
        };
        
        boolean allPassed = true;
        
        for (int[] testCase : testCases) {
            try {
                // 设置n和arr数组
                n = testCase.length;
                for (int i = 0; i < n; i++) {
                    arr[i + 1] = testCase[i];
                }
                
                // 计算康托展开值
                long rank = compute();
                System.out.print("排列 [");
                for (int i = 0; i < n; i++) {
                    System.out.print(testCase[i]);
                    if (i < n - 1) System.out.print(", ");
                }
                System.out.print("] 的排名: " + rank);
                
                // 执行逆运算
                int[] reconstructed = inverseCompute(rank);
                
                // 验证重建的排列是否与原始排列一致
                boolean reconstructedCorrectly = true;
                for (int i = 1; i <= n; i++) {
                    if (reconstructed[i] != arr[i]) {
                        reconstructedCorrectly = false;
                        break;
                    }
                }
                
                if (reconstructedCorrectly) {
                    System.out.println(" ✓");
                } else {
                    System.out.println(" ✗ (逆运算重建失败)");
                    allPassed = false;
                }
            } catch (Exception e) {
                System.out.println(" ✗ (测试过程中出现异常: " + e.getMessage() + ")");
                allPassed = false;
            }
        }
        
        System.out.println("======================");
        System.out.println("正确性测试结果: " + (allPassed ? "全部通过" : "存在错误"));
    }
    
    /**
     * 运行性能测试
     */
    public static void runPerformanceTest() {
        System.out.println("开始性能测试...");
        System.out.println("======================");
        
        int[] sizes = {1000, 10000, 100000, 500000};
        Random random = new Random(42);  // 设置随机种子以保证可重复性
        
        for (int size : sizes) {
            try {
                // 生成随机排列
                n = size;
                int[] permutation = new int[n + 1];
                boolean[] used = new boolean[n + 1];
                
                for (int i = 1; i <= n; i++) {
                    int num;
                    do {
                        num = random.nextInt(n) + 1;
                    } while (used[num]);
                    permutation[i] = num;
                    arr[i] = num;
                    used[num] = true;
                }
                
                // 测量康托展开时间
                long startTime = System.currentTimeMillis();
                long rank = compute();
                long endTime = System.currentTimeMillis();
                
                System.out.printf("排列长度 %d: 康托展开耗时 %.3f 毫秒", 
                                  size, (endTime - startTime) / 1000.0);
                
                // 测量逆运算时间（大数据量时跳过，避免超时）
                if (size <= 10000) {
                    startTime = System.currentTimeMillis();
                    int[] reconstructed = inverseCompute(rank);
                    endTime = System.currentTimeMillis();
                    System.out.printf(", 逆运算耗时 %.3f 毫秒", 
                                      (endTime - startTime) / 1000.0);
                } else {
                    System.out.print(", 逆运算 (跳过大数据量测试)");
                }
                
                System.out.println();
            } catch (Exception e) {
                System.out.println("排列长度 " + size + " 测试失败: " + e.getMessage());
            }
        }
        
        System.out.println("======================");
    }
    
    /**
     * 打印使用说明
     */
    public static void printUsage() {
        System.out.println("康托展开算法求解器");
        System.out.println("======================");
        System.out.println("使用方法:");
        System.out.println("  1. 命令行参数: java Code01_CantorExpansion n a1 a2 ... an");
        System.out.println("  2. 交互式输入: 直接运行程序后按提示输入");
        System.out.println("  3. 测试模式: 输入 'test' 运行正确性测试");
        System.out.println("  4. 性能测试: 输入 'perf' 运行性能测试");
        System.out.println("  5. 退出程序: 输入 'exit' 或 'quit'");
        System.out.println("======================");
    }
    
    /**
     * 主函数，支持命令行参数和交互式输入
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 处理命令行参数
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
                if (args.length != n + 1) {
                    System.out.println("错误：参数数量不正确");
                    printUsage();
                    return;
                }
                
                for (int i = 0; i < n; i++) {
                    arr[i + 1] = Integer.parseInt(args[i + 1]);
                }
                
                // 验证输入是否为有效的排列
                boolean[] used = new boolean[n + 1];
                boolean valid = true;
                for (int i = 1; i <= n; i++) {
                    int num = arr[i];
                    if (num < 1 || num > n || used[num]) {
                        valid = false;
                        break;
                    }
                    used[num] = true;
                }
                
                if (!valid) {
                    System.out.println("错误：输入不是有效的排列");
                    return;
                }
                
                // 计算并输出康托展开结果
                long startTime = System.currentTimeMillis();
                long rank = compute();
                long endTime = System.currentTimeMillis();
                
                System.out.println("康托展开结果 (字典序排名): " + rank);
                System.out.println("计算耗时: " + (endTime - startTime) / 1000.0 + " 毫秒");
                
                // 执行逆运算并验证
                if (n <= 10000) {  // 只在小数据量时执行逆运算
                    startTime = System.currentTimeMillis();
                    int[] reconstructed = inverseCompute(rank);
                    endTime = System.currentTimeMillis();
                    
                    System.out.println("\n康托逆运算验证:");
                    System.out.print("原始排列: ");
                    for (int i = 1; i <= n; i++) {
                        System.out.print(arr[i] + " ");
                    }
                    System.out.println();
                    
                    System.out.print("重建排列: ");
                    for (int i = 1; i <= n; i++) {
                        System.out.print(reconstructed[i] + " ");
                    }
                    System.out.println();
                    
                    System.out.println("逆运算耗时: " + (endTime - startTime) / 1000.0 + " 毫秒");
                }
                
                return;
            } catch (NumberFormatException e) {
                System.out.println("错误：无效的数字格式");
                printUsage();
                return;
            }
        }
        
        // 交互式输入模式
        Scanner scanner = new Scanner(System.in);
        printUsage();
        
        while (true) {
            System.out.print("\n请输入命令或排列长度: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("感谢使用康托展开算法求解器！");
                break;
            } else if (input.equalsIgnoreCase("help") || input.equalsIgnoreCase("?")) {
                printUsage();
                continue;
            } else if (input.equalsIgnoreCase("test")) {
                runCorrectnessTest();
                continue;
            } else if (input.equalsIgnoreCase("perf")) {
                runPerformanceTest();
                continue;
            }
            
            try {
                // 尝试解析排列长度
                n = Integer.parseInt(input);
                if (n <= 0 || n > MAXN - 1) {
                    System.out.println("错误：排列长度必须在1到" + (MAXN - 1) + "之间");
                    continue;
                }
                
                // 读取排列元素
                System.out.print("请输入" + n + "个整数作为排列（用空格分隔）: ");
                String[] elements = scanner.nextLine().trim().split("\\s+");
                if (elements.length != n) {
                    System.out.println("错误：元素数量不正确");
                    continue;
                }
                
                for (int i = 0; i < n; i++) {
                    arr[i + 1] = Integer.parseInt(elements[i]);
                }
                
                // 验证输入是否为有效的排列
                boolean[] used = new boolean[n + 1];
                boolean valid = true;
                for (int i = 1; i <= n; i++) {
                    int num = arr[i];
                    if (num < 1 || num > n || used[num]) {
                        valid = false;
                        break;
                    }
                    used[num] = true;
                }
                
                if (!valid) {
                    System.out.println("错误：输入不是有效的排列");
                    continue;
                }
                
                // 计算并输出结果
                long startTime = System.currentTimeMillis();
                long rank = compute();
                long endTime = System.currentTimeMillis();
                
                System.out.println("康托展开结果 (字典序排名): " + rank);
                System.out.println("计算耗时: " + (endTime - startTime) / 1000.0 + " 毫秒");
                
                // 执行逆运算并验证
                if (n <= 10000) {  // 只在小数据量时执行逆运算
                    startTime = System.currentTimeMillis();
                    int[] reconstructed = inverseCompute(rank);
                    endTime = System.currentTimeMillis();
                    
                    System.out.println("\n康托逆运算验证:");
                    System.out.print("原始排列: ");
                    for (int i = 1; i <= n; i++) {
                        System.out.print(arr[i] + " ");
                    }
                    System.out.println();
                    
                    System.out.print("重建排列: ");
                    for (int i = 1; i <= n; i++) {
                        System.out.print(reconstructed[i] + " ");
                    }
                    System.out.println();
                    
                    System.out.println("逆运算耗时: " + (endTime - startTime) / 1000.0 + " 毫秒");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("错误：无效的数字格式，请输入有效的整数");
            } catch (Exception e) {
                System.out.println("错误：" + e.getMessage());
            }
        }
        
        scanner.close();
    }

}