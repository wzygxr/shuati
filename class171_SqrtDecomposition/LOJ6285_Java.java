package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * LOJ 6285. 数列分块入门 9 - Java实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间乘法，区间加法，区间求和。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护：
 * 1. 乘法标记和加法标记用于延迟更新
 * 2. 块内元素和用于快速区间求和
 * 当进行区间乘法操作时：
 * 1. 更新块的乘法标记和加法标记
 * 2. 更新块内元素和
 * 当进行区间加法操作时：
 * 1. 更新块的加法标记
 * 2. 更新块内元素和
 * 区间求和时：
 * 1. 对不完整块，先下传标记，再暴力计算
 * 2. 对完整块，直接利用块内元素和
 * 
 * 时间复杂度：
 * - 区间操作：O(√n)
 * - 区间求和：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用延迟标记减少实际操作次数
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 取模操作：注意溢出问题，使用long类型存储中间结果
 */

public class LOJ6285_Java {
    // 最大数组大小
    public static final int MAXN = 100010;
    // 取模值
    public static final long MOD = 10007;
    
    // 原数组
    private long[] arr = new long[MAXN];
    // 每个元素所属的块
    private int[] belong = new int[MAXN];
    // 每个块的乘法标记
    private long[] mul = new long[MAXN];
    // 每个块的加法标记
    private long[] add = new long[MAXN];
    // 每个块的左右边界
    private int[] blockLeft = new int[MAXN];
    private int[] blockRight = new int[MAXN];
    // 每个块的元素和
    private long[] sum = new long[MAXN];
    
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
        
        // 初始化标记，乘法标记初始化为1，加法标记初始化为0
        for (int i = 1; i <= blockNum; i++) {
            mul[i] = 1;
            add[i] = 0;
            sum[i] = 0;
        }
    }
    
    /**
     * 更新块内元素和
     * 
     * @param block 块号
     */
    public void updateSum(int block) {
        sum[block] = 0;
        for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
            // 计算元素的实际值并累加到块和中
            long actualVal = (arr[i] * mul[block] + add[block]) % MOD;
            if (actualVal < 0) {
                actualVal += MOD;
            }
            sum[block] = (sum[block] + actualVal) % MOD;
        }
    }
    
    /**
     * 向下传递标记（将块的标记应用到每个元素）
     * 
     * @param block 块号
     */
    public void pushDown(int block) {
        // 如果该块有标记
        if (mul[block] != 1 || add[block] != 0) {
            // 对块内所有元素应用标记
            for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
                arr[i] = (arr[i] * mul[block] + add[block]) % MOD;
                // 确保结果为非负数
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            // 重置标记
            mul[block] = 1;
            add[block] = 0;
        }
    }
    
    /**
     * 区间乘法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要乘的值
     */
    public void multiply(int l, int r, long val) {
        val %= MOD;
        // 确保val为正数
        if (val < 0) {
            val += MOD;
        }
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 下传标记
            pushDown(leftBlock);
            // 更新元素值
            for (int i = l; i <= r; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            // 更新块和
            updateSum(leftBlock);
        } else {
            // 处理左边不完整块
            pushDown(leftBlock);
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            updateSum(leftBlock);
            
            // 处理右边不完整块
            pushDown(rightBlock);
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            updateSum(rightBlock);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                // 更新乘法标记
                mul[i] = (mul[i] * val) % MOD;
                if (mul[i] < 0) {
                    mul[i] += MOD;
                }
                // 更新加法标记
                add[i] = (add[i] * val) % MOD;
                if (add[i] < 0) {
                    add[i] += MOD;
                }
                // 更新块和
                sum[i] = (sum[i] * val) % MOD;
                if (sum[i] < 0) {
                    sum[i] += MOD;
                }
            }
        }
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要加的值
     */
    public void add(int l, int r, long val) {
        val %= MOD;
        // 确保val为正数
        if (val < 0) {
            val += MOD;
        }
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 下传标记
            pushDown(leftBlock);
            // 更新元素值
            for (int i = l; i <= r; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            // 更新块和
            updateSum(leftBlock);
        } else {
            // 处理左边不完整块
            pushDown(leftBlock);
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            updateSum(leftBlock);
            
            // 处理右边不完整块
            pushDown(rightBlock);
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            updateSum(rightBlock);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                // 更新加法标记
                add[i] = (add[i] + val) % MOD;
                if (add[i] < 0) {
                    add[i] += MOD;
                }
                // 更新块和
                int blockSize = blockRight[i] - blockLeft[i] + 1;
                sum[i] = (sum[i] + val * blockSize) % MOD;
                if (sum[i] < 0) {
                    sum[i] += MOD;
                }
            }
        }
    }
    
    /**
     * 区间求和查询
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 查询结果
     */
    public long query(int l, int r) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        long result = 0;
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 下传标记
            pushDown(leftBlock);
            // 暴力计算和
            for (int i = l; i <= r; i++) {
                result = (result + arr[i]) % MOD;
                if (result < 0) {
                    result += MOD;
                }
            }
        } else {
            // 处理左边不完整块
            pushDown(leftBlock);
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                result = (result + arr[i]) % MOD;
                if (result < 0) {
                    result += MOD;
                }
            }
            
            // 处理右边不完整块
            pushDown(rightBlock);
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                result = (result + arr[i]) % MOD;
                if (result < 0) {
                    result += MOD;
                }
            }
            
            // 处理中间完整块，直接累加块和
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                result = (result + sum[i]) % MOD;
                if (result < 0) {
                    result += MOD;
                }
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
        arr[index] = value % MOD;
        // 确保值为非负数
        if (arr[index] < 0) {
            arr[index] += MOD;
        }
        // 更新所属块的和
        updateSum(belong[index]);
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
        LOJ6285_Java solution = new LOJ6285_Java();
        solution.init(n);
        
        // 读取初始数组
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            long value = Long.parseLong(elements[i - 1]);
            solution.setValue(i, value);
        }
        
        // 处理操作
        for (int i = 0; i < n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            long c = Long.parseLong(operation[3]);
            
            if (op == 0) {
                // 区间乘法
                solution.multiply(l, r, c);
            } else if (op == 1) {
                // 区间加法
                solution.add(l, r, c);
            } else {
                // 区间求和
                writer.println(solution.query(l, r) % MOD);
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}