import java.io.*;
import java.util.*;

/**
 * P6773 [NOI2020] 命运 - 线段树合并加速DP转移
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P6773
 * 
 * 题目描述:
 * 给定一棵树，每个节点有一个权值。需要选择一些节点，使得任意两个被选节点在树上的路径上至少有一个被选节点。
 * 求最小权值和。
 * 
 * 核心算法: 树形DP + 线段树合并优化
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 定义DP状态: dp[u][0/1] 表示以u为根的子树，u节点选或不选的最小权值
 * 2. 状态转移:
 *    - 如果u不选，则所有子节点必须选
 *    - 如果u选，则子节点可选可不选
 * 3. 使用线段树合并来优化DP转移过程
 * 4. 维护每个节点的DP值线段树，通过合并子树线段树来更新当前节点的DP值
 * 
 * 线段树合并加速DP转移的关键:
 * 1. 动态开点线段树维护DP值
 * 2. 合并子树线段树时进行DP状态转移
 * 3. 懒标记维护区间最小值
 * 4. 支持区间加、区间取最小值操作
 */
public class Code21_P6773_DP_Optimization {
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
            int left, right; // 左右子节点索引
            long minVal;     // 区间最小值
            long lazy;       // 懒标记
            
            Node() {
                this.left = -1;
                this.right = -1;
                this.minVal = Long.MAX_VALUE / 2;
                this.lazy = 0;
            }
        }
        
        Node[] tree;
        int root;
        int cnt;
        
        public SegmentTree() {
            tree = new Node[2000000]; // 预分配足够空间
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
            root = -1;
            cnt = 0;
        }
        
        // 创建新节点
        int newNode() {
            if (cnt >= tree.length) {
                // 动态扩容
                Node[] newTree = new Node[tree.length * 2];
                System.arraycopy(tree, 0, newTree, 0, tree.length);
                for (int i = tree.length; i < newTree.length; i++) {
                    newTree[i] = new Node();
                }
                tree = newTree;
            }
            tree[cnt].minVal = Long.MAX_VALUE / 2;
            tree[cnt].lazy = 0;
            tree[cnt].left = -1;
            tree[cnt].right = -1;
            return cnt++;
        }
        
        // 下推懒标记
        void pushDown(int p) {
            if (tree[p].lazy != 0) {
                if (tree[p].left == -1) tree[p].left = newNode();
                if (tree[p].right == -1) tree[p].right = newNode();
                
                tree[tree[p].left].minVal += tree[p].lazy;
                tree[tree[p].left].lazy += tree[p].lazy;
                
                tree[tree[p].right].minVal += tree[p].lazy;
                tree[tree[p].right].lazy += tree[p].lazy;
                
                tree[p].lazy = 0;
            }
        }
        
        // 更新区间最小值
        void update(int p, int l, int r, int pos, long val) {
            if (l == r) {
                tree[p].minVal = Math.min(tree[p].minVal, val);
                return;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            
            if (pos <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, pos, val);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, pos, val);
            }
            
            tree[p].minVal = Math.min(
                tree[p].left != -1 ? tree[tree[p].left].minVal : Long.MAX_VALUE / 2,
                tree[p].right != -1 ? tree[tree[p].right].minVal : Long.MAX_VALUE / 2
            );
        }
        
        // 区间加操作
        void add(int p, int l, int r, int ql, int qr, long val) {
            if (ql > qr || p == -1) return;
            if (ql <= l && r <= qr) {
                tree[p].minVal += val;
                tree[p].lazy += val;
                return;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            
            if (ql <= mid && tree[p].left != -1) {
                add(tree[p].left, l, mid, ql, qr, val);
            }
            if (qr > mid && tree[p].right != -1) {
                add(tree[p].right, mid + 1, r, ql, qr, val);
            }
            
            tree[p].minVal = Math.min(
                tree[p].left != -1 ? tree[tree[p].left].minVal : Long.MAX_VALUE / 2,
                tree[p].right != -1 ? tree[tree[p].right].minVal : Long.MAX_VALUE / 2
            );
        }
        
        // 查询区间最小值
        long query(int p, int l, int r, int ql, int qr) {
            if (ql > qr || p == -1) return Long.MAX_VALUE / 2;
            if (ql <= l && r <= qr) {
                return tree[p].minVal;
            }
            
            pushDown(p);
            int mid = (l + r) / 2;
            long res = Long.MAX_VALUE / 2;
            
            if (ql <= mid && tree[p].left != -1) {
                res = Math.min(res, query(tree[p].left, l, mid, ql, qr));
            }
            if (qr > mid && tree[p].right != -1) {
                res = Math.min(res, query(tree[p].right, mid + 1, r, ql, qr));
            }
            
            return res;
        }
        
        // 合并两棵线段树（关键操作）
        int merge(int p, int q, int l, int r, long addP, long addQ) {
            if (p == -1 && q == -1) return -1;
            if (p == -1) {
                add(q, l, r, l, r, addQ);
                return q;
            }
            if (q == -1) {
                add(p, l, r, l, r, addP);
                return p;
            }
            
            if (l == r) {
                // 叶子节点合并
                int newP = newNode();
                tree[newP].minVal = Math.min(tree[p].minVal + addP, tree[q].minVal + addQ);
                return newP;
            }
            
            pushDown(p);
            pushDown(q);
            
            int mid = (l + r) / 2;
            
            // 递归合并左右子树
            int leftMerge = merge(
                tree[p].left, tree[q].left, l, mid, 
                addP, addQ
            );
            int rightMerge = merge(
                tree[p].right, tree[q].right, mid + 1, r,
                addP, addQ
            );
            
            int newP = newNode();
            tree[newP].left = leftMerge;
            tree[newP].right = rightMerge;
            tree[newP].minVal = Math.min(
                leftMerge != -1 ? tree[leftMerge].minVal : Long.MAX_VALUE / 2,
                rightMerge != -1 ? tree[rightMerge].minVal : Long.MAX_VALUE / 2
            );
            
            return newP;
        }
    }
    
    static int n;
    static List<Integer>[] graph;
    static long[] weight;
    static SegmentTree[] dpTrees; // 每个节点的DP线段树
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        weight = new long[n + 1];
        graph = new ArrayList[n + 1];
        dpTrees = new SegmentTree[n + 1];
        
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
            weight[i] = io.nextLong();
        }
        
        for (int i = 1; i < n; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        dfs(1, 0);
        
        // 根节点的最小权值和
        long result = dpTrees[1].tree[dpTrees[1].root].minVal;
        io.println(result);
        io.close();
    }
    
    static void dfs(int u, int parent) {
        dpTrees[u] = new SegmentTree();
        dpTrees[u].root = dpTrees[u].newNode();
        
        // 初始化DP状态
        // dp[u][0]: u不选，所有子节点必须选
        // dp[u][1]: u选，子节点可选可不选
        
        // 初始时，u节点的DP线段树包含两个状态
        dpTrees[u].update(dpTrees[u].root, 0, n, 0, 0); // 初始状态
        
        for (int v : graph[u]) {
            if (v == parent) continue;
            
            dfs(v, u);
            
            // 合并子树v的DP线段树到当前节点u
            SegmentTree childTree = dpTrees[v];
            
            if (childTree.root != -1) {
                // 计算合并后的DP值
                long minChildSelect = childTree.query(childTree.root, 0, n, 0, n);
                long minChildNotSelect = childTree.query(childTree.root, 0, n, 1, n);
                
                // 更新u节点的DP线段树
                dpTrees[u].root = dpTrees[u].merge(
                    dpTrees[u].root, childTree.root, 0, n,
                    minChildSelect, minChildNotSelect + weight[u]
                );
            }
        }
        
        // 最终处理：考虑u节点自身的权值
        if (u != 1) { // 非根节点需要特殊处理
            dpTrees[u].add(dpTrees[u].root, 0, n, 0, n, weight[u]);
        }
    }
}

/**
 * 线段树合并加速DP转移的核心优势:
 * 1. 避免重复计算: 通过线段树维护DP状态，避免重复遍历子树
 * 2. 高效合并: O(log n)时间复杂度合并两棵线段树
 * 3. 支持复杂操作: 支持区间加、区间查询等复杂操作
 * 4. 动态开点: 节省空间，只维护必要的节点
 * 
 * 类似题目推荐:
 * 1. P5298 [PKUWC2018] Minimax - 概率DP + 线段树合并
 * 2. CF455D Serega and Fun - 区间操作 + DP优化
 * 3. CF868F Yet Another Minimization Problem - 分治DP + 线段树
 * 4. CF321E Ciel and Gondolas - 四边形不等式优化
 * 5. P4770 [NOI2018] 你的名字 - 后缀自动机 + 线段树合并
 * 
 * 算法复杂度分析:
 * - 时间复杂度: O(n log n)，每个节点最多被合并log n次
 * - 空间复杂度: O(n log n)，动态开点线段树的空间消耗
 * 
 * 适用场景:
 * 1. 树形DP问题，需要合并子树信息
 * 2. DP状态转移涉及区间操作
 * 3. 需要支持动态修改和查询
 * 4. 数据规模较大，需要高效算法
 */