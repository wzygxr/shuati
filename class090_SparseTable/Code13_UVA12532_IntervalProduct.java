package class117;

/**
 * UVA 12532 Interval Product - 区间乘积符号查询 - Sparse Table应用
 * 题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3977
 * 
 * 【题目描述】
 * 给定一个整数数组，支持两种操作：
 * 1. 修改某个位置的数值
 * 2. 查询区间乘积的符号（正数、负数或零）
 * 
 * 【示例】
 * 输入：
 * 4 6
 * -2 6 0 -1
 * C 1 10
 * P 1 4
 * C 3 7
 * P 2 2
 * C 4 -5
 * P 1 4
 * 
 * 输出：
 * 0
 * +
 * -
 * 
 * 【算法核心思想】
 * 由于我们只关心乘积的符号，可以将数值映射为：正数→1，负数→-1，零→0
 * 然后使用Sparse Table预处理区间乘积的符号信息
 * 
 * 【核心原理】
 * 1. 数值映射：将原始数值映射为符号表示（1, -1, 0）
 * 2. Sparse Table：预处理区间乘积的符号信息
 * 3. 查询处理：根据乘积结果判断符号
 * 4. 更新处理：支持单点更新（需要重新构建ST表）
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n log n) - 构建Sparse Table
 * - 查询：O(1) - 每次查询一次ST表查询
 * - 更新：O(n log n) - 需要重新构建ST表（实际应用中可使用线段树优化）
 * - 总时间复杂度：O((n + q) log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table：O(n log n)
 * - 映射数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【应用场景】
 * 1. 金融数据分析中的趋势判断
 * 2. 信号处理中的符号分析
 * 3. 统计学中的相关性分析
 * 4. 游戏开发中的状态判断
 * 5. 控制系统中的稳定性分析
 * 6. 数据挖掘中的模式识别
 * 7. 风险评估中的趋势预测
 * 
 * 【工程化考量】
 * 1. 数值映射：处理各种边界情况和特殊值
 * 2. 符号计算：避免数值溢出问题
 * 3. 更新效率：对于频繁更新场景，考虑使用线段树替代
 * 4. 内存管理：优化大数组的内存使用
 * 5. 错误处理：处理无效操作和边界条件
 */
import java.util.*;

public class Code13_UVA12532_IntervalProduct {
    
    /**
     * 区间乘积符号查询解决方案类
     */
    static class IntervalProductSolver {
        private int[] originalArr;      // 原始数组
        private int[] signArr;          // 符号数组（1:正数, -1:负数, 0:零）
        private SparseTable st;         // Sparse Table
        
        public IntervalProductSolver(int[] arr) {
            this.originalArr = arr.clone();
            this.signArr = new int[arr.length];
            
            // 初始化符号数组
            initializeSignArray();
            
            // 构建Sparse Table
            rebuildSparseTable();
        }
        
        /**
         * 初始化符号数组
         */
        private void initializeSignArray() {
            for (int i = 0; i < originalArr.length; i++) {
                if (originalArr[i] > 0) {
                    signArr[i] = 1;
                } else if (originalArr[i] < 0) {
                    signArr[i] = -1;
                } else {
                    signArr[i] = 0;
                }
            }
        }
        
        /**
         * 重新构建Sparse Table
         */
        private void rebuildSparseTable() {
            st = new SparseTable(signArr);
        }
        
        /**
         * 查询区间[l, r]的乘积符号
         * @return 符号字符串："+"（正数），"-"（负数），"0"（零）
         */
        public String query(int l, int r) {
            if (l < 0 || r >= signArr.length || l > r) {
                throw new IllegalArgumentException("Invalid query range: [" + l + ", " + r + "]");
            }
            
            int productSign = st.query(l, r);
            
            if (productSign == 0) {
                return "0";
            } else if (productSign > 0) {
                return "+";
            } else {
                return "-";
            }
        }
        
        /**
         * 更新指定位置的值
         */
        public void update(int index, int newValue) {
            if (index < 0 || index >= originalArr.length) {
                throw new IllegalArgumentException("Invalid index: " + index);
            }
            
            // 更新原始数组
            originalArr[index] = newValue;
            
            // 更新符号数组
            if (newValue > 0) {
                signArr[index] = 1;
            } else if (newValue < 0) {
                signArr[index] = -1;
            } else {
                signArr[index] = 0;
            }
            
            // 重新构建Sparse Table
            rebuildSparseTable();
        }
        
        /**
         * 获取当前数组状态（用于调试）
         */
        public String getArrayState() {
            StringBuilder sb = new StringBuilder();
            sb.append("Original: ").append(Arrays.toString(originalArr)).append("\n");
            sb.append("Signs:    ").append(Arrays.toString(signArr));
            return sb.toString();
        }
    }
    
    /**
     * 支持乘积运算的Sparse Table实现类
     */
    static class SparseTable {
        private int[][] st;             // ST表
        private int[] logTable;         // 预处理log2值
        
        public SparseTable(int[] arr) {
            int n = arr.length;
            if (n == 0) return;
            
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            st = new int[n][maxLog];
            logTable = new int[n + 1];
            
            preprocessLog(n);
            
            // 初始化第一层
            for (int i = 0; i < n; i++) {
                st[i][0] = arr[i];
            }
            
            // 动态规划构建ST表
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) - 1 < n; i++) {
                    // 乘积运算：如果任一因子为0，结果为0；否则计算符号乘积
                    if (st[i][j - 1] == 0 || st[i + (1 << (j - 1))][j - 1] == 0) {
                        st[i][j] = 0;
                    } else {
                        st[i][j] = st[i][j - 1] * st[i + (1 << (j - 1))][j - 1];
                    }
                }
            }
        }
        
        /**
         * 预处理log2值
         */
        private void preprocessLog(int n) {
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 查询区间[l, r]的乘积符号
         */
        public int query(int l, int r) {
            if (l > r) {
                throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
            }
            
            int len = r - l + 1;
            int k = logTable[len];
            
            // 处理两个重叠区间
            int leftProduct = st[l][k];
            int rightProduct = st[r - (1 << k) + 1][k];
            
            // 如果任一区间乘积为0，整体结果为0
            if (leftProduct == 0 || rightProduct == 0) {
                return 0;
            }
            
            // 计算符号乘积
            return leftProduct * rightProduct;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：UVA示例
        int[] arr1 = {-2, 6, 0, -1};
        IntervalProductSolver solver1 = new IntervalProductSolver(arr1);
        
        System.out.println("测试用例1 - UVA示例:");
        System.out.println("初始状态:");
        System.out.println(solver1.getArrayState());
        
        // 查询初始状态
        System.out.println("查询[1,4]: " + solver1.query(0, 3) + " (期望: 0)");
        
        // 更新操作
        solver1.update(0, 10); // C 1 10
        System.out.println("\n更新位置1为10后:");
        System.out.println(solver1.getArrayState());
        System.out.println("查询[1,4]: " + solver1.query(0, 3) + " (期望: 0)");
        
        solver1.update(2, 7); // C 3 7
        System.out.println("\n更新位置3为7后:");
        System.out.println(solver1.getArrayState());
        System.out.println("查询[2,2]: " + solver1.query(1, 1) + " (期望: +)");
        
        solver1.update(3, -5); // C 4 -5
        System.out.println("\n更新位置4为-5后:");
        System.out.println(solver1.getArrayState());
        System.out.println("查询[1,4]: " + solver1.query(0, 3) + " (期望: -)");
        
        // 测试用例2：全正数数组
        int[] arr2 = {1, 2, 3, 4, 5};
        IntervalProductSolver solver2 = new IntervalProductSolver(arr2);
        System.out.println("\n测试用例2 - 全正数数组:");
        System.out.println("查询[0,4]: " + solver2.query(0, 4) + " (期望: +)");
        
        // 测试用例3：全负数数组
        int[] arr3 = {-1, -2, -3, -4, -5};
        IntervalProductSolver solver3 = new IntervalProductSolver(arr3);
        System.out.println("\n测试用例3 - 全负数数组:");
        System.out.println("查询[0,4]: " + solver3.query(0, 4) + " (期望: +)"); // 偶数个负数乘积为正
        System.out.println("查询[0,3]: " + solver3.query(0, 3) + " (期望: -)"); // 奇数个负数乘积为负
        
        // 测试用例4：包含零的数组
        int[] arr4 = {1, 0, -2, 3, 0};
        IntervalProductSolver solver4 = new IntervalProductSolver(arr4);
        System.out.println("\n测试用例4 - 包含零的数组:");
        System.out.println("查询[0,4]: " + solver4.query(0, 4) + " (期望: 0)");
        System.out.println("查询[0,1]: " + solver4.query(0, 1) + " (期望: 0)");
        System.out.println("查询[2,3]: " + solver4.query(2, 3) + " (期望: -)");
        
        // 性能测试
        int[] largeArr = new int[10000];
        Random random = new Random();
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = random.nextInt(21) - 10; // -10到10的随机数
        }
        
        IntervalProductSolver largeSolver = new IntervalProductSolver(largeArr);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int l = random.nextInt(largeArr.length - 100);
            int r = l + random.nextInt(100);
            largeSolver.query(l, r);
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n性能测试: 1000次查询耗时 " + (endTime - startTime) + "ms");
        
        // 更新性能测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(largeArr.length);
            int value = random.nextInt(21) - 10;
            largeSolver.update(index, value);
        }
        endTime = System.currentTimeMillis();
        
        System.out.println("更新性能测试: 100次更新耗时 " + (endTime - startTime) + "ms");
        
        System.out.println("\n所有测试用例执行完成！");
    }
}