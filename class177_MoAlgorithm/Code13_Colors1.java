package class179;

// 数颜色 - 带修莫队算法实现 (Java版本)
// 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题目大意: 给定一个长度为n的序列，支持两种操作：
// 1. 修改某个位置的颜色
// 2. 查询区间[l,r]内有多少种不同的颜色
// 解题思路: 使用带修莫队算法，增加时间维度处理修改操作
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 2. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
//
// 3. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
//
// 4. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 5. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 6. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 7. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 8. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 9. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 10. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py

import java.io.*;
import java.util.*;

public class Code13_Colors1 {
    public static int MAXN = 10010;
    public static int MAXM = 10010;
    public static int n, m, cntq, cntm; // cntq: 查询数, cntm: 修改数
    public static int[] arr = new int[MAXN]; // 原数组
    public static int[] bi = new int[MAXN];
    
    // 查询操作: l, r, 时间戳, id
    public static int[][] query = new int[MAXM][4];
    // 修改操作: 位置, 原值, 新值
    public static int[][] modify = new int[MAXM][3];
    
    public static int[] cnt = new int[1000010]; // 记录每种颜色的出现次数
    public static int curAns = 0; // 当前区间的答案
    public static int[] ans = new int[MAXM]; // 存储答案

    // 查询排序比较器
    public static class QueryCmp implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            if (bi[a[0]] != bi[b[0]]) {
                return bi[a[0]] - bi[b[0]];
            }
            if (bi[a[1]] != bi[b[1]]) {
                return bi[a[1]] - bi[b[1]];
            }
            return a[2] - b[2]; // 按时间戳排序
        }
    }

    // 添加元素到区间
    public static void add(int color) {
        if (cnt[color] == 0) {
            curAns++;
        }
        cnt[color]++;
    }

    // 从区间中删除元素
    public static void del(int color) {
        cnt[color]--;
        if (cnt[color] == 0) {
            curAns--;
        }
    }

    // 应用修改操作
    public static void apply(int time, int l, int r) {
        int pos = modify[time][0];
        int oldColor = modify[time][1];
        int newColor = modify[time][2];
        
        // 如果修改位置在当前查询区间内，需要更新答案
        if (pos >= l && pos <= r) {
            del(oldColor);
            add(newColor);
        }
        arr[pos] = newColor;
    }

    // 撤销修改操作
    public static void undo(int time, int l, int r) {
        int pos = modify[time][0];
        int oldColor = modify[time][1];
        int newColor = modify[time][2];
        
        // 如果修改位置在当前查询区间内，需要更新答案
        if (pos >= l && pos <= r) {
            del(newColor);
            add(oldColor);
        }
        arr[pos] = oldColor;
    }

    // 计算查询结果
    public static void compute() {
        int winl = 1, winr = 0, now = 0; // now: 当前处理到第几个修改操作
        for (int i = 1; i <= cntq; i++) {
            int jobl = query[i][0]; // 目标区间左端点
            int jobr = query[i][1]; // 目标区间右端点
            int jobt = query[i][2]; // 目标时间戳
            int id = query[i][3];   // 查询编号
            
            // 处理时间维度
            while (now < jobt) {
                now++;
                apply(now, winl, winr);
            }
            while (now > jobt) {
                undo(now, winl, winr);
                now--;
            }
            
            // 扩展左边界
            while (winl > jobl) {
                add(arr[--winl]);
            }
            
            // 扩展右边界
            while (winr < jobr) {
                add(arr[++winr]);
            }
            
            // 收缩左边界
            while (winl < jobl) {
                del(arr[winl++]);
            }
            
            // 收缩右边界
            while (winr > jobr) {
                del(arr[winr--]);
            }
            
            ans[id] = curAns;
        }
    }

    // 预处理
    public static void prepare() {
        int blen = (int) Math.pow(n, 2.0 / 3.0); // 带修莫队的块大小
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        // 只对查询排序
        Arrays.sort(query, 1, cntq + 1, new QueryCmp());
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        n = in.nextInt();
        m = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        for (int i = 1; i <= m; i++) {
            char op = in.nextChar();
            if (op == 'Q') { // 查询操作
                cntq++;
                query[cntq][0] = in.nextInt(); // l
                query[cntq][1] = in.nextInt(); // r
                query[cntq][2] = cntm;         // 时间戳
                query[cntq][3] = cntq;         // id
            } else { // 修改操作
                cntm++;
                int pos = in.nextInt();
                int color = in.nextInt();
                modify[cntm][0] = pos;
                modify[cntm][1] = arr[pos];
                modify[cntm][2] = color;
                arr[pos] = color;
            }
        }
        
        prepare();
        compute();
        
        for (int i = 1; i <= cntq; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private final InputStream in;
        private final byte[] buffer;
        private int ptr, len;

        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        private boolean hasNextByte() throws IOException {
            if (ptr < len)
                return true;
            ptr = 0;
            len = in.read(buffer);
            return len > 0;
        }

        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
            return buffer[ptr++];
        }

        char nextChar() throws IOException {
            byte c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            return (char) c;
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