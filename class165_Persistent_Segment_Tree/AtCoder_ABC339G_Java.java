package class158;

// Problem: AtCoder ABC339 G - Smaller Sum
// Link: https://atcoder.jp/contests/abc339/tasks/abc339_g
// Description: 给定一个数组，每次在线查询区间[l,r]中不超过x的元素和
// Solution: 使用可持久化线段树解决在线区间和查询问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

import java.io.*;
import java.util.*;

public class AtCoder_ABC339G_Java {
    static final int MAXN = 200005;
    static long[] a = new long[MAXN];         // 原始数组
    static long[] b = new long[MAXN];         // 离散化数组
    static int n, q;
    
    // 主席树节点
    static class Node {
        int l, r;
        long sum;
        Node(int l, int r, long sum) {
            this.l = l;
            this.r = r;
            this.sum = sum;
        }
    }
    
    static Node[] T = new Node[40 * MAXN];    // 主席树节点数组
    static int[] root = new int[MAXN];        // 每个版本的根节点
    static int cnt = 0;                       // 节点计数器
    
    // 创建新节点
    static int createNode(int l, int r, long sum) {
        T[++cnt] = new Node(l, r, sum);
        return cnt;
    }
    
    // 插入值到主席树
    static int insert(int pre, int l, int r, int val) {
        int now = createNode(0, 0, 0);
        T[now].sum = T[pre].sum + b[val];
        
        if (l == r) return now;
        
        int mid = (l + r) >> 1;
        if (val <= mid) {
            T[now].l = insert(T[pre].l, l, mid, val);
            T[now].r = T[pre].r;
        } else {
            T[now].l = T[pre].l;
            T[now].r = insert(T[pre].r, mid + 1, r, val);
        }
        return now;
    }
    
    // 查询区间中小于等于val的元素和
    static long query(int u, int v, int l, int r, int val) {
        if (l == r) {
            if (l <= val) return T[v].sum - T[u].sum;
            else return 0;
        }
        
        int mid = (l + r) >> 1;
        if (val <= mid) {
            return query(T[u].l, T[v].l, l, mid, val);
        } else {
            long leftSum = T[T[v].l].sum - T[T[u].l].sum;
            return leftSum + query(T[u].r, T[v].r, mid + 1, r, val);
        }
    }
    
    // 离散化
    static int getId(long x) {
        return Arrays.binarySearch(b, 1, n + 1, x) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        n = Integer.parseInt(br.readLine());
        
        String[] line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Long.parseLong(line[i - 1]);
            b[i] = a[i];
        }
        
        // 离散化
        Arrays.sort(b, 1, n + 1);
        int sz = 1;
        for (int i = 2; i <= n; i++) {
            if (b[i] != b[i - 1]) {
                b[++sz] = b[i];
            }
        }
        
        // 构建主席树
        root[0] = createNode(0, 0, 0);
        T[root[0]].l = 0;
        T[root[0]].r = 0;
        T[root[0]].sum = 0;
        
        for (int i = 1; i <= n; i++) {
            root[i] = insert(root[i - 1], 1, sz, getId(a[i]));
        }
        
        q = Integer.parseInt(br.readLine());
        long last_ans = 0;
        
        for (int i = 0; i < q; i++) {
            line = br.readLine().split(" ");
            long alpha = Long.parseLong(line[0]);
            long beta = Long.parseLong(line[1]);
            long gamma = Long.parseLong(line[2]);
            
            // 解密
            int l = (int)((alpha ^ last_ans) % n + 1);
            int r = (int)((beta ^ last_ans) % n + 1);
            long x = gamma ^ last_ans;
            
            if (l > r) {
                int temp = l;
                l = r;
                r = temp;
            }
            
            last_ans = query(root[l - 1], root[r], 1, sz, getId(x));
            out.println(last_ans);
        }
        
        out.close();
    }
}