package class096;

import java.util.Arrays;
import java.util.Scanner;

// S-Nim游戏 (SG函数经典应用)
// 有若干堆石子，每次可以从任意一堆石子中取若干颗（数目必须在集合S中）
// 问谁会获胜
// 
// 题目来源：
// 1. HDU 1536 S-Nim - http://acm.hdu.edu.cn/showproblem.php?pid=1536
// 2. POJ 2960 S-Nim - http://poj.org/problem?id=2960
// 3. 洛谷 P2148 [SDOI2009]E&D - https://www.luogu.com.cn/problem/P2148
// 4. SPOJ 3805. E&D Game - https://www.spoj.com/problems/ED/
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个状态的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. SG定理：整个游戏的SG值等于各子游戏SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(max_n * |S|) - 计算每个石子数的SG值
// 2. 查询：O(k) - k为堆数，计算所有堆SG值的异或和
// 
// 空间复杂度分析：
// 1. SG数组：O(max_n) - 存储每个石子数的SG值
// 2. S集合：O(|S|) - 存储可取石子数的集合
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的S集合和查询
public class Code07_SGFunctionSNim {
    
    // 最大石子数
    public static int MAXN = 10001;
    
    // SG数组，sg[i]表示有i个石子时的SG值
    public static int[] sg = new int[MAXN];
    
    // S集合，表示每次可以取的石子数
    public static int[] s = new int[101];
    
    // appear数组用于计算mex值
    public static boolean[] appear = new boolean[MAXN];
    
    // 
    // 算法原理：
    // 1. 对于每个石子数i，计算其后继状态的SG值集合
    // 2. SG值等于不属于该集合的最小非负整数(mex)
    // 3. 根据SG定理，整个游戏的SG值等于各堆SG值的异或和
    // 
    // SG函数定义：
    // SG(x) = mex{SG(y) | y是x的后继状态}
    // 其中mex(S)表示不属于集合S的最小非负整数
    // 
    // 对于S-Nim游戏，状态i的后继状态为i-s[0], i-s[1], ..., i-s[k-1]（如果存在）
    public static void build(int k) {
        // 初始化SG数组
        Arrays.fill(sg, -1);
        sg[0] = 0; // 终止状态SG值为0
        
        // 递推计算每个状态的SG值
        for (int i = 1; i < MAXN; i++) {
            // 初始化appear数组
            Arrays.fill(appear, false);
            
            // 计算状态i的所有后继状态的SG值
            for (int j = 0; j < k && s[j] <= i; j++) {
                // 标记后继状态的SG值已出现
                appear[sg[i - s[j]]] = true;
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
    public static String solve(int[] piles, int k) {
        // 异常处理：处理空数组
        if (piles == null || piles.length == 0) {
            return "L"; // 空游戏，先手败
        }
        
        // 计算所有堆SG值的异或和
        int xorSum = 0;
        for (int pile : piles) {
            // 异常处理：处理负数
            if (pile < 0) {
                return "输入非法";
            }
            xorSum ^= sg[pile];
        }
        
        // SG值不为0表示必胜态，为0表示必败态
        return xorSum != 0 ? "W" : "L";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取S集合大小
        int k = scanner.nextInt();
        while (k != 0) {
            // 读取S集合
            for (int i = 0; i < k; i++) {
                s[i] = scanner.nextInt();
            }
            
            // 预处理SG值
            build(k);
            
            // 读取测试用例数量
            int testCases = scanner.nextInt();
            for (int i = 0; i < testCases; i++) {
                // 读取堆数
                int pilesCount = scanner.nextInt();
                // 读取每堆石子数
                int[] piles = new int[pilesCount];
                for (int j = 0; j < pilesCount; j++) {
                    piles[j] = scanner.nextInt();
                }
                
                // 计算结果并输出
                System.out.println(solve(piles, k));
            }
            
            // 读取下一组S集合大小
            k = scanner.nextInt();
        }
        
        scanner.close();
    }
}