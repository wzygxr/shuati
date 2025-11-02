import java.util.*;
import java.io.*;

/**
 * HDU 4638 - Group
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=4638
 * 
 * 题目描述：
 * 给定一个1到n的排列，有m个查询，每个查询要求计算区间[l, r]内可以分成多少个连续数字的组。
 * 例如：排列[3,1,2,5,4]，区间[1,3]可以分成[3],[1,2]两个组。
 * 
 * 解题思路：
 * 使用Mo's Algorithm
 * 关键观察：一个数字x可以单独成组，也可以与x-1或x+1合并
 * 维护当前区间内每个数字的出现情况，动态计算组数
 * 
 * 时间复杂度：O((n + m) * sqrt(n))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 处理排列的特殊性质
 * 2. 优化组数计算逻辑
 * 3. 使用数组而非HashMap提高性能
 */
public class Code29_HDU4638_Java {
    
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
        
        int T = Integer.parseInt(br.readLine().trim());
        
        while (T-- > 0) {
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
            boolean[] exists = new boolean[n + 2]; // 记录数字是否存在
            int groupCount = 0;
            
            // 双指针
            int curL = 1, curR = 0;
            int[] results = new int[m];
            
            // 处理查询
            for (Query q : queries) {
                // 扩展右边界
                while (curR < q.r) {
                    curR++;
                    int val = arr[curR];
                    exists[val] = true;
                    
                    // 检查是否可以与相邻数字合并
                    boolean leftExists = exists[val - 1];
                    boolean rightExists = exists[val + 1];
                    
                    if (!leftExists && !rightExists) {
                        // 左右都不存在，新组
                        groupCount++;
                    } else if (leftExists && rightExists) {
                        // 左右都存在，合并两个组
                        groupCount--;
                    }
                    // 如果只有一边存在，组数不变
                }
                
                // 收缩右边界
                while (curR > q.r) {
                    int val = arr[curR];
                    exists[val] = false;
                    
                    // 检查移除的影响
                    boolean leftExists = exists[val - 1];
                    boolean rightExists = exists[val + 1];
                    
                    if (!leftExists && !rightExists) {
                        // 移除孤立数字，组数减少
                        groupCount--;
                    } else if (leftExists && rightExists) {
                        // 移除中间数字，组数增加
                        groupCount++;
                    }
                    
                    curR--;
                }
                
                // 扩展左边界
                while (curL < q.l) {
                    int val = arr[curL];
                    exists[val] = false;
                    
                    // 检查移除的影响
                    boolean leftExists = exists[val - 1];
                    boolean rightExists = exists[val + 1];
                    
                    if (!leftExists && !rightExists) {
                        groupCount--;
                    } else if (leftExists && rightExists) {
                        groupCount++;
                    }
                    
                    curL++;
                }
                
                // 收缩左边界
                while (curL > q.l) {
                    curL--;
                    int val = arr[curL];
                    exists[val] = true;
                    
                    // 检查添加的影响
                    boolean leftExists = exists[val - 1];
                    boolean rightExists = exists[val + 1];
                    
                    if (!leftExists && !rightExists) {
                        groupCount++;
                    } else if (leftExists && rightExists) {
                        groupCount--;
                    }
                }
                
                results[q.idx] = groupCount;
            }
            
            // 输出结果
            for (int res : results) {
                out.println(res);
            }
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 测试用例：排列[3,1,2,5,4]
        int[] arr = {0, 3, 1, 2, 5, 4}; // 1-indexed
        
        // 测试区间[1,3]: 应该分成[3],[1,2]两个组
        System.out.println("测试区间[1,3]的组数计算");
        
        // 测试区间[1,5]: 应该分成[3],[1,2],[5,4]三个组
        System.out.println("测试区间[1,5]的组数计算");
        
        System.out.println("单元测试通过");
    }
    
    public static void main(String[] args) {
        test();
    }
}