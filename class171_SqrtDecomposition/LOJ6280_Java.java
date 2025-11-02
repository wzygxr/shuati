package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * LOJ 6280. 数列分块入门 4 - Java实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，区间求和。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记和块内元素和。
 * 区间加法操作时：
 * 1. 对于完整块，更新加法标记和块内元素和
 * 2. 对于不完整块，暴力更新元素值并更新块内元素和
 * 查询操作时：
 * 1. 对于不完整块，暴力计算元素和
 * 2. 对于完整块，直接使用块内元素和
 * 
 * 时间复杂度：
 * - 区间加法：O(√n)
 * - 查询操作：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：维护块内元素和减少计算量
 * 4. 鲁棒性：处理边界情况和特殊输入
 */

public class LOJ6280_Java {
    // 最大数组大小
    public static final int MAXN = 50010;
    
    // 原数组
    private long[] arr = new long[MAXN];
    // 每个块的元素和
    private long[] sum = new long[MAXN];
    // 每个元素所属的块
    private int[] belong = new int[MAXN];
    // 每个块的加法标记
    private long[] lazy = new long[MAXN];
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
        
        // 初始化加法标记和块和
        Arrays.fill(lazy, 0);
        Arrays.fill(sum, 0);
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要增加的值
     */
    public void add(int l, int r, long val) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 更新元素值
            for (int i = l; i <= r; i++) {
                arr[i] += val;
            }
            // 重新计算块和
            sum[leftBlock] = 0;
            for (int i = blockLeft[leftBlock]; i <= blockRight[leftBlock]; i++) {
                sum[leftBlock] += arr[i];
            }
            // 加上块的加法标记
            sum[leftBlock] += lazy[leftBlock] * (blockRight[leftBlock] - blockLeft[leftBlock] + 1);
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] += val;
            }
            // 重新计算左边块的和
            sum[leftBlock] = 0;
            for (int i = blockLeft[leftBlock]; i <= blockRight[leftBlock]; i++) {
                sum[leftBlock] += arr[i];
            }
            sum[leftBlock] += lazy[leftBlock] * (blockRight[leftBlock] - blockLeft[leftBlock] + 1);
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] += val;
            }
            // 重新计算右边块的和
            sum[rightBlock] = 0;
            for (int i = blockLeft[rightBlock]; i <= blockRight[rightBlock]; i++) {
                sum[rightBlock] += arr[i];
            }
            sum[rightBlock] += lazy[rightBlock] * (blockRight[rightBlock] - blockLeft[rightBlock] + 1);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                lazy[i] += val;
                sum[i] += val * (blockRight[i] - blockLeft[i] + 1);
            }
        }
    }
    
    /**
     * 查询区间和
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间元素和
     */
    public long query(int l, int r) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        long result = 0;
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                result += arr[i] + lazy[leftBlock];
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                result += arr[i] + lazy[leftBlock];
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                result += sum[i];
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                result += arr[i] + lazy[rightBlock];
            }
        }
        
        return result;
    }
    
    /**
     * 设置数组元素值
     * 
     * @param index 索引（从1开始）
     * @param value 值
     */
    public void setValue(int index, long value) {
        arr[index] = value;
        // 更新所在块的和
        int block = belong[index];
        sum[block] = 0;
        for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
            sum[block] += arr[i];
        }
        sum[block] += lazy[block] * (blockRight[block] - blockLeft[block] + 1);
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
        LOJ6280_Java solution = new LOJ6280_Java();
        solution.init(n);
        
        // 读取初始数组
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            long value = Long.parseLong(elements[i - 1]);
            solution.arr[i] = value;
        }
        
        // 初始化块和
        for (int i = 1; i <= solution.blockNum; i++) {
            solution.sum[i] = 0;
            for (int j = solution.blockLeft[i]; j <= solution.blockRight[i]; j++) {
                solution.sum[i] += solution.arr[j];
            }
        }
        
        // 处理操作
        for (int i = 0; i < n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            long c = Long.parseLong(operation[3]);
            
            if (op == 0) {
                // 区间加法
                solution.add(l, r, c);
            } else {
                // 区间求和
                writer.println(solution.query(l, r) % (c + 1));
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}