// 小Z的袜子 - 普通莫队算法实现 (Java版本)
// 题目来源: 洛谷 P1494 [国家集训队]小Z的袜子
// 题目链接: https://www.luogu.com.cn/problem/P1494
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内随机选择两个数，求这两个数相等的概率
// 解题思路: 使用普通莫队算法，通过维护区间内每种颜色的个数来计算概率
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 3. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
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

// 小Z的袜子 - 普通莫队算法实现 (Java版本)
// 题目来源: BZOJ 2038 [2009国家集训队]小Z的袜子(hose)
// 题目链接: https://www.luogu.com.cn/problem/P1494
// 题目大意: 给定一个长度为n的序列，每个元素代表袜子的颜色
// 有m次询问，每次询问区间[l,r]中随机抽取两只袜子颜色相同的概率
// 输出要求: 每个询问输出一个分数A/B表示概率，要求为最简分数
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)

import java.io.*;
import java.util.*;

public class Code09_Socks1 {
    public static int MAXN = 50010;
    public static int n, m;
    public static int[] arr = new int[MAXN];
    public static int[][] query = new int[MAXN][3];
    public static int[] bi = new int[MAXN];
    public static long[] cnt = new long[MAXN]; // 记录每种颜色的出现次数
    public static long curAns = 0; // 当前区间的答案（分子）
    public static long[] ansA = new long[MAXN]; // 答案分子
    public static long[] ansB = new long[MAXN]; // 答案分母

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
    public static void add(int color) {
        // 当添加一个颜色时，该颜色对答案的贡献增加2*cnt[color]
        // 因为对于每一对已存在的该颜色袜子，新加入的袜子都能和它们组成一对
        curAns += cnt[color] * 2;
        cnt[color]++;
    }

    // 从区间中删除元素
    public static void del(int color) {
        cnt[color]--;
        // 当删除一个颜色时，该颜色对答案的贡献减少2*cnt[color]
        // 因为对于每一对剩余的该颜色袜子，被删除的袜子不能再和它们组成一对
        curAns -= cnt[color] * 2;
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
            
            // 特殊情况：区间长度为1时概率为0
            if (jobl == jobr) {
                ansA[id] = 0;
                ansB[id] = 1;
                continue;
            }
            
            // 计算答案
            ansA[id] = curAns;
            long len = jobr - jobl + 1;
            ansB[id] = len * (len - 1);
            
            // 化简分数
            long gcd = gcd(ansA[id], ansB[id]);
            ansA[id] /= gcd;
            ansB[id] /= gcd;
        }
    }

    // 求最大公约数
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
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
        m = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        for (int i = 1; i <= m; i++) {
            query[i][0] = in.nextInt();
            query[i][1] = in.nextInt();
            query[i][2] = i;
        }
        
        prepare();
        compute();
        
        for (int i = 1; i <= m; i++) {
            out.println(ansA[i] + "/" + ansB[i]);
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