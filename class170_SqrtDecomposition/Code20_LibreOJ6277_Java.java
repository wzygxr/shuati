package class172;

// LibreOJ #6277 数列分块入门1 - Java实现
// 题目来源：LibreOJ #6277 数列分块入门1
// 题目链接：https://loj.ac/p/6277
// 题目大意：
// 给出一个长为n的数列，以及n个操作，操作涉及区间加法，单点查值
// 操作0：区间加法 [l, r] + c
// 操作1：单点查值 查询位置x的值
// 测试链接：https://vjudge.net/problem/LibreOJ-6277

// 解题思路：
// 使用分块算法解决此问题
// 1. 将数组分成sqrt(n)大小的块
// 2. 对于区间加法操作，不完整块直接更新原数组，完整块使用懒惰标记
// 3. 对于单点查询操作，返回原值加上所属块的懒惰标记

// 时间复杂度分析：
// 1. 预处理：O(n)，构建分块结构
// 2. 区间加法操作：O(√n)，处理不完整块 + 更新完整块的懒惰标记
// 3. 单点查询操作：O(1)，直接返回结果
// 空间复杂度：O(n)，存储原数组、块信息和懒惰标记数组

// 工程化考量：
// 1. 异常处理：检查输入边界，防止数组越界
// 2. 性能优化：使用懒惰标记减少不必要的更新
// 3. 可读性：清晰的变量命名和注释
// 4. 测试用例：包含边界测试和性能测试

import java.io.*;
import java.util.*;

public class Code20_LibreOJ6277_Java {
    
    // 最大数组大小，根据题目约束设置
    public static final int MAXN = 50001;
    
    // 原数组，存储初始值
    public static int[] arr = new int[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块编号
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的懒惰标记（区间加法标记）
    public static int[] lazy = new int[MAXN];
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param n 数组长度
     */
    public static void build(int n) {
        // 计算块大小，通常取sqrt(n)
        blockSize = (int) Math.sqrt(n);
        // 计算块数量，向上取整
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 确定每个元素属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化懒惰标记为0
        Arrays.fill(lazy, 1, blockNum + 1, 0);
    }
    
    /**
     * 区间加法操作
     * 时间复杂度：O(√n)
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 要加的值
     * @param n 数组长度
     */
    public static void add(int l, int r, int c, int n) {
        // 检查输入边界
        if (l < 1 || r > n || l > r) {
            throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
        }
        
        int bl = belong[l]; // 左端点所在块
        int br = belong[r]; // 右端点所在块
        
        if (bl == br) {
            // 情况1：区间在同一个块内，直接遍历更新
            for (int i = l; i <= r; i++) {
                arr[i] += c;
            }
        } else {
            // 情况2：区间跨越多个块
            
            // 处理左边不完整的块
            for (int i = l; i <= blockRight[bl]; i++) {
                arr[i] += c;
            }
            
            // 处理中间完整的块（使用懒惰标记）
            for (int i = bl + 1; i < br; i++) {
                lazy[i] += c;
            }
            
            // 处理右边不完整的块
            for (int i = blockLeft[br]; i <= r; i++) {
                arr[i] += c;
            }
        }
    }
    
    /**
     * 单点查询操作
     * 时间复杂度：O(1)
     * 
     * @param x 查询位置
     * @param n 数组长度
     * @return 位置x的值
     */
    public static int query(int x, int n) {
        // 检查输入边界
        if (x < 1 || x > n) {
            throw new IllegalArgumentException("Invalid position: " + x);
        }
        
        // 返回原值加上所属块的懒惰标记
        return arr[x] + lazy[belong[x]];
    }
    
    /**
     * 主函数：处理输入输出和测试
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n
        int n = Integer.parseInt(br.readLine());
        
        // 读取初始数组
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 构建分块结构
        build(n);
        
        // 处理n个操作
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            
            if (op == 0) {
                // 区间加法操作
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                add(l, r, c, n);
            } else {
                // 单点查询操作
                int x = Integer.parseInt(st.nextToken());
                int result = query(x, n);
                pw.println(result);
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /**
     * 单元测试方法
     * 测试用例1：基础功能测试
     * 测试用例2：边界测试
     * 测试用例3：性能测试
     */
    public static void test() {
        System.out.println("=== 开始单元测试 ===");
        
        // 测试用例1：基础功能测试
        System.out.println("测试用例1：基础功能测试");
        int[] testArr = {0, 1, 2, 3, 4, 5}; // 索引0不使用
        System.arraycopy(testArr, 0, arr, 0, testArr.length);
        
        build(5); // 构建长度为5的分块结构
        
        // 测试区间加法
        add(2, 4, 10, 5);
        
        // 验证结果
        assert query(1, 5) == 1 : "位置1的值错误";
        assert query(2, 5) == 12 : "位置2的值错误";
        assert query(3, 5) == 13 : "位置3的值错误";
        assert query(4, 5) == 14 : "位置4的值错误";
        assert query(5, 5) == 5 : "位置5的值错误";
        
        System.out.println("测试用例1通过");
        
        // 测试用例2：边界测试
        System.out.println("测试用例2：边界测试");
        try {
            add(0, 3, 1, 5); // 非法左边界
            assert false : "应该抛出异常";
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过：" + e.getMessage());
        }
        
        // 测试用例3：跨块操作测试
        System.out.println("测试用例3：跨块操作测试");
        int[] testArr2 = new int[100];
        for (int i = 1; i <= 100; i++) {
            testArr2[i] = i;
        }
        System.arraycopy(testArr2, 0, arr, 0, testArr2.length);
        
        build(100);
        add(5, 95, 100, 100); // 跨多个块的操作
        
        // 验证跨块操作结果
        assert query(1, 100) == 1 : "边界值错误";
        assert query(5, 100) == 105 : "左边界值错误";
        assert query(95, 100) == 195 : "右边界值错误";
        assert query(100, 100) == 100 : "最右边界值错误";
        
        System.out.println("测试用例3通过");
        System.out.println("=== 所有测试通过 ===");
    }
    
    /**
     * 性能测试方法
     * 测试大规模数据下的性能表现
     */
    public static void performanceTest() {
        System.out.println("=== 开始性能测试 ===");
        
        int n = 50000; // 5万数据量
        int[] largeArr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            largeArr[i] = i;
        }
        System.arraycopy(largeArr, 0, arr, 0, largeArr.length);
        
        long startTime = System.currentTimeMillis();
        
        build(n);
        
        // 执行大量操作
        int operations = 50000;
        for (int i = 0; i < operations; i++) {
            int l = (int) (Math.random() * n) + 1;
            int r = l + (int) (Math.random() * 100);
            if (r > n) r = n;
            add(l, r, 1, n);
            
            if (i % 10000 == 0) {
                int x = (int) (Math.random() * n) + 1;
                query(x, n);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试完成，耗时：" + (endTime - startTime) + "ms");
        System.out.println("操作数量：" + operations + "，数据规模：" + n);
    }
}

// 复杂度分析总结：
// 时间复杂度：
// - 预处理：O(n)
// - 区间加法：O(√n) 最坏情况下需要遍历两个不完整块和中间完整块的懒惰标记
// - 单点查询：O(1) 直接返回结果
//
// 空间复杂度：O(n) 用于存储原数组、块信息和懒惰标记
//
// 算法优势：
// 1. 实现简单，代码易于理解和维护
// 2. 对于区间更新+单点查询的场景效率较高
// 3. 懒惰标记机制减少了不必要的更新操作
//
// 算法局限性：
// 1. 对于需要频繁区间查询的场景，效率不如线段树
// 2. 块大小选择影响性能，需要根据具体场景调整
//
// 适用场景：
// 1. 区间更新操作较多，查询操作较少的场景
// 2. 数据规模中等，不需要极致性能的场景
// 3. 需要快速实现和调试的场景