package class114;

// POJ 3468 A Simple Problem with Integers - 区间加法与区间求和
// 题目描述：
// 给定一个长度为 N 的整数序列，执行以下操作：
// 1. C a b c: 将区间 [a,b] 中的每个数都加上 c
// 2. Q a b: 查询区间 [a,b] 中所有数的和
//
// 解题思路：
// 使用线段树 + 懒惰标记优化区间加法操作
// 每个节点存储区间和，使用懒惰标记延迟更新
//
// 关键技术：
// 1. 懒惰标记：延迟更新子区间，提高效率
// 2. pushDown操作：在需要时下传懒惰标记
// 3. pushUp操作：合并子区间信息
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 区间更新：O(log n)
// 3. 区间查询：O(log n)
// 4. 空间复杂度：O(n)
//
// 是否最优解：是
// 这是解决区间加法与区间求和问题的最优解法

import java.io.*;
import java.util.*;

public class Code11_RangeAddQuery {
    
    static class SegmentTree {
        static final int MAXN = 100005;
        long[] sum = new long[MAXN << 2];  // 区间和
        long[] lazy = new long[MAXN << 2]; // 懒惰标记
        
        // 向上更新节点信息
        void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }
        
        // 向下传递懒惰标记
        void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                // 更新左子树
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                
                // 更新右子树
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                
                // 清除当前节点的懒惰标记
                lazy[rt] = 0;
            }
        }
        
        // 建立线段树
        void build(int[] arr, int l, int r, int rt) {
            lazy[rt] = 0;
            if (l == r) {
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(arr, l, mid, rt << 1);
            build(arr, mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }
        
        // 区间加法操作
        void update(int L, int R, long val, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] += val * (r - l + 1);
                lazy[rt] += val;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            
            if (L <= mid) {
                update(L, R, val, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, val, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }
        
        // 区间查询操作
        long query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            
            long res = 0;
            if (L <= mid) {
                res += query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                res += query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return res;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());
        
        int[] arr = new int[N + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        SegmentTree segTree = new SegmentTree();
        segTree.build(arr, 1, N, 1);
        
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            
            if (op.equals("C")) {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                long c = Long.parseLong(st.nextToken());
                segTree.update(a, b, c, 1, N, 1);
            } else {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                out.println(segTree.query(a, b, 1, N, 1));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}