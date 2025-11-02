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

// 最大坐标值
const int MAXN = 21;

// Nim积数组，nim[i][j]表示i和j的Nim积
int nim[MAXN][MAXN];

// SG数组，sg[x][y][z]表示位置(x,y,z)的SG值
int sg[MAXN][MAXN][MAXN];

// appear数组用于计算mex值
int appear[MAXN * MAXN];

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
void build() {
    int i, j, k;
    
    // 计算Nim积
    for (i = 0; i < MAXN; i++) {
        for (j = 0; j < MAXN; j++) {
            if (i == 0 || j == 0) {
                nim[i][j] = 0;
            } else {
                // 初始化appear数组
                int a, b;
                for (a = 0; a < MAXN * MAXN; a++) {
                    appear[a] = 0;
                }
                
                // 计算i和j的Nim积
                for (a = 0; a < i; a++) {
                    for (b = 0; b < j; b++) {
                        // Nim积定义：a⊗b = mex{(a'⊗b)⊕(a⊗b')⊕(a'⊗b') | a'<a, b'<b}
                        int val = (nim[a][j] ^ nim[i][b] ^ nim[a][b]);
                        if (val < MAXN * MAXN) {
                            appear[val] = 1;
                        }
                    }
                }
                
                // 计算mex值
                for (a = 0; a < MAXN * MAXN; a++) {
                    if (appear[a] == 0) {
                        nim[i][j] = a;
                        break;
                    }
                }
            }
        }
    }
    
    // 计算每个位置的SG值
    for (i = 0; i < MAXN; i++) {
        for (j = 0; j < MAXN; j++) {
            for (k = 0; k < MAXN; k++) {
                // 位置(i,j,k)的SG值为i⊗j⊗k
                sg[i][j][k] = nim[nim[i][j]][k];
            }
        }
    }
}

// 
// 算法原理：
// 根据SG函数计算整个游戏的SG值
// 1. 整个游戏的SG值为所有开灯位置SG值的异或和
// 2. SG值不为0表示必胜态，为0表示必败态
int solve(int lights[][3], int k) {
    // 异常处理：处理空数组
    if (k <= 0) {
        return 0; // 空游戏，先手败
    }
    
    // 计算所有开灯位置SG值的异或和
    int xorSum = 0;
    int i;
    for (i = 0; i < k; i++) {
        int x = lights[i][0];
        int y = lights[i][1];
        int z = lights[i][2];
        
        // 异常处理：处理非法坐标
        if (x < 0 || x >= MAXN || y < 0 || y >= MAXN || z < 0 || z >= MAXN) {
            return -1; // 输入非法
        }
        
        xorSum ^= sg[x][y][z];
    }
    
    // SG值不为0表示必胜态，为0表示必败态
    return xorSum != 0 ? 1 : 0; // 1表示Yes，0表示No
}

// 测试示例
int main() {
    // 预处理Nim积
    build();
    
    // 示例1: lights = {{1,1,1}, {2,2,2}}, k = 2
    // sg[1][1][1] = nim[nim[1][1]][1] = nim[1][1] = 1
    // sg[2][2][2] = nim[nim[2][2]][2] = nim[3][2] = 1
    // xorSum = 1^1 = 0
    // 预期结果: 0 (No)
    int lights1[2][3] = {{1, 1, 1}, {2, 2, 2}};
    int result1 = solve(lights1, 2);
    
    // 示例2: lights = {{1,2,3}}, k = 1
    // sg[1][2][3] = nim[nim[1][2]][3] = nim[2][3] = 1
    // xorSum = 1
    // 预期结果: 1 (Yes)
    int lights2[1][3] = {{1, 2, 3}};
    int result2 = solve(lights2, 1);
    
    return 0;
}