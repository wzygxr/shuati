package class157;

import java.io.*;

/**
 * 范围修改的可持久化线段树，标记永久化减少空间占用，Java版
 * 
 * 题目来源: HDU 4348 To the moon
 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=4348
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
 * 一共有m条查询，每条查询为如下四种类型中的一种
 * C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
 * Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
 * H x y z : z版本的数组，打印[x..y]范围的累加和
 * B x     : 当前时间戳t设置成x
 * 
 * 解题思路:
 * 使用标记永久化技术实现可持久化线段树，以减少空间占用。
 * 1. 标记永久化是一种优化技巧，在处理区间更新时，不立即下传标记，
 *    而是在查询时根据路径上的标记计算结果
 * 2. 在更新时，只复制被修改路径上的节点，共享未修改的部分
 * 3. 通过标记永久化减少节点复制，从而减少空间占用
 * 4. sum数组存储的是考虑所有任务后的累加和，而不是真实累加和
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n、m <= 10^5
 * -10^9 <= arr[i] <= +10^9
 * 
 * 示例:
 * 输入:
 * 5 10
 * 5 6 7 8 9
 * Q 1 5
 * C 2 4 10
 * Q 1 5
 * H 1 5 0
 * B 3
 * Q 1 5
 * C 1 5 20
 * Q 1 5
 * H 1 5 3
 * Q 1 5
 * 
 * 输出:
 * 35
 * 55
 * 35
 * 55
 * 75
 * 55
 */
public class Code05_RangePersistentLessSpace2 {

    public static int MAXN = 100001;
    public static int MAXT = MAXN * 25;

    public static int n, m, t = 0;
    public static long[] arr = new long[MAXN];
    public static int[] root = new int[MAXN];
    public static int[] left = new int[MAXT];
    public static int[] right = new int[MAXT];
    
    // 不是真实累加和，而是之前的任务中
    // 不考虑被上方范围截住的任务，只考虑来到当前范围 或者 往下走的任务
    // 累加和变成了什么
    public static long[] sum = new long[MAXT];
    
    // 不再是懒更新信息，变成标记信息
    public static long[] addTag = new long[MAXT];
    public static int cnt = 0;

    /**
     * 构建线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    public static int build(int l, int r) {
        int rt = ++cnt;
        addTag[rt] = 0;
        if (l == r) {
            sum[rt] = arr[l];
        } else {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
            sum[rt] = sum[left[rt]] + sum[right[rt]];
        }
        return rt;
    }

    /**
     * 区间增加操作（标记永久化）
     * @param jobl 操作区间左端点
     * @param jobr 操作区间右端点
     * @param jobv 增加的值
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 新节点编号
     */
    public static int add(int jobl, int jobr, long jobv, int l, int r, int i) {
        int rt = ++cnt, a = Math.max(jobl, l), b = Math.min(jobr, r);
        left[rt] = left[i];
        right[rt] = right[i];
        sum[rt] = sum[i] + jobv * (b - a + 1);
        addTag[rt] = addTag[i];
        
        if (jobl <= l && r <= jobr) {
            addTag[rt] += jobv;
        } else {
            int mid = (l + r) / 2;
            if (jobl <= mid) {
                left[rt] = add(jobl, jobr, jobv, l, mid, left[rt]);
            }
            if (jobr > mid) {
                right[rt] = add(jobl, jobr, jobv, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }

    /**
     * 区间查询操作（标记永久化）
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param addHistory 历史标记累加值
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 区间和
     */
    public static long query(int jobl, int jobr, long addHistory, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i] + addHistory * (r - l + 1);
        }
        int mid = (l + r) / 2;
        long ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, addHistory + addTag[i], l, mid, left[i]);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, addHistory + addTag[i], mid + 1, r, right[i]);
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = in.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        line = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(line[i - 1]);
        }
        
        root[0] = build(1, n);
        
        for (int i = 1; i <= m; i++) {
            line = in.readLine().split(" ");
            String op = line[0];
            
            if (op.equals("C")) {
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                long z = Long.parseLong(line[3]);
                root[t + 1] = add(x, y, z, 1, n, root[t]);
                t++;
            } else if (op.equals("Q")) {
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                out.write(query(x, y, 0, 1, n, root[t]) + "\n");
            } else if (op.equals("H")) {
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                int z = Integer.parseInt(line[3]);
                out.write(query(x, y, 0, 1, n, root[z]) + "\n");
            } else {
                int x = Integer.parseInt(line[1]);
                t = x;
            }
        }
        
        out.flush();
        out.close();
        in.close();
    }
}