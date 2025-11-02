package class174;

// LOJ 数列分块入门4 - Java实现
// 题目：区间加法，区间求和
// 链接：https://loj.ac/p/6280
// 题目描述：
// 给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和。
// 操作 0 l r c : 将位于[l,r]的之间的数字都加c
// 操作 1 l r c : 询问[l,r]区间的和 mod (c+1)
// 数据范围：1 <= n <= 50000

import java.io.*;
import java.util.*;

public class Code04_BlockProblem4_1 {
    // 最大数组大小
    public static final int MAXN = 50010;
    
    // 输入数组
    public static long[] arr = new long[MAXN];
    
    // 块的大小和数量
    public static int blockSize;
    public static int blockNum;
    
    // 每个元素所属的块编号
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的懒惰标记（记录整个块增加的值）
    public static long[] lazy = new long[MAXN];
    
    // 每个块的元素和
    public static long[] sum = new long[MAXN];
    
    // 初始化分块结构
    public static void build(int n) {
        // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
        blockSize = (int) Math.sqrt(n);
        // 块数量，向上取整
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 为每个元素分配所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化每个块的元素和
        for (int i = 1; i <= blockNum; i++) {
            sum[i] = 0;
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                sum[i] += arr[j];
            }
        }
    }
    
    // 区间加法操作
    // 将区间[l,r]中的每个元素都加上val
    public static void update(int l, int r, long val) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果区间在同一个块内，直接暴力处理
        if (belongL == belongR) {
            // 直接对区间内每个元素加上val
            for (int i = l; i <= r; i++) {
                arr[i] += val;
            }
            // 更新块的元素和
            sum[belongL] += val * (r - l + 1);
            return;
        }
        
        // 处理左端点所在的不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            arr[i] += val;
        }
        // 更新块的元素和
        sum[belongL] += val * (blockRight[belongL] - l + 1);
        
        // 处理右端点所在的不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            arr[i] += val;
        }
        // 更新块的元素和
        sum[belongR] += val * (r - blockLeft[belongR] + 1);
        
        // 处理中间的完整块，使用懒惰标记优化
        for (int i = belongL + 1; i < belongR; i++) {
            lazy[i] += val;
            sum[i] += val * blockSize;
        }
    }
    
    // 查询区间[l,r]的和
    public static long query(int l, int r, long mod) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        long result = 0;
        
        // 如果区间在同一个块内，直接暴力统计
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                result += arr[i] + lazy[belong[i]];
            }
            return result % mod;
        }
        
        // 处理左端点所在的不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            result += arr[i] + lazy[belong[i]];
        }
        
        // 处理右端点所在的不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            result += arr[i] + lazy[belong[i]];
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i < belongR; i++) {
            result += sum[i] + lazy[i] * blockSize;
        }
        
        return result % mod;
    }
    
    // 主函数
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(elements[i - 1]);
        }
        
        // 初始化分块结构
        build(n);
        
        // 处理n个操作
        for (int i = 1; i <= n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            
            if (op == 0) {
                // 区间加法操作
                long c = Long.parseLong(operation[3]);
                update(l, r, c);
            } else {
                // 查询操作
                long c = Long.parseLong(operation[3]);
                writer.println(query(l, r, c + 1));
            }
        }
        
        // 输出结果
        writer.flush();
        writer.close();
        reader.close();
    }
    
    /*
     * 算法解析：
     * 
     * 时间复杂度分析：
     * 1. 建立分块结构：O(n)
     * 2. 区间更新操作：O(√n) - 最多处理两个不完整块(2*√n)和一些完整块(√n)
     * 3. 区间查询操作：O(√n) - 处理两个不完整块和一些完整块
     * 
     * 空间复杂度：O(n) - 存储原数组和分块相关信息
     * 
     * 算法思想：
     * 分块是一种"优雅的暴力"算法，通过将数组分成大小约为√n的块来平衡时间复杂度。
     * 
     * 核心思想：
     * 1. 对于不完整的块（区间端点所在的块），直接暴力处理
     * 2. 对于完整的块，使用懒惰标记来延迟更新，避免每次都修改块内所有元素
     * 3. 维护每个块的元素和，快速计算完整块的和
     * 4. 查询时，实际值 = 原始值 + 所属块的懒惰标记
     * 
     * 优势：
     * 1. 实现相对简单，比线段树等数据结构容易理解和编码
     * 2. 可以处理大多数区间操作问题
     * 3. 对于在线算法有很好的适应性
     * 
     * 适用场景：
     * 1. 需要区间修改和区间查询的问题
     * 2. 不适合用线段树等复杂数据结构的场景
     * 3. 对代码复杂度有要求的场景
     */
}