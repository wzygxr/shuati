package class178;

// 洛谷P4887 莫队二次离线模板题 - Java版本
// 题目来源: 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）
// 题目链接: https://www.luogu.com.cn/problem/P4887
// 题目大意: 给定一个数组，定义k1二元组为满足arr[i] XOR arr[j]的二进制表示中有k个1的二元组(i,j)
// 查询区间内k1二元组的个数
// 数据范围: 1 <= n、m <= 10^5, 0 <= arr[i]、k < 16384(2的14次方)
// 解题思路: 使用莫队二次离线算法优化普通莫队的转移操作
// 时间复杂度: O(n*sqrt(n) + n*C(k,14)) 其中C(k,14)表示14位二进制数中恰好有k个1的数的个数
// 空间复杂度: O(n + 2^14)
// 相关题目:
// 1. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）: https://www.luogu.com.cn/problem/P4887
// 2. 洛谷 P5501 [LnOI2019] 来者不拒，去者不追: https://www.luogu.com.cn/problem/P5501
// 3. 洛谷 P5398 [Ynoi2019 模拟赛] Yuno loves sqrt technology II: https://www.luogu.com.cn/problem/P5398
// 4. 洛谷 P5047 [Ynoi2019 模拟赛] Yuno loves sqrt technology III: https://www.luogu.com.cn/problem/P5047
// 5. Codeforces 617E XOR and Favorite Number: https://codeforces.com/contest/617/problem/E
// 6. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
// 7. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 8. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
// 9. POJ 2104 K-th Number: http://poj.org/problem?id=2104

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class P4887_MoOfflineTwice {

    public static int MAXN = 100002;
    public static int MAXV = 1 << 14;  // 2^14 = 16384
    public static int n, m, k;
    public static int[] arr = new int[MAXN];
    public static int[] bi = new int[MAXN];  // 块编号
    public static int[] kOneArr = new int[MAXV];  // 存储二进制中有k个1的数
    public static int cntk;  // 二进制中有k个1的数的个数

    // 莫队查询任务: l, r, id
    public static int[][] query = new int[MAXN][3];

    // 离线任务: x, l, r, op, id
    // headl[x]: x在l~r左侧的离线任务列表
    // headr[x]: x在l~r右侧的离线任务列表
    public static int[] headl = new int[MAXN];
    public static int[] headr = new int[MAXN];
    public static int[] nextq = new int[MAXN << 1];  // 链式前向星
    public static int[] ql = new int[MAXN << 1];
    public static int[] qr = new int[MAXN << 1];
    public static int[] qop = new int[MAXN << 1];
    public static int[] qid = new int[MAXN << 1];
    public static int cntq;  // 离线任务计数

    // cnt[v]: 当前数字v作为第二个数，之前出现的数字作为第一个数，产生多少k1二元组
    public static int[] cnt = new int[MAXV];
    // 前缀和与后缀和
    public static long[] pre = new long[MAXN];
    public static long[] suf = new long[MAXN];

    public static long[] ans = new long[MAXN];  // 答案数组

    // 莫队查询排序比较器
    public static class QueryCmp implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            if (bi[a[0]] != bi[b[0]]) {
                return bi[a[0]] - bi[b[0]];
            }
            return a[1] - b[1];
        }
    }

    // 计算一个数二进制表示中1的个数
    public static int lowbit(int i) {
        return i & -i;
    }

    // 计算一个数二进制表示中1的个数
    public static int countOne(int num) {
        int ret = 0;
        while (num > 0) {
            ret++;
            num -= lowbit(num);
        }
        return ret;
    }

    // 添加左侧离线任务
    public static void addLeftOffline(int x, int l, int r, int op, int id) {
        nextq[++cntq] = headl[x];
        headl[x] = cntq;
        ql[cntq] = l;
        qr[cntq] = r;
        qop[cntq] = op;
        qid[cntq] = id;
    }

    // 添加右侧离线任务
    public static void addRightOffline(int x, int l, int r, int op, int id) {
        nextq[++cntq] = headr[x];
        headr[x] = cntq;
        ql[cntq] = l;
        qr[cntq] = r;
        qop[cntq] = op;
        qid[cntq] = id;
    }

    // 预处理函数
    public static void prepare() {
        // 计算块大小和块编号
        int blen = (int) Math.sqrt(n);
        for (int i = 1; i <= n; i++) {
            bi[i] = (i - 1) / blen + 1;
        }
        
        // 对查询进行排序
        Arrays.sort(query, 1, m + 1, new QueryCmp());
        
        // 预处理所有二进制表示中有k个1的数
        for (int v = 0; v < MAXV; v++) {
            if (countOne(v) == k) {
                kOneArr[++cntk] = v;
            }
        }
    }

    // 计算函数
    public static void compute() {
        // 正向计算前缀贡献
        for (int i = 1; i <= n; i++) {
            // pre[i] = pre[i-1] + 以arr[i]为第二个数的k1二元组个数
            pre[i] = pre[i - 1] + cnt[arr[i]];
            
            // 更新cnt数组：对于每个二进制中有k个1的数t，arr[i] XOR t的值作为第一个数
            // 与arr[i]组成k1二元组，所以cnt[arr[i] XOR t]增加1
            for (int j = 1; j <= cntk; j++) {
                cnt[arr[i] ^ kOneArr[j]]++;
            }
        }
        
        // 清空cnt数组
        Arrays.fill(cnt, 0);
        
        // 反向计算后缀贡献
        for (int i = n; i >= 1; i--) {
            // suf[i] = suf[i+1] + 以arr[i]为第一个数的k1二元组个数
            suf[i] = suf[i + 1] + cnt[arr[i]];
            
            // 更新cnt数组
            for (int j = 1; j <= cntk; j++) {
                cnt[arr[i] ^ kOneArr[j]]++;
            }
        }
        
        // 执行莫队
        int winl = 1, winr = 0;  // 当前窗口[l, r]
        for (int i = 1; i <= m; i++) {
            int jobl = query[i][0];  // 查询左端点
            int jobr = query[i][1];  // 查询右端点
            int id = query[i][2];    // 查询编号
            
            // 右端点向右扩展
            if (winr < jobr) {
                // 添加左侧离线任务
                addLeftOffline(winl - 1, winr + 1, jobr, -1, id);
                // 累加前缀贡献
                ans[id] += pre[jobr] - pre[winr];
            }
            
            // 右端点向左收缩
            if (winr > jobr) {
                // 添加左侧离线任务
                addLeftOffline(winl - 1, jobr + 1, winr, 1, id);
                // 减去前缀贡献
                ans[id] -= pre[winr] - pre[jobr];
            }
            winr = jobr;  // 更新右端点
            
            // 左端点向左扩展
            if (winl > jobl) {
                // 添加右侧离线任务
                addRightOffline(winr + 1, jobl, winl - 1, -1, id);
                // 累加后缀贡献
                ans[id] += suf[jobl] - suf[winl];
            }
            
            // 左端点向右收缩
            if (winl < jobl) {
                // 添加右侧离线任务
                addRightOffline(winr + 1, winl, jobl - 1, 1, id);
                // 减去后缀贡献
                ans[id] -= suf[winl] - suf[jobl];
            }
            winl = jobl;  // 更新左端点
        }
        
        // 清空cnt数组
        Arrays.fill(cnt, 0);
        
        // 处理左侧离线任务
        for (int x = 0; x <= n; x++) {
            // 更新cnt数组
            if (x >= 1) {
                for (int j = 1; j <= cntk; j++) {
                    cnt[arr[x] ^ kOneArr[j]]++;
                }
            }
            
            // 处理x位置的离线任务
            for (int q = headl[x]; q > 0; q = nextq[q]) {
                int l = ql[q], r = qr[q], op = qop[q], id = qid[q];
                for (int j = l; j <= r; j++) {
                    ans[id] += (long) op * cnt[arr[j]];
                }
            }
        }
        
        // 清空cnt数组
        Arrays.fill(cnt, 0);
        
        // 处理右侧离线任务
        for (int x = n + 1; x >= 1; x--) {
            // 更新cnt数组
            if (x <= n) {
                for (int j = 1; j <= cntk; j++) {
                    cnt[arr[x] ^ kOneArr[j]]++;
                }
            }
            
            // 处理x位置的离线任务
            for (int q = headr[x]; q > 0; q = nextq[q]) {
                int l = ql[q], r = qr[q], op = qop[q], id = qid[q];
                for (int j = l; j <= r; j++) {
                    ans[id] += (long) op * cnt[arr[j]];
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        
        for (int i = 1; i <= m; i++) {
            query[i][0] = in.nextInt();
            query[i][1] = in.nextInt();
            query[i][2] = i;
        }
        
        // 预处理
        prepare();
        
        // 计算答案
        compute();
        
        // ans[i]代表答案变化量，需要加工成前缀和才是每个查询的答案
        // 注意在普通莫队的顺序下，去生成前缀和
        for (int i = 2; i <= m; i++) {
            ans[query[i][2]] += ans[query[i - 1][2]];
        }
        
        // 输出答案
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 快速读取工具类
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
    
    /*
     * 算法分析:
     * 
     * 时间复杂度: O(n*sqrt(n) + n*C(k,14))
     * 其中C(k,14)表示14位二进制数中恰好有k个1的数的个数
     * 
     * 空间复杂度: O(n + 2^14)
     * 
     * 算法思路:
     * 1. 使用莫队二次离线算法优化普通莫队的转移操作
     * 2. 预处理所有二进制表示中有k个1的数
     * 3. 通过前缀和与后缀和预计算部分贡献
     * 4. 将莫队的扩展操作离线处理，批量计算
     * 
     * 核心思想:
     * 1. 对于查询[l,r]，我们维护区间内所有k1二元组的个数
     * 2. k1二元组定义为满足arr[i] XOR arr[j]的二进制表示中有k个1的二元组
     * 3. 通过预处理所有二进制中有k个1的数，可以在O(1)时间内判断两个数的XOR是否满足条件
     * 4. 使用莫队算法处理区间扩展，通过二次离线优化转移复杂度
     * 
     * 工程化考量:
     * 1. 使用快速输入输出优化IO性能
     * 2. 合理使用静态数组避免动态分配
     * 3. 使用链式前向星存储离线任务
     * 4. 通过位运算优化计算性能
     * 
     * 调试技巧:
     * 1. 可以通过打印中间结果验证算法正确性
     * 2. 使用断言检查关键变量的正确性
     * 3. 对比暴力算法验证结果
     */
}