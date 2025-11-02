package class169.supplementary_solutions;

import java.io.*;
import java.util.*;

/**
 * 洛谷P4602 [CTSC2018]混合果汁 - Java实现
 * 题目来源：https://www.luogu.com.cn/problem/P4602
 * 题目描述：线段树与整体二分结合的优化问题
 * 
 * 问题描述：
 * 有n种果汁，每种果汁有美味度d，单价p，数量l。现在有m个询问，每个询问给出需要的总数量g和最高预算v，
 * 要求选一些果汁，使得总数量至少g，总费用不超过v，并且所选果汁的最低美味度尽可能大。
 * 
 * 解题思路：
 * 使用整体二分法来二分可能的最低美味度d，对于每个d，使用线段树维护满足d' >= d的果汁，
 * 并支持查询在预算v下最多能买多少果汁。
 * 
 * 时间复杂度：O((n+m) * log(n) * log(max_p))
 * 空间复杂度：O(n + m)
 */
public class CTSC2018混合果汁 {
    static final int MAXN = 100005;
    static final int MAXM = 100005;
    static final int INF = 1 << 30;
    
    // 果汁信息
    static int[] d = new int[MAXN]; // 美味度
    static int[] p = new int[MAXN]; // 单价
    static int[] l = new int[MAXN]; // 数量
    
    // 查询信息
    static int[] g = new int[MAXM]; // 需要的总数量
    static int[] v = new int[MAXM]; // 最高预算
    static int[] ans = new int[MAXM]; // 答案
    
    // 整体二分相关
    static int[] ql = new int[MAXM];
    static int[] qr = new int[MAXM];
    static int[] qmid = new int[MAXM];
    static int[] qans = new int[MAXM];
    static int[] qid = new int[MAXM]; // 查询的原始顺序
    
    // 离散化
    static int[] disD = new int[MAXN];
    static int[] disP = new int[MAXN];
    static int dSize, pSize;
    
    // 线段树结构
    static class Node {
        int sumL; // 总数量
        long sumCost; // 总费用
    }
    
    static Node[] tree = new Node[MAXN * 4];
    
    // 初始化线段树节点
    static void initTree() {
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Node();
        }
    }
    
    // 线段树更新
    static void update(int o, int l, int r, int pos, int addL, long addCost) {
        tree[o].sumL += addL;
        tree[o].sumCost += addCost;
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(o << 1, l, mid, pos, addL, addCost);
        } else {
            update(o << 1 | 1, mid + 1, r, pos, addL, addCost);
        }
    }
    
    // 线段树查询：最多能买多少果汁，总费用不超过v
    static long query(int o, int l, int r, int need, long cost) {
        if (need <= 0) return 0;
        if (l == r) {
            int canBuy = Math.min(need, tree[o].sumL);
            return Math.min((long)canBuy, v / l); // 这里l是价格，可能需要调整
        }
        int mid = (l + r) >> 1;
        // 优先选择价格低的（左子树）
        long leftCost = tree[o << 1].sumCost;
        int leftL = tree[o << 1].sumL;
        if (leftCost <= cost) {
            // 左子树全部购买，再买右子树的
            return leftL + query(o << 1 | 1, mid + 1, r, need - leftL, cost - leftCost);
        } else {
            // 只买左子树的一部分
            return query(o << 1, l, mid, need, cost);
        }
    }
    
    // 离散化处理
    static void discrete(int n) {
        // 离散化美味度
        Set<Integer> dSet = new HashSet<>();
        for (int i = 1; i <= n; i++) {
            dSet.add(d[i]);
        }
        dSize = 0;
        for (int val : dSet) {
            disD[++dSize] = val;
        }
        Arrays.sort(disD, 1, dSize + 1);
        
        // 离散化价格
        Set<Integer> pSet = new HashSet<>();
        for (int i = 1; i <= n; i++) {
            pSet.add(p[i]);
        }
        pSize = 0;
        for (int val : pSet) {
            disP[++pSize] = val;
        }
        Arrays.sort(disP, 1, pSize + 1);
        
        // 转换为离散化后的值
        for (int i = 1; i <= n; i++) {
            d[i] = Arrays.binarySearch(disD, 1, dSize + 1, d[i]) - disD[0] + 1;
            p[i] = Arrays.binarySearch(disP, 1, pSize + 1, p[i]) - disP[0] + 1;
        }
    }
    
    // 整体二分核心函数
    static void solve(int ql, int qr, int l, int r) {
        if (ql > qr || l > r) return;
        
        if (l == r) {
            // 所有查询的答案都是disD[l]
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = disD[l];
            }
            return;
        }
        
        int mid = (l + r + 1) >> 1;
        
        // 将所有美味度>=mid的果汁加入线段树
        for (int i = 1; i <= n; i++) {
            if (d[i] >= mid) {
                int pos = p[i];
                update(1, 1, pSize, pos, l[i], (long)p[i] * l[i]);
            }
        }
        
        // 记录哪些查询可以满足
        int[] left = new int[qr - ql + 2];
        int[] right = new int[qr - ql + 2];
        int lc = 0, rc = 0;
        
        for (int i = ql; i <= qr; i++) {
            int idx = qid[i];
            long maxBuy = query(1, 1, pSize, g[idx], v[idx]);
            if (maxBuy >= g[idx]) {
                // 可以满足，答案可能更大
                left[++lc] = idx;
            } else {
                // 无法满足，答案必须更小
                right[++rc] = idx;
            }
        }
        
        // 从线段树中移除所有果汁
        for (int i = 1; i <= n; i++) {
            if (d[i] >= mid) {
                int pos = p[i];
                update(1, 1, pSize, pos, -l[i], -(long)p[i] * l[i]);
            }
        }
        
        // 合并查询顺序
        int ptr = ql;
        for (int i = 1; i <= lc; i++) {
            qid[ptr++] = left[i];
        }
        for (int i = 1; i <= rc; i++) {
            qid[ptr++] = right[i];
        }
        
        // 递归处理左右两部分
        solve(ql, ql + lc - 1, mid, r);
        solve(ql + lc, qr, l, mid - 1);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        // 读取果汁信息
        for (int i = 1; i <= n; i++) {
            parts = br.readLine().split(" ");
            d[i] = Integer.parseInt(parts[0]);
            p[i] = Integer.parseInt(parts[1]);
            l[i] = Integer.parseInt(parts[2]);
        }
        
        // 离散化
        discrete(n);
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            g[i] = Integer.parseInt(parts[0]);
            v[i] = Integer.parseInt(parts[1]);
            qid[i] = i;
        }
        
        // 初始化线段树
        initTree();
        
        // 整体二分求解
        solve(1, m, 1, dSize);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}