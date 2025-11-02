import java.util.*;

/**
 * 洛谷 P5356 由乃打扑克
 * 题目要求：区间查询第k小，区间加法
 * 核心技巧：分块排序 + 二分答案
 * 时间复杂度：O(√n * log n) / 查询，O(√n) / 修改
 * 空间复杂度：O(n)
 * 测试链接：https://www.luogu.com.cn/problem/P5356
 * 
 * 算法思想详解：
 * 1. 将数组分成大小为√n的块
 * 2. 对每个块维护一个排序后的副本，便于二分查找
 * 3. 对每个块维护一个加法标记（lazy标记）
 * 4. 区间加法操作：
 *    - 对于完整块，更新块的加法标记
 *    - 对于不完整块，暴力修改原始数组，并重新排序该块
 * 5. 区间第k小查询：
 *    - 对整个值域进行二分查找
 *    - 对于每个中间值mid，统计区间内小于等于mid的元素个数
 *    - 根据统计结果调整二分边界
 */
public class Code33_LuoguP5356_Java {
    private int[] arr;          // 原始数组
    private int[] blockAdd;     // 每个块的加法标记
    private int blockSize;      // 块的大小
    private int blockCount;     // 块的数量
    private List<List<Integer>> sortedBlocks; // 每个块的排序副本
    
    /**
     * 构造函数，初始化数据结构
     * @param array 输入数组
     */
    public Code33_LuoguP5356_Java(int[] array) {
        arr = Arrays.copyOf(array, array.length);
        int n = array.length;
        blockSize = (int) Math.sqrt(n) + 1;
        blockCount = (n + blockSize - 1) / blockSize;
        blockAdd = new int[blockCount];
        sortedBlocks = new ArrayList<>(blockCount);
        
        // 初始化每个块的排序副本
        for (int i = 0; i < blockCount; i++) {
            int start = i * blockSize;
            int end = Math.min((i + 1) * blockSize, n);
            List<Integer> block = new ArrayList<>(end - start);
            for (int j = start; j < end; j++) {
                block.add(arr[j]);
            }
            Collections.sort(block);
            sortedBlocks.add(block);
        }
    }
    
    /**
     * 区间加法操作
     * @param l 左边界（包含，0-based）
     * @param r 右边界（包含，0-based）
     * @param val 要加的值
     */
    public void addRange(int l, int r, int val) {
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        
        // 如果在同一个块内，直接暴力修改
        if (leftBlock == rightBlock) {
            // 先处理块标记
            for (int i = l; i <= r; i++) {
                arr[i] += val;
            }
            // 重新排序该块
            rebuildBlock(leftBlock);
            return;
        }
        
        // 处理左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
            arr[i] += val;
        }
        rebuildBlock(leftBlock);
        
        // 处理中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            blockAdd[i] += val;
        }
        
        // 处理右边不完整块
        for (int i = rightBlock * blockSize; i <= r; i++) {
            arr[i] += val;
        }
        rebuildBlock(rightBlock);
    }
    
    /**
     * 重建指定块的排序数组
     * @param blockId 块的索引
     */
    private void rebuildBlock(int blockId) {
        int start = blockId * blockSize;
        int end = Math.min((blockId + 1) * blockSize, arr.length);
        List<Integer> block = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            block.add(arr[i]);
        }
        Collections.sort(block);
        sortedBlocks.set(blockId, block);
    }
    
    /**
     * 统计区间[l,r]内小于等于x的元素个数
     * @param l 左边界
     * @param r 右边界
     * @param x 目标值
     * @return 元素个数
     */
    private int countLeq(int l, int r, int x) {
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        int count = 0;
        
        // 如果在同一个块内，直接暴力统计
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                if (arr[i] + blockAdd[leftBlock] <= x) {
                    count++;
                }
            }
            return count;
        }
        
        // 统计左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
            if (arr[i] + blockAdd[leftBlock] <= x) {
                count++;
            }
        }
        
        // 统计中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            // 在排序后的块中二分查找x - blockAdd[i]
            int target = x - blockAdd[i];
            List<Integer> block = sortedBlocks.get(i);
            // 二分查找第一个大于target的位置
            int pos = Collections.binarySearch(block, target);
            if (pos < 0) {
                pos = -pos - 1;
            } else {
                // 找到最后一个等于target的位置
                while (pos < block.size() && block.get(pos) == target) {
                    pos++;
                }
            }
            count += pos;
        }
        
        // 统计右边不完整块
        for (int i = rightBlock * blockSize; i <= r; i++) {
            if (arr[i] + blockAdd[rightBlock] <= x) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 查询区间[l,r]内的第k小元素
     * @param l 左边界（包含，0-based）
     * @param r 右边界（包含，0-based）
     * @param k 第k小（k>=1）
     * @return 第k小的元素值
     */
    public int queryKth(int l, int r, int k) {
        // 首先确定值域范围
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int i = l; i <= r; i++) {
            int blockId = i / blockSize;
            int val = arr[i] + blockAdd[blockId];
            minVal = Math.min(minVal, val);
            maxVal = Math.max(maxVal, val);
        }
        
        // 二分查找第k小的元素
        int left = minVal;
        int right = maxVal;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int cnt = countLeq(l, r, mid);
            if (cnt >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * 获取指定位置的当前值
     * @param index 索引
     * @return 当前值
     */
    public int getValue(int index) {
        int blockId = index / blockSize;
        return arr[index] + blockAdd[blockId];
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }
        
        Code33_LuoguP5356_Java solution = new Code33_LuoguP5356_Java(array);
        
        while (m-- > 0) {
            int op = scanner.nextInt();
            if (op == 1) {
                // 区间加法
                int l = scanner.nextInt() - 1;
                int r = scanner.nextInt() - 1;
                int val = scanner.nextInt();
                solution.addRange(l, r, val);
            } else if (op == 2) {
                // 查询第k小
                int l = scanner.nextInt() - 1;
                int r = scanner.nextInt() - 1;
                int k = scanner.nextInt();
                System.out.println(solution.queryKth(l, r, k));
            }
        }
        
        scanner.close();
    }
    
    /**
     * 算法优化说明：
     * 1. 块大小选择√n是时间复杂度的平衡点
     * 2. 对于区间加法，只对不完整的块进行重建排序，完整块只更新标记
     * 3. 查询第k小时使用二分答案法，利用排序块的特性快速统计
     * 
     * 时间复杂度分析：
     * - 区间加法：O(√n)
     *   - 两个不完整块：O(√n)每个
     *   - 完整块：O(1)每个
     * - 查询第k小：O(√n * log V)，其中V是值域范围
     *   - 二分需要log V次迭代
     *   - 每次迭代需要O(√n)时间统计
     * 
     * 空间复杂度分析：
     * - O(n) 用于存储原始数组
     * - O(n) 用于存储排序后的块
     * - O(√n) 用于存储块标记
     * - 总体空间复杂度：O(n)
     */
}