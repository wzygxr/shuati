package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * LOJ 6277. 数列分块入门 1 - Java实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，单点查值。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记，表示该块内所有元素需要增加的值。
 * 区间加法操作时：
 * 1. 对于完整块，直接更新加法标记
 * 2. 对于不完整块，暴力更新元素值
 * 单点查询时，返回元素值加上所在块的加法标记
 * 
 * 时间复杂度：
 * - 区间加法：O(√n)
 * - 单点查询：O(1)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用标记减少重复计算
 * 4. 鲁棒性：处理边界情况
 */

public class LOJ6277_Java {
    // 最大数组大小
    public static final int MAXN = 50010;
    
    // 原数组
    private int[] arr = new int[MAXN];
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
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] += val;
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] += val;
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                lazy[i] += val;
            }
        }
    }
    
    /**
     * 单点查询
     * 
     * @param pos 位置
     * @return 该位置的值
     */
    public int query(int pos) {
        // 返回元素值加上所在块的加法标记
        return arr[pos] + lazy[belong[pos]];
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
        LOJ6277_Java solution = new LOJ6277_Java();
        solution.init(n);
        
        // 读取初始数组
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
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
                // 单点查询
                writer.println(solution.query(r));
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}