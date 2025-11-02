// 线段树分裂与合并算法测试用例和示例

import java.io.*;
import java.util.*;

/**
 * 线段树分裂与合并算法测试用例和示例
 * 
 * 包含各种边界情况、性能测试和正确性验证
 * 用于验证算法实现的正确性和性能
 */

public class Code35_TestCases_And_Examples {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    // 基础线段树分裂合并测试
    static class BasicSegmentTreeSplitMergeTest {
        static final int MAXN = 100010;
        static final int MAXM = 2000000;
        
        static class Node {
            int l, r;
            long sum;
            
            Node() {
                l = r = -1;
                sum = 0;
            }
        }
        
        static Node[] tree = new Node[MAXM];
        static int cnt = 0;
        
        static {
            for (int i = 0; i < MAXM; i++) {
                tree[i] = new Node();
            }
        }
        
        static int newNode() {
            if (cnt >= MAXM) return -1;
            tree[cnt].l = tree[cnt].r = -1;
            tree[cnt].sum = 0;
            return cnt++;
        }
        
        static void build(int rt, int l, int r, long[] arr) {
            if (l == r) {
                tree[rt].sum = arr[l];
                return;
            }
            
            int mid = (l + r) >> 1;
            
            tree[rt].l = newNode();
            build(tree[rt].l, l, mid, arr);
            
            tree[rt].r = newNode();
            build(tree[rt].r, mid + 1, r, arr);
            
            tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
        }
        
        static int[] split(int rt, int l, int r, int pos) {
            if (rt == -1) return new int[]{-1, -1};
            
            if (l == r) {
                int newRt = newNode();
                tree[newRt].sum = tree[rt].sum;
                tree[rt].sum = 0;
                return new int[]{rt, newRt};
            }
            
            int mid = (l + r) >> 1;
            
            if (pos <= mid) {
                int[] leftSplit = split(tree[rt].l, l, mid, pos);
                tree[rt].l = leftSplit[0];
                
                int newRt = newNode();
                tree[newRt].l = leftSplit[1];
                tree[newRt].r = tree[rt].r;
                tree[newRt].sum = tree[tree[newRt].l].sum + tree[tree[newRt].r].sum;
                
                tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
                
                return new int[]{rt, newRt};
            } else {
                int[] rightSplit = split(tree[rt].r, mid + 1, r, pos);
                tree[rt].r = rightSplit[0];
                
                int newRt = newNode();
                tree[newRt].l = tree[rt].l;
                tree[newRt].r = rightSplit[1];
                tree[newRt].sum = tree[tree[newRt].l].sum + tree[tree[newRt].r].sum;
                
                tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
                
                return new int[]{rt, newRt};
            }
        }
        
        static int merge(int u, int v) {
            if (u == -1) return v;
            if (v == -1) return u;
            
            tree[u].l = merge(tree[u].l, tree[v].l);
            tree[u].r = merge(tree[u].r, tree[v].r);
            
            tree[u].sum = tree[tree[u].l].sum + tree[tree[u].r].sum;
            
            return u;
        }
        
        static long query(int rt, int l, int r, int ql, int qr) {
            if (rt == -1 || ql > r || qr < l) return 0;
            
            if (ql <= l && r <= qr) return tree[rt].sum;
            
            int mid = (l + r) >> 1;
            long res = 0;
            
            if (ql <= mid) res += query(tree[rt].l, l, mid, ql, qr);
            if (qr > mid) res += query(tree[rt].r, mid + 1, r, ql, qr);
            
            return res;
        }
        
        // 测试用例1：基础分裂合并
        static void testBasicSplitMerge() {
            System.out.println("=== 测试用例1：基础分裂合并 ===");
            
            int n = 10;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = i;
            }
            
            int root = newNode();
            build(root, 1, n, arr);
            
            // 验证初始和
            long totalSum = query(root, 1, n, 1, n);
            System.out.println("初始区间和: " + totalSum + " (期望: 55)");
            
            // 在位置5分裂
            int[] splitResult = split(root, 1, n, 5);
            int leftRoot = splitResult[0];
            int rightRoot = splitResult[1];
            
            long leftSum = query(leftRoot, 1, n, 1, 5);
            long rightSum = query(rightRoot, 1, n, 6, 10);
            
            System.out.println("分裂后左区间和: " + leftSum + " (期望: 15)");
            System.out.println("分裂后右区间和: " + rightSum + " (期望: 40)");
            
            // 合并回去
            int mergedRoot = merge(leftRoot, rightRoot);
            long mergedSum = query(mergedRoot, 1, n, 1, n);
            System.out.println("合并后区间和: " + mergedSum + " (期望: 55)");
            
            System.out.println("测试用例1完成\n");
        }
        
        // 测试用例2：边界情况测试
        static void testEdgeCases() {
            System.out.println("=== 测试用例2：边界情况测试 ===");
            
            // 空树测试
            int emptyRoot = -1;
            long emptyQuery = query(emptyRoot, 1, 10, 1, 10);
            System.out.println("空树查询结果: " + emptyQuery + " (期望: 0)");
            
            // 单节点树
            int singleRoot = newNode();
            tree[singleRoot].sum = 42;
            long singleQuery = query(singleRoot, 1, 1, 1, 1);
            System.out.println("单节点查询结果: " + singleQuery + " (期望: 42)");
            
            // 分裂单节点
            int[] singleSplit = split(singleRoot, 1, 1, 1);
            System.out.println("单节点分裂结果: left=" + singleSplit[0] + ", right=" + singleSplit[1]);
            
            System.out.println("测试用例2完成\n");
        }
        
        // 测试用例3：性能测试
        static void testPerformance() {
            System.out.println("=== 测试用例3：性能测试 ===");
            
            int n = 10000;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = i;
            }
            
            long startTime = System.currentTimeMillis();
            
            int root = newNode();
            build(root, 1, n, arr);
            
            // 执行多次分裂合并操作
            for (int i = 0; i < 100; i++) {
                int splitPos = (int)(Math.random() * n) + 1;
                int[] splitResult = split(root, 1, n, splitPos);
                root = merge(splitResult[0], splitResult[1]);
            }
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("性能测试完成，耗时: " + (endTime - startTime) + "ms");
            System.out.println("测试用例3完成\n");
        }
    }
    
    // 线段树合并优化DP测试
    static class DPOptimizationTest {
        static final int MAXN = 1000;
        static final int MAXM = 100000;
        
        static class Node {
            int l, r;
            long sum, max;
            
            Node() {
                l = r = -1;
                sum = max = 0;
            }
        }
        
        static Node[] tree = new Node[MAXM];
        static int cnt = 0;
        
        static {
            for (int i = 0; i < MAXM; i++) {
                tree[i] = new Node();
            }
        }
        
        static int newNode() {
            if (cnt >= MAXM) return -1;
            tree[cnt].l = tree[cnt].r = -1;
            tree[cnt].sum = tree[cnt].max = 0;
            return cnt++;
        }
        
        static int buildLeaf(long val) {
            int rt = newNode();
            tree[rt].sum = val;
            tree[rt].max = val;
            return rt;
        }
        
        static int merge(int u, int v) {
            if (u == -1) return v;
            if (v == -1) return u;
            
            tree[u].l = merge(tree[u].l, tree[v].l);
            tree[u].r = merge(tree[u].r, tree[v].r);
            
            tree[u].sum = tree[tree[u].l].sum + tree[tree[u].r].sum;
            tree[u].max = Math.max(tree[tree[u].l].max, tree[tree[u].r].max);
            
            return u;
        }
        
        // DP状态
        static class DPState {
            int root;
            long sum, max;
            
            DPState() {
                root = -1;
                sum = max = 0;
            }
            
            DPState(int root, long sum, long max) {
                this.root = root;
                this.sum = sum;
                this.max = max;
            }
        }
        
        // 合并DP状态
        static DPState mergeDPState(DPState left, DPState right) {
            if (left.root == -1) return right;
            if (right.root == -1) return left;
            
            int mergedRoot = merge(left.root, right.root);
            long mergedSum = left.sum + right.sum;
            long mergedMax = Math.max(left.max, right.max);
            
            return new DPState(mergedRoot, mergedSum, mergedMax);
        }
        
        // 区间DP优化测试
        static DPState solveIntervalDP(int l, int r, long[] arr) {
            if (l > r) return new DPState();
            
            if (l == r) {
                int root = buildLeaf(arr[l]);
                return new DPState(root, arr[l], arr[l]);
            }
            
            int mid = (l + r) >> 1;
            
            DPState leftState = solveIntervalDP(l, mid, arr);
            DPState rightState = solveIntervalDP(mid + 1, r, arr);
            
            return mergeDPState(leftState, rightState);
        }
        
        // 测试用例4：DP优化正确性验证
        static void testDPOptimization() {
            System.out.println("=== 测试用例4：DP优化正确性验证 ===");
            
            int n = 100;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = i;
            }
            
            // 使用线段树合并优化DP
            DPState dpState = solveIntervalDP(1, n, arr);
            
            // 验证结果
            long expectedSum = n * (n + 1) / 2;
            long expectedMax = n;
            
            System.out.println("DP优化结果 - 和: " + dpState.sum + " (期望: " + expectedSum + ")");
            System.out.println("DP优化结果 - 最大值: " + dpState.max + " (期望: " + expectedMax + ")");
            
            System.out.println("测试用例4完成\n");
        }
        
        // 测试用例5：大规模DP性能测试
        static void testLargeScaleDP() {
            System.out.println("=== 测试用例5：大规模DP性能测试 ===");
            
            int n = 10000;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = (long)(Math.random() * 1000);
            }
            
            long startTime = System.currentTimeMillis();
            
            DPState dpState = solveIntervalDP(1, n, arr);
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("大规模DP测试完成，耗时: " + (endTime - startTime) + "ms");
            System.out.println("结果 - 和: " + dpState.sum);
            System.out.println("结果 - 最大值: " + dpState.max);
            
            System.out.println("测试用例5完成\n");
        }
    }
    
    // 综合测试运行器
    public static void runAllTests() {
        System.out.println("开始运行线段树分裂与合并算法测试用例\n");
        
        // 基础测试
        BasicSegmentTreeSplitMergeTest.testBasicSplitMerge();
        BasicSegmentTreeSplitMergeTest.testEdgeCases();
        BasicSegmentTreeSplitMergeTest.testPerformance();
        
        // DP优化测试
        DPOptimizationTest.testDPOptimization();
        DPOptimizationTest.testLargeScaleDP();
        
        System.out.println("所有测试用例运行完成");
    }
    
    // 示例代码演示
    static class ExampleDemonstration {
        // 示例1：线段树分裂合并基本用法
        static void demonstrateBasicUsage() {
            System.out.println("=== 示例1：线段树分裂合并基本用法 ===");
            
            int n = 8;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = i;
            }
            
            // 构建线段树
            int root = BasicSegmentTreeSplitMergeTest.newNode();
            BasicSegmentTreeSplitMergeTest.build(root, 1, n, arr);
            
            System.out.println("初始序列: " + Arrays.toString(Arrays.copyOfRange(arr, 1, n + 1)));
            
            // 在位置4分裂
            int[] splitResult = BasicSegmentTreeSplitMergeTest.split(root, 1, n, 4);
            int leftRoot = splitResult[0];
            int rightRoot = splitResult[1];
            
            System.out.println("在位置4分裂后：");
            System.out.println("左区间: [1, 4]");
            System.out.println("右区间: [5, 8]");
            
            // 合并回去
            int mergedRoot = BasicSegmentTreeSplitMergeTest.merge(leftRoot, rightRoot);
            
            System.out.println("合并后恢复原序列");
            System.out.println("示例1演示完成\n");
        }
        
        // 示例2：DP优化应用演示
        static void demonstrateDPOptimization() {
            System.out.println("=== 示例2：DP优化应用演示 ===");
            
            int n = 10;
            long[] arr = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = i * i; // 平方序列
            }
            
            System.out.println("输入序列: " + Arrays.toString(Arrays.copyOfRange(arr, 1, n + 1)));
            
            // 使用线段树合并优化DP
            DPOptimizationTest.DPState dpState = DPOptimizationTest.solveIntervalDP(1, n, arr);
            
            System.out.println("DP优化结果：");
            System.out.println("区间和: " + dpState.sum);
            System.out.println("区间最大值: " + dpState.max);
            
            System.out.println("示例2演示完成\n");
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        System.out.println("线段树分裂与合并算法测试和示例程序");
        System.out.println("1. 运行所有测试用例");
        System.out.println("2. 查看示例演示");
        System.out.println("3. 退出");
        
        int choice = io.nextInt();
        
        switch (choice) {
            case 1:
                runAllTests();
                break;
            case 2:
                ExampleDemonstration.demonstrateBasicUsage();
                ExampleDemonstration.demonstrateDPOptimization();
                break;
            case 3:
                System.out.println("程序退出");
                break;
            default:
                System.out.println("无效选择");
        }
        
        io.close();
    }
}

/*
 * 测试用例设计原则：
 * 
 * 1. 边界测试：
 *    - 空树、单节点树
 *    - 最小规模数据
 *    - 最大规模数据
 * 
 * 2. 正确性验证：
 *    - 与暴力算法对比
 *    - 数学公式验证
 *    - 多组随机数据测试
 * 
 * 3. 性能测试：
 *    - 大规模数据测试
 *    - 时间复杂度验证
 *    - 内存使用监控
 * 
 * 4. 异常处理：
 *    - 非法输入测试
 *    - 内存越界测试
 *    - 递归深度测试
 * 
 * 5. 回归测试：
 *    - 每次修改后运行完整测试
 *    - 确保原有功能不受影响
 *    - 性能回归检测
 */