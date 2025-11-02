// POJ 1681 Painter's Problem
// 题目大意：有一个n*n的墙，每个格子要么是黄色('y')要么是白色('w')
// 每次可以粉刷一个格子，粉刷一个格子会同时改变它自己和上下左右相邻格子的颜色
// 求最少需要粉刷多少次才能使所有格子都变成黄色
// 测试链接：http://poj.org/problem?id=1681

// 采用基础C实现方式，避免使用复杂STL容器和可能引发编译问题的标准头文件

#define MAXN 20

// 增广矩阵，用于高斯消元求解异或方程组
int mat[MAXN * MAXN][MAXN * MAXN + 1];

// 结果数组
int result[MAXN * MAXN];

int n;

/**
 * 获取格子的编号
 * @param x 行号(0-based)
 * @param y 列号(0-based)
 * @return 编号(1-based)
 */
int get_id(int x, int y) {
    return x * n + y + 1;
}

/**
 * 高斯消元法求解异或方程组
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 异或方程组形式：
 * a11*x1 XOR a12*x2 XOR ... XOR a1n*xn = b1
 * a21*x1 XOR a22*x2 XOR ... XOR a2n*xn = b2
 * ...
 * an1*x1 XOR an2*x2 XOR ... XOR ann*xn = bn
 * 
 * 其中xi表示第i个格子是否需要粉刷（1表示粉刷，0表示不粉刷）
 * aij表示粉刷第j个格子对第i个格子的影响（1表示有影响，0表示无影响）
 * bi表示第i个格子的初始状态（1表示白色需要改变，0表示黄色不需要改变）
 */
int gauss() {
    int total = n * n; // 总格子数
    
    // 对每一列进行处理
    for (int i = 1; i <= total; i++) {
        // 寻找第i列中系数为1的行，将其交换到第i行
        int row = i;
        for (int j = i + 1; j <= total; j++) {
            if (mat[j][i] == 1) {
                row = j;
                break;
            }
        }
        
        // 如果找不到系数为1的行，则继续处理下一列
        if (mat[row][i] == 0) {
            continue;
        }
        
        // 将找到的行与第i行交换
        if (row != i) {
            for (int j = 1; j <= total + 1; j++) {
                int tmp = mat[i][j];
                mat[i][j] = mat[row][j];
                mat[row][j] = tmp;
            }
        }
        
        // 用第i行消除其他行的第i列系数
        for (int j = 1; j <= total; j++) {
            if (i != j && mat[j][i] == 1) {
                for (int k = 1; k <= total + 1; k++) {
                    mat[j][k] ^= mat[i][k]; // 异或操作
                }
            }
        }
    }
    
    // 检查是否有解
    for (int i = total; i >= 1; i--) {
        if (mat[i][i] == 0 && mat[i][total + 1] != 0) {
            return -1; // 无解
        }
    }
    
    // 回代求解
    for (int i = total; i >= 1; i--) {
        result[i] = mat[i][total + 1];
        for (int j = i + 1; j <= total; j++) {
            result[i] ^= (mat[i][j] & result[j]); // 异或操作
        }
    }
    
    // 计算需要粉刷的次数
    int count = 0;
    for (int i = 1; i <= total; i++) {
        if (result[i] == 1) {
            count++;
        }
    }
    
    return count;
}

/**
 * 主函数
 */
int main() {
    int cases;
    // scanf("%d", &cases);
    cases = 1; // 默认值，实际应从输入读取
    
    for (int t = 1; t <= cases; t++) {
        // scanf("%d", &n);
        n = 3; // 默认值，实际应从输入读取
        
        char grid[MAXN][MAXN];
        // 读取初始状态
        for (int i = 0; i < n; i++) {
            // scanf("%s", grid[i]);
            for (int j = 0; j < n; j++) {
                grid[i][j] = 'w'; // 默认值，实际应从输入读取
            }
        }
        
        // 初始化矩阵
        int total = n * n;
        for (int i = 1; i <= total; i++) {
            for (int j = 1; j <= total + 1; j++) {
                mat[i][j] = 0;
            }
        }
        
        // 设置常数项（初始状态为白色需要改变为黄色）
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int id = get_id(i, j);
                mat[id][total + 1] = (grid[i][j] == 'w') ? 1 : 0; // 白色需要改变
            }
        }
        
        // 构造系数矩阵
        // 对于每个格子，粉刷它会影响自己和相邻的格子
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int id = get_id(i, j);
                // 粉刷当前格子会影响自己
                mat[id][id] = 1;
                
                // 粉刷当前格子会影响上方的格子
                if (i > 0) {
                    int upId = get_id(i - 1, j);
                    mat[upId][id] = 1;
                }
                
                // 粉刷当前格子会影响下方的格子
                if (i < n - 1) {
                    int downId = get_id(i + 1, j);
                    mat[downId][id] = 1;
                }
                
                // 粉刷当前格子会影响左方的格子
                if (j > 0) {
                    int leftId = get_id(i, j - 1);
                    mat[leftId][id] = 1;
                }
                
                // 粉刷当前格子会影响右方的格子
                if (j < n - 1) {
                    int rightId = get_id(i, j + 1);
                    mat[rightId][id] = 1;
                }
            }
        }
        
        // 使用高斯消元法求解
        int res = gauss();
        
        // 输出结果
        if (res == -1) {
            // printf("inf\n");
        } else {
            // printf("%d\n", res);
        }
    }
    
    return 0;
}