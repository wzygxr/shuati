import java.util.*;
import java.io.*;

/**
 * SPOJ GIVEAWAY - Give Away
 * 题目链接：https://www.spoj.com/problems/GIVEAWAY/
 * 
 * 题目描述：
 * 给定一个数组，支持两种操作：
 * 1. 查询操作：查询区间[l, r]内大于等于x的元素个数
 * 2. 更新操作：将位置i的元素值修改为y
 * 
 * 解题思路：
 * 使用平方根分解 + 块内排序
 * 1. 将数组分成sqrt(n)个块
 * 2. 每个块维护一个有序数组
 * 3. 查询时：完整块使用二分查找，不完整块暴力统计
 * 4. 更新时：更新原数组，并重新排序对应块
 * 
 * 时间复杂度：
 * - 查询：O(sqrt(n) * log(sqrt(n)))
 * - 更新：O(sqrt(n) * log(sqrt(n)))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用Buffered IO处理大数据量
 * 2. 优化二分查找性能
 * 3. 处理边界情况和极端输入
 */
public class Code25_SPOJGIVEAWAY_Java {
    
    static int n;
    static int[] arr;
    static int blockSize, blockCount;
    static List<Integer>[] blocks;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取数组大小
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        
        // 读取数组元素
        String[] tokens = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(tokens[i]);
        }
        
        // 初始化分块
        initializeBlocks();
        
        // 读取操作数量
        int m = Integer.parseInt(br.readLine().trim());
        
        for (int i = 0; i < m; i++) {
            tokens = br.readLine().split(" ");
            String operation = tokens[0];
            
            if (operation.equals("0")) {
                // 查询操作: 0 l r x
                int l = Integer.parseInt(tokens[1]) - 1; // 0-indexed
                int r = Integer.parseInt(tokens[2]) - 1;
                int x = Integer.parseInt(tokens[3]);
                
                int result = query(l, r, x);
                out.println(result);
            } else {
                // 更新操作: 1 i y
                int idx = Integer.parseInt(tokens[1]) - 1;
                int newVal = Integer.parseInt(tokens[2]);
                
                update(idx, newVal);
            }
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 初始化分块结构
     */
    static void initializeBlocks() {
        blockSize = (int) Math.sqrt(n);
        if (blockSize == 0) blockSize = 1;
        blockCount = (n + blockSize - 1) / blockSize;
        
        blocks = new ArrayList[blockCount];
        for (int i = 0; i < blockCount; i++) {
            blocks[i] = new ArrayList<>();
        }
        
        // 将元素分配到各个块中
        for (int i = 0; i < n; i++) {
            int blockIdx = i / blockSize;
            blocks[blockIdx].add(arr[i]);
        }
        
        // 对每个块进行排序
        for (int i = 0; i < blockCount; i++) {
            Collections.sort(blocks[i]);
        }
    }
    
    /**
     * 查询操作：统计区间[l, r]内大于等于x的元素个数
     */
    static int query(int l, int r, int x) {
        int result = 0;
        int startBlock = l / blockSize;
        int endBlock = r / blockSize;
        
        if (startBlock == endBlock) {
            // 区间在同一个块内，暴力统计
            for (int i = l; i <= r; i++) {
                if (arr[i] >= x) {
                    result++;
                }
            }
        } else {
            // 处理左边界不完整块
            for (int i = l; i < (startBlock + 1) * blockSize && i < n; i++) {
                if (arr[i] >= x) {
                    result++;
                }
            }
            
            // 处理中间完整块
            for (int blockIdx = startBlock + 1; blockIdx < endBlock; blockIdx++) {
                List<Integer> block = blocks[blockIdx];
                // 在有序块中使用二分查找统计大于等于x的元素数量
                int pos = findFirstGreaterOrEqual(block, x);
                result += (block.size() - pos);
            }
            
            // 处理右边界不完整块
            for (int i = endBlock * blockSize; i <= r; i++) {
                if (arr[i] >= x) {
                    result++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 更新操作：将位置idx的元素值修改为newVal
     */
    static void update(int idx, int newVal) {
        int blockIdx = idx / blockSize;
        int oldVal = arr[idx];
        arr[idx] = newVal;
        
        // 更新对应块的有序列表
        List<Integer> block = blocks[blockIdx];
        
        // 找到旧值的位置并移除
        int oldPos = Collections.binarySearch(block, oldVal);
        if (oldPos >= 0) {
            block.remove(oldPos);
        }
        
        // 插入新值到正确位置（保持有序）
        int newPos = findInsertPosition(block, newVal);
        block.add(newPos, newVal);
    }
    
    /**
     * 在有序列表中查找第一个大于等于target的元素位置
     */
    static int findFirstGreaterOrEqual(List<Integer> list, int target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * 在有序列表中查找插入位置（保持有序）
     */
    static int findInsertPosition(List<Integer> list, int value) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 测试用例1
        n = 5;
        arr = new int[]{1, 2, 3, 4, 5};
        initializeBlocks();
        
        // 查询测试
        int result1 = query(0, 4, 3); // 查询大于等于3的元素个数
        System.out.println("测试用例1查询结果: " + result1 + " (预期: 3)");
        
        // 更新测试
        update(2, 6); // 将位置2的元素从3改为6
        int result2 = query(0, 4, 3); // 再次查询
        System.out.println("测试用例1更新后查询结果: " + result2 + " (预期: 3)");
        
        System.out.println("单元测试通过");
    }
}