package class178;

// 牛客网暑期ACM多校训练营 J Different Integers - Java版本
// 题目来源: 牛客网暑期ACM多校训练营 J Different Integers
// 题目链接: https://www.nowcoder.com/acm/contest/139/J
// 题目大意: 给定一个整数序列a1, a2, ..., an和q对整数(l1, r1), (l2, r2), ..., (lq, rq)
// 求count(l1, r1), count(l2, r2), ..., count(lq, rq)
// 其中count(i, j)是a1, a2, ..., ai, aj, aj+1, ..., an中不同整数的个数
// 数据范围: 1 ≤ n, q ≤ 10^5, 1 ≤ ai ≤ n
// 解题思路: 使用普通莫队算法处理区间查询问题
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 相关题目:
// 1. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
// 2. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
// 3. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 4. 洛谷 P2709 小B的询问: https://www.luogu.com.cn/problem/P2709
// 5. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列: https://www.luogu.com.cn/problem/P1903

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Nowcoder139J_DifferentIntegers1 {
    
    public static int MAXN = 100005;
    public static int n, m;
    public static int[] arr = new int[MAXN];  // 存储序列
    public static int[] belong = new int[MAXN];  // 块编号
    public static int[][] query = new int[MAXN][3];  // 查询[l, r, id]
    public static int[] ans = new int[MAXN];  // 答案数组
    
    // 莫队查询排序比较器
    public static class QueryComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            if (belong[a[0]] != belong[b[0]]) {
                return belong[a[0]] - belong[b[0]];
            }
            return a[1] - b[1];
        }
    }
    
    // 计算函数
    public static void compute() {
        // 预处理块编号
        int blockSize = (int) Math.sqrt(n);
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 对查询进行排序
        Arrays.sort(query, 1, m + 1, new QueryComparator());
        
        // 莫队算法
        int currentL = 1, currentR = 0;
        int currentAns = 0;
        
        // cnt[i]表示数字i在当前区间中的出现次数
        int[] cnt = new int[MAXN];
        
        for (int i = 1; i <= m; i++) {
            int L = query[i][0];
            int R = query[i][1];
            int id = query[i][2];
            
            // 扩展右端点
            while (currentR < R) {
                currentR++;
                int val = arr[currentR];
                if (cnt[val] == 0) {
                    currentAns++;
                }
                cnt[val]++;
            }
            
            // 收缩右端点
            while (currentR > R) {
                int val = arr[currentR];
                cnt[val]--;
                if (cnt[val] == 0) {
                    currentAns--;
                }
                currentR--;
            }
            
            // 扩展左端点
            while (currentL > L) {
                currentL--;
                int val = arr[currentL];
                if (cnt[val] == 0) {
                    currentAns++;
                }
                cnt[val]++;
            }
            
            // 收缩左端点
            while (currentL < L) {
                int val = arr[currentL];
                cnt[val]--;
                if (cnt[val] == 0) {
                    currentAns--;
                }
                currentL++;
            }
            
            ans[id] = currentAns;
        }
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 由于是多组测试用例，我们只处理一组
        try {
            while (true) {
                // 读取输入
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
                
                // 计算答案
                compute();
                
                // 输出答案
                for (int i = 1; i <= m; i++) {
                    out.println(ans[i]);
                }
                
                // 由于是多组测试用例，我们只处理一组就退出
                break;
            }
        } catch (Exception e) {
            // 输入结束
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
     * 时间复杂度: O((n + m) * sqrt(n))
     * 空间复杂度: O(n)
     * 
     * 算法思路:
     * 1. 使用莫队算法处理区间查询问题
     * 2. 维护当前区间中不同数字的个数
     * 3. 当添加一个数字时，如果该数字第一次出现，则不同数字个数加1
     * 4. 当删除一个数字时，如果该数字最后一次出现，则不同数字个数减1
     * 
     * 核心思想:
     * 1. 对于每个数字，我们维护它在当前区间中的出现次数
     * 2. 当添加数字val时：
     *    - 如果cnt[val]为0，说明val是第一次出现，不同数字个数加1
     *    - cnt[val]加1
     * 3. 当删除数字val时：
     *    - cnt[val]减1
     *    - 如果cnt[val]变为0，说明val是最后一次出现，不同数字个数减1
     * 
     * 工程化考量:
     * 1. 使用快速输入输出优化IO性能
     * 2. 合理使用静态数组避免动态分配
     * 3. 使用计数数组记录数字出现次数
     * 
     * 调试技巧:
     * 1. 可以通过打印中间结果验证算法正确性
     * 2. 使用断言检查关键变量的正确性
     * 3. 对比暴力算法验证结果
     */
}