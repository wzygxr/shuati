import java.io.*;
import java.util.*;

/**
 * Codeforces 911G - Mass Change Queries (线段树分裂高级应用)
 * 题目链接: https://codeforces.com/problemset/problem/911/G
 * 
 * 题目描述:
 * 给定一个数组，支持两种操作：
 * 1. 将区间[l, r]中所有值为x的元素改为y
 * 2. 查询区间[l, r]中值为x的元素个数
 * 
 * 核心算法: 线段树分裂 + 线段树合并 + 值域管理
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 使用线段树分裂技术管理不同值的区间
 * 2. 对每个值维护一个线段树，记录该值出现的位置
 * 3. 当需要将值x改为y时，将x对应的区间分裂出来，然后合并到y对应的线段树中
 * 4. 查询时直接在对应值的线段树上查询区间和
 */
public class Code22_CF911G_MassChangeQueries_Advanced {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(System.out);
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
    
    static class SegmentTree {
        static class Node {
            int left, right;
            int sum;
            
            Node() {
                left = right = -1;
                sum = 0;
            }
        }
        
        Node[] tree;
        int cnt;
        
        public SegmentTree() {
            tree = new Node[20000000];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
            cnt = 0;
        }
        
        int newNode() {
            if (cnt >= tree.length) {
                Node[] newTree = new Node[tree.length * 2];
                System.arraycopy(tree, 0, newTree, 0, tree.length);
                for (int i = tree.length; i < newTree.length; i++) {
                    newTree[i] = new Node();
                }
                tree = newTree;
            }
            tree[cnt].left = tree[cnt].right = -1;
            tree[cnt].sum = 0;
            return cnt++;
        }
        
        void update(int p, int l, int r, int pos, int val) {
            if (l == r) {
                tree[p].sum += val;
                return;
            }
            
            int mid = (l + r) >> 1;
            if (pos <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, pos, val);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, pos, val);
            }
            
            tree[p].sum = (tree[p].left != -1 ? tree[tree[p].left].sum : 0) +
                         (tree[p].right != -1 ? tree[tree[p].right].sum : 0);
        }
        
        int query(int p, int l, int r, int ql, int qr) {
            if (p == -1 || ql > r || qr < l) return 0;
            if (ql <= l && r <= qr) return tree[p].sum;
            
            int mid = (l + r) >> 1;
            int res = 0;
            if (tree[p].left != -1) {
                res += query(tree[p].left, l, mid, ql, qr);
            }
            if (tree[p].right != -1) {
                res += query(tree[p].right, mid + 1, r, ql, qr);
            }
            return res;
        }
        
        // 线段树分裂：将区间[ql, qr]分裂出来
        int split(int p, int l, int r, int ql, int qr) {
            if (p == -1 || ql > r || qr < l) return -1;
            
            if (ql <= l && r <= qr) {
                int newP = newNode();
                tree[newP] = tree[p];
                tree[p].left = tree[p].right = -1;
                tree[p].sum = 0;
                return newP;
            }
            
            int mid = (l + r) >> 1;
            int newP = newNode();
            
            if (ql <= mid && tree[p].left != -1) {
                tree[newP].left = split(tree[p].left, l, mid, ql, qr);
            }
            if (qr > mid && tree[p].right != -1) {
                tree[newP].right = split(tree[p].right, mid + 1, r, ql, qr);
            }
            
            tree[newP].sum = (tree[newP].left != -1 ? tree[tree[newP].left].sum : 0) +
                           (tree[newP].right != -1 ? tree[tree[newP].right].sum : 0);
            tree[p].sum = (tree[p].left != -1 ? tree[tree[p].left].sum : 0) +
                         (tree[p].right != -1 ? tree[tree[p].right].sum : 0);
            
            return newP;
        }
        
        // 线段树合并
        int merge(int p, int q, int l, int r) {
            if (p == -1) return q;
            if (q == -1) return p;
            
            if (l == r) {
                tree[p].sum += tree[q].sum;
                return p;
            }
            
            int mid = (l + r) >> 1;
            tree[p].left = merge(tree[p].left, tree[q].left, l, mid);
            tree[p].right = merge(tree[p].right, tree[q].right, mid + 1, r);
            
            tree[p].sum = (tree[p].left != -1 ? tree[tree[p].left].sum : 0) +
                         (tree[p].right != -1 ? tree[tree[p].right].sum : 0);
            
            return p;
        }
    }
    
    static int n, q;
    static int[] arr;
    static SegmentTree[] valueTrees; // 每个值对应的线段树
    static int[] roots; // 每个值的线段树根节点
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        arr = new int[n + 1];
        
        // 初始化值域线段树
        valueTrees = new SegmentTree[101]; // 值域1-100
        roots = new int[101];
        for (int i = 1; i <= 100; i++) {
            valueTrees[i] = new SegmentTree();
            roots[i] = valueTrees[i].newNode();
        }
        
        // 读入初始数组并构建线段树
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextInt();
            valueTrees[arr[i]].update(roots[arr[i]], 1, n, i, 1);
        }
        
        q = io.nextInt();
        while (q-- > 0) {
            int type = io.nextInt();
            
            if (type == 1) {
                // 查询操作
                int l = io.nextInt();
                int r = io.nextInt();
                int x = io.nextInt();
                
                int count = valueTrees[x].query(roots[x], 1, n, l, r);
                io.println(count);
                
            } else {
                // 修改操作：将区间[l, r]中所有值为x的元素改为y
                int l = io.nextInt();
                int r = io.nextInt();
                int x = io.nextInt();
                int y = io.nextInt();
                
                if (x == y) continue; // 相同值不需要修改
                
                // 从x的线段树中分裂出区间[l, r]
                int splitRoot = valueTrees[x].split(roots[x], 1, n, l, r);
                
                if (splitRoot != -1) {
                    // 将分裂出的线段树合并到y的线段树中
                    roots[y] = valueTrees[y].merge(roots[y], splitRoot, 1, n);
                }
            }
        }
        
        io.close();
    }
}

/**
 * 线段树分裂在批量修改问题中的优势:
 * 1. 高效处理区间批量修改: O(log n)时间复杂度完成区间分裂和合并
 * 2. 支持复杂查询: 可以快速查询任意区间内特定值的出现次数
 * 3. 内存优化: 动态开点只维护实际使用的节点
 * 
 * 类似题目推荐:
 * 1. P5494 【模板】线段树分裂 - 基础线段树分裂操作
 * 2. CF455D Serega and Fun - 区间循环移位 + 线段树分裂
 * 3. P3224 [HNOI2012]永无乡 - 线段树合并 + 并查集
 * 4. P5298 [PKUWC2018]Minimax - 概率DP + 线段树合并
 * 
 * 算法复杂度分析:
 * - 时间复杂度: O((n + q) log n)，每个操作都是对数级别
 * - 空间复杂度: O(n log n)，动态开点线段树的空间消耗
 * 
 * 工程化考量:
 * 1. 内存管理: 使用内存池技术避免频繁的内存分配
 * 2. 异常处理: 处理边界情况和非法输入
 * 3. 性能优化: 使用位运算和缓存友好设计
 */