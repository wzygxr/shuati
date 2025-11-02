package class175;

// Little Elephant and Array问题 - Mo算法实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/220/B
// 题目大意: 给定一个长度为n的数组arr，有m次查询
// 每次查询[l,r]区间内，有多少个数x满足在该区间内x恰好出现了x次
// 约束条件: 
// 1 <= n, m <= 10^5
// 1 <= arr[i] <= 10^9

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class Code09_LittleElephantAndArray1 {

    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, m: 查询次数, blen: 块大小
    public static int n, m, blen;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // ans: 存储每个查询的答案
    public static int[] ans = new int[MAXN];
    
    // count: 记录当前窗口中每个数字的出现次数
    public static HashMap<Integer, Integer> count = new HashMap<>();

    // 查询结构
    static class Query {
        int l, r, id;

        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }

    // queries: 存储所有查询
    public static Query[] queries = new Query[MAXN];

    /**
     * 添加元素到当前窗口
     * @param pos 位置
     */
    public static void add(int pos) {
        int val = arr[pos];
        int oldCount = count.getOrDefault(val, 0);
        
        // 如果之前恰好出现了val次，现在要减少一个计数
        if (oldCount == val) {
            ans[0]--;
        }
        
        // 更新计数
        count.put(val, oldCount + 1);
        
        // 如果现在恰好出现了val次，增加一个计数
        if (count.get(val) == val) {
            ans[0]++;
        }
    }

    /**
     * 从当前窗口移除元素
     * @param pos 位置
     */
    public static void remove(int pos) {
        int val = arr[pos];
        int oldCount = count.get(val);
        
        // 如果之前恰好出现了val次，现在要减少一个计数
        if (oldCount == val) {
            ans[0]--;
        }
        
        // 更新计数
        count.put(val, oldCount - 1);
        
        // 如果现在恰好出现了val次，增加一个计数
        if (count.get(val) == val) {
            ans[0]++;
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和查询次数m
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }

        // 读取所有查询
        for (int i = 1; i <= m; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            queries[i] = new Query(l, r, i);
        }

        // 使用Mo算法进行排序
        // 按照左端点所在的块排序，块内按照右端点排序
        blen = (int) Math.sqrt(n);
        Arrays.sort(queries, 1, m + 1, (a, b) -> {
            int blockA = (a.l - 1) / blen;
            int blockB = (b.l - 1) / blen;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });

        // Mo算法处理
        int curL = 1, curR = 0;
        for (int i = 1; i <= m; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int id = queries[i].id;

            // 扩展右边界
            while (curR < r) {
                curR++;
                add(curR);
            }

            // 收缩左边界
            while (curL > l) {
                curL--;
                add(curL);
            }

            // 收缩右边界
            while (curR > r) {
                remove(curR);
                curR--;
            }

            // 扩展左边界
            while (curL < l) {
                remove(curL);
                curL++;
            }

            // 记录当前查询的答案
            ans[id] = ans[0];
        }

        // 输出所有查询的结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }

        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}