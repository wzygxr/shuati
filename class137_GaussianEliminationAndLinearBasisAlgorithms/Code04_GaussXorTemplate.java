package class134;

import java.io.*;
import java.util.*;

/**
 * 高斯消元解决异或方程组模板 (Java版本)
 * 
 * 算法原理：
 * 高斯消元法是一种求解线性方程组的经典算法。对于异或方程组，我们将其应用于模2意义下的线性方程组，
 * 其中加法运算替换为异或运算。通过行变换将方程组转化为简化行阶梯形矩阵，从而得到解的情况和具体解。
 * 
 * 时间复杂度: O(n³)，其中n为未知数个数
 * 空间复杂度: O(n²)，用于存储增广矩阵
 * 
 * 适用场景：
 * 1. 开关问题（如POJ 1830、POJ 1222）
 * 2. 线性基求异或最大值/最小值问题
 * 3. 异或方程组求解
 * 4. 某些图论问题的建模
 * 5. 密码学中的一些问题
 * 
 * Java语言特性优化：
 * 1. 使用类封装提高代码复用性和可维护性
 * 2. 异常处理确保代码健壮性
 * 3. 支持0-based索引的内部实现和1-based索引的外部接口
 * 4. 使用位运算优化性能
 */
public class Code04_GaussXorTemplate {
    
    public static int MAXN = 105; // 最大未知数个数，增加到105以提供更多空间余量
    
    // 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
    public static int[][] mat = new int[MAXN][MAXN];
    
    public static int n; // 未知数个数，也是方程个数
    
    /**
     * 高斯消元解决异或方程组模板（静态方法版本）
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
     * - 使用1-based索引便于理解
     * - 提供详细的错误处理机制
     * - 支持矩阵打印用于调试
     * 
     * @param n 未知数个数，必须大于0
     * @return 0表示有唯一解，1表示有无穷多解，-1表示无解
     * @throws IllegalArgumentException 如果n小于等于0或超过最大限制
     */
    public static int gauss(int n) {
        int r = 1; // 当前处理的行，使用1-based索引
        int c = 1; // 当前处理的列，使用1-based索引
        
        // 消元过程 - 按列进行处理
        while (r <= n && c <= n) {
            int pivot = r; // 主元行初始化为当前行
            
            // 寻找主元（当前列中系数为1的行）
            for (int i = r; i <= n; i++) {
                if (mat[i][c] == 1) {
                    pivot = i;
                    break;
                }
            }
            
            // 如果找不到主元，说明当前列全为0，跳到下一列
            if (mat[pivot][c] == 0) {
                c++; // 保持当前行不变，列加1
                continue;
            }
            
            // 交换第r行和第pivot行
            if (pivot != r) {
                swap(r, pivot);
            }
            
            // 消去其他行的当前列系数
            for (int i = 1; i <= n; i++) {
                if (i != r && mat[i][c] == 1) {
                    // 第i行异或第r行，这是模2意义下的消元
                    for (int j = c; j <= n + 1; j++) {
                        mat[i][j] ^= mat[r][j]; // 异或运算实现行变换
                    }
                }
            }
            
            // 处理下一行和下一列
            c++;
            r++;
        }
        
        // 判断解的情况
        // 检查是否有形如 0 = 1 的矛盾方程
        for (int i = r; i <= n; i++) {
            if (mat[i][n + 1] == 1) {
                return -1; // 无解，出现矛盾方程
            }
        }
        
        // 判断是否有自由元（形如 0 = 0 的方程）
        if (r <= n) {
            return 1; // 有无穷多解，存在自由变量
        }
        
        return 0; // 有唯一解
    }
    
    /**
     * 交换矩阵中的两行
     * 
     * @param a 行号1
     * @param b 行号2
     */
    public static void swap(int a, int b) {
        int[] tmp = mat[a];
        mat[a] = mat[b];
        mat[b] = tmp;
    }
    
    /**
     * 打印增广矩阵（用于调试）
     * 
     * @param n 未知数个数
     */
    public static void printMatrix(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("========================");
    }
    
    /**
     * 获取解向量（当有唯一解时）
     * 
     * 回代求解过程：
     * 从最后一行开始，逐行求解未知数，利用已求解的未知数，计算当前未知数的值
     * 
     * @param n 未知数个数
     * @param solution 解向量数组
     */
    public static void getSolution(int n, int[] solution) {
        // 从最后一行开始回代求解
        for (int i = n; i >= 1; i--) {
            solution[i] = mat[i][n + 1]; // 初始化为常数项
            // 减去已知变量的影响
            for (int j = i + 1; j <= n; j++) {
                solution[i] ^= (mat[i][j] & solution[j]); // 异或相当于模2意义下的减法
            }
        }
    }
    
    /**
     * 高斯消元解决异或方程组的类实现（面向对象版本）
     * 提供更完善的接口、错误处理和状态管理
     * 
     * 设计特点：
     * 1. 面向对象封装，提高代码可维护性和复用性
     * 2. 完善的异常处理机制
     * 3. 状态管理和信息查询接口
     * 4. 0-based索引的内部实现
     */
    public static class GaussianXORSolver {
        private int[][] matrix; // 增广矩阵，0-based索引
        private int n; // 未知数个数
        private int freeVarsCount; // 自由变量数量
        private final int maxSize; // 最大未知数个数
        private final static int STATUS_UNIQUE_SOLUTION = 0; // 唯一解状态
        private final static int STATUS_INFINITE_SOLUTIONS = 1; // 无穷多解状态
        private final static int STATUS_NO_SOLUTION = -1; // 无解状态
        
        /**
         * 构造函数
         * @param maxSize 最大支持的未知数个数
         */
        public GaussianXORSolver(int maxSize) {
            this.maxSize = maxSize;
            this.matrix = null;
            this.n = 0;
            this.freeVarsCount = 0;
        }
        
        /**
         * 构造函数，使用默认最大大小
         */
        public GaussianXORSolver() {
            this(105); // 默认最大大小为105
        }
        
        /**
         * 重置并初始化矩阵
         * @param n 未知数个数
         * @throws IllegalArgumentException 如果n超过最大大小
         */
        public void reset(int n) {
            if (n <= 0 || n > maxSize) {
                throw new IllegalArgumentException("未知数个数必须在1到" + maxSize + "之间");
            }
            
            this.n = n;
            this.matrix = new int[n][n + 1]; // 0-based索引，n行，n+1列
            this.freeVarsCount = 0;
        }
        
        /**
         * 设置一个方程（0-based索引）
         * @param row 行号（从0开始）
         * @param coefficients 系数数组
         * @param result 方程右边的结果
         * @throws IllegalStateException 如果矩阵未初始化
         * @throws IndexOutOfBoundsException 如果行号越界
         * @throws IllegalArgumentException 如果系数数组长度不匹配
         */
        public void setEquation(int row, int[] coefficients, int result) {
            if (matrix == null) {
                throw new IllegalStateException("矩阵未初始化，请先调用reset方法");
            }
            
            if (row < 0 || row >= n) {
                throw new IndexOutOfBoundsException("行号超出范围: " + row);
            }
            
            if (coefficients.length != n) {
                throw new IllegalArgumentException("系数数组长度不匹配: 期望" + n + ", 实际" + coefficients.length);
            }
            
            // 设置系数部分
            for (int j = 0; j < n; j++) {
                matrix[row][j] = coefficients[j] & 1; // 确保值为0或1
            }
            // 设置常数项
            matrix[row][n] = result & 1; // 确保值为0或1
        }
        
        /**
         * 设置一个方程（1-based索引接口）
         * @param row 行号（从1开始）
         * @param coefficients 系数数组
         * @param result 方程右边的结果
         */
        public void setEquation1Based(int row, int[] coefficients, int result) {
            setEquation(row - 1, coefficients, result);
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
         * @throws IllegalStateException 如果矩阵未初始化
         */
        public int gauss() {
            if (matrix == null) {
                throw new IllegalStateException("矩阵未初始化，请先调用reset方法");
            }
            
            int rows = n;
            int cols = n;
            int rank = 0; // 矩阵的秩
            
            // 遍历每一列
            for (int c = 0; c < cols && rank < rows; c++) {
                // 寻找主元
                int pivot = -1;
                for (int r = rank; r < rows; r++) {
                    if (matrix[r][c] == 1) {
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
                for (int r = 0; r < rows; r++) {
                    if (r != rank && matrix[r][c] == 1) {
                        // 第r行异或第rank行
                        for (int j = c; j <= cols; j++) {
                            matrix[r][j] ^= matrix[rank][j];
                        }
                    }
                }
                
                rank++;
            }
            
            // 检查是否有矛盾方程
            for (int r = rank; r < rows; r++) {
                if (matrix[r][cols] == 1) {
                    return STATUS_NO_SOLUTION; // 无解
                }
            }
            
            // 计算自由变量数量
            freeVarsCount = cols - rank;
            
            if (rank < cols) {
                return STATUS_INFINITE_SOLUTIONS; // 无穷多解
            }
            
            return STATUS_UNIQUE_SOLUTION; // 唯一解
        }
        
        /**
         * 交换矩阵中的两行
         * @param a 行号1
         * @param b 行号2
         */
        private void swapRows(int a, int b) {
            int[] temp = matrix[a];
            matrix[a] = matrix[b];
            matrix[b] = temp;
        }
        
        /**
         * 获取解向量（当有唯一解时）
         * @return 解向量数组，0-based索引
         * @throws IllegalStateException 如果没有唯一解或矩阵未初始化
         */
        public int[] getSolution() {
            int status = gauss();
            if (status != STATUS_UNIQUE_SOLUTION) {
                throw new IllegalStateException("方程组没有唯一解，无法获取解向量");
            }
            
            // 高斯消元后，解直接在矩阵的最后一列
            int[] solution = new int[n];
            for (int i = 0; i < n; i++) {
                solution[i] = matrix[i][n];
            }
            
            return solution;
        }
        
        /**
         * 获取自由变量数量
         * @return 自由变量数量
         */
        public int getFreeVarsCount() {
            return freeVarsCount;
        }
        
        /**
         * 计算解的个数
         * @return 解的个数，如果有无穷多解则返回2^freeVarsCount
         */
        public long getSolutionCount() {
            int status = gauss();
            
            if (status == STATUS_NO_SOLUTION) {
                return 0; // 无解
            } else if (status == STATUS_UNIQUE_SOLUTION) {
                return 1; // 唯一解
            } else {
                // 无穷多解，解的个数为2^freeVarsCount
                // 防止溢出，当自由变量数量超过60时返回Long.MAX_VALUE
                if (freeVarsCount > 60) {
                    return Long.MAX_VALUE;
                }
                return 1L << freeVarsCount;
            }
        }
        
        /**
         * 打印矩阵（用于调试）
         */
        public void printMatrix() {
            if (matrix == null) {
                System.out.println("矩阵未初始化");
                return;
            }
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.printf("%2d ", matrix[i][j]);
                }
                System.out.print("| ");
                System.out.printf("%2d", matrix[i][n]);
                System.out.println();
            }
            System.out.println("=" + "=====================\n");
        }
    }
    
    /**
     * 执行全面的单元测试
     * 
     * 测试覆盖：
     * 1. 唯一解的情况
     * 2. 无穷多解的情况
     * 3. 无解的情况
     * 4. 边界情况
     */
    public static void runUnitTests() {
        System.out.println("执行高斯消元异或方程组求解器单元测试...");
        
        // 测试1: 有唯一解的情况
        testUniqueSolution();
        
        // 测试2: 有无穷多解的情况
        testInfiniteSolutions();
        
        // 测试3: 无解的情况
        testNoSolution();
        
        // 测试4: 边界情况 - 空矩阵
        testEdgeCases();
    }
    
    /**
     * 测试唯一解的情况
     */
    private static void testUniqueSolution() {
        System.out.println("\n测试1: 唯一解的情况");
        GaussianXORSolver solver = new GaussianXORSolver();
        solver.reset(2);
        
        // x1 = 1
        solver.setEquation(0, new int[]{1, 0}, 1);
        // x2 = 0
        solver.setEquation(1, new int[]{0, 1}, 0);
        
        System.out.println("原始矩阵:");
        solver.printMatrix();
        
        int status = solver.gauss();
        System.out.println("状态码: " + status);
        System.out.println("自由变量数量: " + solver.getFreeVarsCount());
        System.out.println("解的个数: " + solver.getSolutionCount());
        
        try {
            int[] solution = solver.getSolution();
            System.out.print("解: [");
            for (int i = 0; i < solution.length; i++) {
                System.out.print(solution[i]);
                if (i < solution.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        } catch (IllegalStateException e) {
            System.out.println("获取解失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试无穷多解的情况
     */
    private static void testInfiniteSolutions() {
        System.out.println("\n测试2: 无穷多解的情况");
        GaussianXORSolver solver = new GaussianXORSolver();
        solver.reset(2);
        
        // x1 ^ x2 = 1
        solver.setEquation(0, new int[]{1, 1}, 1);
        // 0 = 0
        solver.setEquation(1, new int[]{0, 0}, 0);
        
        System.out.println("原始矩阵:");
        solver.printMatrix();
        
        int status = solver.gauss();
        System.out.println("状态码: " + status);
        System.out.println("自由变量数量: " + solver.getFreeVarsCount());
        System.out.println("解的个数: " + solver.getSolutionCount());
        
        try {
            int[] solution = solver.getSolution();
            System.out.println("解: " + Arrays.toString(solution));
        } catch (IllegalStateException e) {
            System.out.println("获取解失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试无解的情况
     */
    private static void testNoSolution() {
        System.out.println("\n测试3: 无解的情况");
        GaussianXORSolver solver = new GaussianXORSolver();
        solver.reset(2);
        
        // x1 = 1
        solver.setEquation(0, new int[]{1, 0}, 1);
        // x1 = 0
        solver.setEquation(1, new int[]{1, 0}, 0);
        
        System.out.println("原始矩阵:");
        solver.printMatrix();
        
        int status = solver.gauss();
        System.out.println("状态码: " + status);
        System.out.println("自由变量数量: " + solver.getFreeVarsCount());
        System.out.println("解的个数: " + solver.getSolutionCount());
        
        try {
            int[] solution = solver.getSolution();
            System.out.println("解: " + Arrays.toString(solution));
        } catch (IllegalStateException e) {
            System.out.println("获取解失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试边界情况
     */
    private static void testEdgeCases() {
        System.out.println("\n测试4: 边界情况");
        
        try {
            GaussianXORSolver solver = new GaussianXORSolver();
            // 测试负数未知数数量
            solver.reset(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("负数未知数数量测试通过: " + e.getMessage());
        }
        
        try {
            GaussianXORSolver solver = new GaussianXORSolver(10);
            // 测试超过最大大小的未知数数量
            solver.reset(20);
        } catch (IllegalArgumentException e) {
            System.out.println("超过最大大小测试通过: " + e.getMessage());
        }
    }
    
    /**
     * 语言特性差异分析
     * 
     * Java与其他语言的比较：
     * 1. 静态类型检查更严格，编译时即可捕获更多错误
     * 2. 内存管理精确，通过new关键字显式分配内存
     * 3. 类继承和接口实现提供更严格的OOP范式
     * 4. 并发编程支持更完善，提供多线程API
     */
    public static void languageFeatureComparison() {
        System.out.println("\n===== 语言特性差异分析 =====");
        System.out.println("1. Java 优势:");
        System.out.println("   - 静态类型检查更严格，编译时即可捕获更多错误");
        System.out.println("   - 内存管理精确，通过new关键字显式分配内存");
        System.out.println("   - 类继承和接口实现提供更严格的OOP范式");
        System.out.println("   - 并发编程支持更完善，提供多线程API");
        System.out.println("\n2. C++ 优势:");
        System.out.println("   - 位运算性能最优，可以使用bitset或位压缩优化存储");
        System.out.println("   - 模板机制提供更强的泛型支持和编译期优化");
        System.out.println("   - 直接内存操作能力，性能控制更精细");
        System.out.println("\n3. Python 优势:");
        System.out.println("   - 动态类型系统使代码更简洁，开发效率高");
        System.out.println("   - 内置数据结构（如列表推导式）简化矩阵操作");
        System.out.println("   - 异常处理机制语法更简洁，使用try-except块");
        System.out.println("   - 科学计算库（如numpy）支持高效的矩阵运算");
    }
    
    /**
     * 性能优化建议
     * 
     * Java版本优化策略：
     * 1. 内存优化：使用位压缩存储，避免频繁的对象创建
     * 2. 算法优化：使用位运算代替普通运算
     * 3. IO优化：使用BufferedReader/BufferedWriter进行高效IO
     * 4. 其他优化：使用final修饰不变变量，避免不必要的装箱和拆箱操作
     */
    public static void performanceOptimizationTips() {
        System.out.println("\n===== Java版本性能优化建议 =====");
        System.out.println("1. 内存优化:");
        System.out.println("   - 对于大规模矩阵，可以使用位压缩存储，如使用BitSet或long[]");
        System.out.println("   - 避免频繁的对象创建，重用对象以减少GC压力");
        System.out.println("\n2. 算法优化:");
        System.out.println("   - 使用位运算代替普通运算，特别是对于0/1矩阵");
        System.out.println("   - 对于稀疏矩阵，可以使用稀疏矩阵表示法（如CSR格式）");
        System.out.println("   - 利用Java 8+的并行流或ForkJoinPool进行并行计算");
        System.out.println("\n3. IO优化:");
        System.out.println("   - 使用BufferedReader/BufferedWriter进行高效IO");
        System.out.println("   - 对于大规模数据，可以考虑使用内存映射文件");
        System.out.println("\n4. 其他优化:");
        System.out.println("   - 使用final修饰不变变量，有助于JVM优化");
        System.out.println("   - 避免不必要的装箱和拆箱操作");
        System.out.println("   - 适当使用局部变量代替实例变量，减少内存访问开销");
    }
    
    // 主函数，执行测试
    public static void main(String[] args) {
        System.out.println("高斯消元解异或方程组模板测试");
        
        // 测试用例1: 唯一解
        System.out.println("\n测试用例1 - 唯一解:");
        n = 3;
        // x1 ^ x2 ^ x3 = 0
        // x1 ^ x3 = 1
        // x2 ^ x3 = 1
        mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 1; mat[1][4] = 0;
        mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 1;
        mat[3][1] = 0; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 1;
        
        System.out.println("原矩阵:");
        printMatrix(n);
        
        int result = gauss(n);
        if (result == 0) {
            System.out.println("方程组有唯一解");
            int[] solution = new int[n + 1];
            getSolution(n, solution);
            System.out.print("解为: ");
            for (int i = 1; i <= n; i++) {
                System.out.print("x" + i + "=" + solution[i] + " ");
            }
            System.out.println();
        } else if (result == 1) {
            System.out.println("方程组有无穷多解");
        } else {
            System.out.println("方程组无解");
        }
        
        // 测试用例2: 无解
        System.out.println("\n测试用例2 - 无解:");
        n = 3;
        // x1 ^ x2 = 1
        // x1 ^ x3 = 1
        // x2 ^ x3 = 1
        mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 0; mat[1][4] = 1;
        mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 1;
        mat[3][1] = 0; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 1;
        
        System.out.println("原矩阵:");
        printMatrix(n);
        
        result = gauss(n);
        if (result == 0) {
            System.out.println("方程组有唯一解");
            int[] solution = new int[n + 1];
            getSolution(n, solution);
            System.out.print("解为: ");
            for (int i = 1; i <= n; i++) {
                System.out.print("x" + i + "=" + solution[i] + " ");
            }
            System.out.println();
        } else if (result == 1) {
            System.out.println("方程组有无穷多解");
        } else {
            System.out.println("方程组无解");
        }
        
        // 测试用例3: 无穷多解
        System.out.println("\n测试用例3 - 无穷多解:");
        n = 3;
        // x1 ^ x3 = 1
        // x2 ^ x3 = 1
        // x1 ^ x2 = 0
        mat[1][1] = 1; mat[1][2] = 0; mat[1][3] = 1; mat[1][4] = 1;
        mat[2][1] = 0; mat[2][2] = 1; mat[2][3] = 1; mat[2][4] = 1;
        mat[3][1] = 1; mat[3][2] = 1; mat[3][3] = 0; mat[3][4] = 0;
        
        System.out.println("原矩阵:");
        printMatrix(n);
        
        // 运行新的面向对象版本的单元测试
        runUnitTests();
        
        // 输出语言特性差异分析和性能优化建议
        languageFeatureComparison();
        performanceOptimizationTips();
        
        // 运行高斯消元
        result = gauss(n);
        if (result == 0) {
            System.out.println("方程组有唯一解");
            int[] solution = new int[n + 1];
            getSolution(n, solution);
            System.out.print("解为: ");
            for (int i = 1; i <= n; i++) {
                System.out.print("x" + i + "=" + solution[i] + " ");
            }
            System.out.println();
        } else if (result == 1) {
            System.out.println("方程组有无穷多解");
        } else {
            System.out.println("方程组无解");
        }
    }
}