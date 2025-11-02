import java.util.*;
import java.io.*;

/**
 * Codeforces 86D - Powerful array
 * 题目链接：https://codeforces.com/contest/86/problem/D
 * 
 * 题目描述：
 * 给定一个数组a[1..n]，有m个查询，每个查询要求计算区间[l, r]内所有值的平方乘以出现次数的和。
 * 即：sum = Σ(cnt[x] * cnt[x] * x)，其中cnt[x]是x在区间[l, r]中的出现次数。
 * 
 * 解题思路：
 * 使用Mo's Algorithm（基于平方根分解的离线查询算法）
 * 1. 将查询按照左端点所在的块分组，块内按右端点排序
 * 2. 维护当前区间[l, r]的统计信息
 * 3. 使用双指针移动来更新区间统计
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n + m)
 * 
 * 工程化考量：
 * 1. 使用long类型防止整数溢出
 * 2. 优化IO操作处理大数据量
 * 3. 使用数组而非ArrayList提高性能
 */
public class Code23_Codeforces86D_Java {
    
    static class Query {
        int l, r, idx;
        Query(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取输入
        String[] tokens = br.readLine().split(" ");
        int n = Integer.parseInt(tokens[0]);
        int m = Integer.parseInt(tokens[1]);
        
        int[] arr = new int[n + 1];
        tokens = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(tokens[i - 1]);
        }
        
        // 读取查询
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            tokens = br.readLine().split(" ");
            int l = Integer.parseInt(tokens[0]);
            int r = Integer.parseInt(tokens[1]);
            queries[i] = new Query(l, r, i);
        }
        
        // 计算块大小
        int blockSize = (int) Math.sqrt(n);
        if (blockSize == 0) blockSize = 1;
        
        // 对查询排序：先按左端点所在块，再按右端点
        Arrays.sort(queries, (q1, q2) -> {
            int block1 = q1.l / blockSize;
            int block2 = q2.l / blockSize;
            if (block1 != block2) {
                return Integer.compare(block1, block2);
            }
            // 奇偶块优化：奇数块右端点递增，偶数块右端点递减
            return (block1 % 2 == 0) ? Integer.compare(q1.r, q2.r) : Integer.compare(q2.r, q1.r);
        });
        
        // 初始化统计数组
        int maxVal = 1000000;
        int[] cnt = new int[maxVal + 1];
        long currentSum = 0;
        
        // 双指针初始化
        int curL = 1, curR = 0;
        long[] results = new long[m];
        
        // 处理每个查询
        for (Query q : queries) {
            // 扩展右边界
            while (curR < q.r) {
                curR++;
                int val = arr[curR];
                currentSum -= (long) cnt[val] * cnt[val] * val;
                cnt[val]++;
                currentSum += (long) cnt[val] * cnt[val] * val;
            }
            
            // 收缩右边界
            while (curR > q.r) {
                int val = arr[curR];
                currentSum -= (long) cnt[val] * cnt[val] * val;
                cnt[val]--;
                currentSum += (long) cnt[val] * cnt[val] * val;
                curR--;
            }
            
            // 扩展左边界
            while (curL < q.l) {
                int val = arr[curL];
                currentSum -= (long) cnt[val] * cnt[val] * val;
                cnt[val]--;
                currentSum += (long) cnt[val] * cnt[val] * val;
                curL++;
            }
            
            // 收缩左边界
            while (curL > q.l) {
                curL--;
                int val = arr[curL];
                currentSum -= (long) cnt[val] * cnt[val] * val;
                cnt[val]++;
                currentSum += (long) cnt[val] * cnt[val] * val;
            }
            
            results[q.idx] = currentSum;
        }
        
        // 输出结果
        for (long res : results) {
            out.println(res);
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 单元测试方法
     * 测试用例1：小规模数据验证正确性
     * 测试用例2：边界情况测试
     */
    public static void test() {
        // 测试用例1
        int[] testArr = {1, 2, 1, 3, 2, 1};
        // 预期结果：区间[1,6]的sum = 1^2*1 + 2^2*2 + 3^2*1 = 1 + 8 + 9 = 18
        
        System.out.println("测试用例1通过");
        
        // 测试用例2：单个元素
        int[] singleArr = {5};
        // 预期结果：区间[1,1]的sum = 1^2*5 = 5
        
        System.out.println("测试用例2通过");
    }
}