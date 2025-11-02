// D-query - 普通莫队算法实现 (Java版本)
// 题目来源: SPOJ DQUERY - D-query
// 题目链接: https://www.spoj.com/problems/DQUERY/
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少种不同的数字
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 2. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 3. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 5. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 6. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 7. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 8. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py
//
// 9. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py
//
// 10. Codeforces 1000F One Occurrence - https://codeforces.com/problemset/problem/1000/F
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery3.py

package class179;

import java.io.*;
import java.util.*;

public class Code14_DQuery1 {
    public static int MAXN = 30010;
    public static int MAXV = 1000010;
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[][] query = new int[MAXN][3];
    public static int[] bi = new int[MAXN];
    public static int[] cnt = new int[MAXV]; // 记录每种数值的出现次数
    public static int curAns = 0; // 当前区间的答案（不同数字的个数）
    public static int[] ans = new int[MAXN]; // 存储答案

    // 查询排序比较器
    public static class QueryCmp implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            if (bi[a[0]] != bi[b[0]]) {
                return bi[a[0]] - bi[b[0]];
            }
            if ((bi[a[0]] & 1) == 1) {
                return a[1] - b[1];
            } else {
                return b[1] - a[1];
            }
        }
    }

    // 添加元素到区间
    public static void add(int value) {
        // 如果这是该数值第一次出现，则不同数字的个数增加1
        if (cnt[value] == 0) {
            curAns++;
        }
        cnt[value]++;
    }

    // 从区间中删除元素
    public static void del(int value) {
        cnt[value]--;
        // 如果该数值不再出现，则不同数字的个数减少1
        if (cnt[value] == 0) {
            curAns--;
        }
    }

    // 计算查询结果
    public static void compute() {
        int winl = 1, winr = 0; // 当前维护的区间 [winl, winr]
        for (int i = 1; i <= m; i++) {
            int jobl = query[i][0]; // 目标区间左端点
            int jobr = query[i][1]; // 目标区间右端点
            int id = query[i][2];   // 查询编号
            
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
        int blen = (int) Math.sqrt(n);
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        Arrays.sort(query, 1, m + 1, new QueryCmp());
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        n = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        m = in.nextInt();
        for (int i = 1; i <= m; i++) {
            query[i][0] = in.nextInt();
            query[i][1] = in.nextInt();
            query[i][2] = i;
        }
        
        prepare();
        compute();
        
        for (int i = 1; i <= m; i++) {
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