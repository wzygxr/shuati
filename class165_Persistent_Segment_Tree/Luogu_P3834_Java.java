package class158;

// Problem: 洛谷P3834 - 【模板】可持久化线段树2 (静态区间第k小)
// Link: https://www.luogu.com.cn/problem/P3834
// Description: 给定一个包含n个数字的序列，每次查询区间[l,r]中第k小的数
// Solution: 使用可持久化线段树(主席树)解决静态区间第k小问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

import java.io.*;
import java.util.*;

public class Luogu_P3834_Java {
    static final int MAXN = 200005;
    static int[] a = new int[MAXN];           // 原始数组
    static int[] b = new int[MAXN];           // 离散化数组
    static int n, m;
    
    // 主席树节点
    static class Node {
        int l, r, sum;
        Node(int l, int r, int sum) {
            this.l = l;
            this.r = r;
            this.sum = sum;
        }
    }
    
    static Node[] T = new Node[40 * MAXN];    // 主席树节点数组
    static int[] root = new int[MAXN];        // 每个版本的根节点
    static int cnt = 0;                       // 节点计数器
    
    // 创建新节点
    static int createNode(int l, int r, int sum) {
        T[++cnt] = new Node(l, r, sum);
        return cnt;
    }
    
    // 插入值到主席树
    static int insert(int pre, int l, int r, int val) {
        int now = createNode(0, 0, 0);
        T[now].sum = T[pre].sum + 1;
        
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
    
    // 查询区间第k小
    static int query(int u, int v, int l, int r, int k) {
        if (l == r) return l;
        
        int mid = (l + r) >> 1;
        int x = T[T[v].l].sum - T[T[u].l].sum;
        
        if (k <= x) {
            return query(T[u].l, T[v].l, l, mid, k);
        } else {
            return query(T[u].r, T[v].r, mid + 1, r, k - x);
        }
    }
    
    // 离散化
    static int getId(int x) {
        return Arrays.binarySearch(b, 1, n + 1, x) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(line[i - 1]);
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
        T[root[0]].l = T[root[0]].r = T[root[0]].sum = 0;
        
        for (int i = 1; i <= n; i++) {
            root[i] = insert(root[i - 1], 1, sz, getId(a[i]));
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            
            int id = query(root[l - 1], root[r], 1, sz, k);
            out.println(b[id]);
        }
        
        out.close();
    }
}