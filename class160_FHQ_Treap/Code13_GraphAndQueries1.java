package class152;

// FHQ-Treap实现Graph and Queries
// Codeforces Problem F. Graph and Queries
// 实现图相关的查询操作
// 测试链接 : https://codeforces.com/contest/1416/problem/F

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code13_GraphAndQueries1 {
    
    // 最大节点数
    public static int MAXN = 100001;
    
    // 最大边数
    public static int MAXM = 100001;
    
    // 并查集数组
    public static int[] parent = new int[MAXN];
    
    // FHQ Treap相关变量
    public static int head = 0;
    public static int cnt = 0;
    public static int[] key = new int[MAXN];
    public static int[] count = new int[MAXN];
    public static int[] left = new int[MAXN];
    public static int[] right = new int[MAXN];
    public static int[] size = new int[MAXN];
    public static double[] priority = new double[MAXN];
    
    // 边的结构
    static class Edge {
        int u, v, w;
        boolean deleted;
        
        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
            this.deleted = false;
        }
    }
    
    // 初始化并查集
    public static void initUnionFind(int n) {
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }
    
    // 查找根节点（路径压缩）
    public static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    // 合并两个集合
    public static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }
    
    // 初始化FHQ Treap
    public static void initFHQTreap() {
        head = 0;
        cnt = 0;
        Arrays.fill(key, 0);
        Arrays.fill(count, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + count[i];
    }
    
    // 按值分裂
    public static void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
            }
            up(i);
        }
    }
    
    // 合并操作
    public static int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            up(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            up(r);
            return r;
        }
    }
    
    // 查找值为num的节点
    public static int findNode(int i, int num) {
        if (i == 0) {
            return 0;
        }
        if (key[i] == num) {
            return i;
        } else if (key[i] > num) {
            return findNode(left[i], num);
        } else {
            return findNode(right[i], num);
        }
    }
    
    // 改变节点计数
    public static void changeCount(int i, int num, int change) {
        if (key[i] == num) {
            count[i] += change;
        } else if (key[i] > num) {
            changeCount(left[i], num, change);
        } else {
            changeCount(right[i], num, change);
        }
        up(i);
    }
    
    // 插入数值
    public static void insert(int num) {
        if (findNode(head, num) != 0) {
            changeCount(head, num, 1);
        } else {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            count[cnt] = size[cnt] = 1;
            priority[cnt] = Math.random();
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    // 删除数值
    public static void remove(int num) {
        int i = findNode(head, num);
        if (i != 0) {
            if (count[i] > 1) {
                changeCount(head, num, -1);
            } else {
                split(0, 0, head, num);
                int lm = right[0];
                int r = left[0];
                split(0, 0, lm, num - 1);
                int l = right[0];
                head = merge(l, r);
            }
        }
    }
    
    // 查询排名为x的数值
    public static int index(int i, int x) {
        if (size[left[i]] >= x) {
            return index(left[i], x);
        } else if (size[left[i]] + count[i] < x) {
            return index(right[i], x - size[left[i]] - count[i]);
        }
        return key[i];
    }
    
    // 查询最大值
    public static int queryMax() {
        if (head == 0) {
            return 0;
        }
        int i = head;
        while (right[i] != 0) {
            i = right[i];
        }
        return key[i];
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval; // 节点数
        in.nextToken();
        int m = (int) in.nval; // 边数
        
        // 读取节点权重
        int[] weights = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            weights[i] = (int) in.nval;
        }
        
        // 读取边
        Edge[] edges = new Edge[m + 1];
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            int w = (int) in.nval;
            edges[i] = new Edge(u, v, w);
        }
        
        in.nextToken();
        int q = (int) in.nval; // 查询数
        
        // 初始化并查集
        initUnionFind(n);
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            in.nextToken();
            int type = (int) in.nval;
            
            if (type == 1) {
                // 删除边
                in.nextToken();
                int edgeId = (int) in.nval;
                edges[edgeId].deleted = true;
            } else {
                // 查询连通分量最大权重
                in.nextToken();
                int nodeId = (int) in.nval;
                
                // 重新初始化FHQ Treap
                initFHQTreap();
                
                // 将与nodeId在同一连通分量的所有节点的权重插入到FHQ Treap中
                int root = find(nodeId);
                for (int j = 1; j <= n; j++) {
                    if (find(j) == root) {
                        insert(weights[j]);
                    }
                }
                
                // 查询最大权重
                int maxWeight = queryMax();
                out.println(maxWeight);
                
                // 从FHQ Treap中删除最大权重的节点
                if (maxWeight > 0) {
                    remove(maxWeight);
                    // 更新节点权重为0
                    for (int j = 1; j <= n; j++) {
                        if (find(j) == root && weights[j] == maxWeight) {
                            weights[j] = 0;
                            break;
                        }
                    }
                }
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}