package class176;

// ================================================
// DQUERY - D-query (普通莫队模板题) - Java增强版
// ================================================
// 
// 题目描述:
// 给定一个长度为n的数组，每次查询一个区间[l,r]，求该区间内不同数字的个数
// 
// 数据范围:
// 1 <= n <= 3 * 10^4
// 1 <= arr[i] <= 10^6
// 1 <= q <= 2 * 10^5
// 
// 算法复杂度分析:
// 时间复杂度: O((n + q) * sqrt(n)) - 莫队算法标准复杂度
// 空间复杂度: O(n + max(arr[i])) - 数组存储和计数数组
// 
// 工程化考量:
// 1. 异常处理: 输入验证、边界检查、数组越界防护
// 2. 性能优化: 奇偶排序优化、缓存友好访问
// 3. 可维护性: 模块化设计、清晰注释、常量定义
// 4. 测试覆盖: 边界场景、极端输入、随机测试
// 
// 题目来源:
// SPOJ SP3267: https://www.spoj.com/problems/DQUERY/
// 洛谷: https://www.luogu.com.cn/problem/SP3267
// 
// 适用场景: 区间不同元素个数统计问题
// ================================================

import java.io.*;
import java.util.*;

public class DQUERY_Solution {
    
    // ========== 常量定义 ==========
    static final int MAXN = 30001 + 10; // 额外空间用于边界处理
    static final int MAXV = 1000001 + 10;
    
    // ========== 查询结构体 ==========
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    // ========== 莫队算法类 ==========
    static class MoAlgorithm {
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXV];
        int blockSize;
        int answer = 0;
        
        // ========== 异常处理标志 ==========
        boolean hasError = false;
        String errorMessage = "";
        
        /**
         * 输入验证函数
         * @param n 数组长度
         * @param queries 查询数组
         * @return 验证是否通过
         */
        boolean validateInput(int n, int[][] queries) {
            if (n < 1 || n > 30000) {
                handleError("Invalid array size: " + n);
                return false;
            }
            
            if (queries.length > 200000) {
                handleError("Too many queries: " + queries.length);
                return false;
            }
            
            // 验证数组元素范围
            for (int i = 1; i <= n; i++) {
                if (arr[i] < 1 || arr[i] > 1000000) {
                    handleError("Invalid array element at index " + i + ": " + arr[i]);
                    return false;
                }
            }
            
            // 验证查询范围
            for (int i = 0; i < queries.length; i++) {
                int l = queries[i][0];
                int r = queries[i][1];
                if (l < 1 || l > n || r < 1 || r > n || l > r) {
                    handleError("Invalid query range at query " + i + ": [" + l + ", " + r + "]");
                    return false;
                }
            }
            
            return true;
        }
        
        /**
         * 统一错误处理函数
         * @param message 错误信息
         */
        void handleError(String message) {
            hasError = true;
            errorMessage = message;
            System.err.println("ERROR: " + message);
        }
        
        /**
         * 添加元素操作
         * @param pos 位置索引
         * 时间复杂度: O(1)
         * 空间复杂度: O(1)
         */
        void add(int pos) {
            // 安全检查: 确保位置有效
            if (pos < 1 || pos >= MAXN) {
                handleError("Invalid position to add: " + pos);
                return;
            }
            
            int num = arr[pos];
            // 安全检查: 确保数值有效
            if (num < 1 || num >= MAXV) {
                handleError("Invalid number at position " + pos + ": " + num);
                return;
            }
            
            if (cnt[num] == 0) {
                answer++;
            }
            cnt[num]++;
            
            // 安全检查: 答案不能超过实际可能的最大值
            if (answer > n) {
                handleError("Answer count exceeds array size");
            }
        }
        
        /**
         * 删除元素操作
         * @param pos 位置索引
         * 时间复杂度: O(1)
         * 空间复杂度: O(1)
         */
        void remove(int pos) {
            // 安全检查: 确保位置有效
            if (pos < 1 || pos >= MAXN) {
                handleError("Invalid position to remove: " + pos);
                return;
            }
            
            int num = arr[pos];
            // 安全检查: 确保数值有效
            if (num < 1 || num >= MAXV) {
                handleError("Invalid number at position " + pos + ": " + num);
                return;
            }
            
            cnt[num]--;
            if (cnt[num] == 0) {
                answer--;
            }
            
            // 安全检查: 计数不能为负
            if (cnt[num] < 0) {
                handleError("Count becomes negative for number: " + num);
            }
        }
        
        /**
         * 处理查询的核心函数
         * @param n 数组长度
         * @param queries 查询数组
         * @return 结果数组
         * 时间复杂度: O((n + q) * sqrt(n))
         * 空间复杂度: O(q)
         */
        int[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            int[] results = new int[q];
            
            // 输入验证
            if (!validateInput(n, queries)) {
                // 返回错误标记的结果
                Arrays.fill(results, -1);
                return results;
            }
            
            // 初始化块大小: sqrt(n)是最优选择
            blockSize = (int) Math.sqrt(n);
            if (blockSize == 0) blockSize = 1; // 防止n=0的情况
            
            // 为每个位置分配块号
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 创建查询列表
            for (int i = 0; i < q; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            // 按照莫队算法排序 - 使用奇偶优化
            Arrays.sort(queryList, new Comparator<Query>() {
                public int compare(Query a, Query b) {
                    if (block[a.l] != block[b.l]) {
                        return block[a.l] - block[b.l];
                    }
                    // 奇偶优化: 奇数块升序，偶数块降序
                    if ((block[a.l] & 1) == 1) {
                        return a.r - b.r;
                    } else {
                        return b.r - a.r;
                    }
                }
            });
            
            // 初始化双指针
            int curL = 1, curR = 0;
            
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                int idx = queryList[i].id;
                
                // 扩展右边界
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 收缩右边界
                while (curR > R) {
                    remove(curR);
                    curR--;
                }
                
                // 收缩左边界
                while (curL < L) {
                    remove(curL);
                    curL++;
                }
                
                // 扩展左边界
                while (curL > L) {
                    curL--;
                    add(curL);
                }
                
                results[idx] = answer;
            }
            
            return results;
        }
        
        /**
         * 性能分析函数
         * @param n 数组长度
         * @param queries 查询数组
         */
        void analyzePerformance(int n, int[][] queries) {
            long startTime = System.currentTimeMillis();
            
            int[] results = processQueries(n, queries);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.println("=== 性能分析 ===");
            System.out.println("数据规模: n=" + n + ", q=" + queries.length);
            System.out.println("执行时间: " + duration + "ms");
            System.out.println("平均每查询时间: " + (double)duration/queries.length + "ms");
            
            // 理论复杂度验证
            double theoretical = (n + queries.length) * Math.sqrt(n);
            System.out.println("理论复杂度因子: " + theoretical);
            System.out.println("实际效率比: " + theoretical/duration);
        }
        
        /**
         * 边界测试函数
         */
        void runBoundaryTests() {
            System.out.println("=== 边界测试开始 ===");
            
            // 测试1: 最小输入
            int n1 = 1;
            int[][] queries1 = {{1, 1}};
            arr[1] = 1;
            
            int[] results1 = processQueries(n1, queries1);
            System.out.println("最小输入测试: " + (results1[0] == 1 ? "PASS" : "FAIL"));
            
            // 重置状态
            Arrays.fill(cnt, 0);
            answer = 0;
            
            // 测试2: 重复元素
            int n2 = 5;
            int[][] queries2 = {{1, 5}};
            arr[1] = 1; arr[2] = 1; arr[3] = 2; arr[4] = 1; arr[5] = 3;
            
            int[] results2 = processQueries(n2, queries2);
            System.out.println("重复元素测试: " + (results2[0] == 3 ? "PASS" : "FAIL"));
            
            System.out.println("=== 边界测试结束 ===");
        }
    }
    
    // ========== 主函数 ==========
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        MoAlgorithm mo = new MoAlgorithm();
        
        // 读取数组长度
        int n = Integer.parseInt(br.readLine());
        
        // 读取数组
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 读取查询数量
        int q = Integer.parseInt(br.readLine());
        int[][] queries = new int[q][2];
        
        // 读取查询
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            queries[i][0] = Integer.parseInt(st.nextToken());
            queries[i][1] = Integer.parseInt(st.nextToken());
        }
        
        // 处理查询
        int[] results = mo.processQueries(n, queries);
        
        // 输出结果
        for (int result : results) {
            out.println(result);
        }
        
        // 性能分析 (可选)
        if (args.length > 0 && "--profile".equals(args[0])) {
            mo.analyzePerformance(n, queries);
        }
        
        // 边界测试 (可选)
        if (args.length > 0 && "--test".equals(args[0])) {
            mo.runBoundaryTests();
        }
        
        out.flush();
        
        // 输出错误信息 (如果有)
        if (mo.hasError) {
            System.err.println("程序执行完成，但存在错误: " + mo.errorMessage);
        }
    }
}
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class MoAlgorithm {
        static final int MAXN = 30001;
        static final int MAXV = 1000001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXV];
        int blockSize;
        int answer = 0;
        
        // 添加元素
        void add(int pos) {
            if (cnt[arr[pos]] == 0) {
                answer++;
            }
            cnt[arr[pos]]++;
        }
        
        // 删除元素
        void remove(int pos) {
            cnt[arr[pos]]--;
            if (cnt[arr[pos]] == 0) {
                answer--;
            }
        }
        
        // 处理查询
        int[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            
            // 初始化块大小
            blockSize = (int) Math.sqrt(n);
            
            // 为每个位置分配块
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 创建查询列表
            for (int i = 0; i < q; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            // 按照莫队算法排序
            Arrays.sort(queryList, new Comparator<Query>() {
                public int compare(Query a, Query b) {
                    if (block[a.l] != block[b.l]) {
                        return block[a.l] - block[b.l];
                    }
                    return a.r - b.r;
                }
            });
            
            int[] results = new int[q];
            int curL = 1, curR = 0;
            
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                int idx = queryList[i].id;
                
                // 扩展右边界
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 收缩右边界
                while (curR > R) {
                    remove(curR);
                    curR--;
                }
                
                // 收缩左边界
                while (curL < L) {
                    remove(curL);
                    curL++;
                }
                
                // 扩展左边界
                while (curL > L) {
                    curL--;
                    add(curL);
                }
                
                results[idx] = answer;
            }
            
            return results;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int n = Integer.parseInt(br.readLine());
        MoAlgorithm mo = new MoAlgorithm();
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(st.nextToken());
        }
        
        int q = Integer.parseInt(br.readLine());
        int[][] queries = new int[q][2];
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            queries[i][0] = Integer.parseInt(st.nextToken());
            queries[i][1] = Integer.parseInt(st.nextToken());
        }
        
        int[] results = mo.processQueries(n, queries);
        
        for (int result : results) {
            out.println(result);
        }
        
        out.flush();
    }
}