import java.io.*;
import java.util.*;

/**
 * P5298 [PKUWC2018] Minimax - 线段树分裂算法实现
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P5298
 * 
 * 题目描述:
 * 给定一棵二叉树，每个叶子节点有一个权值，非叶子节点有一个概率p。
 * 对于每个非叶子节点，其权值有p的概率取左子树的最大值，有1-p的概率取右子树的最大值。
 * 求根节点取每个可能权值的概率。
 * 
 * 核心算法: 线段树合并 + 概率DP
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 对每个节点维护一个权值线段树，记录每个权值出现的概率
 * 2. 使用动态开点线段树，支持线段树合并
 * 3. 在合并过程中维护概率转移
 * 4. 最后遍历根节点的线段树得到答案
 */
public class Code18_P5298_Minimax {
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
        double nextDouble() { return Double.parseDouble(next()); }
        
        void println(Object obj) { out.println(obj); }
        void print(Object obj) { out.print(obj); }
        void close() { out.close(); }
    }
    
    static class Node {
        int l, r; // 左右儿子节点编号
        double p; // 概率值
        Node() {}
        Node(int l, int r, double p) {
            this.l = l;
            this.r = r;
            this.p = p;
        }
    }
    
    static class SegmentTreeNode {
        int l, r; // 左右儿子在节点数组中的下标
        double sum; // 区间概率和
        double mul; // 懒标记，用于概率转移
        
        SegmentTreeNode() {
            l = r = -1;
            sum = 0;
            mul = 1;
        }
    }
    
    static final int MAXN = 300010;
    static final int MOD = 998244353;
    static final int INV10000 = 796898467; // 10000的逆元
    
    static Node[] tree = new Node[MAXN];
    static SegmentTreeNode[] seg = new SegmentTreeNode[MAXN * 40];
    static int[] root = new int[MAXN];
    static int segCnt = 0;
    static int n;
    static List<Integer> vals = new ArrayList<>();
    static Map<Integer, Integer> mapping = new HashMap<>();
    
    // 动态开点线段树 - 新建节点
    static int newNode() {
        if (segCnt >= seg.length) {
            seg = Arrays.copyOf(seg, seg.length * 2);
        }
        if (seg[segCnt] == null) {
            seg[segCnt] = new SegmentTreeNode();
        } else {
            seg[segCnt].l = seg[segCnt].r = -1;
            seg[segCnt].sum = 0;
            seg[segCnt].mul = 1;
        }
        return segCnt++;
    }
    
    // 下传懒标记
    static void pushDown(int p) {
        if (seg[p].mul != 1) {
            if (seg[p].l != -1) {
                seg[seg[p].l].sum = (int)((long)seg[seg[p].l].sum * seg[p].mul % MOD);
                seg[seg[p].l].mul = (int)((long)seg[seg[p].l].mul * seg[p].mul % MOD);
            }
            if (seg[p].r != -1) {
                seg[seg[p].r].sum = (int)((long)seg[seg[p].r].sum * seg[p].mul % MOD);
                seg[seg[p].r].mul = (int)((long)seg[seg[p].r].mul * seg[p].mul % MOD);
            }
            seg[p].mul = 1;
        }
    }
    
    // 单点更新
    static void update(int p, int l, int r, int pos, double val) {
        if (l == r) {
            seg[p].sum = (seg[p].sum + val) % MOD;
            return;
        }
        pushDown(p);
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (seg[p].l == -1) seg[p].l = newNode();
            update(seg[p].l, l, mid, pos, val);
        } else {
            if (seg[p].r == -1) seg[p].r = newNode();
            update(seg[p].r, mid + 1, r, pos, val);
        }
        seg[p].sum = 0;
        if (seg[p].l != -1) seg[p].sum = (seg[p].sum + seg[seg[p].l].sum) % MOD;
        if (seg[p].r != -1) seg[p].sum = (seg[p].sum + seg[seg[p].r].sum) % MOD;
    }
    
    // 线段树合并
    static int merge(int x, int y, double p, double sumX, double sumY) {
        if (x == -1 && y == -1) return -1;
        if (x == -1) {
            // 只有y树存在，整个y树乘以p*sumX + (1-p)*sumY
            double mul = (p * sumX + (1 - p) * sumY) % MOD;
            seg[y].sum = (int)((long)seg[y].sum * mul % MOD);
            seg[y].mul = (int)((long)seg[y].mul * mul % MOD);
            return y;
        }
        if (y == -1) {
            // 只有x树存在，整个x树乘以p*sumX + (1-p)*sumY
            double mul = (p * sumX + (1 - p) * sumY) % MOD;
            seg[x].sum = (int)((long)seg[x].sum * mul % MOD);
            seg[x].mul = (int)((long)seg[x].mul * mul % MOD);
            return x;
        }
        
        pushDown(x);
        pushDown(y);
        
        // 递归合并左右子树
        double leftSumX = seg[seg[x].l] != null ? seg[seg[x].l].sum : 0;
        double leftSumY = seg[seg[y].l] != null ? seg[seg[y].l].sum : 0;
        double rightSumX = seg[seg[x].r] != null ? seg[seg[x].r].sum : 0;
        double rightSumY = seg[seg[y].r] != null ? seg[seg[y].r].sum : 0;
        
        seg[x].l = merge(seg[x].l, seg[y].l, p, sumX + rightSumX, sumY + rightSumY);
        seg[x].r = merge(seg[x].r, seg[y].r, p, sumX + leftSumX, sumY + leftSumY);
        
        // 更新当前节点
        seg[x].sum = 0;
        if (seg[x].l != -1) seg[x].sum = (seg[x].sum + seg[seg[x].l].sum) % MOD;
        if (seg[x].r != -1) seg[x].sum = (seg[x].sum + seg[seg[x].r].sum) % MOD;
        
        return x;
    }
    
    // DFS遍历树结构
    static void dfs(int u) {
        if (tree[u].l == 0 && tree[u].r == 0) {
            // 叶子节点，初始化线段树
            root[u] = newNode();
            int pos = mapping.get((int)tree[u].p);
            update(root[u], 1, vals.size(), pos, 1);
            return;
        }
        
        // 递归处理左右子树
        dfs(tree[u].l);
        dfs(tree[u].r);
        
        // 合并左右子树的线段树
        root[u] = merge(root[tree[u].l], root[tree[u].r], tree[u].p, 0, 0);
    }
    
    // 收集答案
    static void collectAnswer(int p, int l, int r, List<Double> ans) {
        if (p == -1) return;
        if (l == r) {
            ans.add(seg[p].sum);
            return;
        }
        pushDown(p);
        int mid = (l + r) >> 1;
        collectAnswer(seg[p].l, l, mid, ans);
        collectAnswer(seg[p].r, mid + 1, r, ans);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        for (int i = 1; i <= n; i++) {
            tree[i] = new Node();
        }
        
        // 读取树结构
        for (int i = 1; i <= n; i++) {
            int fa = io.nextInt();
            if (fa != 0) {
                if (tree[fa].l == 0) {
                    tree[fa].l = i;
                } else {
                    tree[fa].r = i;
                }
            }
        }
        
        // 读取概率和权值
        for (int i = 1; i <= n; i++) {
            int val = io.nextInt();
            if (tree[i].l == 0 && tree[i].r == 0) {
                // 叶子节点，存储权值
                tree[i].p = val;
                vals.add(val);
            } else {
                // 非叶子节点，存储概率
                tree[i].p = val * INV10000 / 10000.0;
            }
        }
        
        // 离散化权值
        Collections.sort(vals);
        int idx = 1;
        for (int val : vals) {
            if (!mapping.containsKey(val)) {
                mapping.put(val, idx++);
            }
        }
        
        // 初始化线段树节点数组
        for (int i = 0; i < seg.length; i++) {
            seg[i] = new SegmentTreeNode();
        }
        
        // 从根节点开始DFS
        dfs(1);
        
        // 收集答案
        List<Double> ans = new ArrayList<>();
        collectAnswer(root[1], 1, vals.size(), ans);
        
        // 输出答案
        for (double prob : ans) {
            io.println((int)prob);
        }
        
        io.close();
    }
}

/**
 * 类似题目推荐:
 * 1. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
 * 2. P3224 [HNOI2012]永无乡 - 平衡树合并/线段树合并
 * 3. P6773 [NOI2020]命运 - 树形DP + 线段树合并
 * 4. CF911G Mass Change Queries - 线段树合并 + 映射维护
 * 5. CF1401F Reverse and Swap - 线段树分裂经典应用
 * 
 * 线段树分裂算法总结:
 * 线段树分裂是线段树合并的逆操作，主要用于:
 * 1. 将一棵线段树按照某种条件拆分成多棵
 * 2. 支持区间分裂操作
 * 3. 与线段树合并配合实现复杂的数据结构
 * 
 * 时间复杂度: O(log n) 每次分裂
 * 空间复杂度: O(n log n)
 */