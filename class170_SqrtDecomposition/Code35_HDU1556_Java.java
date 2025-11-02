/**
 * HDU 1556 Color the ball
 * 题目要求：区间更新，单点查询
 * 核心技巧：分块标记（懒惰标记）
 * 时间复杂度：O(√n) / 操作
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1556
 * 
 * 算法思想详解：
 * 1. 将数组分成√n大小的块
 * 2. 对于完全覆盖的块，使用懒惰标记记录增量
 * 3. 对于部分覆盖的块，暴力更新每个元素
 * 4. 查询时，累加块标记和元素自身的值
 */

import java.util.Scanner;

public class Code35_HDU1556_Java {
    private int[] arr;      // 原始数组
    private int[] blockAdd; // 块的懒惰标记
    private int blockSize;  // 块的大小
    private int n;          // 数组长度
    
    /**
     * 构造函数，初始化分块数据结构
     * 
     * @param size 数组大小
     */
    public Code35_HDU1556_Java(int size) {
        n = size;
        // 计算块的大小，通常取√n
        blockSize = (int) Math.sqrt(n) + 1;
        arr = new int[n + 1]; // 题目中的球是1-based编号
        blockAdd = new int[(n + blockSize - 1) / blockSize];
    }
    
    /**
     * 区间更新操作：将区间[l, r]的每个元素加1
     * 
     * @param l 左边界（1-based）
     * @param r 右边界（1-based）
     */
    public void updateRange(int l, int r) {
        // 处理越界情况
        if (l < 1) l = 1;
        if (r > n) r = n;
        if (l > r) return;
        
        int blockL = (l - 1) / blockSize;
        int blockR = (r - 1) / blockSize;
        
        // 同一块内，直接暴力更新
        if (blockL == blockR) {
            for (int i = l; i <= r; i++) {
                arr[i]++;
            }
            return;
        }
        
        // 处理左边不完整的块
        for (int i = l; i <= ((blockL + 1) * blockSize); i++) {
            arr[i]++;
        }
        
        // 处理中间完整的块，使用懒惰标记
        for (int i = blockL + 1; i < blockR; i++) {
            blockAdd[i]++;
        }
        
        // 处理右边不完整的块
        for (int i = blockR * blockSize + 1; i <= r; i++) {
            arr[i]++;
        }
    }
    
    /**
     * 单点查询操作：查询位置x的值
     * 
     * @param x 查询位置（1-based）
     * @return 位置x的值
     */
    public int queryPoint(int x) {
        // 处理越界情况
        if (x < 1 || x > n) {
            throw new IllegalArgumentException("查询位置越界：" + x);
        }
        
        int blockIndex = (x - 1) / blockSize;
        // 元素值 = 原始值 + 所属块的标记值
        return arr[x] + blockAdd[blockIndex];
    }
    
    /**
     * 初始化数组，设置所有元素为0
     */
    public void clear() {
        for (int i = 1; i <= n; i++) {
            arr[i] = 0;
        }
        for (int i = 0; i < blockAdd.length; i++) {
            blockAdd[i] = 0;
        }
    }
    
    /**
     * 运行测试
     */
    public static void runTest() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int n = scanner.nextInt();
            if (n == 0) break; // 输入0结束
            
            Code35_HDU1556_Java solution = new Code35_HDU1556_Java(n);
            
            // 处理m个操作
            int m = n; // 题目中m等于n
            for (int i = 0; i < m; i++) {
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                solution.updateRange(l, r);
            }
            
            // 输出每个点的最终颜色数
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                sb.append(solution.queryPoint(i));
                if (i < n) {
                    sb.append(" ");
                }
            }
            System.out.println(sb.toString());
        }
        
        scanner.close();
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 测试不同规模的数据
        int[] testSizes = {100, 1000, 10000, 100000};
        
        for (int size : testSizes) {
            Code35_HDU1556_Java solution = new Code35_HDU1556_Java(size);
            
            long startTime = System.currentTimeMillis();
            
            // 执行size次随机操作
            for (int i = 0; i < size; i++) {
                int l = (int)(Math.random() * size) + 1;
                int r = (int)(Math.random() * size) + 1;
                if (l > r) {
                    int temp = l;
                    l = r;
                    r = temp;
                }
                solution.updateRange(l, r);
            }
            
            // 执行查询
            for (int i = 1; i <= size; i += 100) {
                solution.queryPoint(i);
            }
            
            long endTime = System.currentTimeMillis();
            System.out.printf("数据规模 %d, 耗时: %d ms\n", size, endTime - startTime);
        }
    }
    
    /**
     * 测试正确性的函数
     */
    public static void correctnessTest() {
        System.out.println("=== 正确性测试 ===");
        
        // 简单测试案例
        Code35_HDU1556_Java solution = new Code35_HDU1556_Java(5);
        
        // 执行更新操作
        solution.updateRange(1, 3);
        solution.updateRange(2, 5);
        solution.updateRange(1, 1);
        
        // 检查结果
        int[] expected = {0, 2, 2, 2, 1, 1}; // expected[0]无效，从1开始
        boolean allCorrect = true;
        
        System.out.println("查询结果：");
        for (int i = 1; i <= 5; i++) {
            int actual = solution.queryPoint(i);
            System.out.printf("位置 %d: 预期=%d, 实际=%d\n", i, expected[i], actual);
            if (actual != expected[i]) {
                allCorrect = false;
            }
        }
        
        System.out.println("测试" + (allCorrect ? "通过" : "失败"));
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        System.out.println("HDU 1556 Color the ball 解决方案");
        System.out.println("1. 运行标准测试（按题目输入格式）");
        System.out.println("2. 运行正确性测试");
        System.out.println("3. 运行性能测试");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("请选择测试类型：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                runTest();
                break;
            case 2:
                correctnessTest();
                break;
            case 3:
                performanceTest();
                break;
            default:
                System.out.println("无效选择，运行正确性测试");
                correctnessTest();
                break;
        }
        
        scanner.close();
    }
    
    /**
     * 时间复杂度分析：
     * - 区间更新：
     *   - 对于完整块：O(1)，只更新懒惰标记
     *   - 对于不完整块：O(√n)，最多处理两个不完整块，每个最多√n个元素
     *   - 总时间复杂度：O(√n)
     * 
     * - 单点查询：O(1)，直接返回arr[x] + blockAdd[blockIndex]
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 懒惰标记数组blockAdd：O(√n)
     * - 总空间复杂度：O(n + √n) = O(n)
     * 
     * 优化技巧：
     * 1. 块大小选择：取√n可以使得时间复杂度最优
     * 2. 边界处理：完善的边界检查确保算法的正确性
     * 3. 使用StringBuilder拼接输出，避免频繁字符串拼接
     * 
     * 与线段树对比：
     * - 分块算法实现更简单
     * - 对于这个问题，分块和线段树的时间复杂度相同
     * - 分块算法的常数可能更小，适合实际应用
     */
}