package class096;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// 有向图博弈 (SG函数在有向图上的应用)
// 一个有向无环图，在若干点上有若干棋子，两人轮流移动棋子
// 每次只能将一个棋子沿有向边移动一步
// 当无棋子可移动时输，即移动最后一枚棋子者胜
// 
// 题目来源：
// 1. POJ 2425 A Chess Game - http://poj.org/problem?id=2425
// 2. HDU 1524 A Chess Game - http://acm.hdu.edu.cn/showproblem.php?pid=1524
// 3. POJ 2599 A New Stone Game - http://poj.org/problem?id=2599
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个节点的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. SG定理：整个游戏的SG值等于各棋子所在节点SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(n * max_degree) - 计算每个节点的SG值
// 2. 查询：O(m) - m为棋子数，计算所有棋子SG值的异或和
// 
// 空间复杂度分析：
// 1. 图存储：O(n + e) - n为节点数，e为边数
// 2. SG数组：O(n) - 存储每个节点的SG值
// 
// 工程化考量：
// 1. 异常处理：处理非法输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的图结构和查询
public class Code08_DirectedGraphGame {
    
    // 最大节点数
    public static int MAXN = 1001;
    
    // 图的邻接表表示
    public static ArrayList<Integer>[] graph = new ArrayList[MAXN];
    
    // SG数组，sg[i]表示节点i的SG值
    public static int[] sg = new int[MAXN];
    
    // visited数组用于记忆化搜索
    public static boolean[] visited = new boolean[MAXN];
    
    // appear数组用于计算mex值
    public static boolean[] appear = new boolean[MAXN];
    
    // 初始化图
    static {
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    // 
    // 算法原理：
    // 1. 对于每个节点，计算其后继节点的SG值集合
    // 2. 节点的SG值等于不属于该集合的最小非负整数(mex)
    // 3. 根据SG定理，整个游戏的SG值等于各棋子所在节点SG值的异或和
    // 
    // SG函数定义：
    // SG(x) = mex{SG(y) | 存在从x到y的有向边}
    // 其中mex(S)表示不属于集合S的最小非负整数
    // 
    // 对于有向图博弈，节点x的后继状态为所有可以直接到达的节点
    public static int computeSG(int node) {
        // 记忆化搜索
        if (visited[node]) {
            return sg[node];
        }
        
        // 标记已访问
        visited[node] = true;
        
        // 初始化appear数组
        Arrays.fill(appear, false);
        
        // 计算节点node的所有后继节点的SG值
        for (int next : graph[node]) {
            // 标记后继节点的SG值已出现
            appear[computeSG(next)] = true;
        }
        
        // 计算mex值，即不属于appear集合的最小非负整数
        for (int mex = 0; mex < MAXN; mex++) {
            if (!appear[mex]) {
                sg[node] = mex;
                return sg[node];
            }
        }
        
        return 0; // 理论上不会执行到这里
    }
    
    // 
    // 算法原理：
    // 根据SG定理计算整个游戏的SG值
    // 1. 对于每个棋子，计算其所在节点的SG值
    // 2. 整个游戏的SG值等于各棋子所在节点SG值的异或和
    // 3. SG值不为0表示必胜态，为0表示必败态
    public static String solve(int[] chessPositions, int n) {
        // 异常处理：处理空数组
        if (chessPositions == null || chessPositions.length == 0) {
            return "LOSE"; // 空游戏，先手败
        }
        
        // 计算所有棋子所在节点SG值的异或和
        int xorSum = 0;
        for (int pos : chessPositions) {
            // 异常处理：处理非法节点
            if (pos < 0 || pos >= n) {
                return "输入非法";
            }
            xorSum ^= sg[pos];
        }
        
        // SG值不为0表示必胜态，为0表示必败态
        return xorSum != 0 ? "WIN" : "LOSE";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取节点数
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            
            // 清空图
            for (int i = 0; i < n; i++) {
                graph[i].clear();
            }
            
            // 读取图的边
            for (int i = 0; i < n; i++) {
                int degree = scanner.nextInt();
                for (int j = 0; j < degree; j++) {
                    int next = scanner.nextInt();
                    graph[i].add(next);
                }
            }
            
            // 初始化visited和sg数组
            Arrays.fill(visited, false);
            Arrays.fill(sg, 0);
            
            // 计算每个节点的SG值
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    computeSG(i);
                }
            }
            
            // 读取棋子数
            int m = scanner.nextInt();
            // 读取棋子位置
            int[] chessPositions = new int[m];
            for (int i = 0; i < m; i++) {
                chessPositions[i] = scanner.nextInt();
            }
            
            // 计算结果并输出
            System.out.println(solve(chessPositions, n));
        }
        
        scanner.close();
    }
}