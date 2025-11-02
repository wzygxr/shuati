package class096;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

// 线性串取石子游戏 (SG函数在线性串上的应用)
// 一串石子，每次可以取走若干个连续的石子
// 取走最后一颗的胜利，给出选取石子数的约束集合
// 求先手胜负
// 
// 题目来源：
// 1. HDU 2999 Stone Game, Why are you always there? - http://acm.hdu.edu.cn/showproblem.php?pid=2999
// 2. POJ 2311 Cutting Game - http://poj.org/problem?id=2311
// 3. 洛谷 P3185 [HNOI2007]分裂游戏 - https://www.luogu.com.cn/problem/P3185
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个区间状态的SG值
// 2. 区间分割：取石子操作将区间分割为两个子区间
// 3. SG定理：整个游戏的SG值等于各子区间SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(n^3) - 计算每个区间的SG值
// 2. 查询：O(1) - 直接返回整个区间的SG值
// 
// 空间复杂度分析：
// 1. SG数组：O(n^2) - 存储每个区间的SG值
// 2. S集合：O(|S|) - 存储可取石子数的集合
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：记忆化搜索避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的S集合和查询
public class Code10_StoneGameLinearString {
    
    // 最大石子数
    public static int MAXN = 1001;
    
    // SG数组，sg[l][r]表示区间[l,r]的SG值
    public static int[][] sg = new int[MAXN][MAXN];
    
    // S集合，表示每次可以取的连续石子数
    public static TreeSet<Integer> s = new TreeSet<>();
    
    // visited数组用于记忆化搜索
    public static boolean[][] visited = new boolean[MAXN][MAXN];
    
    // appear数组用于计算mex值
    public static boolean[] appear = new boolean[MAXN];
    
    // 
    // 算法原理：
    // 1. 对于每个区间[l,r]，计算其后继状态的SG值集合
    // 2. 区间[l,r]的后继状态为取走连续k个石子后分割成的两个子区间
    // 3. 区间[l,r]的SG值等于不属于后继状态SG值集合的最小非负整数(mex)
    // 
    // SG函数定义：
    // SG([l,r]) = mex{SG([l,i-1]) XOR SG([i+k,r]) | i∈[l,r-k+1], k∈S}
    // 其中mex(S)表示不属于集合S的最小非负整数
    // XOR表示异或运算
    // 
    // 对于线性串取石子游戏，区间[l,r]的后继状态为取走连续k个石子后
    // 分割成的两个子区间[l,i-1]和[i+k,r]
    public static int computeSG(int l, int r) {
        // 边界条件：区间为空
        if (l > r) {
            return 0;
        }
        
        // 记忆化搜索
        if (visited[l][r]) {
            return sg[l][r];
        }
        
        // 标记已访问
        visited[l][r] = true;
        
        // 初始化appear数组
        Arrays.fill(appear, false);
        
        // 计算区间[l,r]的所有后继状态的SG值
        for (int k : s) {
            // 枚举取走k个连续石子的起始位置
            for (int i = l; i <= r - k + 1; i++) {
                // 取走区间[i,i+k-1]的石子后，分割成两个子区间[l,i-1]和[i+k,r]
                // 根据SG定理，后继状态的SG值为两个子区间SG值的异或和
                int nextStateSG = computeSG(l, i - 1) ^ computeSG(i + k, r);
                // 标记后继状态的SG值已出现
                appear[nextStateSG] = true;
            }
        }
        
        // 计算mex值，即不属于appear集合的最小非负整数
        for (int mex = 0; mex < MAXN; mex++) {
            if (!appear[mex]) {
                sg[l][r] = mex;
                return sg[l][r];
            }
        }
        
        return 0; // 理论上不会执行到这里
    }
    
    // 
    // 算法原理：
    // 根据SG函数计算整个游戏的SG值
    // 1. 整个游戏的SG值为区间[1,n]的SG值
    // 2. SG值不为0表示必胜态，为0表示必败态
    public static String solve(int n) {
        // 异常处理：处理非法输入
        if (n <= 0) {
            return "LOSE"; // 空游戏，先手败
        }
        
        // 计算区间[1,n]的SG值
        int result = computeSG(1, n);
        
        // SG值不为0表示必胜态，为0表示必败态
        return result != 0 ? "WIN" : "LOSE";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取S集合大小
        while (scanner.hasNextInt()) {
            int k = scanner.nextInt();
            
            // 清空S集合
            s.clear();
            
            // 读取S集合
            for (int i = 0; i < k; i++) {
                s.add(scanner.nextInt());
            }
            
            // 初始化visited和sg数组
            for (int i = 0; i < MAXN; i++) {
                Arrays.fill(visited[i], false);
                Arrays.fill(sg[i], 0);
            }
            
            // 读取石子数
            int n = scanner.nextInt();
            
            // 计算结果并输出
            System.out.println(solve(n));
        }
        
        scanner.close();
    }
}