package class157;

import java.io.*;
import java.util.*;

/**
 * LOJ 6280 数列分块入门4
 * 
 * 题目来源: LibreOJ 6280
 * 题目链接: https://loj.ac/p/6280
 * 
 * 题目描述:
 * 给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和。
 * 并且要求支持查询历史版本。
 * 
 * 解题思路:
 * 使用可持久化线段树解决带历史版本的区间加法和区间求和问题。
 * 1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
 * 2. 使用懒惰标记技术处理区间修改
 * 3. 通过clone函数实现节点的复制，确保历史版本的完整性
 * 4. 在需要下传懒惰标记时，先复制子节点再进行操作
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n, m <= 50000
 * 0 <= value[i] <= 10^9
 * 
 * 示例:
 * 输入:
 * 4
 * 1 2 2 3
 * 5
 * 1 1 3 2
 * 2 1 3
 * 1 2 3 1
 * 2 1 3
 * 2 2 4
 * 
 * 输出:
 * 7
 * 8
 * 6
 */
public class LOJ6280_BlockArray4 {

    public static int MAXN = 50010;
    public static int MAXT = MAXN * 50;

    public static int n, m, version = 0;
    public static long[] arr = new long[MAXN];
    public static int[] root = new int[MAXN];
    public static int[] left = new int[MAXT];
    public static int[] right = new int[MAXT];
    
    // 累加和信息
    public static long[] sum = new long[MAXT];
    
    // 懒更新信息，范围增加的懒更新
    public static long[] add = new long[MAXT];
    
    public static int cnt = 0;

    /**
     * 克隆节点
     * @param i 要克隆的节点编号
     * @return 新节点编号
     */
    public static int clone(int i) {
        int rt = ++cnt;
        left[rt] = left[i];
        right[rt] = right[i];
        sum[rt] = sum[i];
        add[rt] = add[i];
        return rt;
    }

    /**
     * 更新节点信息
     * @param i 节点编号
     */
    public static void up(int i) {
        sum[i] = sum[left[i]] + sum[right[i]];
    }

    /**
     * 懒更新操作
     * @param i 节点编号
     * @param v 增加的值
     * @param n 区间长度
     */
    public static void lazy(int i, long v, int n) {
        sum[i] += v * n;
        add[i] += v;
    }

    /**
     * 下传懒更新标记
     * @param i 节点编号
     * @param ln 左子区间长度
     * @param rn 右子区间长度
     */
    public static void down(int i, int ln, int rn) {
        if (add[i] != 0) {
            left[i] = clone(left[i]);
            right[i] = clone(right[i]);
            lazy(left[i], add[i], ln);
            lazy(right[i], add[i], rn);
            add[i] = 0;
        }
    }

    /**
     * 建立线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    public static int build(int l, int r) {
        int rt = ++cnt;
        add[rt] = 0;
        if (l == r) {
            sum[rt] = arr[l];
        } else {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
            up(rt);
        }
        return rt;
    }

    /**
     * 区间增加操作
     * @param jobl 操作区间左端点
     * @param jobr 操作区间右端点
     * @param jobv 增加的值
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 新节点编号
     */
    public static int add(int jobl, int jobr, long jobv, int l, int r, int i) {
        int rt = clone(i);
        if (jobl <= l && r <= jobr) {
            lazy(rt, jobv, r - l + 1);
        } else {
            int mid = (l + r) / 2;
            down(rt, mid - l + 1, r - mid);
            if (jobl <= mid) {
                left[rt] = add(jobl, jobr, jobv, l, mid, left[rt]);
            }
            if (jobr > mid) {
                right[rt] = add(jobl, jobr, jobv, mid + 1, r, right[rt]);
            }
            up(rt);
        }
        return rt;
    }

    /**
     * 区间查询操作
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 区间和
     */
    public static long query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) / 2;
        down(i, mid - l + 1, r - mid);
        long ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, left[i]);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, right[i]);
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(in.readLine());
        
        String[] line = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(line[i - 1]);
        }
        
        root[0] = build(1, n);
        
        m = Integer.parseInt(in.readLine());
        
        for (int i = 1; i <= m; i++) {
            line = in.readLine().split(" ");
            int op = Integer.parseInt(line[0]);
            
            if (op == 1) {
                // 区间增加操作
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                long z = Long.parseLong(line[3]);
                root[version + 1] = add(x, y, z, 1, n, root[version]);
                version++;
            } else {
                // 区间查询操作
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                long result = query(x, y, 1, n, root[version]);
                out.println(result % (1000000007L));
            }
        }
        
        out.flush();
        out.close();
        in.close();
    }
}