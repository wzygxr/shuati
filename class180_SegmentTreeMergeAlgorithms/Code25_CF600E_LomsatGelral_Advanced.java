import java.io.*;
import java.util.*;

/**
 * Codeforces 600E - Lomsat gelral (线段树合并经典应用)
 * 题目链接: https://codeforces.com/problemset/problem/600/E
 * 
 * 题目描述:
 * 给定一棵树，每个节点有一个颜色。对于每个节点，求其子树中出现次数最多的颜色编号和。
 * 如果有多个颜色出现次数相同且最多，则将这些颜色的编号相加。
 * 
 * 核心算法: 线段树合并 + 启发式合并优化
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 对每个节点维护一个值域线段树，记录各种颜色的出现次数
 * 2. 使用线段树合并技术将子节点的信息合并到父节点
 * 3. 在线段树中维护最大值和对应的颜色编号和
 * 4. 使用启发式合并优化合并顺序
 */
public class Code25_CF600E_LomsatGelral_Advanced {
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
            int maxCnt;     // 最大出现次数
            long sumColors; // 对应颜色的编号和
            int cnt;        // 当前节点的颜色计数
            
            Node() {
                left = right = -1;
                maxCnt = 0;
                sumColors = 0;
                cnt = 0;
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
            tree[cnt].maxCnt = 0;
            tree[cnt].sumColors = 0;
            tree[cnt].cnt = 0;
            return cnt++;
        }
        
        void pushUp(int p) {
            if (p == -1) return;
            
            int leftMax = (tree[p].left != -1) ? tree[tree[p].left].maxCnt : 0;
            int rightMax = (tree[p].right != -1) ? tree[tree[p].right].maxCnt : 0;
            
            if (leftMax > rightMax) {
                tree[p].maxCnt = leftMax;
                tree[p].sumColors = (tree[p].left != -1) ? tree[tree[p].left].sumColors : 0;
            } else if (rightMax > leftMax) {
                tree[p].maxCnt = rightMax;
                tree[p].sumColors = (tree[p].right != -1) ? tree[tree[p].right].sumColors : 0;
            } else {
                tree[p].maxCnt = leftMax;
                long leftSum = (tree[p].left != -1) ? tree[tree[p].left].sumColors : 0;
                long rightSum = (tree[p].right != -1) ? tree[tree[p].right].sumColors : 0;
                tree[p].sumColors = leftSum + rightSum;
            }
        }
        
        void update(int p, int l, int r, int color, int delta) {
            if (l == r) {
                tree[p].cnt += delta;
                if (tree[p].cnt > tree[p].maxCnt) {
                    tree[p].maxCnt = tree[p].cnt;
                    tree[p].sumColors = color;
                } else if (tree[p].cnt == tree[p].maxCnt) {
                    tree[p].sumColors += color;
                }
                return;
            }
            
            int mid = (l + r) >> 1;
            if (color <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, color, delta);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, color, delta);
            }
            pushUp(p);
        }
        
        // 线段树合并（启发式合并优化）
        int merge(int p, int q, int l, int r) {
            if (p == -1) return q;
            if (q == -1) return p;
            
            if (l == r) {
                // 叶子节点合并
                tree[p].cnt += tree[q].cnt;
                if (tree[p].cnt > tree[p].maxCnt) {
                    tree[p].maxCnt = tree[p].cnt;
                    tree[p].sumColors = l;
                } else if (tree[p].cnt == tree[p].maxCnt) {
                    tree[p].sumColors += l;
                }
                return p;
            }
            
            int mid = (l + r) >> 1;
            
            // 启发式合并：总是将较小的树合并到较大的树中
            if (getSize(p) < getSize(q)) {
                // 交换p和q，确保p是较大的树
                Node temp = tree[p];
                tree[p] = tree[q];
                tree[q] = temp;
            }
            
            // 递归合并左右子树
            tree[p].left = merge(tree[p].left, tree[q].left, l, mid);
            tree[p].right = merge(tree[p].right, tree[q].right, mid + 1, r);
            
            pushUp(p);
            return p;
        }
        
        int getSize(int p) {
            if (p == -1) return 0;
            return 1 + getSize(tree[p].left) + getSize(tree[p].right);
        }
        
        long getAnswer(int p) {
            return p == -1 ? 0 : tree[p].sumColors;
        }
    }
    
    static int n;
    static int[] colors;
    static List<Integer>[] graph;
    static SegmentTree segTree;
    static int[] root;
    static long[] ans;
    
    static void dfs(int u, int parent) {
        // 初始化当前节点的线段树
        segTree.update(root[u], 1, n, colors[u], 1);
        
        for (int v : graph[u]) {
            if (v == parent) continue;
            
            dfs(v, u);
            
            // 合并子节点的线段树到当前节点
            root[u] = segTree.merge(root[u], root[v], 1, n);
        }
        
        // 记录答案
        ans[u] = segTree.getAnswer(root[u]);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        colors = new int[n + 1];
        graph = new ArrayList[n + 1];
        
        for (int i = 1; i <= n; i++) {
            colors[i] = io.nextInt();
            graph[i] = new ArrayList<>();
        }
        
        // 读入边
        for (int i = 1; i < n; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 初始化线段树
        segTree = new SegmentTree();
        root = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            root[i] = segTree.newNode();
        }
        
        ans = new long[n + 1];
        
        // 从根节点开始DFS
        dfs(1, 0);
        
        // 输出答案
        for (int i = 1; i <= n; i++) {
            io.print(ans[i] + " ");
        }
        io.println();
        
        io.close();
    }
}

/**
 * 线段树合并与启发式合并的结合优势:
 * 
 * 1. 启发式合并优化:
 *    - 总是将较小的树合并到较大的树中
 *    - 时间复杂度从O(n²)优化到O(n log n)
 *    - 减少线段树合并的操作次数
 * 
 * 2. 线段树合并的优势:
 *    - 高效维护子树信息: O(log n)时间复杂度
 *    - 支持复杂统计: 可以维护最大值、和等多种信息
 *    - 动态开点: 节省空间，只维护必要的节点
 * 
 * 3. 应用场景扩展:
 *    - 子树统计问题
 *    - 树上路径查询
 *    - 动态维护树结构信息
 * 
 * 类似题目推荐:
 * 1. CF570D Tree Requests - 子树深度统计 + 线段树合并
 * 2. CF208E Blood Cousins - 树上k级祖先查询 + 线段树合并
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集 + 线段树合并
 * 
 * 算法复杂度分析:
 * - 线段树合并: O(n log n)
 * - 启发式合并: 优化总体复杂度
 * - 空间复杂度: O(n log n)
 * 
 * 工程化优化:
 * 1. 内存池管理: 避免频繁的内存分配
 * 2. 缓存友好: 优化内存访问模式
 * 3. 异常处理: 处理边界情况和非法输入
 * 4. 性能监控: 监控算法执行时间和内存使用
 */