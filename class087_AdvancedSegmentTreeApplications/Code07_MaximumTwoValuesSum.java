package class114;

// SPOJ KGSS - Maximum Sum
// 题目描述：
// 给定一个长度为n的整数序列，执行m次操作
// 操作类型：
// 1. U i x: 将第i个位置的值更新为x
// 2. Q l r: 查询[l,r]区间内两个最大值的和
//
// 解题思路：
// 使用线段树维护区间信息，每个节点存储以下信息：
// 1. 区间最大值(max1)
// 2. 区间次大值(max2)
//
// 合并两个子区间[l,mid]和[mid+1,r]的信息：
// 1. 区间最大值 = max(左区间max1, 右区间max1)
// 2. 区间次大值 = max(左区间max2, 右区间max2, min(左区间max1, 右区间max1))
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 更新：O(log n)
// 3. 查询：O(log n)
// 4. 空间复杂度：O(n)
//
// 是否最优解：是
// 这是解决区间两个最大值之和查询问题的最优解法，时间复杂度为O(log n)

import java.io.*;
import java.util.*;

public class Code07_MaximumTwoValuesSum {
    // 节点信息类
    static class Node {
        int max1; // 区间最大值
        int max2; // 区间次大值
        
        Node() {}
        
        Node(int max1, int max2) {
            this.max1 = max1;
            this.max2 = max2;
        }
    }
    
    static final int MAXN = 100001;
    static int[] arr = new int[MAXN];
    static Node[] tree = new Node[MAXN << 2];
    
    // 合并两个子节点的信息
    static Node pushUp(Node left, Node right) {
        int max1 = Math.max(left.max1, right.max1);
        int max2 = Math.max(Math.max(left.max2, right.max2), Math.min(left.max1, right.max1));
        return new Node(max1, max2);
    }
    
    // 建立线段树
    static void build(int l, int r, int i) {
        tree[i] = new Node();
        if (l == r) {
            tree[i].max1 = arr[l];
            tree[i].max2 = Integer.MIN_VALUE;
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
    }
    
    // 更新第idx个位置的值为val
    static void update(int idx, int val, int l, int r, int i) {
        if (l == r) {
            tree[i].max1 = val;
            tree[i].max2 = Integer.MIN_VALUE;
            return;
        }
        int mid = (l + r) >> 1;
        if (idx <= mid) {
            update(idx, val, l, mid, i << 1);
        } else {
            update(idx, val, mid + 1, r, i << 1 | 1);
        }
        tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
    }
    
    // 查询区间[l,r]内两个最大值的和
    static Node query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i];
        }
        int mid = (l + r) >> 1;
        if (jobr <= mid) {
            return query(jobl, jobr, l, mid, i << 1);
        } else if (jobl > mid) {
            return query(jobl, jobr, mid + 1, r, i << 1 | 1);
        } else {
            Node left = query(jobl, jobr, l, mid, i << 1);
            Node right = query(jobl, jobr, mid + 1, r, i << 1 | 1);
            return pushUp(left, right);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取序列长度
        int n = Integer.parseInt(br.readLine());
        
        // 读取序列元素
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 建立线段树
        build(1, n, 1);
        
        // 读取操作次数
        int m = Integer.parseInt(br.readLine());
        
        // 处理每次操作
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            if (op.equals("U")) {
                int idx = Integer.parseInt(st.nextToken());
                int val = Integer.parseInt(st.nextToken());
                update(idx, val, 1, n, 1);
            } else {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                Node result = query(l, r, 1, n, 1);
                out.println(result.max1 + result.max2);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}