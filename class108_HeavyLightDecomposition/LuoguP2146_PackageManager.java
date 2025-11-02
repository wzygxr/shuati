package class161;

import java.io.*;
import java.util.*;

// 洛谷P2146[NOI2015]软件包管理器
// 题目来源：洛谷P2146 [NOI2015]软件包管理器
// 题目链接：https://www.luogu.com.cn/problem/P2146
// 
// 题目描述：
// 你决定设计你自己的软件包管理器。不可避免地，你要解决软件包之间的依赖问题。
// 如果软件包a依赖软件包b，那么安装软件包a以前，必须先安装软件包b。
// 同时，如果想要卸载软件包b，则必须卸载软件包a。
// 现在你已经获得了所有的软件包之间的依赖关系。而且，由于你之前的工作，
// 除0号软件包以外，在你的管理器当中的软件包都会依赖一个且仅一个软件包，
// 而0号软件包不依赖任何一个软件包。且依赖关系不存在环。
// 
// 两种操作：
// install x：安装x号软件包
// uninstall x：卸载x号软件包
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 将依赖关系看作树结构，0号软件包为根节点
// 2. install操作：将节点x到根节点路径上的所有未安装节点安装
// 3. uninstall操作：将以节点x为根的子树中所有已安装节点卸载
// 4. 用线段树维护区间状态（0表示未安装，1表示已安装）
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的安装状态
// 3. 对于安装操作：更新从节点到根节点路径上所有未安装的节点为已安装
// 4. 对于卸载操作：更新以该节点为根的子树中所有已安装的节点为未安装
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P2146 [NOI2015]软件包管理器（本题）：https://www.luogu.com.cn/problem/P2146
// 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
// 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：LuoguP2146_PackageManager.java（当前文件）
// Python实现参考：Code_LuoguP2146_PackageManager.py
// C++实现参考：Code_LuoguP2146_PackageManager.cpp

public class LuoguP2146_PackageManager {
    public static int MAXN = 100001;
    
    // 图相关
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 0;
    
    // 树链剖分相关
    public static int[] fa = new int[MAXN];
    public static int[] dep = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[] son = new int[MAXN];
    public static int[] top = new int[MAXN];
    public static int[] dfn = new int[MAXN];
    public static int[] rnk = new int[MAXN];
    public static int cntd = 0;
    
    // 线段树相关
    public static int[] sum = new int[MAXN << 2];
    public static int[] lazy = new int[MAXN << 2];
    
    // 软件包状态：0表示未安装，1表示已安装
    public static boolean[] installed = new boolean[MAXN];
    
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次dfs，计算树链剖分所需信息
    public static void dfs1(int u, int f) {
        fa[u] = f;
        dep[u] = dep[f] + 1;
        siz[u] = 1;
        
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u);
                siz[u] += siz[v];
                if (son[u] == 0 || siz[son[u]] < siz[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 第二次dfs，计算重链剖分
    public static void dfs2(int u, int t) {
        top[u] = t;
        dfn[u] = ++cntd;
        rnk[cntd] = u;
        
        if (son[u] == 0) return;
        dfs2(son[u], t);
        
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                dfs2(v, v);
            }
        }
    }
    
    // 线段树操作
    public static void up(int i) {
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }
    
    // 设置懒标记：-1表示无标记，0表示设置为0，1表示设置为1
    public static void down(int i, int ln, int rn) {
        if (lazy[i] != -1) {
            sum[i << 1] = lazy[i] * ln;
            sum[i << 1 | 1] = lazy[i] * rn;
            lazy[i << 1] = lazy[i];
            lazy[i << 1 | 1] = lazy[i];
            lazy[i] = -1;
        }
    }
    
    // 区间更新
    public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            sum[i] = jobv * (r - l + 1);
            lazy[i] = jobv;
            return;
        }
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) update(jobl, jobr, jobv, l, mid, i << 1);
        if (jobr > mid) update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        up(i);
    }
    
    // 区间查询
    public static int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        int ans = 0;
        if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
        if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        return ans;
    }
    
    // 安装软件包：安装从根节点到x的路径上所有软件包
    public static int install(int x) {
        int count = 0;
        int originalSum = 0;
        
        // 计算路径上已经安装的软件包数量
        int temp = x;
        while (temp != 0) {
            originalSum += query(dfn[top[temp]], dfn[temp], 1, cntd, 1);
            temp = fa[top[temp]];
        }
        
        // 安装路径上所有软件包（设置为1）
        while (x != 0) {
            update(dfn[top[x]], dfn[x], 1, 1, cntd, 1);
            x = fa[top[x]];
        }
        
        // 计算新安装的软件包数量
        int newSum = 0;
        temp = x;
        while (temp != 0) {
            newSum += query(dfn[top[temp]], dfn[temp], 1, cntd, 1);
            temp = fa[top[temp]];
        }
        
        // 由于我们无法直接获取路径长度，使用另一种方法
        // 先查询安装前根到x路径上已安装的软件包数量
        // 再将路径上所有点设置为已安装
        // 最后查询安装后根到x路径上已安装的软件包数量
        // 增加的数量就是答案
        
        return 0; // 占位符，实际实现见下面
    }
    
    // 正确的install实现
    public static int installCorrect(int x) {
        // 先查询安装前从根到x路径上的安装情况
        int installedBefore = 0;
        int temp = x;
        while (temp != 0) {
            installedBefore += query(dfn[top[temp]], dfn[temp], 1, cntd, 1);
            temp = fa[top[temp]];
        }
        
        // 安装从根到x路径上的所有软件包
        int nodesInPath = 0;
        temp = x;
        while (temp != 0) {
            update(dfn[top[temp]], dfn[temp], 1, 1, cntd, 1);
            // 计算路径上的节点数
            nodesInPath += (dfn[temp] - dfn[top[temp]] + 1);
            temp = fa[top[temp]];
        }
        
        // 查询安装后从根到x路径上的安装情况
        int installedAfter = 0;
        temp = x;
        while (temp != 0) {
            installedAfter += query(dfn[top[temp]], dfn[temp], 1, cntd, 1);
            temp = fa[top[temp]];
        }
        
        return installedAfter - installedBefore;
    }
    
    // 卸载软件包：卸载以x为根的子树中的所有软件包
    public static int uninstall(int x) {
        // 先查询卸载前x子树中已安装的软件包数量
        int installedBefore = query(dfn[x], dfn[x] + siz[x] - 1, 1, cntd, 1);
        
        // 卸载x子树中的所有软件包（设置为0）
        update(dfn[x], dfn[x] + siz[x] - 1, 0, 1, cntd, 1);
        
        return installedBefore; // 返回卸载的软件包数量
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 初始化懒标记数组
        Arrays.fill(lazy, -1);
        
        int n = Integer.parseInt(br.readLine());
        
        // 构建树结构（0是根节点，表示空软件包）
        for (int i = 1; i <= n - 1; i++) {
            int parent = Integer.parseInt(br.readLine());
            addEdge(parent, i);
            addEdge(i, parent);
        }
        
        // 树链剖分
        dfs1(0, 0);
        dfs2(0, 0);
        
        int q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            String[] parts = br.readLine().split(" ");
            String operation = parts[0];
            int x = Integer.parseInt(parts[1]);
            
            if (operation.equals("install")) {
                out.println(installCorrect(x));
            } else { // uninstall
                out.println(uninstall(x));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}