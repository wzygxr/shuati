package class158;

// Problem: CodeChef CLONEME - Cloning
// Link: https://www.codechef.com/JUNE17/problems/CLONEME
// Description: 给定一个数组，每次查询两个区间是否可以通过重新排列变得相同
// Solution: 使用可持久化线段树和哈希技术解决区间相等性查询问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

import java.io.*;
import java.util.*;

public class CodeChef_CLONEME_Java {
    static final int MAXN = 100005;
    static int[] a = new int[MAXN];           // 原始数组
    static int[] b = new int[MAXN];           // 离散化数组
    static int n, q;
    
    // 主席树节点
    static class Node {
        int l, r, sum;
        long hash;
        Node(int l, int r, int sum, long hash) {
            this.l = l;
            this.r = r;
            this.sum = sum;
            this.hash = hash;
        }
    }
    
    static Node[] T = new Node[40 * MAXN];    // 主席树节点数组
    static int[] root = new int[MAXN];        // 每个版本的根节点
    static int cnt = 0;                       // 节点计数器
    
    // 创建新节点
    static int createNode(int l, int r, int sum, long hash) {
        T[++cnt] = new Node(l, r, sum, hash);
        return cnt;
    }
    
    // 插入值到主席树
    static int insert(int pre, int l, int r, int val) {
        int now = createNode(0, 0, 0, 0);
        T[now].sum = T[pre].sum + 1;
        T[now].hash = T[pre].hash + (long)val * val;
        
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
    
    // 查询区间信息
    static class Result {
        int sum;
        long hash;
        Result(int sum, long hash) {
            this.sum = sum;
            this.hash = hash;
        }
    }
    
    static Result query(int u, int v, int l, int r, int L, int R) {
        if (L <= l && r <= R) return new Result(T[v].sum - T[u].sum, T[v].hash - T[u].hash);
        if (L > r || R < l) return new Result(0, 0);
        
        int mid = (l + r) >> 1;
        Result left = query(T[u].l, T[v].l, l, mid, L, R);
        Result right = query(T[u].r, T[v].r, mid + 1, r, L, R);
        
        return new Result(left.sum + right.sum, left.hash + right.hash);
    }
    
    // 离散化
    static int getId(int x) {
        return Arrays.binarySearch(b, 1, n + 1, x) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int t = Integer.parseInt(br.readLine());
        
        while (t-- > 0) {
            String[] line = br.readLine().split(" ");
            n = Integer.parseInt(line[0]);
            q = Integer.parseInt(line[1]);
            
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
            cnt = 0; // 重置计数器
            root[0] = createNode(0, 0, 0, 0);
            T[root[0]].l = T[root[0]].r = T[root[0]].sum = 0;
            T[root[0]].hash = 0;
            
            for (int i = 1; i <= n; i++) {
                root[i] = insert(root[i - 1], 1, sz, getId(a[i]));
            }
            
            // 处理查询
            for (int i = 0; i < q; i++) {
                line = br.readLine().split(" ");
                int l1 = Integer.parseInt(line[0]);
                int r1 = Integer.parseInt(line[1]);
                int l2 = Integer.parseInt(line[2]);
                int r2 = Integer.parseInt(line[3]);
                
                Result res1 = query(root[l1 - 1], root[r1], 1, sz, 1, sz);
                Result res2 = query(root[l2 - 1], root[r2], 1, sz, 1, sz);
                
                if (res1.sum == res2.sum && res1.hash == res2.hash) {
                    out.println("YES");
                } else {
                    out.println("NO");
                }
            }
        }
        
        out.close();
    }
}