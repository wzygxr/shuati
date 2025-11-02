package class132;

import java.util.*;

// SPOJ DQUERY - D-query
// 给定一个长度为n的数组，每次查询一个区间[l,r]内不同元素的个数
// 测试链接: https://www.spoj.com/problems/DQUERY/

public class Code11_DQuery {

    /**
     * 使用树状数组解决DQUERY问题
     * 
     * 解题思路:
     * 1. 这是一个经典的区间不同元素个数查询问题
     * 2. 可以使用离线处理+树状数组来解决
     * 3. 首先将所有查询按照右端点排序
     * 4. 从左到右扫描数组，维护每个元素最后出现的位置
     * 5. 当处理到位置i时，如果元素a[i]之前出现过，就将之前位置的贡献删除，
     *    然后在当前位置添加贡献
     * 6. 使用树状数组维护前缀和，支持单点更新和前缀和查询
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 排序查询: O(q log q)
     * - 处理数组: O(n log n)
     * - 处理查询: O(q log n)
     * - 总时间复杂度: O((n+q) log n + q log q)
     * 
     * 空间复杂度分析:
     * - 树状数组: O(n)
     * - 查询存储: O(q)
     * - 位置记录: O(n)
     * - 总空间复杂度: O(n+q)
     * 
     * 工程化考量:
     * 1. 离线处理优化查询效率
     * 2. 边界条件处理
     * 3. 异常输入检查
     * 4. 详细注释和变量命名
     */

    // 树状数组类
    static class FenwickTree {
        private int[] tree;
        private int n;

        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }

        private int lowbit(int x) {
            return x & (-x);
        }

        // 在位置i上增加值delta
        public void add(int i, int delta) {
            while (i <= n) {
                tree[i] += delta;
                i += lowbit(i);
            }
        }

        // 查询[1, i]的前缀和
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }

    // 查询类
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }

    public static int[] dquery(int[] arr, int[][] queries) {
        int n = arr.length;
        int q = queries.length;
        
        if (n == 0 || q == 0) {
            return new int[q];
        }
        
        // 离散化数组元素（如果需要的话）
        // 对于这个问题，我们假设元素值在合理范围内
        
        // 将查询存储到Query对象中并按右端点排序
        Query[] queryList = new Query[q];
        for (int i = 0; i < q; i++) {
            // 转换为1-indexed
            queryList[i] = new Query(queries[i][0], queries[i][1], i);
        }
        
        // 按右端点排序
        Arrays.sort(queryList, (a, b) -> Integer.compare(a.r, b.r));
        
        // 记录每个元素最后出现的位置
        Map<Integer, Integer> lastPosition = new HashMap<>();
        
        // 创建树状数组
        FenwickTree fenwickTree = new FenwickTree(n);
        
        // 结果数组
        int[] result = new int[q];
        
        // 当前处理到的位置
        int currentPos = 0;
        
        // 处理每个查询
        for (Query query : queryList) {
            // 将数组处理到查询的右端点
            while (currentPos < query.r) {
                int pos = currentPos + 1; // 1-indexed position
                int value = arr[currentPos];
                
                // 如果这个元素之前出现过，需要删除之前的贡献
                if (lastPosition.containsKey(value)) {
                    int prevPos = lastPosition.get(value);
                    fenwickTree.add(prevPos, -1);
                }
                
                // 在当前位置添加贡献
                fenwickTree.add(pos, 1);
                
                // 更新最后出现位置
                lastPosition.put(value, pos);
                
                currentPos++;
            }
            
            // 查询区间[l,r]内不同元素个数
            // 这等于查询[1,r]的前缀和减去[1,l-1]的前缀和
            result[query.id] = fenwickTree.query(query.r) - fenwickTree.query(query.l - 1);
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] arr1 = {1, 1, 2, 1, 3};
        int[][] queries1 = {{1, 5}, {2, 4}, {3, 5}};
        int[] result1 = dquery(arr1, queries1);
        
        System.out.println("Array: [1, 1, 2, 1, 3]");
        System.out.println("Queries:");
        for (int i = 0; i < queries1.length; i++) {
            System.out.printf("Query [%d, %d]: %d\n", queries1[i][0], queries1[i][1], result1[i]);
        }
        // 期望输出: 3, 2, 3
        
        // 测试用例2
        int[] arr2 = {1, 2, 3, 4, 5};
        int[][] queries2 = {{1, 5}, {2, 3}, {1, 3}};
        int[] result2 = dquery(arr2, queries2);
        
        System.out.println("\nArray: [1, 2, 3, 4, 5]");
        System.out.println("Queries:");
        for (int i = 0; i < queries2.length; i++) {
            System.out.printf("Query [%d, %d]: %d\n", queries2[i][0], queries2[i][1], result2[i]);
        }
        // 期望输出: 5, 2, 3
    }
}