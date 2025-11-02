// 测试链接 : https://www.luogu.com.cn/problem/P5494
// 线段树分裂模板题 - Java实现

import java.io.*;
import java.util.*;

/**
 * P5494 【模板】线段树分裂
 * 
 * 题目描述：
 * 给定一个初始为空的序列，支持以下操作：
 * 1. 在某个位置插入一个数
 * 2. 将某个区间分裂成一个新的序列
 * 3. 将两个序列合并
 * 4. 查询某个区间内第k小的数
 * 
 * 核心算法：线段树分裂 + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code15_P5494_SegmentTreeSplitTemplate {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
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
    
    // 线段树节点类
    static class Node {
        int l, r;  // 左右子节点索引
        long sum;   // 区间和
        int cnt;    // 区间内元素个数
        
        Node() {
            l = r = -1;
            sum = 0;
            cnt = 0;
        }
    }
    
    static final int MAXN = 200010;
    static final int MAXM = 20000010;  // 动态开点空间
    static Node[] tree = new Node[MAXM];
    static int[] roots = new int[MAXN];  // 各个序列的根节点
    static int cnt = 0;  // 节点计数器
    static int seqCnt = 1;  // 序列计数器
    
    // 初始化线段树节点池
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new Node();
        }
    }
    
    // 创建新节点
    static int newNode() {
        if (cnt >= MAXM) {
            System.gc();
            return -1;
        }
        tree[cnt].l = tree[cnt].r = -1;
        tree[cnt].sum = 0;
        tree[cnt].cnt = 0;
        return cnt++;
    }
    
    // 更新节点信息
    static void pushUp(int rt) {
        if (rt == -1) return;
        tree[rt].sum = 0;
        tree[rt].cnt = 0;
        if (tree[rt].l != -1) {
            tree[rt].sum += tree[tree[rt].l].sum;
            tree[rt].cnt += tree[tree[rt].l].cnt;
        }
        if (tree[rt].r != -1) {
            tree[rt].sum += tree[tree[rt].r].sum;
            tree[rt].cnt += tree[tree[rt].r].cnt;
        }
    }
    
    // 单点更新
    static void update(int rt, int l, int r, int pos, long val) {
        if (l == r) {
            tree[rt].sum += val;
            tree[rt].cnt += 1;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tree[rt].l == -1) tree[rt].l = newNode();
            update(tree[rt].l, l, mid, pos, val);
        } else {
            if (tree[rt].r == -1) tree[rt].r = newNode();
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        pushUp(rt);
    }
    
    // 线段树分裂：将序列p的[l, r]区间分裂到序列q
    static void split(int p, int q, int l, int r, int L, int R) {
        if (p == -1) return;
        if (L > r || R < l) return;
        
        if (L >= l && R <= r) {
            // 整个区间需要分裂
            if (q == -1) q = newNode();
            tree[q] = tree[p];
            tree[p] = new Node();  // 原节点清空
            return;
        }
        
        int mid = (L + R) >> 1;
        if (tree[p].l != -1 && l <= mid) {
            if (tree[q].l == -1) tree[q].l = newNode();
            split(tree[p].l, tree[q].l, l, r, L, mid);
        }
        if (tree[p].r != -1 && r > mid) {
            if (tree[q].r == -1) tree[q].r = newNode();
            split(tree[p].r, tree[q].r, l, r, mid + 1, R);
        }
        
        pushUp(p);
        pushUp(q);
    }
    
    // 线段树合并：将序列q合并到序列p
    static int merge(int p, int q, int l, int r) {
        if (p == -1) return q;
        if (q == -1) return p;
        
        if (l == r) {
            tree[p].sum += tree[q].sum;
            tree[p].cnt += tree[q].cnt;
            return p;
        }
        
        int mid = (l + r) >> 1;
        tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
        tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
        
        pushUp(p);
        return p;
    }
    
    // 查询区间第k小
    static long queryKth(int rt, int l, int r, int k) {
        if (l == r) return l;
        
        int mid = (l + r) >> 1;
        int leftCnt = (tree[rt].l != -1) ? tree[tree[rt].l].cnt : 0;
        
        if (k <= leftCnt) {
            return queryKth(tree[rt].l, l, mid, k);
        } else {
            return queryKth(tree[rt].r, mid + 1, r, k - leftCnt);
        }
    }
    
    // 查询区间和
    static long querySum(int rt, int l, int r, int L, int R) {
        if (rt == -1 || L > r || R < l) return 0;
        if (L >= l && R <= r) return tree[rt].sum;
        
        int mid = (L + R) >> 1;
        long res = 0;
        if (tree[rt].l != -1) {
            res += querySum(tree[rt].l, l, r, L, mid);
        }
        if (tree[rt].r != -1) {
            res += querySum(tree[rt].r, l, r, mid + 1, R);
        }
        return res;
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int m = io.nextInt();
        
        // 初始化根节点
        roots[1] = newNode();
        
        // 读入初始序列
        for (int i = 1; i <= n; i++) {
            long x = io.nextLong();
            update(roots[1], 1, n, i, x);
        }
        
        while (m-- > 0) {
            int op = io.nextInt();
            
            if (op == 0) {
                // 分裂操作：将序列p的[l, r]区间分裂成新序列q
                int p = io.nextInt();
                int l = io.nextInt();
                int r = io.nextInt();
                
                seqCnt++;
                roots[seqCnt] = newNode();
                split(roots[p], roots[seqCnt], l, r, 1, n);
                
            } else if (op == 1) {
                // 合并操作：将序列q合并到序列p
                int p = io.nextInt();
                int q = io.nextInt();
                roots[p] = merge(roots[p], roots[q], 1, n);
                roots[q] = -1;  // 序列q被合并后清空
                
            } else if (op == 2) {
                // 插入操作：在序列p的末尾插入一个数
                int p = io.nextInt();
                long x = io.nextLong();
                int pos = tree[roots[p]].cnt + 1;  // 插入到末尾
                update(roots[p], 1, n, pos, x);
                
            } else if (op == 3) {
                // 查询操作：查询序列p的[l, r]区间和
                int p = io.nextInt();
                int l = io.nextInt();
                int r = io.nextInt();
                long sum = querySum(roots[p], l, r, 1, n);
                io.println(sum);
                
            } else if (op == 4) {
                // 查询第k小操作
                int p = io.nextInt();
                int k = io.nextInt();
                if (tree[roots[p]].cnt < k) {
                    io.println(-1);  // 不存在第k小
                } else {
                    long kth = queryKth(roots[p], 1, n, k);
                    io.println(kth);
                }
            }
        }
        
        io.close();
    }
}

/*
 * 线段树分裂算法详解：
 * 
 * 1. 算法思想：
 *    线段树分裂是线段树合并的逆操作，用于将一个线段树按照区间拆分成两个独立的线段树。
 *    这种操作在处理序列分裂、区间分离等场景中非常有用。
 * 
 * 2. 核心操作：
 *    - 分裂(split): 将原线段树的某个区间分离出来形成新的线段树
 *    - 合并(merge): 将两个线段树合并成一个
 * 
 * 3. 应用场景：
 *    - 序列操作：支持序列的分裂和合并
 *    - 区间管理：动态管理多个独立的区间
 *    - 持久化数据结构：实现可持久化线段树
 * 
 * 4. 时间复杂度：
 *    - 分裂和合并操作都是O(log n)的
 *    - 整体复杂度为O(n log n)
 * 
 * 5. 类似题目推荐：
 *    - P4556 [Vani有约会]雨天的尾巴 (线段树合并经典题)
 *    - P3224 [HNOI2012]永无乡 (线段树合并+并查集)
 *    - P5298 [PKUWC2018]Minimax (线段树合并+概率DP)
 *    - CF911G Mass Change Queries (线段树分裂+区间赋值)
 *    - P6773 [NOI2020]命运 (线段树合并+树形DP)
 * 
 * 6. 算法优化：
 *    - 使用动态开点避免空间浪费
 *    - 懒标记优化区间操作
 *    - 内存池管理节点分配
 */