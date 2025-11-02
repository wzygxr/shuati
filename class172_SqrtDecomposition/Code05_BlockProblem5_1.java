package class174;

// LOJ 数列分块入门5 - Java实现
// 题目：区间开方，区间求和
// 链接：https://loj.ac/p/6281
// 题目描述：
// 给出一个长为n的数列，以及n个操作，操作涉及区间开方（下取整），区间求和。
// 操作 0 l r c : 将位于[l,r]的之间的数字都开方（下取整）
// 操作 1 l r c : 询问[l,r]区间的和
// 数据范围：1 <= n <= 50000

import java.io.*;
import java.util.*;

public class Code05_BlockProblem5_1 {
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
    
    // 每个块是否全为0或1的标记
    public static boolean[] isZeroOne = new boolean[MAXN];
    
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
        
        // 初始化每个块的元素和和标记
        for (int i = 1; i <= blockNum; i++) {
            sum[i] = 0;
            isZeroOne[i] = true;
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                sum[i] += arr[j];
                if (arr[j] != 0 && arr[j] != 1) {
                    isZeroOne[i] = false;
                }
            }
        }
    }
    
    // 区间开方操作
    // 将区间[l,r]中的每个元素都开方（下取整）
    public static void update(int l, int r) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果区间在同一个块内，直接暴力处理
        if (belongL == belongR) {
            // 直接对区间内每个元素开方
            for (int i = l; i <= r; i++) {
                sum[belongL] -= arr[i];
                arr[i] = (long) Math.sqrt(arr[i]);
                sum[belongL] += arr[i];
            }
            // 检查块是否全为0或1
            isZeroOne[belongL] = true;
            for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
                if (arr[i] != 0 && arr[i] != 1) {
                    isZeroOne[belongL] = false;
                    break;
                }
            }
            return;
        }
        
        // 处理左端点所在的不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            sum[belongL] -= arr[i];
            arr[i] = (long) Math.sqrt(arr[i]);
            sum[belongL] += arr[i];
        }
        // 检查块是否全为0或1
        isZeroOne[belongL] = true;
        for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
            if (arr[i] != 0 && arr[i] != 1) {
                isZeroOne[belongL] = false;
                break;
            }
        }
        
        // 处理右端点所在的不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            sum[belongR] -= arr[i];
            arr[i] = (long) Math.sqrt(arr[i]);
            sum[belongR] += arr[i];
        }
        // 检查块是否全为0或1
        isZeroOne[belongR] = true;
        for (int i = blockLeft[belongR]; i <= blockRight[belongR]; i++) {
            if (arr[i] != 0 && arr[i] != 1) {
                isZeroOne[belongR] = false;
                break;
            }
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i < belongR; i++) {
            // 如果块已经全为0或1，则无需处理
            if (isZeroOne[i]) {
                continue;
            }
            
            // 对块内每个元素开方
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                sum[i] -= arr[j];
                arr[j] = (long) Math.sqrt(arr[j]);
                sum[i] += arr[j];
            }
            
            // 检查块是否全为0或1
            isZeroOne[i] = true;
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                if (arr[j] != 0 && arr[j] != 1) {
                    isZeroOne[i] = false;
                    break;
                }
            }
        }
    }
    
    // 查询区间[l,r]的和
    public static long query(int l, int r) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        long result = 0;
        
        // 如果区间在同一个块内，直接暴力统计
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                result += arr[i];
            }
            return result;
        }
        
        // 处理左端点所在的不完整块
        for (int i = l; i <= blockRight[belongL]; i++) {
            result += arr[i];
        }
        
        // 处理右端点所在的不完整块
        for (int i = blockLeft[belongR]; i <= r; i++) {
            result += arr[i];
        }
        
        // 处理中间的完整块
        for (int i = belongL + 1; i < belongR; i++) {
            result += sum[i];
        }
        
        return result;
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
                // 区间开方操作
                update(l, r);
            } else {
                // 查询操作
                writer.println(query(l, r));
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
     * 2. 对于完整的块，使用标记优化，如果块内元素全为0或1则无需处理
     * 3. 维护每个块的元素和，快速计算完整块的和
     * 
     * 优化技巧：
     * 利用开方次数有限的特性，当块内元素全为0或1时，无需再进行开方操作。
     * 
     * 优势：
     * 1. 实现相对简单，比线段树等数据结构容易理解和编码
     * 2. 利用开方特性进行优化，提高实际运行效率
     * 3. 可以处理大多数区间操作问题
     * 
     * 适用场景：
     * 1. 需要区间开方和区间求和的问题
     * 2. 操作具有有限次数特性的场景
     */
}