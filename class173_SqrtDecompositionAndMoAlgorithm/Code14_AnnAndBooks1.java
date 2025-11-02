package class175;

// Ann and Books问题 - 前缀和+哈希表实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/877/E
// 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间内和等于k的子数组个数
// 约束条件: 1 <= n, q <= 10^5, |arr[i]| <= 10^9

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Code14_AnnAndBooks1 {

    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, q: 查询次数, k: 目标和
    public static int n, q, k;
    
    // arr: 原始数组
    public static int[] arr = new int[MAXN];
    
    // prefixSum: 前缀和数组
    public static long[] prefixSum = new long[MAXN];

    /**
     * 查询[l,r]区间内和等于k的子数组个数
     * 时间复杂度: O(r-l+1)
     * 设计思路: 使用前缀和和哈希表统计满足条件的子数组个数
     * 对于子数组[i,j]，其和为prefixSum[j] - prefixSum[i-1]
     * 要使子数组和等于k，即prefixSum[j] - prefixSum[i-1] = k
     * 变形得prefixSum[i-1] = prefixSum[j] - k
     * 因此我们可以在遍历过程中统计每个前缀和出现的次数，然后查找prefixSum[j] - k是否出现过
     * @param l 查询区间左边界
     * @param r 查询区间右边界
     * @return 区间内和等于k的子数组个数
     */
    public static int query(int l, int r) {
        // 使用哈希表统计前缀和出现次数
        Map<Long, Integer> count = new HashMap<>();
        // 前缀和为0出现1次（表示空前缀）
        count.put(0L, 1);
        
        int result = 0;
        
        // 遍历查询区间内的每个位置
        for (int i = l; i <= r; i++) {
            // 计算从位置l-1到位置i的前缀和
            long currentSum = prefixSum[i] - prefixSum[l - 1];
            
            // 查找是否存在前缀和使得currentSum - prevSum = k
            // 即prevSum = currentSum - k
            result += count.getOrDefault(currentSum - k, 0);
            
            // 更新当前前缀和的计数
            count.put(currentSum, count.getOrDefault(currentSum, 0) + 1);
        }
        
        return result;
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和目标和k
        n = in.nextInt();
        k = in.nextInt();
        
        // 读取初始数组并计算前缀和
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
            // 计算前缀和
            prefixSum[i] = prefixSum[i - 1] + arr[i];
        }

        // 读取查询次数q
        q = in.nextInt();
        
        // 处理q次查询
        for (int i = 1; i <= q; i++) {
            int l = in.nextInt();
            int r = in.nextInt();
            // 输出查询结果
            out.println(query(l, r));
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

        public int nextInt() throws IOException {
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