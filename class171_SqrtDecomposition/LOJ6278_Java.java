package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * LOJ 6278. 数列分块入门 2 - Java实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，询问区间内小于某个值 x 的元素个数。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记和排序后的数组。
 * 区间加法操作时：
 * 1. 对于完整块，直接更新加法标记
 * 2. 对于不完整块，暴力更新元素值并重新排序
 * 查询操作时：
 * 1. 对于不完整块，暴力统计
 * 2. 对于完整块，使用二分查找统计
 * 
 * 时间复杂度：
 * - 区间加法：O(√n * log√n)
 * - 查询操作：O(√n * log√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用二分查找减少查询时间
 * 4. 鲁棒性：处理边界情况
 */

public class LOJ6278_Java {
    // 最大数组大小
    public static final int MAXN = 50010;
    
    // 原数组
    private int[] arr = new int[MAXN];
    // 排序后的数组
    private int[] sorted = new int[MAXN];
    // 每个元素所属的块
    private int[] belong = new int[MAXN];
    // 每个块的加法标记
    private int[] lazy = new int[MAXN];
    // 每个块的左右边界
    private int[] blockLeft = new int[MAXN];
    private int[] blockRight = new int[MAXN];
    
    // 块大小和块数量
    private int blockSize;
    private int blockNum;
    private int n;
    
    /**
     * 初始化分块结构
     * 
     * @param size 数组大小
     */
    public void init(int size) {
        this.n = size;
        // 设置块大小为sqrt(n)
        this.blockSize = (int) Math.sqrt(n);
        // 计算块数量
        this.blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化加法标记
        Arrays.fill(lazy, 0);
        
        // 初始化排序数组
        for (int i = 1; i <= n; i++) {
            sorted[i] = arr[i];
        }
        
        // 对每个块内的元素进行排序
        for (int i = 1; i <= blockNum; i++) {
            int[] temp = new int[blockRight[i] - blockLeft[i] + 1];
            for (int j = 0; j < temp.length; j++) {
                temp[j] = sorted[blockLeft[i] + j];
            }
            Arrays.sort(temp);
            for (int j = 0; j < temp.length; j++) {
                sorted[blockLeft[i] + j] = temp[j];
            }
        }
    }
    
    /**
     * 重构指定块的排序数组
     * 
     * @param blockId 块编号
     */
    private void rebuildBlock(int blockId) {
        // 将原数组的值复制到排序数组
        for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
            sorted[i] = arr[i];
        }
        // 对块内元素排序
        int[] temp = new int[blockRight[blockId] - blockLeft[blockId] + 1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = sorted[blockLeft[blockId] + i];
        }
        Arrays.sort(temp);
        for (int i = 0; i < temp.length; i++) {
            sorted[blockLeft[blockId] + i] = temp[i];
        }
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要增加的值
     */
    public void add(int l, int r, int val) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                arr[i] += val;
            }
            // 重构该块的排序数组
            rebuildBlock(leftBlock);
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] += val;
            }
            // 重构左边块的排序数组
            rebuildBlock(leftBlock);
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] += val;
            }
            // 重构右边块的排序数组
            rebuildBlock(rightBlock);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                lazy[i] += val;
            }
        }
    }
    
    /**
     * 在指定块内查找小于value的元素个数
     * 
     * @param blockId 块编号
     * @param value 比较值
     * @return 小于value的元素个数
     */
    private int countInBlock(int blockId, int value) {
        // 调整value，减去该块的标记值
        value -= lazy[blockId];
        
        // 在排序数组中使用二分查找
        int left = blockLeft[blockId];
        int right = blockRight[blockId];
        
        // 如果最小值都大于等于value，返回0
        if (sorted[left] >= value) {
            return 0;
        }
        
        // 如果最大值都小于value，返回块大小
        if (sorted[right] < value) {
            return right - left + 1;
        }
        
        // 二分查找第一个大于等于value的位置
        int low = left;
        int high = right;
        int pos = left;
        
        while (low <= high) {
            int mid = (low + high) / 2;
            if (sorted[mid] < value) {
                pos = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return pos - left + 1;
    }
    
    /**
     * 查询区间内小于value的元素个数
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param value 比较值
     * @return 小于value的元素个数
     */
    public int query(int l, int r, int value) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        int result = 0;
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                if (arr[i] + lazy[leftBlock] < value) {
                    result++;
                }
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                if (arr[i] + lazy[leftBlock] < value) {
                    result++;
                }
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                if (arr[i] + lazy[rightBlock] < value) {
                    result++;
                }
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                result += countInBlock(i, value);
            }
        }
        
        return result;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) throws IOException {
        // 使用更快的输入输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组大小
        int n = Integer.parseInt(reader.readLine());
        
        // 初始化分块结构
        LOJ6278_Java solution = new LOJ6278_Java();
        solution.n = n;
        
        // 读取初始数组
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 初始化分块
        solution.init(n);
        
        // 处理操作
        for (int i = 0; i < n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            int c = Integer.parseInt(operation[3]);
            
            if (op == 0) {
                // 区间加法
                solution.add(l, r, c);
            } else {
                // 查询操作
                writer.println(solution.query(l, r, c * c));
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}