package class157;

import java.io.*;
import java.util.*;

/**
 * SPOJ DQUERY - D-query
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。
 * 
 * 解题思路:
 * 使用可持久化线段树解决区间不同元素个数问题。
 * 1. 对于每个位置i，记录上一次出现相同数字的位置last[i]
 * 2. 对于每个位置i，建立线段树，将位置i处的值设为1，位置last[i]处的值设为0
 * 3. 查询区间[l,r]时，查询第r个版本的线段树在区间[l,r]上的和
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5
 * 1 1 2 1 3
 * 3
 * 1 5
 * 2 4
 * 3 5
 * 
 * 输出:
 * 3
 * 2
 * 3
 */
public class Code06_DQUERY {
    static final int MAXN = 30010;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    // 记录每个数字上一次出现的位置
    static Map<Integer, Integer> last = new HashMap<>();
    // 每个版本线段树的根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 50];
    static int[] right = new int[MAXN * 50];
    static int[] sum = new int[MAXN * 50];
    
    // 线段树节点计数器
    static int cnt = 0;
    
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
     * 更新线段树中的一个位置
     * @param pos 要更新的位置
     * @param val 更新的值
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int update(int pos, int val, int l, int r, int pre) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        
        if (l == r) {
            sum[rt] = val;
            return rt;
        }
        
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = update(pos, val, l, mid, left[rt]);
        } else {
            right[rt] = update(pos, val, mid + 1, r, right[rt]);
        }
        sum[rt] = sum[left[rt]] + sum[right[rt]];
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
        
        int n = Integer.parseInt(reader.readLine());
        
        // 读取原始数组
        String[] line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 构建空线段树
        root[0] = build(1, n);
        
        // 构建主席树
        for (int i = 1; i <= n; i++) {
            int val = arr[i];
            // 先将当前位置设为1
            root[i] = update(i, 1, 1, n, root[i - 1]);
            // 如果这个数字之前出现过，将之前位置设为0
            if (last.containsKey(val)) {
                int pos = last.get(val);
                root[i] = update(pos, 0, 1, n, root[i]);
            }
            last.put(val, i);
        }
        
        int q = Integer.parseInt(reader.readLine());
        // 处理查询
        for (int i = 0; i < q; i++) {
            line = reader.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            int ans = query(l, r, 1, n, root[r]);
            writer.println(ans);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}