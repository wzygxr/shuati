package class176;

// ================================================
// 普通莫队入门题 - Java增强版 (带工程化考量和异常处理)
// ================================================
// 
// 题目描述:
// 给定一个长度为n的数组arr，一共有q条查询，格式如下
// 查询 l r : 打印arr[l..r]范围上有几种不同的数字
// 
// 数据范围:
// 1 <= n <= 3 * 10^4
// 1 <= arr[i] <= 10^6
// 1 <= q <= 2 * 10^5
// 
// 算法复杂度分析:
// 时间复杂度: O((n + q) * sqrt(n)) - 莫队算法标准复杂度
// 空间复杂度: O(n + max(arr[i])) - 数组存储和计数数组
// 
// 工程化考量:
// 1. 异常处理: 输入验证、边界检查、数组越界防护
// 2. 性能优化: 奇偶排序优化、缓存友好访问
// 3. 可维护性: 模块化设计、清晰注释、常量定义
// 4. 测试覆盖: 边界场景、极端输入、随机测试
// 
// 测试链接:
// https://www.luogu.com.cn/problem/SP3267
// https://www.spoj.com/problems/DQUERY/
// 
// 提交说明: 提交时请把类名改成"Main"
// ================================================

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Code01_MoAlgorithm1_Fixed {
    
    // ========== 常量定义 ==========
    // 最大数组长度，考虑边界扩展
    public static final int MAXN = 30001 + 10; // 额外空间用于边界处理
    // 最大数值范围，考虑极端情况
    public static final int MAXV = 1000001 + 10;
    // 最大查询数量
    public static final int MAXQ = 200001 + 10;
    
    // ========== 全局变量 ==========
    public static int n, q;
    public static int[] arr = new int[MAXN];
    // 查询结构: [l, r, query_id]
    public static int[][] query = new int[MAXQ][3];
    
    // 分块信息
    public static int[] bi = new int[MAXN];
    // 计数数组
    public static int[] cnt = new int[MAXV];
    // 当前区间不同数字种类数
    public static int kind = 0;
    
    // 结果数组
    public static int[] ans = new int[MAXQ];
    
    // ========== 异常处理标志 ==========
    public static boolean hasError = false;
    public static String errorMessage = "";
    
    // ========== 排序比较器 ==========
    
    /**
     * 普通莫队经典排序
     * 按块号排序，块内按右端点排序
     * 时间复杂度: O(q log q)
     */
    public static class QueryCmp1 implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            // 先按块号排序
            if (bi[a[0]] != bi[b[0]]) {
                return bi[a[0]] - bi[b[0]];
            }
            // 块内按右端点排序
            return a[1] - b[1];
        }
    }
    
    /**
     * 普通莫队奇偶排序优化
     * 奇数块右端点升序，偶数块右端点降序
     * 优化效果: 减少指针移动距离，提高缓存命中率
     */
    public static class QueryCmp2 implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            // 先按块号排序
            if (bi[a[0]] != bi[b[0]]) {
                return bi[a[0]] - bi[b[0]];
            }
            // 奇偶优化: 奇数块升序，偶数块降序
            if ((bi[a[0]] & 1) == 1) {
                return a[1] - b[1];
            } else {
                return b[1] - a[1];
            }
        }
    }
    
    // ========== 核心操作函数 ==========
    
    /**
     * 删除元素操作
     * @param num 要删除的数字
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static void del(int num) {
        // 安全检查: 确保数值在有效范围内
        if (num < 1 || num >= MAXV) {
            handleError("Invalid number to delete: " + num);
            return;
        }
        
        if (--cnt[num] == 0) {
            kind--;
        }
        
        // 安全检查: 计数不能为负
        if (cnt[num] < 0) {
            handleError("Count becomes negative for number: " + num);
        }
    }
    
    /**
     * 添加元素操作
     * @param num 要添加的数字
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static void add(int num) {
        // 安全检查: 确保数值在有效范围内
        if (num < 1 || num >= MAXV) {
            handleError("Invalid number to add: " + num);
            return;
        }
        
        if (++cnt[num] == 1) {
            kind++;
        }
        
        // 安全检查: 种类数不能超过实际可能的最大值
        if (kind > n) {
            handleError("Kind count exceeds array size");
        }
    }
    
    // ========== 预处理函数 ==========
    
    /**
     * 预处理函数: 分块和查询排序
     * 时间复杂度: O(n + q log q)
     * 空间复杂度: O(1)
     */
    public static void prepare() {
        // 计算块大小: sqrt(n)是最优选择
        int blen = (int) Math.sqrt(n);
        if (blen == 0) blen = 1; // 防止n=0的情况
        
        // 为每个位置分配块号
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        
        // 选择排序策略: 经典排序或奇偶优化排序
        // Arrays.sort(query, 1, q + 1, new QueryCmp1()); // 经典排序
        Arrays.sort(query, 1, q + 1, new QueryCmp2()); // 奇偶优化排序
    }
    
    // ========== 核心计算函数 ==========
    
    /**
     * 莫队算法核心计算函数
     * 时间复杂度: O((n + q) * sqrt(n))
     * 空间复杂度: O(1)
     */
    public static void compute() {
        // 初始化双指针
        int winl = 1, winr = 0;
        
        // 处理每个查询
        for (int i = 1; i <= q; i++) {
            int jobl = query[i][0];
            int jobr = query[i][1];
            
            // 边界检查
            if (jobl < 1 || jobl > n || jobr < 1 || jobr > n || jobl > jobr) {
                handleError("Invalid query range: [" + jobl + ", " + jobr + "]");
                ans[query[i][2]] = -1; // 标记错误查询
                continue;
            }
            
            // 扩展左边界
            while (winl > jobl) {
                add(arr[--winl]);
            }
            
            // 扩展右边界
            while (winr < jobr) {
                add(arr[++winr]);
            }
            
            // 收缩左边界
            while (winl < jobl) {
                del(arr[winl++]);
            }
            
            // 收缩右边界
            while (winr > jobr) {
                del(arr[winr--]);
            }
            
            // 记录结果
            ans[query[i][2]] = kind;
        }
    }
    
    // ========== 异常处理函数 ==========
    
    /**
     * 统一错误处理函数
     * @param message 错误信息
     */
    public static void handleError(String message) {
        hasError = true;
        errorMessage = message;
        System.err.println("ERROR: " + message);
    }
    
    /**
     * 输入验证函数
     * @return 验证是否通过
     */
    public static boolean validateInput() {
        if (n < 1 || n > 30000) {
            handleError("Invalid array size: " + n);
            return false;
        }
        
        if (q < 1 || q > 200000) {
            handleError("Invalid query count: " + q);
            return false;
        }
        
        // 验证数组元素范围
        for (int i = 1; i <= n; i++) {
            if (arr[i] < 1 || arr[i] > 1000000) {
                handleError("Invalid array element at index " + i + ": " + arr[i]);
                return false;
            }
        }
        
        return true;
    }
    
    // ========== 测试函数 ==========
    
    /**
     * 边界测试函数
     */
    public static void runBoundaryTests() {
        System.out.println("=== 边界测试开始 ===");
        
        // 测试1: 最小输入
        n = 1;
        q = 1;
        arr[1] = 1;
        query[1][0] = 1;
        query[1][1] = 1;
        query[1][2] = 1;
        
        prepare();
        compute();
        System.out.println("最小输入测试: " + (ans[1] == 1 ? "PASS" : "FAIL"));
        
        // 重置状态
        Arrays.fill(cnt, 0);
        kind = 0;
        
        // 测试2: 最大输入边界
        n = 30000;
        q = 200000;
        Random rand = new Random();
        for (int i = 1; i <= n; i++) {
            arr[i] = rand.nextInt(1000000) + 1;
        }
        
        // 创建大量查询
        for (int i = 1; i <= q; i++) {
            int l = rand.nextInt(n) + 1;
            int r = l + rand.nextInt(Math.min(100, n - l + 1));
            query[i][0] = l;
            query[i][1] = r;
            query[i][2] = i;
        }
        
        prepare();
        compute();
        System.out.println("最大输入边界测试: 完成");
        
        System.out.println("=== 边界测试结束 ===");
    }
    
    // ========== 性能分析函数 ==========
    
    /**
     * 性能分析函数
     */
    public static void analyzePerformance() {
        long startTime = System.currentTimeMillis();
        
        prepare();
        compute();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("性能分析:");
        System.out.println("数据规模: n=" + n + ", q=" + q);
        System.out.println("执行时间: " + duration + "ms");
        System.out.println("平均每查询时间: " + (double)duration/q + "ms");
        
        // 理论复杂度验证
        double theoretical = (n + q) * Math.sqrt(n);
        System.out.println("理论复杂度因子: " + theoretical);
    }
    
    // ========== 主函数 ==========
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        n = in.nextInt();
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        // 读取查询数量
        q = in.nextInt();
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            query[i][0] = in.nextInt();
            query[i][1] = in.nextInt();
            query[i][2] = i;
        }
        
        // 输入验证
        if (!validateInput()) {
            out.println("输入数据验证失败: " + errorMessage);
            out.flush();
            out.close();
            return;
        }
        
        // 执行算法
        prepare();
        compute();
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        // 性能分析 (可选)
        if (args.length > 0 && "--profile".equals(args[0])) {
            analyzePerformance();
        }
        
        // 边界测试 (可选)
        if (args.length > 0 && "--test".equals(args[0])) {
            runBoundaryTests();
        }
        
        out.flush();
        out.close();
        
        // 输出错误信息 (如果有)
        if (hasError) {
            System.err.println("程序执行完成，但存在错误: " + errorMessage);
        }
    }
    
    // ========== 读写工具类 ==========
    
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}