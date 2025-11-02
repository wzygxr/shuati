package class114;

// SPOJ GSS1 - Can you answer these queries I
// 题目描述：
// 给定一个长度为n的整数序列，执行m次查询操作
// 每次查询[l,r]区间内的最大子段和
// 最大子段和：在给定区间内找到连续子序列，使得其元素和最大
//
// 解题思路：
// 使用线段树维护区间信息，每个节点存储以下信息：
// 1. 区间最大子段和(maxSum)
// 2. 区间从左端点开始的最大子段和(lSum)
// 3. 区间到右端点结束的最大子段和(rSum)
// 4. 区间总和(sum)
//
// 合并两个子区间[l,mid]和[mid+1,r]的信息：
// 1. 区间总和 = 左区间总和 + 右区间总和
// 2. 区间从左端点开始的最大子段和 = max(左区间lSum, 左区间sum + 右区间lSum)
// 3. 区间到右端点结束的最大子段和 = max(右区间rSum, 右区间sum + 左区间rSum)
// 4. 区间最大子段和 = max(左区间maxSum, 右区间maxSum, 左区间rSum + 右区间lSum)
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 查询：O(log n)
// 3. 空间复杂度：O(n)
//
// 是否最优解：是
// 这是解决最大子段和区间查询问题的最优解法，时间复杂度为O(log n)

import java.io.*;
import java.util.*;

public class Code06_MaximumSubarraySum {
    // 节点信息类
    static class Node {
        int maxSum; // 区间最大子段和
        int lSum;   // 区间从左端点开始的最大子段和
        int rSum;   // 区间到右端点结束的最大子段和
        int sum;    // 区间总和
        
        Node() {}
        
        Node(int maxSum, int lSum, int rSum, int sum) {
            this.maxSum = maxSum;
            this.lSum = lSum;
            this.rSum = rSum;
            this.sum = sum;
        }
    }
    
    static final int MAXN = 50001;
    static int[] arr = new int[MAXN];
    static Node[] tree = new Node[MAXN << 2];
    
    // 合并两个子节点的信息
    static Node pushUp(Node left, Node right) {
        int sum = left.sum + right.sum;
        int lSum = Math.max(left.lSum, left.sum + right.lSum);
        int rSum = Math.max(right.rSum, right.sum + left.rSum);
        int maxSum = Math.max(Math.max(left.maxSum, right.maxSum), left.rSum + right.lSum);
        return new Node(maxSum, lSum, rSum, sum);
    }
    
    // 建立线段树
    static void build(int l, int r, int i) {
        tree[i] = new Node();
        if (l == r) {
            tree[i].maxSum = tree[i].lSum = tree[i].rSum = tree[i].sum = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
    }
    
    // 查询区间[l,r]的最大子段和
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
        
        // 读取查询次数
        int m = Integer.parseInt(br.readLine());
        
        // 处理每次查询
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            out.println(query(l, r, 1, n, 1).maxSum);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}