package class170;

// P2345 [USACO04OPEN] MooFest G
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 二维数点
// 链接: https://www.luogu.com.cn/problem/P2345
// 
// 题目描述:
// 约翰的n头奶牛每年都会参加"哞哞大会"。每头奶牛的坐标为x_i，听力为v_i。
// 第i头和第j头奶牛交流，会发出max{v_i,v_j} × |x_i − x_j|的音量。
// 假设每对奶牛之间同时都在说话，请计算所有奶牛产生的音量之和是多少。
// 
// 解题思路:
// 使用CDQ分治解决二维数点问题。
// 1. 将所有奶牛按x坐标排序
// 2. 对于每对奶牛(i,j)，其中i<j，贡献为max{v_i,v_j} × |x_j - x_i|
// 3. 由于x_j > x_i，所以贡献为max{v_i,v_j} × (x_j - x_i)
// 4. 将贡献拆分为两部分：
//    - max{v_i,v_j} × x_j
//    - -max{v_i,v_j} × x_i
// 5. 对于固定的j，我们需要计算所有i<j的max{v_i,v_j}的和
// 6. 这可以通过CDQ分治来解决，将问题转化为二维数点问题
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.util.*;

class Cow {
    int x, v, id;
    
    public Cow(int x, int v, int id) {
        this.x = x;
        this.v = v;
        this.id = id;
    }
}

class Query {
    int type, x, v, id, idx; // type: 0表示插入，1表示查询
    
    public Query(int type, int x, int v, int id, int idx) {
        this.type = type;
        this.x = x;
        this.v = v;
        this.id = id;
        this.idx = idx;
    }
}

class Solution {
    private long[] bit;  // 树状数组
    private long[] sumBit;  // 用于维护v的和的树状数组
    
    // 树状数组操作
    private int lowbit(int x) {
        return x & (-x);
    }
    
    private void add(int x, long v, long sv, int n) {
        for (int i = x; i <= n; i += lowbit(i)) {
            bit[i] += v;
            sumBit[i] += sv;
        }
    }
    
    private long query(int x) {
        long res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    private long querySum(int x) {
        long res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += sumBit[i];
        }
        return res;
    }
    
    public long solveMooFest(int[] x, int[] v) {
        int n = x.length;
        if (n == 0) return 0;
        
        // 创建奶牛数组并按x坐标排序
        Cow[] cows = new Cow[n];
        for (int i = 0; i < n; i++) {
            cows[i] = new Cow(x[i], v[i], i);
        }
        
        Arrays.sort(cows, (a, b) -> {
            if (a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.v, b.v);
        });
        
        // 离散化v值
        int[] sortedV = v.clone();
        Arrays.sort(sortedV);
        int uniqueSize = removeDuplicates(sortedV);
        
        Query[] queries = new Query[2 * n];
        long[] result = new long[n];
        bit = new long[n + 1];
        sumBit = new long[n + 1];
        
        int cnt = 0;
        // 构造操作序列
        for (int i = 0; i < n; i++) {
            int vId = Arrays.binarySearch(sortedV, 0, uniqueSize, cows[i].v) + 1;
            if (vId < 0) vId = -vId;
            
            // 插入操作
            queries[cnt++] = new Query(0, cows[i].x, cows[i].v, vId, i);
            // 查询操作：查询所有v <= cows[i].v的元素个数和v的和
            queries[cnt++] = new Query(1, cows[i].x, cows[i].v, vId, i);
        }
        
        // 按x坐标排序
        Arrays.sort(queries, 0, cnt, (a, b) -> {
            if (a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.type, b.type); // 插入操作优先于查询操作
        });
        
        // 执行CDQ分治
        cdq(queries, result, 0, cnt - 1, n);
        
        // 计算最终结果
        long total = 0;
        for (int i = 0; i < n; i++) {
            total += result[i];
        }
        
        return total;
    }
    
    // CDQ分治主函数
    private void cdq(Query[] queries, long[] result, int l, int r, int n) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq(queries, result, l, mid, n);
        cdq(queries, result, mid + 1, r, n);
        
        // 合并过程，计算左半部分对右半部分的贡献
        Query[] tmp = new Query[r - l + 1];
        int i = l, j = mid + 1, k = 0;
        
        while (i <= mid && j <= r) {
            if (queries[i].x <= queries[j].x) {
                // 左半部分的元素x坐标小于等于右半部分，处理插入操作
                if (queries[i].type == 0) {
                    add(queries[i].id, 1, queries[i].v, n);  // 插入元素
                }
                tmp[k++] = queries[i++];
            } else {
                // 右半部分的元素x坐标更大，处理查询操作
                if (queries[j].type == 1) {
                    // 查询v <= queries[j].v的元素个数和v的和
                    long count = query(queries[j].id);
                    long sumV = querySum(queries[j].id);
                    // 贡献为：count * queries[j].x - sumV
                    result[queries[j].idx] += count * queries[j].x - sumV;
                }
                tmp[k++] = queries[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            tmp[k++] = queries[i++];
        }
        while (j <= r) {
            if (queries[j].type == 1) {
                long count = query(queries[j].id);
                long sumV = querySum(queries[j].id);
                result[queries[j].idx] += count * queries[j].x - sumV;
            }
            tmp[k++] = queries[j++];
        }
        
        // 清理树状数组
        for (int t = l; t <= mid; t++) {
            if (queries[t].type == 0) {
                add(queries[t].id, -1, -queries[t].v, n);
            }
        }
        
        // 将临时数组内容复制回原数组
        for (int t = 0; t < k; t++) {
            queries[l + t] = tmp[t];
        }
    }
    
    // 去重函数
    private int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int uniqueSize = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[uniqueSize - 1]) {
                nums[uniqueSize++] = nums[i];
            }
        }
        return uniqueSize;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int[] x1 = {1, 2, 3, 4};
        int[] v1 = {1, 2, 3, 4};
        long result1 = solution.solveMooFest(x1, v1);
        
        System.out.println("输入: x = [1,2,3,4], v = [1,2,3,4]");
        System.out.println("输出: " + result1);
    }
}