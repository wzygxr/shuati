package class096;

import java.util.Arrays;
import java.util.Scanner;

// 斐波那契博弈扩展 (SG函数与斐波那契数列)
// 有三堆石子，数量分别是m, n, p个
// 两人轮流走，每次选择一堆取，取的个数必须为斐波那契数列中的数
// 取走最后一颗石子的人获胜
// 
// 题目来源：
// 1. HDU 1848 Fibonacci again and again - http://acm.hdu.edu.cn/showproblem.php?pid=1848
// 2. HDU 1005 Fibonacci again - http://acm.hdu.edu.cn/showproblem.php?pid=1005
// 3. POJ 3533 Light Switching Game - http://poj.org/problem?id=3533
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个石子数的SG值
// 2. 斐波那契数列：可取石子数必须为斐波那契数列中的数
// 3. SG定理：整个游戏的SG值等于各堆SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(max_n * fib_count) - 计算每个石子数的SG值
// 2. 查询：O(1) - 计算三堆石子SG值的异或和
// 
// 空间复杂度分析：
// 1. SG数组：O(max_n) - 存储每个石子数的SG值
// 2. 斐波那契数组：O(fib_count) - 存储斐波那契数列
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的堆数和查询
public class Code11_FibonacciAgainAndAgain {
    
    // 最大石子数
    public static int MAXN = 1001;
    
    // SG数组，sg[i]表示有i个石子时的SG值
    public static int[] sg = new int[MAXN];
    
    // 斐波那契数组
    public static int[] fib = new int[21];
    
    // visited数组用于记忆化搜索
    public static boolean[] visited = new boolean[MAXN];
    
    // appear数组用于计算mex值
    public static boolean[] appear = new boolean[MAXN];
    
    // 
    // 算法原理：
    // 1. 预计算斐波那契数列
    // 2. 对于每个石子数i，计算其后继状态的SG值集合
    // 3. 石子数i的后继状态为i-fib[0], i-fib[1], ..., i-fib[k]（如果存在）
    // 4. 石子数i的SG值等于不属于后继状态SG值集合的最小非负整数(mex)
    // 
    // SG函数定义：
    // SG(x) = mex{SG(y) | y是x的后继状态}
    // 其中mex(S)表示不属于集合S的最小非负整数
    // 
    // 对于斐波那契博弈，状态i的后继状态为i-fib[0], i-fib[1], ..., i-fib[k]（如果存在）
    public static void build() {
        // 预计算斐波那契数列
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 21; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        
        // 初始化SG数组
        Arrays.fill(sg, -1);
        sg[0] = 0; // 终止状态SG值为0
        
        // 递推计算每个状态的SG值
        for (int i = 1; i < MAXN; i++) {
            // 初始化appear数组
            Arrays.fill(appear, false);
            
            // 计算状态i的所有后继状态的SG值
            for (int j = 0; j < 21 && fib[j] <= i; j++) {
                // 标记后继状态的SG值已出现
                appear[sg[i - fib[j]]] = true;
            }
            
            // 计算mex值，即不属于appear集合的最小非负整数
            for (int mex = 0; mex < MAXN; mex++) {
                if (!appear[mex]) {
                    sg[i] = mex;
                    break;
                }
            }
        }
    }
    
    // 
    // 算法原理：
    // 根据SG定理计算整个游戏的SG值
    // 1. 对于每堆石子，计算其SG值
    // 2. 整个游戏的SG值等于各堆SG值的异或和
    // 3. SG值不为0表示必胜态，为0表示必败态
    public static String solve(int m, int n, int p) {
        // 异常处理：处理负数
        if (m < 0 || n < 0 || p < 0) {
            return "输入非法";
        }
        
        // 计算三堆石子SG值的异或和
        int xorSum = sg[m] ^ sg[n] ^ sg[p];
        
        // SG值不为0表示必胜态，为0表示必败态
        return xorSum != 0 ? "Fibo" : "Nacci";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 预处理SG值
        build();
        
        // 读取输入
        while (scanner.hasNextInt()) {
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int p = scanner.nextInt();
            
            // 终止条件
            if (m == 0 && n == 0 && p == 0) {
                break;
            }
            
            // 计算结果并输出
            System.out.println(solve(m, n, p));
        }
        
        scanner.close();
    }
}