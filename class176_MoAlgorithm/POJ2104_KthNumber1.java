package class178;

// POJ 2104 K-th Number - Java版本
// 题目来源: POJ 2104 K-th Number
// 题目链接: http://poj.org/problem?id=2104
// 题目大意: 给定一个数组a[1...n]和一系列问题Q(i, j, k)，对于每个问题Q(i, j, k)
// 求在a[i...j]段中，如果这段被排序后，第k个数字是什么
// 数据范围: 1 <= n <= 100000, 1 <= m <= 5000
// 解题思路: 使用主席树（可持久化线段树）解决静态区间第k大问题
// 时间复杂度: O((n + m) * log n)
// 空间复杂度: O(n * log n)
// 相关题目:
// 1. POJ 2104 K-th Number: http://poj.org/problem?id=2104
// 2. SPOJ MKTHNUM - K-th Number: https://www.spoj.com/problems/MKTHNUM/
// 3. 洛谷 P3834 【模板】可持久化线段树 1（主席树）: https://www.luogu.com.cn/problem/P3834
// 4. HDU 2665 Kth number: http://acm.hdu.edu.cn/showproblem.php?pid=2665
// 5. ZOJ 2112 Dynamic Rankings: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=2112

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class POJ2104_KthNumber1 {
    
    public static int MAXN = 100005;
    public static int n, m;
    public static int[] arr = new int[MAXN];  // 原始数组
    public static int[] sortedArr = new int[MAXN];  // 排序后的数组
    
    // 主席树节点
    public static class Node {
        int l, r;  // 左右子节点索引
        int sum;   // 当前区间内的元素个数
        
        Node() {
            this.l = 0;
            this.r = 0;
            this.sum = 0;
        }
    }
    
    public static int[] root = new int[MAXN];  // 每个版本的根节点
    public static Node[] tree = new Node[MAXN * 20];  // 节点数组
    public static int cnt = 0;  // 节点计数器
    
    // 创建新节点
    public static int createNode() {
        tree[++cnt] = new Node();
        return cnt;
    }
    
    // 插入函数
    public static int insert(int pre, int l, int r, int val) {
        int cur = createNode();
        tree[cur].sum = tree[pre].sum + 1;
        
        if (l == r) {
            return cur;
        }
        
        int mid = (l + r) >> 1;
        if (val <= mid) {
            tree[cur].l = insert(tree[pre].l, l, mid, val);
            tree[cur].r = tree[pre].r;
        } else {
            tree[cur].l = tree[pre].l;
            tree[cur].r = insert(tree[pre].r, mid + 1, r, val);
        }
        
        return cur;
    }
    
    // 查询函数
    public static int query(int u, int v, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        
        int mid = (l + r) >> 1;
        int x = tree[tree[v].l].sum - tree[tree[u].l].sum;
        
        if (k <= x) {
            return query(tree[u].l, tree[v].l, l, mid, k);
        } else {
            return query(tree[u].r, tree[v].r, mid + 1, r, k - x);
        }
    }
    
    // 离散化函数
    public static int getId(int x) {
        return Arrays.binarySearch(sortedArr, 1, n + 1, x);
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        n = in.nextInt();
        m = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
            sortedArr[i] = arr[i];
        }
        
        // 离散化
        Arrays.sort(sortedArr, 1, n + 1);
        
        // 构建主席树
        root[0] = createNode();
        for (int i = 1; i <= n; i++) {
            root[i] = insert(root[i - 1], 1, n, getId(arr[i]));
        }
        
        // 处理查询
        for (int i = 1; i <= m; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            int k = in.nextInt();
            
            int resultId = query(root[l - 1], root[r], 1, n, k);
            out.println(sortedArr[resultId]);
        }
        
        out.flush();
        out.close();
    }
    
    // 快速读取工具类
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
    
    /*
     * 算法分析:
     * 
     * 时间复杂度: O((n + m) * log n)
     * 空间复杂度: O(n * log n)
     * 
     * 算法思路:
     * 1. 使用主席树（可持久化线段树）解决静态区间第k大问题
     * 2. 对数组进行离散化处理
     * 3. 构建n+1个版本的线段树，第i个版本表示前i个元素的信息
     * 4. 对于查询[l,r]的第k大，通过第r个版本和第l-1个版本的差值得到答案
     * 
     * 核心思想:
     * 1. 主席树是一种可持久化数据结构，可以保存历史版本
     * 2. 每个版本的线段树维护对应前缀中每个值的出现次数
     * 3. 通过两个版本的线段树相减，可以得到任意区间的统计信息
     * 4. 在查询时，根据左右子树的元素个数决定向哪边递归
     * 
     * 工程化考量:
     * 1. 使用快速输入输出优化IO性能
     * 2. 合理使用静态数组避免动态分配
     * 3. 使用离散化减少空间和时间复杂度
     * 
     * 调试技巧:
     * 1. 可以通过打印中间结果验证算法正确性
     * 2. 使用断言检查关键变量的正确性
     * 3. 对比暴力算法验证结果
     */
}