package class096;

import java.util.Scanner;

// 三维博弈 (3D Nim Game)
// 一个三维空间里全是灯，每次选出一个正方体，改变八个角灯的状态
// 而且右下角的灯初始必须是开的
// 
// 题目来源：
// 1. POJ 3533 Light Switching Game - http://poj.org/problem?id=3533
// 2. HDU 3404 Nim积 - http://acm.hdu.edu.cn/showproblem.php?pid=3404
// 3. POJ 2975 Nim - http://poj.org/problem?id=2975
// 
// 算法核心思想：
// 1. 三维Nim积：利用Nim积计算三维空间中每个点的SG值
// 2. Nim积性质：(a⊗b)⊗c = a⊗(b⊗c)，a⊗b = b⊗a
// 3. SG函数：整个游戏的SG值为所有开灯位置SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(x*y*z) - 计算每个位置的Nim积
// 2. 查询：O(k) - k为开灯数，计算所有开灯位置SG值的异或和
// 
// 空间复杂度分析：
// 1. Nim积数组：O(x*y*z) - 存储每个位置的Nim积
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预处理Nim积避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的空间大小和查询
public class Code12_3DNimGame {
    
    // 最大坐标值
    public static int MAXN = 101;
    
    // Nim积数组，nim[i][j]表示i和j的Nim积
    public static int[][] nim = new int[MAXN][MAXN];
    
    // SG数组，sg[x][y][z]表示位置(x,y,z)的SG值
    public static int[][][] sg = new int[MAXN][MAXN][MAXN];
    
    // 
    // 算法原理：
    // 1. Nim积定义：a⊗b = mex{(a'⊗b)⊕(a⊗b')⊕(a'⊗b') | a'<a, b'<b}
    // 2. 三维Nim积：sg[x][y][z] = x⊗y⊗z
    // 3. SG函数：整个游戏的SG值为所有开灯位置SG值的异或和
    // 
    // Nim积性质：
    // 1. (a⊗b)⊗c = a⊗(b⊗c)（结合律）
    // 2. a⊗b = b⊗a（交换律）
    // 3. a⊗0 = 0
    // 4. a⊗1 = a
    // 
    // 对于三维博弈，位置(x,y,z)的SG值为x⊗y⊗z
    public static void build() {
        // 计算Nim积
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXN; j++) {
                if (i == 0 || j == 0) {
                    nim[i][j] = 0;
                } else {
                    // 计算i和j的Nim积
                    boolean[] appear = new boolean[MAXN * MAXN];
                    for (int a = 0; a < i; a++) {
                        for (int b = 0; b < j; b++) {
                            // Nim积定义：a⊗b = mex{(a'⊗b)⊕(a⊗b')⊕(a'⊗b') | a'<a, b'<b}
                            int val = (nim[a][j] ^ nim[i][b] ^ nim[a][b]);
                            if (val < MAXN * MAXN) {
                                appear[val] = true;
                            }
                        }
                    }
                    
                    // 计算mex值
                    for (int mex = 0; mex < MAXN * MAXN; mex++) {
                        if (!appear[mex]) {
                            nim[i][j] = mex;
                            break;
                        }
                    }
                }
            }
        }
        
        // 计算每个位置的SG值
        for (int x = 0; x < MAXN; x++) {
            for (int y = 0; y < MAXN; y++) {
                for (int z = 0; z < MAXN; z++) {
                    // 位置(x,y,z)的SG值为x⊗y⊗z
                    sg[x][y][z] = nim[nim[x][y]][z];
                }
            }
        }
    }
    
    // 
    // 算法原理：
    // 根据SG函数计算整个游戏的SG值
    // 1. 整个游戏的SG值为所有开灯位置SG值的异或和
    // 2. SG值不为0表示必胜态，为0表示必败态
    public static String solve(int[][] lights, int k) {
        // 异常处理：处理空数组
        if (lights == null || k <= 0) {
            return "No"; // 空游戏，先手败
        }
        
        // 计算所有开灯位置SG值的异或和
        int xorSum = 0;
        for (int i = 0; i < k; i++) {
            int x = lights[i][0];
            int y = lights[i][1];
            int z = lights[i][2];
            
            // 异常处理：处理非法坐标
            if (x < 0 || x >= MAXN || y < 0 || y >= MAXN || z < 0 || z >= MAXN) {
                return "输入非法";
            }
            
            xorSum ^= sg[x][y][z];
        }
        
        // SG值不为0表示必胜态，为0表示必败态
        return xorSum != 0 ? "Yes" : "No";
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 预处理Nim积
        build();
        
        // 读取测试用例数量
        int testCases = scanner.nextInt();
        for (int i = 0; i < testCases; i++) {
            // 读取开灯数
            int k = scanner.nextInt();
            // 读取开灯位置
            int[][] lights = new int[k][3];
            for (int j = 0; j < k; j++) {
                lights[j][0] = scanner.nextInt();
                lights[j][1] = scanner.nextInt();
                lights[j][2] = scanner.nextInt();
            }
            
            // 计算结果并输出
            System.out.println(solve(lights, k));
        }
        
        scanner.close();
    }
}