package class047;

import java.io.*;
import java.util.*;

/**
 * POJ 3468 A Simple Problem with Integers
 * 
 * 题目描述:
 * 你有N个整数A1, A2, ... , AN。你需要处理两种类型的操作。
 * 一种操作是给定区间的每个数加上一个给定的数。
 * 另一种操作是查询给定区间所有数的和。
 * 
 * 输入:
 * 第一行包含两个数字N和Q。1 ≤ N,Q ≤ 100000。
 * 第二行包含N个数字，表示A1, A2, ... , AN的初始值。-1000000000 ≤ Ai ≤ 1000000000。
 * 接下来Q行，每行包含一个操作：
 * "C a b c" 表示将区间[a,b]中的每个数加上c。-10000 ≤ c ≤ 10000。
 * "Q a b" 表示查询区间[a,b]中所有数的和。
 * 
 * 输出:
 * 对于每个"Q a b"操作，输出一行表示区间和。
 * 
 * 题目链接: http://poj.org/problem?id=3468
 * 
 * 解题思路:
 * 使用树状数组结合差分数组来处理区间更新和区间查询操作。
 * 1. 使用两个树状数组维护差分信息
 * 2. 对于区间加法操作，使用差分标记更新两个树状数组
 * 3. 对于区间查询操作，使用前缀和公式计算区间和
 * 
 * 时间复杂度: O(Q log N) - 每次操作需要O(log N)时间
 * 空间复杂度: O(N) - 需要额外的树状数组空间
 * 
 * 这是最优解，因为需要支持动态区间更新和查询操作。
 */
public class Code25_POJ3468SimpleProblemWithIntegers {
    
    static class BIT {
        private long[] tree;
        private int n;
        
        public BIT(int size) {
            this.n = size;
            this.tree = new long[size + 1];
        }
        
        public void update(int idx, long val) {
            for (int i = idx; i <= n; i += i & -i) {
                tree[i] += val;
            }
        }
        
        public long query(int idx) {
            long sum = 0;
            for (int i = idx; i > 0; i -= i & -i) {
                sum += tree[i];
            }
            return sum;
        }
    }
    
    static class RangeBIT {
        private BIT bit1, bit2;
        private int n;
        
        public RangeBIT(int size) {
            this.n = size;
            this.bit1 = new BIT(size);
            this.bit2 = new BIT(size);
        }
        
        // 区间更新 [l, r] 加上 val
        public void rangeUpdate(int l, int r, long val) {
            bit1.update(l, val);
            bit1.update(r + 1, -val);
            bit2.update(l, val * (l - 1));
            bit2.update(r + 1, -val * r);
        }
        
        // 查询前缀和 [1, idx]
        public long prefixSum(int idx) {
            return bit1.query(idx) * idx - bit2.query(idx);
        }
        
        // 查询区间和 [l, r]
        public long rangeSum(int l, int r) {
            return prefixSum(r) - prefixSum(l - 1);
        }
    }
    
    /**
     * 主函数，处理输入并输出结果
     * 
     * 时间复杂度: O(Q log N) - 每次操作需要O(log N)时间
     * 空间复杂度: O(N) - 需要额外的树状数组空间
     * 
     * 工程化考量:
     * 1. 输入处理: 使用高效的输入处理方式
     * 2. 边界处理: 确保数组索引正确
     * 3. 性能优化: 使用树状数组结合差分处理区间操作
     * 4. 输出格式: 按照题目要求输出结果
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        
        RangeBIT rangeBit = new RangeBIT(n);
        
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            long val = Long.parseLong(st.nextToken());
            rangeBit.rangeUpdate(i, i, val);
        }
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            
            if (op.equals("C")) {
                // 区间更新操作
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                long c = Long.parseLong(st.nextToken());
                rangeBit.rangeUpdate(a, b, c);
            } else {
                // 区间查询操作
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                out.println(rangeBit.rangeSum(a, b));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}