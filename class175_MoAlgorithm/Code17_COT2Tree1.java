package class177;

// SP10707 COT2 - Count on a tree II
// 给定一棵N个节点的树，每个节点有一个权值
// M次询问，每次询问两个节点u,v之间的路径上有多少种不同的权值
// 1 <= N <= 40000
// 1 <= M <= 100000
// 测试链接 : https://www.luogu.com.cn/problem/SP10707

// 解题思路：
// 这是树上莫队的经典模板题
// 树上莫队的关键是将树上路径问题转化为序列问题
// 使用欧拉序（DFS序）将树转化为序列
// 对于树上两点u,v之间的路径，其在欧拉序中的表示需要考虑LCA（最近公共祖先）
// 如果u是v的祖先，则路径对应欧拉序中u第一次出现位置到v第一次出现位置的区间
// 否则，路径对应u第二次出现位置到v第一次出现位置的区间（或相反），并需要单独处理LCA

// 时间复杂度分析：
// 1. 预处理（DFS、LCA）：O(N log N)
// 2. 排序：O(M log M)
// 3. 树上莫队算法处理：O((N + M) * sqrt(N))
// 4. 总时间复杂度：O(N log N + M log M + (N + M) * sqrt(N))
// 空间复杂度分析：
// 1. 存储树结构：O(N)
// 2. 存储欧拉序：O(N)
// 3. 存储查询：O(M)
// 4. LCA预处理：O(N log N)
// 5. 总空间复杂度：O(N log N + M)

// 是否最优解：
// 这是该问题的最优解之一，树上莫队算法在处理这类离线树上路径查询问题时具有很好的时间复杂度
// 对于在线查询问题，可以使用树链剖分套主席树等数据结构，但对于离线问题，树上莫队算法是首选

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Code17_COT2Tree1 {

    public static int MAXN = 40010;
    public static int MAXM = 100010;
    public static int LOGN = 17; // log2(40000) ≈ 16
    
    public static int n, m;
    public static int[] weights = new int[MAXN];
    @SuppressWarnings("unchecked")
    public static ArrayList<Integer>[] graph = new ArrayList[MAXN];
    
    // 欧拉序相关
    public static int[] euler = new int[2 * MAXN]; // 欧拉序，大小为2*N
    public static int[] first = new int[MAXN]; // 每个节点第一次出现在欧拉序中的位置
    public static int[] last = new int[MAXN];  // 每个节点第二次出现在欧拉序中的位置
    public static int eulerLen = 0;
    
    // LCA相关
    public static int[] depth = new int[MAXN];
    public static int[][] parents = new int[MAXN][LOGN];
    
    // 查询相关
    public static int[][] queries = new int[MAXM][4]; // u, v, lca, id
    public static int[] answers = new int[MAXM];
    
    // 分块相关
    public static int blockSize;
    public static int[] belong = new int[2 * MAXN];
    
    // 莫队相关
    public static int[] count = new int[MAXN];
    public static int distinctCount = 0;
    
    // 树上莫队排序规则
    public static class QueryComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            // 按照左端点所在块排序
            if (belong[a[0]] != belong[b[0]]) {
                return belong[a[0]] - belong[b[0]];
            }
            // 同一块内按照右端点排序
            return a[1] - b[1];
        }
    }
    
    // 添加元素到窗口
    public static void add(int pos) {
        int val = weights[euler[pos]];
        // 如果该权值之前没有出现过，现在出现了，种类数增加
        if (count[val] == 0) {
            distinctCount++;
        }
        count[val]++;
    }
    
    // 从窗口删除元素
    public static void remove(int pos) {
        int val = weights[euler[pos]];
        // 如果该权值之前只出现了一次，现在删除后就没有了，种类数减少
        if (count[val] == 1) {
            distinctCount--;
        }
        count[val]--;
    }
    
    // DFS生成欧拉序并预处理LCA
    public static void dfs(int u, int parent, int dep) {
        // 记录第一次访问
        first[u] = ++eulerLen;
        euler[eulerLen] = u;
        depth[u] = dep;
        parents[u][0] = parent;
        
        // 预处理2^j级祖先
        for (int j = 1; (1 << j) < n; j++) {
            if (parents[u][j-1] != -1) {
                parents[u][j] = parents[parents[u][j-1]][j-1];
            }
        }
        
        // 遍历子节点
        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u, dep + 1);
            }
        }
        
        // 记录最后一次访问
        last[u] = ++eulerLen;
        euler[eulerLen] = u;
    }
    
    // 计算两点的LCA
    public static int lca(int u, int v) {
        // 确保u在更深的位置
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到和v同一深度
        for (int i = LOGN - 1; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = parents[u][i];
            }
        }
        
        // 如果v就是u的祖先
        if (u == v) {
            return u;
        }
        
        // 同时向上提升u和v，直到它们的父节点相同
        for (int i = LOGN - 1; i >= 0; i--) {
            if (parents[u][i] != -1 && parents[u][i] != parents[v][i]) {
                u = parents[u][i];
                v = parents[v][i];
            }
        }
        
        return parents[u][0];
    }
    
    // 预处理函数
    public static void prepare() {
        // 初始化图
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 初始化parents数组
        for (int i = 1; i <= n; i++) {
            Arrays.fill(parents[i], -1);
        }
        
        // 生成欧拉序
        eulerLen = 0;
        dfs(1, -1, 0); // 假设节点1是根节点
        
        // 计算分块大小
        blockSize = (int) Math.sqrt(eulerLen);
        
        // 计算每个位置所属的块
        for (int i = 1; i <= eulerLen; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 处理查询，转换为欧拉序上的区间
        for (int i = 1; i <= m; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            int lcaNode = lca(u, v);
            
            // 保存LCA
            queries[i][2] = lcaNode;
            
            // 转换为欧拉序上的区间
            // 确保first[u] <= first[v]
            if (first[u] > first[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 如果u是v的祖先
            if (lcaNode == u) {
                queries[i][0] = first[u];
                queries[i][1] = first[v];
            } else {
                // 否则区间是first[u]到last[v]或first[v]到last[u]
                // 选择较小的作为左端点
                if (first[u] < first[v]) {
                    queries[i][0] = last[u];
                    queries[i][1] = first[v];
                } else {
                    queries[i][0] = first[v];
                    queries[i][1] = last[u];
                }
            }
        }
        
        // 对查询进行排序
        Arrays.sort(queries, 1, m + 1, new QueryComparator());
    }
    
    // 主计算函数
    public static void compute() {
        // 初始化计数数组
        Arrays.fill(count, 0);
        distinctCount = 0;
        
        int l = 1, r = 0;
        
        for (int i = 1; i <= m; i++) {
            int ql = queries[i][0];
            int qr = queries[i][1];
            int lcaNode = queries[i][2];
            int id = i;
            
            // 调整窗口边界
            while (r < qr) add(++r);
            while (r > qr) remove(r--);
            while (l < ql) remove(l++);
            while (l > ql) add(--l);
            
            // 特殊处理LCA节点
            // 如果LCA不在当前区间内，需要临时添加它
            boolean lcaInInterval = false;
            if (first[lcaNode] >= Math.min(ql, qr) && first[lcaNode] <= Math.max(ql, qr)) {
                lcaInInterval = true;
            }
            
            if (!lcaInInterval) {
                // 临时添加LCA节点
                int val = weights[lcaNode];
                if (count[val] == 0) {
                    distinctCount++;
                }
                count[val]++;
            }
            
            answers[id] = distinctCount;
            
            if (!lcaInInterval) {
                // 恢复LCA节点的状态
                int val = weights[lcaNode];
                if (count[val] == 1) {
                    distinctCount--;
                }
                count[val]--;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取节点权值
        for (int i = 1; i <= n; i++) {
            weights[i] = in.nextInt();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            queries[i][0] = in.nextInt(); // u
            queries[i][1] = in.nextInt(); // v
            queries[i][3] = i; // id
        }
        
        prepare();
        compute();
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(answers[i]);
        }
        
        out.flush();
        out.close();
    }
    
    // 读写工具类
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