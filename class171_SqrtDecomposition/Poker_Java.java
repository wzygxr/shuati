package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * 由乃打扑克 - Java实现
 * 
 * 题目来源：洛谷 P5356
 * 题目描述：
 * 给定一个长度为n的数组arr，接下来有m条操作，操作类型如下
 * 操作 1 l r v : 查询arr[l..r]范围上，第v小的数
 * 操作 2 l r v : arr[l..r]范围上每个数加v，v可能是负数
 * 
 * 数据范围：
 * 1 <= n、m <= 10^5
 * -2 * 10^4 <= 数组中的值 <= +2 * 10^4
 * 
 * 解题思路：
 * 使用分块算法解决此问题。将数组分成大小约为√(n/2)的块，对每个块维护以下信息：
 * 1. 原数组arr：存储实际值
 * 2. 排序数组sortv：存储块内元素排序后的结果
 * 3. 懒惰标记lazy：记录块内所有元素需要增加的值
 * 
 * 对于操作2（区间加法）：
 * - 对于完整块，直接更新懒惰标记
 * - 对于不完整块，暴力更新元素值并重新排序块内元素
 * 
 * 对于操作1（查询第k小）：
 * - 使用二分答案的方法，通过统计小于等于某值的元素个数来确定第k小的值
 * - 统计时利用分块结构优化计算
 * 
 * 时间复杂度分析：
 * - 区间加法操作：O(√n)
 *   - 完整块：O(1)更新标记
 *   - 不完整块：O(√n)暴力更新并排序
 * - 查询第k小：O(√n * log(max_val - min_val))
 *   - 二分答案：O(log(max_val - min_val))
 *   - 每次统计：O(√n)
 * 
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查查询参数k的有效性（1 <= k <= 区间长度）
 *    - 处理空区间等边界情况
 * 2. 性能优化：
 *    - 使用懒惰标记避免重复计算
 *    - 合理设置块大小为√(n/2)以平衡完整块和不完整块的处理时间
 * 3. 鲁棒性：
 *    - 处理负数加法操作
 *    - 保证在各种数据分布下的稳定性能
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P5356
 */

public class Poker_Java {
    public static final int MAXN = 100001;
    public static final int MAXB = 1001;
    
    private int n, m;
    private int[] arr = new int[MAXN];
    private int[] sortv = new int[MAXN];
    
    // 分块相关变量
    private int blockSize, blockNum;
    private int[] blockIndex = new int[MAXN];  // 每个元素所属的块
    private int[] blockLeft = new int[MAXB];   // 每个块的左边界
    private int[] blockRight = new int[MAXB];  // 每个块的右边界
    private int[] lazy = new int[MAXB];        // 每个块的懒惰标记
    
    /**
     * 初始化分块结构
     * 
     * @param size 数组大小
     */
    public void init(int size) {
        this.n = size;
        // 设置块大小为sqrt(n/2)，这是一个经验性的优化
        this.blockSize = (int) Math.sqrt(n / 2);
        // 计算块数量
        this.blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; i++) {
            blockIndex[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化懒惰标记
        Arrays.fill(lazy, 0);
    }
    
    /**
     * 对指定区间进行加法操作并维护排序数组
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 要增加的值
     */
    private void innerAdd(int l, int r, int v) {
        // 对区间内每个元素加上v
        for (int i = l; i <= r; i++) {
            arr[i] += v;
        }
        // 更新该块的排序数组
        int blockId = blockIndex[l];
        for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
            sortv[i] = arr[i];
        }
        // 对块内元素重新排序
        Arrays.sort(sortv, blockLeft[blockId], blockRight[blockId] + 1);
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 要增加的值
     */
    public void add(int l, int r, int v) {
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            innerAdd(l, r, v);
        } else {
            // 处理左边不完整块
            innerAdd(l, blockRight[leftBlock], v);
            // 处理右边不完整块
            innerAdd(blockLeft[rightBlock], r, v);
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                lazy[i] += v;
            }
        }
    }
    
    /**
     * 获取区间最小值
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间最小值
     */
    private int getMin(int l, int r) {
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        int ans = Integer.MAX_VALUE;
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                ans = Math.min(ans, arr[i] + lazy[leftBlock]);
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                ans = Math.min(ans, arr[i] + lazy[leftBlock]);
            }
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                ans = Math.min(ans, arr[i] + lazy[rightBlock]);
            }
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                ans = Math.min(ans, sortv[blockLeft[i]] + lazy[i]);
            }
        }
        return ans;
    }
    
    /**
     * 获取区间最大值
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间最大值
     */
    private int getMax(int l, int r) {
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        int ans = Integer.MIN_VALUE;
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                ans = Math.max(ans, arr[i] + lazy[leftBlock]);
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                ans = Math.max(ans, arr[i] + lazy[leftBlock]);
            }
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                ans = Math.max(ans, arr[i] + lazy[rightBlock]);
            }
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                ans = Math.max(ans, sortv[blockRight[i]] + lazy[i]);
            }
        }
        return ans;
    }
    
    /**
     * 返回第blockId块内<= v的数字个数
     * 
     * @param blockId 块编号
     * @param v 比较值
     * @return 第blockId块内<= v的数字个数
     */
    private int blockCount(int blockId, int v) {
        v -= lazy[blockId];
        int left = blockLeft[blockId];
        int right = blockRight[blockId];
        
        if (sortv[left] > v) {
            return 0;
        }
        if (sortv[right] <= v) {
            return right - left + 1;
        }
        
        int mid, pos = left;
        while (left <= right) {
            mid = (left + right) / 2;
            if (sortv[mid] <= v) {
                pos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return pos - blockLeft[blockId] + 1;
    }
    
    /**
     * 返回arr[l..r]范围上<= v的数字个数
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 比较值
     * @return arr[l..r]范围上<= v的数字个数
     */
    private int getCount(int l, int r, int v) {
        int leftBlock = blockIndex[l];
        int rightBlock = blockIndex[r];
        int ans = 0;
        
        // 如果区间在同一个块内
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                if (arr[i] + lazy[leftBlock] <= v) {
                    ans++;
                }
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                if (arr[i] + lazy[leftBlock] <= v) {
                    ans++;
                }
            }
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                if (arr[i] + lazy[rightBlock] <= v) {
                    ans++;
                }
            }
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                ans += blockCount(i, v);
            }
        }
        return ans;
    }
    
    /**
     * 查询区间第k小的数
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param k 第k小
     * @return 第k小的数，如果k无效则返回-1
     */
    public int query(int l, int r, int k) {
        // 检查k的有效性
        if (k < 1 || k > r - l + 1) {
            return -1;
        }
        
        // 获取区间最小值和最大值作为二分的边界
        int minVal = getMin(l, r);
        int maxVal = getMax(l, r);
        int answer = -1;
        
        // 二分答案
        while (minVal <= maxVal) {
            int midVal = minVal + (maxVal - minVal) / 2;
            // 如果小于等于midVal的元素个数>=k，说明第k小的数<=midVal
            if (getCount(l, r, midVal) >= k) {
                answer = midVal;
                maxVal = midVal - 1;
            } else {
                minVal = midVal + 1;
            }
        }
        return answer;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] nm = reader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        Poker_Java solution = new Poker_Java();
        solution.init(n);
        
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 初始化排序数组
        for (int i = 1; i <= n; i++) {
            solution.sortv[i] = solution.arr[i];
        }
        // 对每个块内的元素进行排序
        for (int i = 1; i <= solution.blockNum; i++) {
            Arrays.sort(solution.sortv, solution.blockLeft[i], solution.blockRight[i] + 1);
        }
        
        for (int i = 0; i < m; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            int v = Integer.parseInt(operation[3]);
            
            if (op == 1) {
                writer.println(solution.query(l, r, v));
            } else {
                solution.add(l, r, v);
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}