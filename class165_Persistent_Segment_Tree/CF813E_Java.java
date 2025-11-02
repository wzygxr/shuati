package class158;

// Problem: Codeforces 813E - Army Creation
// Link: https://codeforces.com/contest/813/problem/E
// Description: 给定一个数组，每次查询区间[l,r]中最多能选出多少个数，使得每个数出现次数不超过k
// Solution: 使用可持久化线段树解决区间限制计数问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

import java.io.*;
import java.util.*;

public class CF813E_Java {
    static final int MAXN = 100005;
    static int[] a = new int[MAXN];           // 原始数组
    static int[] prev = new int[MAXN];        // 每个元素前k次出现的位置
    static int[] last = new int[MAXN];        // 每个值最后出现的位置
    static int n, k;
    
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
    
    // 插入位置到主席树
    static int insert(int pre, int l, int r, int pos, int val) {
        int now = createNode(0, 0, 0);
        T[now].sum = T[pre].sum + val;
        
        if (l == r) return now;
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            T[now].l = insert(T[pre].l, l, mid, pos, val);
            T[now].r = T[pre].r;
        } else {
            T[now].l = T[pre].l;
            T[now].r = insert(T[pre].r, mid + 1, r, pos, val);
        }
        return now;
    }
    
    // 查询区间和
    static int query(int u, int v, int l, int r, int L, int R) {
        if (L <= l && r <= R) return T[v].sum - T[u].sum;
        if (L > r || R < l) return 0;
        
        int mid = (l + r) >> 1;
        return query(T[u].l, T[v].l, l, mid, L, R) + 
               query(T[u].r, T[v].r, mid + 1, r, L, R);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        k = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 预处理prev数组
        Arrays.fill(last, 0);
        Arrays.fill(prev, 0);
        
        for (int i = 1; i <= n; i++) {
            if (last[a[i]] != 0) {
                prev[i] = last[a[i]];
            }
            last[a[i]] = i;
        }
        
        // 构建主席树
        root[0] = createNode(0, 0, 0);
        T[root[0]].l = T[root[0]].r = T[root[0]].sum = 0;
        
        for (int i = 1; i <= n; i++) {
            if (prev[i] == 0) {
                // 第一次出现，在位置i加1
                root[i] = insert(root[i - 1], 1, n, i, 1);
            } else {
                // 不是第一次出现，先在prev[i]位置减1，再在i位置加1
                int temp = insert(root[i - 1], 1, n, prev[i], -1);
                root[i] = insert(temp, 1, n, i, 1);
            }
        }
        
        int q = Integer.parseInt(br.readLine());
        int last_ans = 0;
        
        for (int i = 0; i < q; i++) {
            line = br.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            
            // 解密
            l = (l + last_ans) % n + 1;
            r = (r + last_ans) % n + 1;
            
            if (l > r) {
                int temp = l;
                l = r;
                r = temp;
            }
            
            last_ans = query(root[l - 1], root[r], 1, n, l, r);
            out.println(last_ans);
        }
        
        out.close();
    }
}