// 数颜色/维护队列 - 带修莫队算法实现 (Java版本)
// 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题目大意: 维护一个序列，支持两种操作：1. 查询区间不同颜色数 2. 单点修改颜色
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 2. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 3. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
//
// 4. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
//
// 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 6. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 7. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 8. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 9. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 10. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py

package class179;

import java.io.*;
import java.util.*;

public class Code16_ColorMaintenance1 {
    public static int MAXN = 133335;
    public static long qsize;
    
    // 查询结构
    public static class Query {
        long id, t, l, r;
        
        public Query(long id, long t, long l, long r) {
            this.id = id;
            this.t = t;
            this.l = l;
            this.r = r;
        }
        
        public Query() {}
        
        // 排序比较器
        public boolean lessThan(Query b) {
            if (l / qsize != b.l / qsize) {
                return l / qsize < b.l / qsize;
            } else if (r / qsize != b.r / qsize) {
                return r / qsize < b.r / qsize;
            } else {
                return t < b.t;
            }
        }
    }
    
    // 修改操作结构
    public static class Operation {
        long p, x;
        
        public Operation(long p, long x) {
            this.p = p;
            this.x = x;
        }
        
        public Operation() {}
    }
    
    static Query[] q = new Query[150009];
    static Operation[] r = new Operation[150009];
    
    static char op;
    static long n, m, x, y, cur, qcnt, rcnt;
    static long[] mp = new long[1500009]; // 记录每种颜色的出现次数
    static long[] a = new long[150009];   // 原始序列
    static long[] ans = new long[150009]; // 答案数组

    // 添加元素
    public static void add(long x) {
        if (mp[(int)x] == 0) {
            cur += 1;
        }
        mp[(int)x] += 1;
    }

    // 删除元素
    public static void del(long x) {
        mp[(int)x] -= 1;
        if (mp[(int)x] == 0) {
            cur -= 1;
        }
    }

    // 处理查询
    public static void process() {
        // 对查询进行排序
        Arrays.sort(q, 1, (int)(qcnt + 1), new Comparator<Query>() {
            @Override
            public int compare(Query a, Query b) {
                if (a.l / qsize != b.l / qsize) {
                    return Long.compare(a.l / qsize, b.l / qsize);
                } else if (a.r / qsize != b.r / qsize) {
                    return Long.compare(a.r / qsize, b.r / qsize);
                } else {
                    return Long.compare(a.t, b.t);
                }
            }
        });
        
        long L = 1, R = 0, last = 0;
        for (long i = 1; i <= qcnt; i++) {
            while (R < q[(int)i].r) {
                add(a[(int)(++R)]);
            }
            while (R > q[(int)i].r) {
                del(a[(int)(R--)]);
            }
            while (L > q[(int)i].l) {
                add(a[(int)(--L)]);
            }
            while (L < q[(int)i].l) {
                del(a[(int)(L++)]);
            }
            while (last < q[(int)i].t) {
                last += 1;
                if (r[(int)last].p >= L && r[(int)last].p <= R) {
                    add(r[(int)last].x);
                    del(a[(int)r[(int)last].p]);
                }
                // 交换颜色值
                long temp = a[(int)r[(int)last].p];
                a[(int)r[(int)last].p] = r[(int)last].x;
                r[(int)last].x = temp;
            }
            while (last > q[(int)i].t) {
                if (r[(int)last].p >= L && r[(int)last].p <= R) {
                    add(r[(int)last].x);
                    del(a[(int)r[(int)last].p]);
                }
                // 交换颜色值
                long temp = a[(int)r[(int)last].p];
                a[(int)r[(int)last].p] = r[(int)last].x;
                r[(int)last].x = temp;
                last -= 1;
            }
            ans[(int)q[(int)i].id] = cur;
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextLong();
        m = in.nextLong();
        qsize = (long) Math.pow(n, 2.0 / 3.0);
        
        for (long i = 1; i <= n; i++) {
            a[(int)i] = in.nextLong();
        }
        
        for (long i = 1; i <= m; i++) {
            op = in.nextChar();
            x = in.nextLong();
            y = in.nextLong();
            
            if (op == 'Q') {
                ++qcnt;
                q[(int)qcnt] = new Query(qcnt, rcnt, x, y);
            } else if (op == 'R') {
                rcnt++;
                r[(int)rcnt] = new Operation(x, y);
            }
        }
        
        process();
        
        for (long i = 1; i <= qcnt; i++) {
            out.println(ans[(int)i]);
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

        public char nextChar() throws IOException {
            byte c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            return (char) c;
        }

        long nextLong() throws IOException {
            long c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            long val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}