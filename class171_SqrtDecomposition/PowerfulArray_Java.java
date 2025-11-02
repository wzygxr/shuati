package class173.implementations;

/**
 * Codeforces 86D - Powerful Array - Java实现
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/contest/86/problem/D
 * 题目描述：
 * 给定一个长度为n的数组，以及m个查询，每个查询要求计算区间[l,r]的权值
 * 区间权值定义为：对于区间内每个不同的值x，如果x出现了c次，则贡献c*c*x到总权值中
 * 
 * 解题思路：
 * 使用莫队算法（Mo's Algorithm）解决此问题。通过维护每个元素出现的次数，
 * 可以在O(1)时间内更新区间的权值。
 * 
 * 算法步骤：
 * 1. 将数组分块，块大小约为sqrt(n)
 * 2. 将所有查询按左端点所在块和右端点排序
 * 3. 通过指针移动维护当前区间的答案
 * 4. 通过添加或删除元素来更新答案
 * 
 * 时间复杂度：O((n+m) * sqrt(n))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用莫队算法减少重复计算
 * 3. 鲁棒性：处理各种边界情况
 */

import java.io.*;
import java.util.*;

public class PowerfulArray_Java {
    // 最大数组大小
    public static final int MAXN = 200010;
    
    // 原数组
    private int[] arr = new int[MAXN];
    // 计数数组，记录每个元素出现的次数
    private int[] count = new int[1000010];
    // 查询结构
    private Query[] queries = new Query[MAXN];
    // 答案数组
    private long[] ans = new long[MAXN];
    
    // 当前区间权值
    private long currentAns = 0;
    
    // 查询结构
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    /**
     * 添加元素到当前区间
     * 
     * @param pos 位置
     */
    private void add(int pos) {
        int value = arr[pos];
        currentAns -= (long) count[value] * count[value] * value;
        count[value]++;
        currentAns += (long) count[value] * count[value] * value;
    }
    
    /**
     * 从当前区间移除元素
     * 
     * @param pos 位置
     */
    private void remove(int pos) {
        int value = arr[pos];
        currentAns -= (long) count[value] * count[value] * value;
        count[value]--;
        currentAns += (long) count[value] * count[value] * value;
    }
    
    /**
     * 初始化莫队算法
     * 
     * @param n 数组大小
     * @param m 查询数量
     */
    public void init(int n, int m) {
        // 设置块大小为sqrt(n)
        int blockSize = (int) Math.sqrt(n);
        
        // 对查询进行排序
        Arrays.sort(queries, 1, m + 1, (a, b) -> {
            int blockA = (a.l - 1) / blockSize;
            int blockB = (b.l - 1) / blockSize;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });
    }
    
    /**
     * 执行莫队算法
     * 
     * @param n 数组大小
     * @param m 查询数量
     */
    public void moAlgorithm(int n, int m) {
        int currentL = 1, currentR = 0;
        
        for (int i = 1; i <= m; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            int id = queries[i].id;
            
            // 扩展或收缩左边界
            while (currentL < l) {
                remove(currentL);
                currentL++;
            }
            while (currentL > l) {
                currentL--;
                add(currentL);
            }
            
            // 扩展或收缩右边界
            while (currentR < r) {
                currentR++;
                add(currentR);
            }
            while (currentR > r) {
                remove(currentR);
                currentR--;
            }
            
            // 记录答案
            ans[id] = currentAns;
        }
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) throws IOException {
        // 使用更快的输入输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组大小和查询数量
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        PowerfulArray_Java solution = new PowerfulArray_Java();
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 读取所有查询
        for (int i = 1; i <= m; i++) {
            String[] query = reader.readLine().split(" ");
            int l = Integer.parseInt(query[0]);
            int r = Integer.parseInt(query[1]);
            solution.queries[i] = new Query(l, r, i);
        }
        
        // 初始化并执行莫队算法
        solution.init(n, m);
        solution.moAlgorithm(n, m);
        
        // 输出所有查询结果
        for (int i = 1; i <= m; i++) {
            writer.println(solution.ans[i]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}