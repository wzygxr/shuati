package class114;

// POJ 2777 - Count Color
// 题目描述：
// 给定一个长度为L的板条(1 <= L <= 100000)，初始时所有位置都是颜色1
// 执行O次操作(1 <= O <= 100000)，操作类型：
// 1. "C A B C": 将区间[A,B]染成颜色C
// 2. "P A B": 查询区间[A,B]中有多少种不同的颜色
//
// 解题思路：
// 使用线段树维护区间信息，每个节点存储以下信息：
// 1. 区间颜色集合(用位运算表示，第i位为1表示有颜色i)
// 2. 懒惰标记(表示区间被染成的颜色)
//
// 关键技术：
// 1. 位运算优化：用一个整数的二进制位表示颜色集合，第i位为1表示有颜色i
// 2. 懒惰标记：延迟更新子区间
// 3. 区间染色：将整个区间染成同一种颜色
//
// 时间复杂度分析：
// 1. 建树：O(L)
// 2. 更新：O(log L)
// 3. 查询：O(log L)
// 4. 空间复杂度：O(L)
//
// 是否最优解：是
// 这是解决区间染色和颜色计数问题的最优解法，时间复杂度为O(log L)

import java.io.*;
import java.util.*;

public class Code08_CountColor {
    static final int MAXN = 100001;
    static final int MAX_COLOR = 31; // 颜色数不超过30，可以用int的位运算表示
    
    // 线段树节点信息
    static int[] color = new int[MAXN << 2]; // 区间颜色集合(位运算表示)
    static int[] lazy = new int[MAXN << 2];  // 懒惰标记
    
    // 计算一个整数二进制表示中1的个数
    static int countBits(int n) {
        int count = 0;
        while (n > 0) {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }
    
    // 向上更新节点信息
    static void pushUp(int rt) {
        color[rt] = color[rt << 1] | color[rt << 1 | 1];
    }
    
    // 向下传递懒惰标记
    static void pushDown(int rt) {
        if (lazy[rt] != 0) {
            lazy[rt << 1] = lazy[rt];
            lazy[rt << 1 | 1] = lazy[rt];
            color[rt << 1] = lazy[rt];
            color[rt << 1 | 1] = lazy[rt];
            lazy[rt] = 0;
        }
    }
    
    // 建立线段树
    static void build(int l, int r, int rt) {
        lazy[rt] = 0;
        if (l == r) {
            color[rt] = 1; // 初始颜色为1，用二进制表示第0位为1
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间染色操作
    static void update(int L, int R, int c, int l, int r, int rt) {
        if (L <= l && r <= R) {
            color[rt] = 1 << (c - 1); // 将第c位设为1
            lazy[rt] = 1 << (c - 1);
            return;
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, c, l, mid, rt << 1);
        if (R > mid) update(L, R, c, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 查询区间颜色数
    static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return countBits(color[rt]);
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        int res = 0;
        if (L <= mid) res |= query(L, R, l, mid, rt << 1);
        if (R > mid) res |= query(L, R, mid + 1, r, rt << 1 | 1);
        return countBits(res);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int L = Integer.parseInt(st.nextToken()); // 板条长度
        int T = Integer.parseInt(st.nextToken()); // 颜色数
        int O = Integer.parseInt(st.nextToken()); // 操作数
        
        // 建立线段树
        build(1, L, 1);
        
        // 处理每次操作
        for (int i = 0; i < O; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            if (op.equals("C")) {
                int A = Integer.parseInt(st.nextToken());
                int B = Integer.parseInt(st.nextToken());
                int C = Integer.parseInt(st.nextToken());
                // 确保A <= B
                if (A > B) {
                    int temp = A;
                    A = B;
                    B = temp;
                }
                update(A, B, C, 1, L, 1);
            } else { // "P"
                int A = Integer.parseInt(st.nextToken());
                int B = Integer.parseInt(st.nextToken());
                // 确保A <= B
                if (A > B) {
                    int temp = A;
                    A = B;
                    B = temp;
                }
                out.println(query(A, B, 1, L, 1));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}