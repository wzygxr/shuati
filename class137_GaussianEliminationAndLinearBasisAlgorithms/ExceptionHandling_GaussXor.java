package class134;

/**
 * 高斯消元和线性基算法异常处理与边界检查类
 * 
 * 本类提供完整的异常处理机制和边界条件检查，确保算法在各种输入情况下的健壮性
 * 
 * 异常处理策略：
 * 1. 参数校验：检查输入参数的合法性
 * 2. 边界条件：处理各种边界情况
 * 3. 错误恢复：提供优雅的错误处理机制
 * 4. 调试支持：提供详细的错误信息和调试信息
 * 
 * 边界条件检查：
 * 1. 空输入检查
 * 2. 数组越界检查
 * 3. 数值溢出检查
 * 4. 内存限制检查
 */
public class ExceptionHandling_GaussXor {
    
    public static int MAXN = 105; // 最大未知数个数
    
    /**
     * 高斯消元解决异或方程组（带异常处理版本）
     * 
     * @param n 未知数个数
     * @param mat 增广矩阵，1-based索引
     * @return 解的情况：-1表示无解，0表示唯一解，1表示无穷多解
     * @throws IllegalArgumentException 如果输入参数不合法
     */
    public static int gaussWithExceptionHandling(int n, int[][] mat) {
        // 参数校验
        validateInputParameters(n, mat);
        
        try {
            int r = 1; // 当前处理的行
            int c = 1; // 当前处理的列
            
            // 消元过程
            while (r <= n && c <= n) {
                // 边界检查
                if (r < 1 || r > n || c < 1 || c > n) {
                    throw new IllegalStateException("行列索引越界: r=" + r + ", c=" + c);
                }
                
                int pivot = findPivot(r, c, n, mat);
                
                if (mat[pivot][c] == 0) {
                    c++; // 当前列全为0，跳到下一列
                    continue;
                }
                
                if (pivot != r) {
                    swapRows(r, pivot, n, mat);
                }
                
                eliminateOtherRows(r, c, n, mat);
                
                c++;
                r++;
            }
            
            return checkSolution(r, n, mat);
            
        } catch (Exception e) {
            System.err.println("高斯消元过程中发生错误: " + e.getMessage());
            throw new RuntimeException("高斯消元算法执行失败", e);
        }
    }
    
    /**
     * 验证输入参数
     */
    private static void validateInputParameters(int n, int[][] mat) {
        if (n <= 0 || n >= MAXN) {
            throw new IllegalArgumentException("未知数个数n必须在1到" + (MAXN - 1) + "之间，当前n=" + n);
        }
        
        if (mat == null) {
            throw new IllegalArgumentException("增广矩阵不能为null");
        }
        
        if (mat.length < n + 1) {
            throw new IllegalArgumentException("增广矩阵行数不足，需要至少" + (n + 1) + "行，实际只有" + mat.length + "行");
        }
        
        // 检查每行的列数
        for (int i = 1; i <= n; i++) {
            if (mat[i] == null) {
                throw new IllegalArgumentException("第" + i + "行矩阵不能为null");
            }
            
            if (mat[i].length < n + 2) {
                throw new IllegalArgumentException("第" + i + "行列数不足，需要至少" + (n + 2) + "列，实际只有" + mat[i].length + "列");
            }
            
            // 检查矩阵元素是否合法（只能是0或1）
            for (int j = 1; j <= n + 1; j++) {
                if (mat[i][j] != 0 && mat[i][j] != 1) {
                    throw new IllegalArgumentException(
                        "矩阵元素只能是0或1，但mat[" + i + "][" + j + "] = " + mat[i][j]
                    );
                }
            }
        }
    }
    
    /**
     * 寻找主元
     */
    private static int findPivot(int r, int c, int n, int[][] mat) {
        int pivot = r;
        
        for (int i = r; i <= n; i++) {
            // 边界检查
            if (i < 1 || i > n) {
                throw new IllegalStateException("行索引越界: i=" + i);
            }
            
            if (mat[i][c] == 1) {
                pivot = i;
                break;
            }
        }
        
        return pivot;
    }
    
    /**
     * 交换两行
     */
    private static void swapRows(int r1, int r2, int n, int[][] mat) {
        // 边界检查
        if (r1 < 1 || r1 > n || r2 < 1 || r2 > n) {
            throw new IllegalArgumentException("行索引越界: r1=" + r1 + ", r2=" + r2);
        }
        
        for (int j = 1; j <= n + 1; j++) {
            int temp = mat[r1][j];
            mat[r1][j] = mat[r2][j];
            mat[r2][j] = temp;
        }
    }
    
    /**
     * 消去其他行的当前列系数
     */
    private static void eliminateOtherRows(int r, int c, int n, int[][] mat) {
        for (int i = 1; i <= n; i++) {
            if (i != r && mat[i][c] == 1) {
                // 边界检查
                if (i < 1 || i > n) {
                    throw new IllegalStateException("行索引越界: i=" + i);
                }
                
                for (int j = c; j <= n + 1; j++) {
                    // 边界检查
                    if (j < 1 || j > n + 1) {
                        throw new IllegalStateException("列索引越界: j=" + j);
                    }
                    
                    mat[i][j] ^= mat[r][j];
                }
            }
        }
    }
    
    /**
     * 检查解的情况
     */
    private static int checkSolution(int r, int n, int[][] mat) {
        // 检查是否有矛盾方程
        for (int i = r; i <= n; i++) {
            // 边界检查
            if (i < 1 || i > n) {
                throw new IllegalStateException("行索引越界: i=" + i);
            }
            
            boolean allZero = true;
            for (int j = 1; j <= n; j++) {
                if (mat[i][j] != 0) {
                    allZero = false;
                    break;
                }
            }
            
            if (allZero && mat[i][n + 1] == 1) {
                return -1; // 无解
            }
        }
        
        // 判断是否有自由元
        if (r <= n) {
            return 1; // 无穷多解
        }
        
        return 0; // 唯一解
    }
    
    /**
     * 线性基构造（带异常处理版本）
     * 
     * @param arr 输入数组
     * @param n 数组长度
     * @return 线性基数组
     * @throws IllegalArgumentException 如果输入参数不合法
     */
    public static long[] constructLinearBasis(long[] arr, int n) {
        // 参数校验
        if (arr == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        if (n <= 0 || n > arr.length) {
            throw new IllegalArgumentException("数组长度n不合法: n=" + n);
        }
        
        long[] basis = new long[64]; // 64位线性基
        
        try {
            for (int i = 0; i < n; i++) {
                long x = arr[i];
                
                // 检查数值范围
                if (x < 0) {
                    throw new IllegalArgumentException("输入数字不能为负数: " + x);
                }
                
                for (int j = 63; j >= 0; j--) {
                    if ((x >> j & 1) == 1) {
                        if (basis[j] == 0) {
                            basis[j] = x;
                            break;
                        } else {
                            x ^= basis[j];
                        }
                    }
                }
            }
            
            return basis;
            
        } catch (Exception e) {
            System.err.println("线性基构造过程中发生错误: " + e.getMessage());
            throw new RuntimeException("线性基构造失败", e);
        }
    }
    
    /**
     * 计算异或最大值（带异常处理版本）
     * 
     * @param arr 输入数组
     * @param n 数组长度
     * @return 异或最大值
     * @throws IllegalArgumentException 如果输入参数不合法
     */
    public static long getMaxXOR(long[] arr, int n) {
        long[] basis = constructLinearBasis(arr, n);
        
        long result = 0;
        
        try {
            for (int i = 63; i >= 0; i--) {
                if ((result ^ basis[i]) > result) {
                    result ^= basis[i];
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("异或最大值计算过程中发生错误: " + e.getMessage());
            throw new RuntimeException("异或最大值计算失败", e);
        }
    }
    
    /**
     * 测试异常处理功能
     */
    public static void testExceptionHandling() {
        System.out.println("=== 测试异常处理功能 ===");
        
        // 测试用例1：空矩阵
        try {
            gaussWithExceptionHandling(0, null);
            System.out.println("测试用例1失败：应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例1通过：" + e.getMessage());
        }
        
        // 测试用例2：矩阵元素不合法
        try {
            int[][] mat = new int[3][3];
            mat[1][1] = 2; // 非法元素
            gaussWithExceptionHandling(2, mat);
            System.out.println("测试用例2失败：应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例2通过：" + e.getMessage());
        }
        
        // 测试用例3：空数组
        try {
            constructLinearBasis(null, 0);
            System.out.println("测试用例3失败：应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例3通过：" + e.getMessage());
        }
        
        // 测试用例4：负数输入
        try {
            long[] arr = {-1L};
            getMaxXOR(arr, 1);
            System.out.println("测试用例4失败：应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例4通过：" + e.getMessage());
        }
        
        System.out.println("所有异常处理测试完成！");
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        System.out.println("开始运行异常处理测试...\n");
        
        testExceptionHandling();
        
        System.out.println("\n异常处理测试完成！");
    }
}