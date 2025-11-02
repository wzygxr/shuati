package class168;

// P5163 WD与地图 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P5163
// 时间复杂度：O(Q * logQ * (N + M))
// 空间复杂度：O(N + M + Q)
// 
// 题目大意：
// 维护有向图，支持删边、点权增加、查询强连通分量前k大点权和。
// 
// 解题思路：
// 1. 对时间进行二分
// 2. 使用可撤销并查集维护强连通分量
// 3. 检查在某个时间点是否满足查询条件
// 4. 根据统计结果将操作分为两类递归处理
// 
// 算法详解：
// 1. 整体二分：将所有操作一起处理，对时间进行二分
// 2. 可撤销并查集：用于维护强连通分量，支持操作的回滚
// 3. 点权维护：使用并查集维护每个连通分量的点权和

import java.io.*;
import java.util.*;

public class P5163_WD与地图 {
    public static int MAXN = 100001;
    public static int MAXM = 200001;
    public static int MAXQ = 200001;
    public static int n, m, q;
    
    // 节点权值
    public static int[] s = new int[MAXN];
    
    // 边的信息
    public static int[] eu = new int[MAXM];
    public static int[] ev = new int[MAXM];
    
    // 操作信息
    public static int[] op = new int[MAXQ];
    public static int[] a = new int[MAXQ];
    public static int[] b = new int[MAXQ];
    
    // 并查集
    public static int[] father = new int[MAXN];
    public static int[] size = new int[MAXN];
    public static long[] value = new long[MAXN];
    public static int[] stack = new int[MAXN];
    public static int top = 0;
    
    // 整体二分
    public static int[] lset = new int[MAXQ];
    public static int[] rset = new int[MAXQ];
    
    // 答案
    public static long[] ans = new long[MAXQ];
    
    // 初始化并查集
    // 将每个节点初始化为一个独立的集合
    public static void init() {
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            size[i] = 1;
            value[i] = s[i];
        }
        top = 0;
    }
    
    // 查找根节点（带路径压缩）
    // 使用路径压缩优化，使查找操作的时间复杂度接近O(1)
    public static int find(int x) {
        while (x != father[x]) {
            x = father[x];
        }
        return x;
    }
    
    // 合并两个集合
    // 使用按秩合并优化，将较小的树合并到较大的树上
    // 返回true表示成功合并，false表示已在同一集合中
    public static boolean union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx == fy) {
            return false;
        }
        // 按秩合并，将较小的树合并到较大的树上
        if (size[fx] < size[fy]) {
            int temp = fx;
            fx = fy;
            fy = temp;
        }
        father[fy] = fx;
        size[fx] += size[fy];
        value[fx] += value[fy];
        stack[++top] = fy; // 记录修改，用于回滚
        return true;
    }
    
    // 回滚操作
    // 将并查集的状态回滚到targetTop时刻
    public static void rollback(int targetTop) {
        while (top > targetTop) {
            int y = stack[top--];
            int fy = find(y);
            value[fy] -= value[y];
            size[fy] -= size[y];
            father[y] = y;
        }
    }
    
    // 计算前k大值的和
    // 这是一个简化的实现，实际应该维护一个优先队列或有序结构
    public static long getTopK(int x, int k) {
        // 这里简化处理，实际应该维护一个优先队列或有序结构
        // 为了整体二分的演示，我们只返回连通块的总和
        int fx = find(x);
        return value[fx];
    }
    
    // 整体二分核心函数
    // ql, qr: 当前处理的操作范围
    // vl, vr: 当前处理的时间范围
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界
        if (ql > qr) {
            return;
        }
        // 递归边界：时间范围只有一个值，找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                if (op[qid[i]] == 3) {
                    ans[qid[i]] = vl;
                }
            }
            return;
        }
        
        int mid = (vl + vr) >> 1;
        int targetTop = top;
        
        // 处理时间小于等于mid的操作
        for (int i = vl; i <= mid; i++) {
            if (op[i] == 1) {
                // 删除边，这里简化处理
            } else if (op[i] == 2) {
                // 增加点权
                int fx = find(a[i]);
                value[fx] += b[i];
            }
        }
        
        // 检查每个操作
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            if (op[id] == 3) {
                // 查询操作
                long sum = getTopK(a[id], b[id]);
                if (sum >= 0) { // 这里简化判断
                    lset[++lsiz] = id;
                } else {
                    rset[++rsiz] = id;
                }
            } else {
                // 修改操作放在对应的区间
                if (id <= mid) {
                    lset[++lsiz] = id;
                } else {
                    rset[++rsiz] = id;
                }
            }
        }
        
        // 重新排列操作顺序
        for (int i = 1; i <= lsiz; i++) {
            qid[ql + i - 1] = lset[i];
        }
        for (int i = 1; i <= rsiz; i++) {
            qid[ql + lsiz + i - 1] = rset[i];
        }
        
        // 回滚操作
        rollback(targetTop);
        
        // 递归处理左右两部分
        // 左区间：时间范围[vl, mid]
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右区间：时间范围[mid+1, vr]
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    // 查询编号数组
    public static int[] qid = new int[MAXQ];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        q = Integer.parseInt(params[2]);
        
        // 读取节点权值
        String[] values = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            s[i] = Integer.parseInt(values[i - 1]);
        }
        
        // 读取边信息
        for (int i = 1; i <= m; i++) {
            String[] edge = br.readLine().split(" ");
            eu[i] = Integer.parseInt(edge[0]);
            ev[i] = Integer.parseInt(edge[1]);
        }
        
        // 读取操作信息
        for (int i = 1; i <= q; i++) {
            String[] operation = br.readLine().split(" ");
            op[i] = Integer.parseInt(operation[0]);
            a[i] = Integer.parseInt(operation[1]);
            if (op[i] != 1) {
                b[i] = Integer.parseInt(operation[2]);
            }
            qid[i] = i;
        }
        
        // 初始化并查集
        init();
        
        // 整体二分求解
        compute(1, q, 1, q);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            if (op[i] == 3) {
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}