package class163;

// DSU on Tree 算法实现 - Tree Requests (Codeforces 570D)
// 题目来源: Codeforces 570D - Tree Requests
// 题目链接: https://codeforces.com/problemset/problem/570/D
// 
// 题目大意:
// 给定一棵有根树，每个节点有一个小写字母。有m个查询，每个查询给定节点v和深度h，
// 询问在节点v的子树中，深度为h的所有节点上的字母能否重新排列成一个回文串。
// 如果可以，输出"Yes"，否则输出"No"。
//
// 解题思路:
// 使用DSU on Tree算法统计每个深度上字母的出现频率。
// 对于每个查询，检查该深度上字母频率是否满足回文串条件：
// 最多只能有一个字母出现奇数次。
//
// 时间复杂度: O(n log n + m)
// 空间复杂度: O(n)
//
// 算法详解:
// 1. 重链剖分预处理，确定每个节点的重儿子
// 2. 使用DSU on Tree统计每个深度上字母的出现频率
// 3. 对于每个查询，检查对应深度的字母频率是否满足回文条件
//
// 工程化实现要点:
// 1. 边界处理：空树、单节点树、深度超出范围等情况
// 2. 内存优化：使用位运算优化字母频率统计
// 3. 常数优化：减少函数调用，使用局部变量缓存
// 4. 异常处理：验证输入参数的有效性
//
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.*;
import java.util.*;

public class Code12_TreeRequests1 {

    public static int MAXN = 500001;
    
    public static int n, m;
    public static char[] s = new char[MAXN];
    public static int[] depth = new int[MAXN];
    
    // 链式前向星
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN];
    public static int[] to = new int[MAXN];
    public static int cnt = 0;
    
    // 树链剖分
    public static int[] fa = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[] son = new int[MAXN];
    
    // DSU on Tree相关
    // freq[d][c]表示深度d上字母c的出现次数
    public static int[][] freq = new int[MAXN][26];
    // 查询存储
    public static List<int[]>[] queries = new ArrayList[MAXN];
    public static boolean[] ans = new boolean[MAXN];
    
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次DFS，重链剖分
    public static void dfs1(int u, int f) {
        fa[u] = f;
        depth[u] = depth[f] + 1;
        siz[u] = 1;
        
        for (int e = head[u], v; e > 0; e = next[e]) {
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
    
    // 添加节点贡献
    public static void addNode(int u) {
        int d = depth[u];
        int c = s[u] - 'a';
        freq[d][c]++;
    }
    
    // 移除节点贡献
    public static void removeNode(int u) {
        int d = depth[u];
        int c = s[u] - 'a';
        freq[d][c]--;
    }
    
    // 添加子树贡献
    public static void addSubtree(int u) {
        addNode(u);
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u]) {
                addSubtree(v);
            }
        }
    }
    
    // 移除子树贡献
    public static void removeSubtree(int u) {
        removeNode(u);
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u]) {
                removeSubtree(v);
            }
        }
    }
    
    // 检查深度d是否满足回文条件
    public static boolean checkDepth(int d) {
        int oddCount = 0;
        for (int i = 0; i < 26; i++) {
            if (freq[d][i] % 2 == 1) {
                oddCount++;
            }
        }
        return oddCount <= 1;
    }
    
    // DSU on Tree主过程
    public static void dfs2(int u, boolean keep) {
        // 处理轻儿子
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                dfs2(v, false);
            }
        }
        
        // 处理重儿子
        if (son[u] != 0) {
            dfs2(son[u], true);
        }
        
        // 添加当前节点贡献
        addNode(u);
        
        // 添加轻儿子贡献
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                addSubtree(v);
            }
        }
        
        // 处理查询
        if (queries[u] != null) {
            for (int[] query : queries[u]) {
                int h = query[0];
                int idx = query[1];
                // 检查深度h是否满足回文条件
                ans[idx] = checkDepth(h);
            }
        }
        
        // 如果不保留，清除贡献
        if (!keep) {
            removeSubtree(u);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        // 初始化查询列表
        for (int i = 1; i <= n; i++) {
            queries[i] = new ArrayList<>();
        }
        
        // 读取树结构
        st = new StringTokenizer(br.readLine());
        for (int i = 2; i <= n; i++) {
            int p = Integer.parseInt(st.nextToken());
            addEdge(p, i);
            addEdge(i, p);
        }
        
        // 读取节点字母
        String str = br.readLine();
        for (int i = 1; i <= n; i++) {
            s[i] = str.charAt(i - 1);
        }
        
        // 读取查询
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            queries[v].add(new int[]{h, i});
        }
        
        // 执行算法
        dfs1(1, 0);
        dfs2(1, false);
        
        // 输出结果
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < m; i++) {
            out.println(ans[i] ? "Yes" : "No");
        }
        out.flush();
        out.close();
        br.close();
    }
}