// XOR and Favorite Number - 普通莫队算法实现 (Java版本)
// 题目来源: CodeForces 617E XOR and Favorite Number
// 题目链接: https://codeforces.com/problemset/problem/617/E
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少对(i,j)满足i<=j且a[i]^a[i+1]^...^a[j]=k
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. CodeForces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite3.py
//
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 3. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 4. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 5. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 8. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 9. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
//
// 10. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)） - https://www.luogu.com.cn/problem/P4887
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline3.py

package class179;

import java.io.*;
import java.util.*;

public class Code19_XorFavorite1 {
    public static int MAXN = 100010;
    public static int n, m, k;
    public static int[] a = new int[MAXN]; // 原始数组
    public static int[] prefixXor = new int[MAXN]; // 前缀异或和
    
    // 查询结构
    public static class Query {
        int l, r, id;
        
        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
        
        public Query() {}
    }
    
    public static Query[] q = new Query[MAXN];
    
    // 莫队相关
    public static int[] pos = new int[MAXN]; // 每个位置所属的块
    public static int[] cnt = new int[MAXN * 2]; // 计数数组，需要足够大以容纳所有可能的异或值
    public static long curAns = 0; // 当前答案
    public static long[] ans = new long[MAXN]; // 答案数组

    // 查询排序比较器
    public static class QueryComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            if (pos[a.l] != pos[b.l]) {
                return pos[a.l] - pos[b.l];
            }
            if ((pos[a.l] & 1) == 1) {
                return a.r - b.r;
            } else {
                return b.r - a.r;
            }
        }
    }

    // 添加元素到区间
    public static void add(int value) {
        curAns += cnt[value ^ k];
        cnt[value]++;
    }

    // 从区间中删除元素
    public static void del(int value) {
        cnt[value]--;
        curAns -= cnt[value ^ k];
    }

    // 计算查询结果
    public static void compute() {
        int winl = 1, winr = 0; // 当前维护的区间 [winl, winr]
        for (int i = 1; i <= m; i++) {
            int jobl = q[i].l; // 目标区间左端点
            int jobr = q[i].r; // 目标区间右端点
            int id = q[i].id;   // 查询编号
            
            // 扩展左边界
            while (winl > jobl) {
                add(prefixXor[--winl - 1]); // 转换为0索引
            }
            
            // 扩展右边界
            while (winr < jobr) {
                add(prefixXor[++winr]); // 转换为0索引
            }
            
            // 收缩左边界
            while (winl < jobl) {
                del(prefixXor[winl++ - 1]); // 转换为0索引
            }
            
            // 收缩右边界
            while (winr > jobr) {
                del(prefixXor[winr--]); // 转换为0索引
            }
            
            ans[id] = curAns;
        }
    }

    // 预处理
    public static void prepare() {
        int blen = (int) Math.sqrt(n);
        for (int i = 1; i <= n + 1; i++) {
            pos[i] = (i - 1) / blen + 1;
        }
        Arrays.sort(q, 1, m + 1, new QueryComparator());
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();
        
        // 初始化查询数组
        for (int i = 0; i < MAXN; i++) {
            q[i] = new Query();
        }
        
        // 读取数组
        for (int i = 1; i <= n; i++) {
            a[i] = in.nextInt();
        }
        
        // 计算前缀异或和
        prefixXor[0] = 0;
        for (int i = 1; i <= n; i++) {
            prefixXor[i] = prefixXor[i - 1] ^ a[i];
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            q[i] = new Query(l, r, i);
        }
        
        // 初始化计数数组
        Arrays.fill(cnt, 0);
        cnt[0] = 1; // 空前缀的异或和为0
        
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