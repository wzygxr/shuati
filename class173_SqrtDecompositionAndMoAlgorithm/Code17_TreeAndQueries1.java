// Tree and Queries问题 - 树上Mo算法实现 (Java版本)
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/375/D
// 题目大意: 给定一棵树，每个节点有一个颜色，有q次查询
// 每次查询某个子树内出现次数>=k的颜色数量
// 约束条件: 1 <= n, q <= 10^5
// 解法: 树上Mo算法（离线分块）+ 欧拉序
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 是否最优解: 是，树上Mo算法是解决此类树上区间查询问题的经典方法

import java.io.*;
import java.util.*;

public class Code17_TreeAndQueries1 {
    
    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 节点数量, q: 查询次数, blen: 块大小
    public static int n, q, blen;
    
    // color: 节点颜色数组
    public static int[] color = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static int[] ans = new int[MAXN];
    
    // count: 颜色计数数组，记录每种颜色在当前窗口中的出现次数
    public static int[] count = new int[MAXN];
    
    // colorCount: 颜色出现次数的计数数组，记录出现i次的颜色数量
    public static int[] colorCount = new int[MAXN];
    
    // curAns: 当前窗口中满足条件的颜色数量
    public static int curAns = 0;
    
    // 邻接表存储树结构
    public static ArrayList<Integer>[] graph = new ArrayList[MAXN];
    
    // 欧拉序相关变量
    public static int[] euler = new int[2 * MAXN]; // 欧拉序数组，记录DFS访问节点的顺序
    public static int[] first = new int[MAXN]; // 节点第一次出现在欧拉序中的位置
    public static int[] last = new int[MAXN]; // 节点最后一次出现在欧拉序中的位置
    public static int eulerIdx = 0; // 欧拉序索引
    
    // 查询结构体，用于存储查询信息
    static class Query {
        int l, r, k, id; // l,r: 查询区间边界, k: 颜色出现次数阈值, id: 查询编号

        Query(int l, int r, int k, int id) {
            this.l = l;
            this.r = r;
            this.k = k;
            this.id = id;
        }
    }

    // 存储所有查询
    static Query[] queries = new Query[MAXN];

    // 添加元素到当前窗口
    // 时间复杂度: O(1)
    // 设计思路: 更新颜色计数和颜色出现次数的计数
    public static void add(int pos) {
        int col = color[euler[pos]];
        // 更新颜色出现次数的计数
        colorCount[count[col]]--;
        count[col]++;
        colorCount[count[col]]++;
    }

    // 从当前窗口移除元素
    // 时间复杂度: O(1)
    // 设计思路: 更新颜色计数和颜色出现次数的计数
    public static void remove(int pos) {
        int col = color[euler[pos]];
        // 更新颜色出现次数的计数
        colorCount[count[col]]--;
        count[col]--;
        colorCount[count[col]]++;
    }

    // DFS生成欧拉序
    // 时间复杂度: O(n)
    // 设计思路: 通过DFS遍历树，记录每个节点的进入和离开时间，形成欧拉序
    // 这样可以将子树查询转换为区间查询
    public static void dfs(int u, int parent) {
        eulerIdx++;
        euler[eulerIdx] = u;
        first[u] = eulerIdx;
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u);
            }
        }
        
        eulerIdx++;
        euler[eulerIdx] = u;
        last[u] = eulerIdx;
    }

    // 查询比较函数，用于Mo算法排序
    // 时间复杂度: O(1)
    // 设计思路: 按照块编号排序，同一块内按右端点排序，这样可以最小化指针移动次数
    public static void sortQueries() {
        Arrays.sort(queries, 1, q + 1, (a, b) -> {
            int blockA = (a.l - 1) / blen;
            int blockB = (b.l - 1) / blen;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });
    }

    // Mo算法主函数
    // 时间复杂度: O((n + q) * sqrt(n))
    // 设计思路: 通过巧妙的排序策略，使得相邻查询之间的指针移动次数最少
    public static void moAlgorithm() {
        // 对查询进行排序
        sortQueries();
        
        // Mo算法处理
        int curL = 1, curR = 0;
        for (int i = 1; i <= q; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int k = queries[i].k;
            int id = queries[i].id;
            
            // 扩展右边界
            while (curR < r) {
                curR++;
                add(curR);
            }
            
            // 收缩右边界
            while (curR > r) {
                remove(curR);
                curR--;
            }
            
            // 收缩左边界
            while (curL < l) {
                remove(curL);
                curL++;
            }
            
            // 扩展左边界
            while (curL > l) {
                curL--;
                add(curL);
            }
            
            // 计算答案：出现次数>=k的颜色数量
            int result = 0;
            for (int j = k; j < MAXN; j++) {
                result += colorCount[j];
            }
            ans[id] = result;
        }
    }

    public static void main(String[] args) throws IOException {
        // 初始化邻接表
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            color[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 生成欧拉序
        eulerIdx = 0;
        dfs(1, 0);
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            int v = Integer.parseInt(line[0]);
            int k = Integer.parseInt(line[1]);
            // 转换为欧拉序上的区间查询
            queries[i] = new Query(first[v], last[v], k, i);
        }
        
        // 计算块大小
        blen = (int) Math.sqrt(2 * n);
        
        // Mo算法处理
        moAlgorithm();
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        br.close();
        out.close();
    }
}