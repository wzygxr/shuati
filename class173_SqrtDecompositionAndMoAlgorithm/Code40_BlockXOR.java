// 基于块分解的子数组信息预处理 - 区间异或和查询 (Java版本)
// 题目来源: Codeforces 914C
// 题目链接: https://codeforces.com/problemset/problem/914/C
// 题目大意: 维护一个数组，支持区间异或和查询
// 约束条件: 数组长度n ≤ 1e5，查询次数m ≤ 1e5

import java.io.*;
import java.util.*;

public class Code40_BlockXOR {
    static final int MAXN = 100005;
    static final int MAX_BLOCK = 320; // √1e5 ≈ 316
    
    int n, m, blen, block_count; // n是数组长度，m是操作次数，blen是块的大小，block_count是块的数量
    int[] arr; // 原始数组
    int[] block; // 每个元素所属的块
    int[] block_xor; // 每个块的异或和
    int[][] pre_xor; // pre_xor[i][j]表示从第i个块到第j个块的异或和
    
    // 初始化分块结构
    void init() {
        blen = (int) Math.sqrt(n);
        if (blen == 0) blen = 1;
        block_count = (n + blen - 1) / blen;
        
        block = new int[n];
        // 为每个元素分配块
        for (int i = 0; i < n; i++) {
            block[i] = i / blen;
        }
        
        block_xor = new int[block_count];
        // 计算每个块的异或和
        for (int i = 0; i < block_count; i++) {
            int start = i * blen;
            int end = Math.min((i + 1) * blen, n);
            int xor_sum = 0;
            for (int j = start; j < end; j++) {
                xor_sum ^= arr[j];
            }
            block_xor[i] = xor_sum;
        }
        
        // 预处理块间的异或和
        pre_xor = new int[block_count][block_count];
        for (int i = 0; i < block_count; i++) {
            int current_xor = 0;
            for (int j = i; j < block_count; j++) {
                current_xor ^= block_xor[j];
                pre_xor[i][j] = current_xor;
            }
        }
    }
    
    // 区间异或和查询
    int query_xor(int l, int r) {
        int left_block = block[l];
        int right_block = block[r];
        int xor_sum = 0;
        
        if (left_block == right_block) {
            // 所有元素都在同一个块内，直接暴力查询
            for (int i = l; i <= r; i++) {
                xor_sum ^= arr[i];
            }
        } else {
            // 处理左边不完整的块
            for (int i = l; i < (left_block + 1) * blen; i++) {
                xor_sum ^= arr[i];
            }
            
            // 处理中间完整的块，使用预处理的块间异或和
            if (left_block + 1 <= right_block - 1) {
                xor_sum ^= pre_xor[left_block + 1][right_block - 1];
            }
            
            // 处理右边不完整的块
            for (int i = right_block * blen; i <= r; i++) {
                xor_sum ^= arr[i];
            }
        }
        
        return xor_sum;
    }
    
    // 单点更新
    void update_point(int pos, int val) {
        int old_val = arr[pos];
        arr[pos] = val;
        
        // 更新对应块的异或和
        int block_id = block[pos];
        int start = block_id * blen;
        int end = Math.min((block_id + 1) * blen, n);
        int xor_sum = 0;
        for (int i = start; i < end; i++) {
            xor_sum ^= arr[i];
        }
        block_xor[block_id] = xor_sum;
        
        // 重新计算受影响的预处理块间异或和
        for (int i = 0; i < block_count; i++) {
            if (i > block_id) break;
            int current_xor = 0;
            for (int j = i; j < block_count; j++) {
                current_xor ^= block_xor[j];
                pre_xor[i][j] = current_xor;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        Code40_BlockXOR solution = new Code40_BlockXOR();
        
        // 读取n
        solution.n = Integer.parseInt(br.readLine());
        
        // 读取初始数组
        solution.arr = new int[solution.n];
        String[] input = br.readLine().split(" ");
        for (int i = 0; i < solution.n; i++) {
            solution.arr[i] = Integer.parseInt(input[i]);
        }
        
        // 初始化分块结构
        solution.init();
        
        // 读取m
        solution.m = Integer.parseInt(br.readLine());
        
        // 处理每个操作
        for (int i = 0; i < solution.m; i++) {
            input = br.readLine().split(" ");
            int op = Integer.parseInt(input[0]);
            
            if (op == 0) {
                // 单点更新操作
                int a = Integer.parseInt(input[1]) - 1; // 转换为0-based索引
                int b = Integer.parseInt(input[2]);
                solution.update_point(a, b);
            } else if (op == 1) {
                // 区间查询操作
                int a = Integer.parseInt(input[1]) - 1; // 转换为0-based索引
                int b = Integer.parseInt(input[2]) - 1;
                int result = solution.query_xor(a, b);
                pw.println(result);
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
}

/*
时间复杂度分析：
- 初始化：
  - 计算块异或和：O(n)
  - 预处理块间异或和：O(n√n)
- 单点更新：
  - 重新计算块异或和：O(√n)
  - 重新计算预处理块间异或和：O(n)
  - 总体时间复杂度：O(n)
- 区间查询：
  - 不完整块的处理：O(√n)
  - 完整块的处理：O(1)（使用预处理的块间异或和）
  - 总体时间复杂度：O(√n)

空间复杂度分析：
- 数组arr：O(n)
- 块分配数组block：O(n)
- 块异或和数组block_xor：O(√n)
- 预处理块间异或和数组pre_xor：O(n)
- 总体空间复杂度：O(n)

Java语言特性注意事项：
1. 使用BufferedReader和PrintWriter提高输入输出效率
2. 二维数组的初始化方式
3. 注意Java中数组的索引是0-based
4. Math.sqrt返回的是double类型，需要转换为int
5. 对于大规模数据，必须使用快速输入方法

算法说明：
基于块分解的子数组信息预处理是分块算法的一个重要应用，特别适用于区间异或和查询等问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 预处理每个块的异或和
3. 预处理块间的异或和，pre_xor[i][j]表示从第i个块到第j个块的异或和
4. 对于区间异或和查询操作：
   - 对于不完整的块，直接遍历块中的元素，计算异或和
   - 对于完整的块，使用预处理的块间异或和
   - 综合所有部分的结果，得到最终的异或和
5. 对于单点更新操作：
   - 更新原始数组中的值
   - 重新计算对应块的异或和
   - 重新计算受影响的预处理块间异或和

优化说明：
1. 使用预处理的块间异或和，减少查询时的计算量
2. 块的大小选择为√n，平衡了查询和更新的时间复杂度
3. 在更新时，只重新计算受影响的预处理块间异或和

与其他方法的对比：
- 暴力法：每次查询O(n)，每次更新O(1)，总时间复杂度O(mn)
- 前缀异或数组：每次查询O(1)，但更新需要O(n)时间
- 线段树：每次更新和查询都是O(log n)，但实现复杂
- 基于块分解的预处理：查询O(√n)，更新O(n)，实现相对简单

工程化考虑：
1. 使用BufferedReader提高输入效率，避免超时
2. 对于大规模数据，可以考虑使用更快的输入方法
3. 注意内存的使用，避免不必要的数组分配
4. 可以将块的大小作为参数，根据具体数据调整以获得最佳性能
*/