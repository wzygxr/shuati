package class158;

import java.io.*;
import java.util.*;

/**
 * SPOJ COT - Count on a tree
 * 
 * 题目来源：SPOJ
 * 题目链接：https://www.spoj.com/problems/COT/
 * 
 * 题目描述:
 * 给定一棵N个节点的树，每个点有一个权值，对于M个询问(u,v,k)，你需要回答u和v这两个节点间路径上的第K小的点权。
 * 
 * 解题思路:
 * 使用树上可持久化线段树（树上主席树）结合LCA解决该问题。
 * 1. 对节点权值进行离散化处理
 * 2. 通过DFS遍历树，为每个节点建立主席树
 * 3. 利用DFS序和LCA算法计算树上路径信息
 * 4. 对于查询u到v的路径，利用容斥原理计算路径上的第k小值
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 3
 * 1 2 3 4 5
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 4 5 2
 * 3 4 3
 * 1 2 1
 * 
 * 输出:
 * 3
 * 4
 * 1
 * 
 * 解释:
 * 查询4 5 2：节点4到节点5的路径为[4,2,5]，点权为[4,2,5]，第2小为3
 * 查询3 4 3：节点3到节点4的路径为[3,1,2,4]，点权为[3,1,2,4]，第3小为4
 * 查询1 2 1：节点1到节点2的路径为[1,2]，点权为[1,2]，第1小为1
 */
public class SPOJ_COT {
    static final int MAXN = 100010;
    static final int MAXH = 20;
    static final int MAXT = MAXN * MAXH;
    
    static int n, m, s;
    
    // 节点权值
    static int[] arr = new int[MAXN];
    
    // 离散化后的权值
    static int[] sorted = new int[MAXN];
    
    // 链式前向星
    static int[] head = new int[MAXN];
    static int[] to = new int[MAXN << 1];
    static int[] next = new int[MAXN << 1];
    static int cntg = 0;
    
    // 可持久化线段树
    static int[] root = new int[MAXN];
    static int[] left = new int[MAXT];
    static int[] right = new int[MAXT];
    static int[] size = new int[MAXT];
    static int cntt = 0;
    
    // LCA倍增
    static int[] deep = new int[MAXN];
    static int[][] stjump = new int[MAXN][MAXH];
    
    /**
     * 二分查找数字num在sorted数组中的位置
     * @param num 要查找的数字
     * @return 数字在sorted数组中的位置
     */
    static int getId(int num) {
        int left = 1, right = s, mid;
        while (left <= right) {
            mid = (left + right) / 2;
            if (sorted[mid] == num) {
                return mid;
            } else if (sorted[mid] < num) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++cntt;
        size[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 预处理，对节点权值进行离散化
     */
    static void prepare() {
        for (int i = 1; i <= n; i++) {
            sorted[i] = arr[i];
        }
        Arrays.sort(sorted, 1, n + 1);
        s = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[s] != sorted[i]) {
                sorted[++s] = sorted[i];
            }
        }
        root[0] = build(1, s);
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    static void addEdge(int u, int v) {
        next[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的位置
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int insert(int pos, int l, int r, int pre) {
        int rt = ++cntt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        size[rt] = size[pre] + 1;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt]);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }
    
    /**
     * 查询路径上第k小的点权
     * @param k 要查询的排名
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param u 节点u的根节点
     * @param v 节点v的根节点
     * @param lca 节点u和v的LCA的根节点
     * @param lcafa LCA父节点的根节点
     * @return 第k小的点权在离散化数组中的位置
     */
    static int query(int k, int l, int r, int u, int v, int lca, int lcafa) {
        if (l == r) {
            return l;
        }
        // 计算左子树中数的个数
        int lsize = size[left[u]] + size[left[v]] - size[left[lca]] - size[left[lcafa]];
        int mid = (l + r) / 2;
        if (lsize >= k) {
            return query(k, l, mid, left[u], left[v], left[lca], left[lcafa]);
        } else {
            return query(k - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa]);
        }
    }
    
    /**
     * DFS遍历树并构建主席树
     * @param u 当前节点
     * @param f 父节点
     */
    static void dfs(int u, int f) {
        root[u] = insert(getId(arr[u]), 1, s, root[f]);
        deep[u] = deep[f] + 1;
        stjump[u][0] = f;
        for (int p = 1; p < MAXH; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        for (int ei = head[u]; ei > 0; ei = next[ei]) {
            if (to[ei] != f) {
                dfs(to[ei], u);
            }
        }
    }
    
    /**
     * 计算节点a和节点b的最近公共祖先(LCA)
     * @param a 节点a
     * @param b 节点b
     * @return 节点a和节点b的LCA
     */
    static int lca(int a, int b) {
        if (deep[a] < deep[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        // 将a提升到与b同一深度
        for (int p = MAXH - 1; p >= 0; p--) {
            if (deep[stjump[a][p]] >= deep[b]) {
                a = stjump[a][p];
            }
        }
        if (a == b) {
            return a;
        }
        // 同时提升a和b直到它们的父节点相同
        for (int p = MAXH - 1; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        return stjump[a][0];
    }
    
    /**
     * 查询节点u到节点v路径上第k小的点权
     * @param u 起点
     * @param v 终点
     * @param k 要查询的排名
     * @return 第k小的点权
     */
    static int kth(int u, int v, int k) {
        int lcaNode = lca(u, v);
        int i = query(k, 1, s, root[u], root[v], root[lcaNode], root[stjump[lcaNode][0]]);
        return sorted[i];
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = reader.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        // 读取节点权值
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        prepare();
        
        // 读取边信息
        for (int i = 1, u, v; i < n; i++) {
            line = reader.readLine().split(" ");
            u = Integer.parseInt(line[0]);
            v = Integer.parseInt(line[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // DFS构建主席树
        dfs(1, 0);
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            writer.println(kth(u, v, k));
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}