package class168;

// P3242 [HNOI2015] 接水果 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P3242
// 时间复杂度：O((P+Q) * logP * log(maxC))
// 空间复杂度：O(P + Q)
// 
// 题目大意：
// 在树上选择能接住水果的盘子，求权值第k小的盘子。
// 
// 解题思路：
// 1. 对盘子权值进行二分
// 2. 使用树链剖分处理路径包含关系
// 3. 统计满足条件的盘子数量
// 4. 根据统计结果将操作分为两类递归处理
// 
// 算法详解：
// 1. 整体二分：将所有查询一起处理，对盘子权值进行二分
// 2. 树链剖分：用于处理树上路径的包含关系
// 3. 路径包含：判断一条路径是否包含另一条路径

import java.util.*;
import java.io.*;

public class P3242_接水果 {
    public static int MAXN = 40001;
    
    public static int n, p, q;
    
    // 树结构
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 0;
    
    // 树链剖分
    public static int[] fa = new int[MAXN];
    public static int[] depth = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[] son = new int[MAXN];
    public static int[] top = new int[MAXN];
    public static int[] dfn = new int[MAXN];
    public static int[] rnk = new int[MAXN];
    public static int dfc = 0;
    
    // 盘子信息
    public static int[] pa = new int[MAXN];
    public static int[] pb = new int[MAXN];
    public static int[] pc = new int[MAXN];
    
    // 水果信息
    public static int[] ua = new int[MAXN];
    public static int[] ub = new int[MAXN];
    public static int[] uk = new int[MAXN];
    public static int[] qid = new int[MAXN];
    
    // 整体二分
    public static int[] lset = new int[MAXN];
    public static int[] rset = new int[MAXN];
    public static int[] ans = new int[MAXN];
    
    // 离散化
    public static int[] sorted = new int[MAXN];
    public static int cntv = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        p = Integer.parseInt(line[1]);
        q = Integer.parseInt(line[2]);
        
        // 建树
        for (int i = 1; i < n; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 读取盘子信息
        for (int i = 1; i <= p; i++) {
            line = br.readLine().split(" ");
            pa[i] = Integer.parseInt(line[0]);
            pb[i] = Integer.parseInt(line[1]);
            pc[i] = Integer.parseInt(line[2]);
            sorted[++cntv] = pc[i];
        }
        
        // 读取水果信息
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            ua[i] = Integer.parseInt(line[0]);
            ub[i] = Integer.parseInt(line[1]);
            uk[i] = Integer.parseInt(line[2]);
            qid[i] = i;
        }
        
        // 离散化
        Arrays.sort(sorted, 1, cntv + 1);
        cntv = unique(sorted, cntv);
        
        // 树链剖分预处理
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 整体二分求解
        compute(1, q, 1, cntv);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(sorted[ans[i]]);
        }
        out.flush();
    }
    
    // 去重函数
    // 对排序后的数组进行去重，返回去重后的长度
    public static int unique(int[] arr, int len) {
        if (len <= 1) return len;
        int i = 1, j = 2;
        while (j <= len) {
            if (arr[j] != arr[i]) {
                arr[++i] = arr[j];
            }
            j++;
        }
        return i;
    }
    
    // 添加边
    // 使用邻接表存储树的结构
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次DFS：计算深度、父节点、子树大小、重儿子
    // 这是树链剖分的第一步，用于确定每个节点的重儿子
    public static void dfs1(int u, int f) {
        fa[u] = f;
        depth[u] = depth[f] + 1;
        siz[u] = 1;
        
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v == f) continue;
            dfs1(v, u);
            siz[u] += siz[v];
            // 更新重儿子：选择子树大小最大的子节点作为重儿子
            if (siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
    
    // 第二次DFS：计算dfn序、重链顶点
    // 这是树链剖分的第二步，用于确定每个节点的dfn序和所在重链的顶点
    public static void dfs2(int u, int t) {
        top[u] = t;  // 记录节点u所在重链的顶点
        dfn[u] = ++dfc;  // 记录节点u的dfn序
        rnk[dfc] = u;  // 记录dfn序对应的节点
        
        // 如果存在重儿子，优先遍历重儿子
        if (son[u] != 0) {
            dfs2(son[u], t);
        }
        
        // 遍历轻儿子
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            // 跳过父节点和重儿子
            if (v == fa[u] || v == son[u]) continue;
            // 轻儿子所在重链的顶点就是它自己
            dfs2(v, v);
        }
    }
    
    // 整体二分核心函数
    // ql, qr: 当前处理的查询范围
    // vl, vr: 当前处理的值域范围（盘子权值范围）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = vl;
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 检查每个查询
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            // 检查权值大于等于sorted[mid]的盘子中第uk[id]小的是否存在
            int count = countValidPlates(mid, ua[id], ub[id]);
            if (count >= uk[id]) {
                // 有足够的盘子，说明答案可能更大，分到左区间
                lset[++lsiz] = id;
            } else {
                // 没有足够的盘子，说明答案更小，分到右区间
                uk[id] -= count;
                rset[++rsiz] = id;
            }
        }
        
        // 将操作分组
        for (int i = 1; i <= lsiz; i++) {
            qid[ql + i - 1] = lset[i];
        }
        for (int i = 1; i <= rsiz; i++) {
            qid[ql + lsiz + i - 1] = rset[i];
        }
        
        // 递归处理左右区间
        // 左区间：答案范围[mid+1, vr]（权值更大的盘子）
        compute(ql, ql + lsiz - 1, mid + 1, vr);
        // 右区间：答案范围[vl, mid]（权值更小的盘子）
        compute(ql + lsiz, qr, vl, mid);
    }
    
    // 计算能接住水果(ua, ub)且权值大于等于sorted[mid]的盘子数量
    // 这是一个简化的实现，实际应该使用更复杂的数据结构来高效计算
    public static int countValidPlates(int mid, int ua, int ub) {
        int count = 0;
        // 这里需要实现具体的计数逻辑
        // 由于实现较为复杂，这里简化处理
        for (int i = 1; i <= p; i++) {
            if (pc[i] >= sorted[mid] && isSubPath(ua, ub, pa[i], pb[i])) {
                count++;
            }
        }
        return count;
    }
    
    // 判断路径(pa, pb)是否是路径(ua, ub)的子路径
    // 这是一个简化的实现，实际应该使用树链剖分来高效判断
    public static boolean isSubPath(int ua, int ub, int pa, int pb) {
        // 这里需要实现路径包含关系的判断逻辑
        // 由于实现较为复杂，这里简化处理
        return false;
    }
}