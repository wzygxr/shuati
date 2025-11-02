package class157;

import java.io.*;
import java.util.*;

/**
 * Codeforces 813E - Army Creation
 * 
 * 题目描述:
 * 给定一个长度为n的数组和k，有q个查询，每个查询给出l和r，
 * 要求在区间[l,r]中最多能选出多少个数，使得每种数字最多选k个。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合贪心策略解决带限制的区间元素选择问题。
 * 1. 对于每个位置i，预处理出从位置i开始，每种数字最多选k个时能选到的最远位置
 * 2. 对于每个查询[l,r]，在预处理的基础上使用主席树进行区间查询
 * 3. 使用贪心策略，尽可能多地选择满足条件的数字
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 2
 * 1 2 1 2 3
 * 3
 * 1 3
 * 2 4
 * 1 5
 * 
 * 输出:
 * 3
 * 3
 * 5
 */
public class Codeforces_813E {
    static final int MAXN = 100010;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    // 记录每种数字出现的位置
    static List<Integer>[] positions = new List[MAXN];
    // 每个版本线段树的根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20];
    
    // 线段树节点计数器
    static int cnt = 0;
    
    static {
        for (int i = 0; i < MAXN; i++) {
            positions[i] = new ArrayList<>();
        }
    }
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++cnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的位置
     * @param val 插入的值
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int insert(int pos, int val, int l, int r, int pre) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + val;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, val, l, mid, left[rt]);
            } else {
                right[rt] = insert(pos, val, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }
    
    /**
     * 查询区间和
     * @param L 查询区间左端点
     * @param R 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param rt 当前节点编号
     * @return 区间和
     */
    static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        
        int mid = (l + r) / 2;
        int ans = 0;
        if (L <= mid) ans += query(L, R, l, mid, left[rt]);
        if (R > mid) ans += query(L, R, mid + 1, r, right[rt]);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int k = Integer.parseInt(line[1]);
        
        // 读取原始数组
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
            positions[arr[i]].add(i);
        }
        
        // 构建主席树
        root[0] = build(1, n);
        
        // 预处理：对于每个位置i，计算从该位置开始最多能选多少个数
        int[] next = new int[n + 2]; // next[i]表示位置i之后第一个不能选的位置
        for (int i = 1; i <= n; i++) {
            next[i] = n + 1; // 初始化为n+1，表示可以选到末尾
        }
        
        // 对每种数字，计算其限制位置
        for (int i = 1; i < MAXN; i++) {
            if (positions[i].size() > k) {
                // 如果数字i出现次数超过k，需要限制
                for (int j = 0; j <= positions[i].size() - k - 1; j++) {
                    // 从第j个位置开始，第j+k个位置就是限制位置
                    int start = positions[i].get(j);
                    int limit = positions[i].get(j + k);
                    next[start] = Math.min(next[start], limit);
                }
            }
        }
        
        // 构建主席树，维护next数组的信息
        for (int i = 1; i <= n; i++) {
            root[i] = insert(i, next[i], 1, n, root[i - 1]);
        }
        
        int q = Integer.parseInt(reader.readLine());
        // 处理查询
        for (int i = 0; i < q; i++) {
            line = reader.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            
            // 贪心策略：尽可能多地选择满足条件的数字
            int ans = 0;
            int pos = l;
            while (pos <= r) {
                // 查询位置pos的next值
                int nextPos = query(pos, pos, 1, n, root[pos]);
                if (nextPos > r) {
                    // 可以选到r位置
                    ans += r - pos + 1;
                    break;
                } else {
                    // 只能选到nextPos-1位置
                    ans += nextPos - pos;
                    pos = nextPos;
                }
            }
            
            writer.println(ans);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}