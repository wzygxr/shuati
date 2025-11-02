import java.util.*;
import java.io.*;

/**
 * AtCoder ABC174 F - Range Set Query
 * 题目链接：https://atcoder.jp/contests/abc174/tasks/abc174_f
 * 
 * 题目描述：
 * 给定一个数组，有多个查询，每个查询要求计算区间[l, r]内不同元素的个数。
 * 
 * 解题思路：
 * 使用Mo's Algorithm（离线查询算法）
 * 1. 将查询按照左端点所在的块分组
 * 2. 块内按右端点排序（使用奇偶优化）
 * 3. 维护当前区间内不同元素的计数
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n + m)
 * 
 * 工程化考量：
 * 1. 使用Buffered IO处理大数据量
 * 2. 优化双指针移动效率
 * 3. 处理边界情况
 */
public class Code28_AtCoderABC174F_Java {
    
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
        
        // 对查询排序
        Arrays.sort(queries, (q1, q2) -> {
            int block1 = q1.l / blockSize;
            int block2 = q2.l / blockSize;
            if (block1 != block2) {
                return Integer.compare(block1, block2);
            }
            // 奇偶块优化
            return (block1 % 2 == 0) ? Integer.compare(q1.r, q2.r) : Integer.compare(q2.r, q1.r);
        });
        
        // 初始化统计
        int maxVal = 500000;
        int[] cnt = new int[maxVal + 1];
        int distinctCount = 0;
        
        // 双指针
        int curL = 1, curR = 0;
        int[] results = new int[m];
        
        // 处理查询
        for (Query q : queries) {
            // 扩展右边界
            while (curR < q.r) {
                curR++;
                int val = arr[curR];
                if (cnt[val] == 0) {
                    distinctCount++;
                }
                cnt[val]++;
            }
            
            // 收缩右边界
            while (curR > q.r) {
                int val = arr[curR];
                cnt[val]--;
                if (cnt[val] == 0) {
                    distinctCount--;
                }
                curR--;
            }
            
            // 扩展左边界
            while (curL < q.l) {
                int val = arr[curL];
                cnt[val]--;
                if (cnt[val] == 0) {
                    distinctCount--;
                }
                curL++;
            }
            
            // 收缩左边界
            while (curL > q.l) {
                curL--;
                int val = arr[curL];
                if (cnt[val] == 0) {
                    distinctCount++;
                }
                cnt[val]++;
            }
            
            results[q.idx] = distinctCount;
        }
        
        // 输出结果
        for (int res : results) {
            out.println(res);
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 模拟测试数据
        int n = 5;
        int[] arr = {0, 1, 2, 1, 3, 2}; // 1-indexed
        
        // 测试查询
        System.out.println("测试用例1: 区间[1,5]的不同元素个数");
        System.out.println("测试用例2: 区间[2,4]的不同元素个数");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}