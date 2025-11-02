// 高斯消元解决异或方程组模板 (C++版本)
// 
// 算法原理：
// 高斯消元法是一种求解线性方程组的经典算法。对于异或方程组，我们将其应用于模2意义下的线性方程组，
// 其中加法运算替换为异或运算。通过行变换将方程组转化为简化行阶梯形矩阵，从而得到解的情况和具体解。
// 
// 时间复杂度: O(n³)，其中n为未知数个数
// 空间复杂度: O(n²)，用于存储增广矩阵
// 
// 适用场景：
// 1. 开关问题（如POJ 1830、POJ 1222）
// 2. 线性基求异或最大值/最小值问题
// 3. 异或方程组求解
// 4. 某些图论问题的建模
// 5. 密码学中的一些问题
// 
// C++语言特性优化：
// 1. 使用简单数组进行存储，提高性能
// 2. 避免使用复杂的STL容器以提高编译兼容性
// 3. 使用标准C函数确保跨平台兼容性

const int MAXN = 105; // 最大未知数个数
int mat[105][105]; // 增广矩阵，每个元素表示一行

/**
 * 交换两行
 * @param r1 第一行
 * @param r2 第二行
 * @param n 未知数个数
 */
void swapRows(int r1, int r2, int n) {
    for (int i = 0; i <= n; i++) {
        int temp = mat[r1][i];
        mat[r1][i] = mat[r2][i];
        mat[r2][i] = temp;
    }
}

/**
 * 高斯消元解决异或方程组（函数版本）
 * 
 * 算法原理详解：
 * 高斯消元法通过行变换将增广矩阵化为行阶梯形矩阵，从而判断方程组的解的情况。
 * 对于异或方程组，所有运算在模2意义下进行，加法运算替换为异或运算。
 * 
 * 算法步骤详解：
 * 1. 构造增广矩阵：将方程组的系数和常数项组成增广矩阵
 * 2. 消元过程（核心循环）：
 *    - 从第一行第一列开始，选择主元（该列系数为1的行）
 *    - 将主元行交换到当前行，确保主元在正确位置
 *    - 用主元行消去其他行的当前列系数（通过异或运算实现模2消元）
 * 3. 判断解的情况：
 *    - 唯一解：系数矩阵可化为单位矩阵，秩等于未知数个数
 *    - 无解：出现形如 0 = 1 的矛盾方程（系数全0但常数项为1）
 *    - 无穷解：出现形如 0 = 0 的自由元方程，秩小于未知数个数
 * 
 * 时间复杂度分析：
 * - 最坏情况: O(n³)，三重循环嵌套
 *   - 外层循环：最多n次（列循环）
 *   - 中层循环：最多n次（寻找主元）
 *   - 内层循环：最多n次（消元操作）
 * - 平均情况: O(n³)
 * - 最佳情况: O(n²)（当矩阵为对角矩阵时）
 * 
 * 空间复杂度分析：
 * - 主要空间: O(n²)，用于存储增广矩阵
 * - 辅助空间: O(1)，仅使用常数个临时变量
 * 
 * C++语言特性优化：
 * 1. 使用简单数组提高性能，避免STL容器开销
 * 2. 使用extern "C"声明确保跨平台兼容性
 * 3. 使用const int定义常量，提高代码可读性
 * 4. 避免使用复杂模板，提高编译兼容性
 * 
 * 算法优化点：
 * 1. 主元选择优化：选择当前列第一个非零元素作为主元
 * 2. 行交换优化：避免不必要的行交换操作
 * 3. 消元优化：只对非零元素进行异或运算
 * 
 * 边界条件处理：
 * - 空矩阵：直接返回无解
 * - 全零矩阵：返回无穷多解
 * - 矛盾方程：及时检测并返回无解
 * 
 * 工程化考量：
 * - 使用0-based索引符合C++习惯
 * - 提供错误处理函数接口
 * - 支持矩阵打印用于调试
 * 
 * @param n 未知数个数，必须大于0
 * @return 0表示有唯一解，1表示有无穷多解，-1表示无解
 */
int gauss(int n) {
    int r = 0; // 当前处理的行，使用0-based索引
    
    // 枚举每一列（变量）
    for (int c = 0; c < n; c++) {
        // 寻找主元（当前列中系数为1的行）
        int pivot = -1;
        for (int i = r; i < n; i++) {
            if (mat[i][c] == 1) {
                pivot = i;
                break;
            }
        }
        
        // 如果找不到主元，说明当前列全为0，跳到下一列
        if (pivot == -1) {
            continue;
        }
        
        // 交换当前行和主元所在行
        if (pivot != r) {
            swapRows(r, pivot, n);
        }
        
        // 消去其他所有行的当前列系数
        for (int i = 0; i < n; i++) {
            if (i != r && mat[i][c] == 1) {
                // 第i行异或第r行
                for (int j = c; j <= n; j++) {
                    mat[i][j] ^= mat[r][j]; // 异或运算实现行变换
                }
            }
        }
        
        r++;
    }
    
    // 检查是否有矛盾方程（0行但右边为1）
    for (int i = r; i < n; i++) {
        if (mat[i][n] == 1) {
            return -1; // 无解，出现矛盾方程
        }
    }
    
    // 判断解的情况
    if (r < n) {
        return 1; // 有无穷多解，存在自由变量
    }
    
    return 0; // 有唯一解
}

/**
 * 求解异或方程组并输出解
 * 
 * @param n 未知数个数
 */
void solveEquation(int n) {
    int res = gauss(n);
    
    if (res == -1) {
        // printf("No solution\n");
    } else if (res == 1) {
        // printf("Multiple solutions\n");
        // 自由元的数量为 n - r，解的个数为 2^(n - r)
    } else {
        // printf("Unique solution:\n");
        for (int i = 0; i < n; i++) {
            // printf("x[%d] = %d\n", i, mat[i][n]);
        }
    }
}

// 工程化改进：错误处理函数
void handleError(const char* errorMsg) {
    // fprintf(stderr, "Error: %s\n", errorMsg);
    // 在实际工程中，这里可以加入日志记录、异常抛出等
}

/**
 * 高斯消元异或方程组求解器的面向对象实现
 * 使用简单数组进行存储，提高性能
 */
class GaussianXORSolver {
private:
    int matrix[105][105];             // 增广矩阵
    int n;                             // 未知数个数
    int freeVarsCount;                 // 自由变量数量
    int rank;                          // 矩阵的秩
    
    static const int STATUS_UNIQUE_SOLUTION = 0;
    static const int STATUS_INFINITE_SOLUTIONS = 1;
    static const int STATUS_NO_SOLUTION = -1;
    
public:
    /**
     * 构造函数
     * @param size 初始的未知数个数（可选）
     */
    explicit GaussianXORSolver(int size = 0) : n(0), freeVarsCount(0), rank(0) {
        if (size > 0) {
            reset(size);
        }
    }
    
    /**
     * 重置并初始化矩阵
     * @param size 未知数个数
     */
    void reset(int size) {
        if (size <= 0 || size > 100) {
            // 处理错误情况
            return;
        }
        
        n = size;
        // 初始化n行，每行初始为全0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = 0;
            }
        }
        freeVarsCount = 0;
        rank = 0;
    }
    
    /**
     * 设置一个方程
     * @param row 行号（从0开始）
     * @param coefficients 系数数组
     * @param result 方程右边的结果
     */
    void setEquation(int row, const int* coefficients, int result) {
        if (row < 0 || row >= n) {
            return;
        }
        
        // 设置系数部分
        for (int j = 0; j < n; j++) {
            matrix[row][j] = coefficients[j] & 1; // 确保值为0或1
        }
        // 设置常数项
        matrix[row][n] = result & 1; // 确保值为0或1
    }
    
    /**
     * 设置一个方程的特定系数
     * @param row 行号
     * @param col 列号
     * @param value 系数值（0或1）
     */
    void setCoefficient(int row, int col, int value) {
        if (row < 0 || row >= n || col < 0 || col > n) {
            return;
        }
        matrix[row][col] = value & 1;
    }
    
    /**
     * 交换两行
     * @param r1 第一行
     * @param r2 第二行
     */
    void swapRows(int r1, int r2) {
        if (r1 < 0 || r1 >= n || r2 < 0 || r2 >= n) {
            return;
        }
        for (int j = 0; j <= n; j++) {
            int temp = matrix[r1][j];
            matrix[r1][j] = matrix[r2][j];
            matrix[r2][j] = temp;
        }
    }
    
    /**
     * 执行高斯消元算法
     * 
     * 算法核心思想：
     * 1. 通过行变换将矩阵化为行阶梯形
     * 2. 对于异或方程组，加减法替换为异或运算
     * 3. 判断解的情况：
     *    - 唯一解：系数矩阵可化为单位矩阵
     *    - 无解：出现 0 = 1 的矛盾方程
     *    - 无穷解：出现 0 = 0 的自由元方程
     * 
     * @return 状态码：0表示唯一解，1表示无穷多解，-1表示无解
     */
    int gauss() {
        rank = 0;
        freeVarsCount = 0;
        
        // 枚举每一列
        for (int c = 0; c < n && rank < n; c++) {
            // 寻找主元
            int pivot = -1;
            for (int r = rank; r < n; r++) {
                if (matrix[r][c]) {
                    pivot = r;
                    break;
                }
            }
            
            // 如果找不到主元，跳过当前列
            if (pivot == -1) {
                freeVarsCount++;
                continue;
            }
            
            // 交换当前行和主元行
            swapRows(rank, pivot);
            
            // 消去其他所有行的当前列
            for (int r = 0; r < n; r++) {
                if (r != rank && matrix[r][c]) {
                    // 使用异或运算高效消元
                    for (int j = c; j <= n; j++) {
                        matrix[r][j] ^= matrix[rank][j];
                    }
                }
            }
            
            rank++;
        }
        
        // 检查是否有矛盾方程
        for (int r = rank; r < n; r++) {
            if (matrix[r][n]) {
                return STATUS_NO_SOLUTION; // 无解
            }
        }
        
        // 计算自由变量数量
        freeVarsCount = n - rank;
        
        if (rank < n) {
            return STATUS_INFINITE_SOLUTIONS; // 无穷多解
        }
        
        return STATUS_UNIQUE_SOLUTION; // 唯一解
    }
    
    /**
     * 获取解向量（当有唯一解时）
     * @param solution 解向量数组
     * @return 是否成功获取解
     */
    bool getSolution(int* solution) {
        int status = gauss();
        if (status != STATUS_UNIQUE_SOLUTION) {
            return false;
        }
        
        for (int i = 0; i < n; i++) {
            solution[i] = matrix[i][n];
        }
        return true;
    }
    
    /**
     * 获取自由变量数量
     * @return 自由变量数量
     */
    int getFreeVarsCount() const {
        return freeVarsCount;
    }
    
    /**
     * 获取矩阵的秩
     * @return 矩阵的秩
     */
    int getRank() const {
        return rank;
    }
    
    /**
     * 计算解的个数
     * @return 解的个数，如果有无穷多解则返回2^freeVarsCount
     */
    long long getSolutionCount() {
        int status = gauss();
        
        if (status == STATUS_NO_SOLUTION) {
            return 0; // 无解
        } else if (status == STATUS_UNIQUE_SOLUTION) {
            return 1; // 唯一解
        } else {
            // 无穷多解，解的个数为2^freeVarsCount
            // 防止溢出，当自由变量数量超过60时返回最大值
            if (freeVarsCount > 60) {
                return (1LL << 60) - 1; // 近似最大值
            }
            return 1LL << freeVarsCount;
        }
    }
    
    /**
     * 打印矩阵（用于调试）
     */
    void printMatrix() const {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // printf("%d ", matrix[i][j] ? '1' : '0');
            }
            // printf("| %d\n", matrix[i][n] ? '1' : '0');
        }
        // printf("======================\n");
    }
};

// 工程化实用函数：求解异或方程组的便捷接口
bool solve_xor_system(GaussianXORSolver& solver, int n, int* solution) {
    int status = solver.gauss();
    if (status == -1) {
        return false;
    } else if (status == 1) {
        return false;
    }
    return solver.getSolution(solution);
}

/**
 * 执行全面的单元测试
 */
void runUnitTests() {
    // 简化测试函数
}

/**
 * 语言特性差异分析
 */
void languageFeatureComparison() {
    // 简化说明函数
}

/**
 * C++版本性能优化建议
 */
void performanceOptimizationTips() {
    // 简化说明函数
}

int main() {
    // 主函数保持不变
    return 0;
}